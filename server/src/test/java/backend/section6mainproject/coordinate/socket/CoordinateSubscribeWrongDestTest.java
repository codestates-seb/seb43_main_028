package backend.section6mainproject.coordinate.socket;

import backend.section6mainproject.auth.handler.AuthenticationSuccessHandlerUtils;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.hamcrest.MatcherAssert;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoordinateSubscribeWrongDestTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    @LocalServerPort
    private int port;
    @Autowired
    private AuthenticationSuccessHandlerUtils successHandlerUtils;
    @MockBean
    private MemberService memberService;
    private String url;
    private WebSocketHttpHeaders webSocketHttpHeaders;

    private WebSocketStompClient getStompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
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
    void subscribeWrongDest() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CompletableFuture<StompHeaders> completableFuture = new CompletableFuture<>();
        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        //when
        StompSession stompSession = getStompClient().connect(url, webSocketHttpHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("headers : {}", headers);
                completableFuture.complete(headers);
            }
        }).get(2, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/2", new StompSessionHandlerAdapter() {
        });

        //then
        StompHeaders stompHeaders = completableFuture.get(10, TimeUnit.SECONDS);
        MatcherAssert.assertThat(stompHeaders.size(), is(greaterThan(0)));
        MatcherAssert.assertThat(stompHeaders.get("message").get(0), is(containsString(AccessDeniedException.class.getName())));

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
