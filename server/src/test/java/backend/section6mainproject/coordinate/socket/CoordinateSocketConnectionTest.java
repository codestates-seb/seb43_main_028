package backend.section6mainproject.coordinate.socket;

import backend.section6mainproject.auth.handler.AuthenticationSuccessHandlerUtils;
import backend.section6mainproject.auth.jwt.JwtTokenizer;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
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
    private String url;
    private StompHeaders stompHeaders;

    private WebSocketStompClient getStompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport( new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

    @BeforeEach
    void init() {
        this.url = String.format("ws://localhost:%d/ws/walk-logs", port);
        String accessToken = successHandlerUtils.delegateAccessToken(getStubMember());
        stompHeaders = new StompHeaders();
        stompHeaders.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    void connectWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when //then
        Assertions.assertDoesNotThrow(() -> getStompClient().connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }

    @Test
    void connectWithNoWalkLogRecording() {
        //given
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willThrow(BusinessLogicException.class);
        //when //then
        Assertions.assertThrows(Exception.class, () -> getStompClient().connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.MINUTES));

    }
    @Test
    void subscribeWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CompletableFuture<StompHeaders> completableFuture = new CompletableFuture<>();
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when
        StompSession stompSession = getStompClient().connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete(headers);
            }
        }).get(2, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/1", new StompSessionHandlerAdapter() {
        });

        //then
        Assertions.assertThrows(TimeoutException.class, () -> completableFuture.get(10, TimeUnit.SECONDS));

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
