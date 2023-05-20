package backend.section6mainproject.coordinate.socket;

import backend.section6mainproject.TestDataInit;
import backend.section6mainproject.auth.handler.AuthenticationSuccessHandlerUtils;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.Optional;
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
    @Autowired
    private AuthenticationSuccessHandlerUtils successHandlerUtils;
    @MockBean
    private MemberService memberService;
    private WebSocketStompClient stompClient;
    private String url;
    private WebSocketHttpHeaders webSocketHttpHeaders;

    public CoordinateSocketConnectionTest() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @BeforeEach
    void init() {
        this.url = String.format("ws://localhost:%d/ws/walk-logs", port);
        String accessToken = successHandlerUtils.delegateAccessToken(getStubMember());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        this.webSocketHttpHeaders = new WebSocketHttpHeaders(httpHeaders);
    }

    @Test
    void connectWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when //then
        Assertions.assertDoesNotThrow(() -> stompClient.connect(url, webSocketHttpHeaders, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }

    @Test
    void connectWithNoWalkLogRecording() {
        //given
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willThrow(BusinessLogicException.class);
        //when //then
        Assertions.assertThrows(Exception.class, () -> stompClient.connect(url, webSocketHttpHeaders, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }
    @Test
    void subscribeWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CompletableFuture<StompHeaders> completableFuture = new CompletableFuture<>();
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when
        StompSession stompSession = stompClient.connect(url, webSocketHttpHeaders, new StompSessionHandlerAdapter() {
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
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when
        StompSession stompSession = stompClient.connect(url, webSocketHttpHeaders, new StompSessionHandlerAdapter() {
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
        member.setMemberId(2L);
        member.setEmail("kim@gmaill.com");
        member.setRoles(List.of("USER"));
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        walkLog.setWalkLogId(1L);
        member.getWalkLogs().add(walkLog);
        walkLog.setMember(member);
        return member;
    }
}
