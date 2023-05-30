package backend.section6mainproject.coordinate.controller;

import backend.section6mainproject.auth.handler.AuthenticationSuccessHandlerUtils;
import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.service.CoordinateService;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.response.ErrorResponse;
import backend.section6mainproject.walklog.entity.WalkLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoordinateControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private AuthenticationSuccessHandlerUtils successHandlerUtils;
    @MockBean
    private MemberService memberService;
    @MockBean
    private CoordinateMapper mapper;
    @MockBean
    private CoordinateService coordinateService;

    private WebSocketStompClient stompClient;
    private String url;
    private StompSession stompSession;
    private CompletableFuture<CoordinateControllerDTO.Sub> subscribeFuture;
    private CompletableFuture<ErrorResponse> errorFuture;
    private StubData stubData = new StubData();


    public CoordinateControllerTest() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = messageConverter.getObjectMapper();
        objectMapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        stompClient.setMessageConverter(messageConverter);
    }

    @BeforeEach
    void init() throws ExecutionException, InterruptedException, TimeoutException {
        this.url = String.format("ws://localhost:%d/ws/walk-logs", port);
        subscribeFuture = new CompletableFuture<>();
        errorFuture = new CompletableFuture<>();

        given(memberService.findRecordingWalkLog(Mockito.anyLong())).willReturn(1L);

        String accessToken = successHandlerUtils.delegateAccessToken(stubData.getStubMember());
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Authorization", "Bearer " + accessToken);

        this.stompSession = stompClient.connect(url, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                if(headers.containsKey("error")) return ErrorResponse.class;
                return CoordinateControllerDTO.Sub.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if(headers.containsKey("error")) errorFuture.complete((ErrorResponse) payload);
                else subscribeFuture.complete((CoordinateControllerDTO.Sub) payload);
            }
        });
    }

    @Test
    void publishCoordinate() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CoordinateControllerDTO.Pub pub = stubData.getStubCoordinatePub();

        stubbingMockBean();

        //when
        stompSession.send("/pub/walk-logs", pub);

        //then
        CoordinateControllerDTO.Sub sub = subscribeFuture.get(3, TimeUnit.SECONDS);
        MatcherAssert.assertThat(sub.getLat(), is(equalTo(pub.getLat())));
        MatcherAssert.assertThat(sub.getLng(), is(equalTo(pub.getLng())));
    }

    @Test
    void publishCoordinateWithInvalidPayload() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CoordinateControllerDTO.Pub pub = new CoordinateControllerDTO.Pub();
        stubbingMockBean();

        //when
        stompSession.send("/pub/walk-logs", pub);

        //then
        Assertions.assertDoesNotThrow(() -> errorFuture.get(3, TimeUnit.SECONDS));

    }

    private void stubbingMockBean() {
        given(mapper.controllerPubDTOTOServiceInputDTO(Mockito.any(CoordinateControllerDTO.Pub.class)))
                .willReturn(new CoordinateServiceDTO.Input());
        given(coordinateService.createCoordinate(Mockito.any(CoordinateServiceDTO.Input.class)))
                .willReturn(new CoordinateServiceDTO.Output(0, 1L, null, null, null));
        given(mapper.serviceOutputDTOToControllerSubDTO(Mockito.any(CoordinateServiceDTO.Output.class)))
                .willReturn(stubData.getStubCoordinateSub());
    }


    private class StubData {
        private double lat = 1.1;
        private double lng = 2.2;

        private Member getStubMember() {
            Member member = new Member();
            member.setMemberId(1L);
            member.setEmail("kim@gmaill.com");
            member.setRoles(List.of("USER"));
            WalkLog walkLog = new WalkLog();
            walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
            walkLog.setWalkLogId(1L);
            member.getWalkLogs().add(walkLog);
            walkLog.setMember(member);
            return member;
        }

        private CoordinateControllerDTO.Pub getStubCoordinatePub() {
            CoordinateControllerDTO.Pub pub = new CoordinateControllerDTO.Pub();
            pub.setLat(lat);
            pub.setLng(lng);
            return pub;
        }

        private CoordinateControllerDTO.Sub getStubCoordinateSub() {
            return new CoordinateControllerDTO.Sub(1L, lat, lng, LocalDateTime.now());
        }
    }


}