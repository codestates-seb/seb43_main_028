package backend.section6mainproject.coordinate.socket;

import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.coordinate.service.CoordinateService;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoordinateSocketConnectionTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    @LocalServerPort
    private int port;
    @MockBean
    private MemberService memberService;
    private WebSocketStompClient stompClient;
    private String url;


    public CoordinateSocketConnectionTest() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @BeforeEach
    void init() {
        this.url = String.format("ws://localhost:%d/ws/walk-logs", port);
    }

    @Test
    void connectWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(getStubMember());
        //when //then
        Assertions.assertDoesNotThrow(() -> stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }

    @Test
    void connectWithNoWalkLogRecording() {
        //given
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(new Member());
        //when //then
        Assertions.assertThrows(Exception.class, () -> stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }
    @Test
    void subscribeWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CompletableFuture<StompHeaders> completableFuture = new CompletableFuture<>();
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(getStubMember());

        //when
        StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete(headers);
            }
        }).get(2, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/1", new StompSessionHandlerAdapter() {
        });

        //then
        Assertions.assertThrows(TimeoutException.class, () -> completableFuture.get(7, TimeUnit.SECONDS));

    }
    @Test
    void subscribeWrongDest() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CompletableFuture<StompHeaders> completableFuture = new CompletableFuture<>();
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(getStubMember());

        //when
        StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("headers : {}", headers);
                completableFuture.complete(headers);
            }
        }).get(2, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/2", new StompSessionHandlerAdapter() {
        });

        //then
        StompHeaders stompHeaders = completableFuture.get(7, TimeUnit.SECONDS);
        MatcherAssert.assertThat(stompHeaders.size(), is(greaterThan(0)));
        MatcherAssert.assertThat(stompHeaders.get("message").get(0), is(containsString(BusinessLogicException.class.getName())));

    }

    private Member getStubMember() {
        Member member = new Member();
        member.setMemberId(1L);
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        walkLog.setWalkLogId(1L);
        member.getWalkLogs().add(walkLog);
        walkLog.setMember(member);
        return member;
    }
}
