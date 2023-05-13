package backend.section6mainproject.coordinate.controller;

import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.service.CoordinateService;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
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
    @MockBean
    private MemberService memberService;
    @MockBean
    private CoordinateMapper mapper;
    @MockBean
    private CoordinateService coordinateService;

    private WebSocketStompClient stompClient;
    private String url;
    private StompSession stompSession;
    private CompletableFuture<CoordinateControllerDTO.Sub> completableFuture;
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
        completableFuture = new CompletableFuture<>();
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(stubData.getStubMember());
        this.stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(3, TimeUnit.SECONDS);
        stompSession.subscribe("/sub/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CoordinateControllerDTO.Sub.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                completableFuture.complete((CoordinateControllerDTO.Sub) payload);
            }
        });
    }

    @Test
    void publishCoordinate() throws ExecutionException, InterruptedException, TimeoutException {
        //given
        CoordinateControllerDTO.Pub pub = stubData.getStubCoordinatePub();
        given(mapper.controllerPubDTOTOServiceCreateParamDTO(Mockito.any(CoordinateControllerDTO.Pub.class))).willReturn(new CoordinateServiceDTO.CreateParam());
        given(coordinateService.createCoordinate(Mockito.any(CoordinateServiceDTO.CreateParam.class)))
                .willReturn(new CoordinateServiceDTO.CreateReturn(0, 1L, null, null, null));
        given(mapper.serviceCreateReturnDTOToControllerSubDTO(Mockito.any(CoordinateServiceDTO.CreateReturn.class)))
                .willReturn(stubData.getStubCoordinateSub());

        //when
        stompSession.send("/pub/walk-logs", pub);

        //then
        CoordinateControllerDTO.Sub sub = completableFuture.get(3, TimeUnit.SECONDS);
        MatcherAssert.assertThat(sub.getLat(), is(equalTo(pub.getLat())));
        MatcherAssert.assertThat(sub.getLng(), is(equalTo(pub.getLng())));
    }


    private class StubData {
        private double lat = 1.1;
        private double lng = 2.2;

        private Member getStubMember() {
            Member member = new Member();
            member.setMemberId(1L);
            WalkLog walkLog = new WalkLog();
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