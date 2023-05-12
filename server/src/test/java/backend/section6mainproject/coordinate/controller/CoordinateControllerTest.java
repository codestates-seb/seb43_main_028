package backend.section6mainproject.coordinate.controller;

import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CoordinateControllerTest {
    private WebSocketStompClient stompClient;
    private String url;
    private StompSession stompSession;
    private CompletableFuture<CoordinateControllerDTO.Sub> completableFuture;


    public CoordinateControllerTest() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.url = "ws://localhost:8080/ws/walk-logs";
    }

    @BeforeEach
    public void init() throws ExecutionException, InterruptedException, TimeoutException {
        completableFuture = new CompletableFuture<>();
        this.stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(3, TimeUnit.SECONDS);


    }
    @Test
    void publishCoordinate() throws ExecutionException, InterruptedException, TimeoutException {
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
        CoordinateControllerDTO.Pub pub = new CoordinateControllerDTO.Pub();
        double lat = 1.1;
        pub.setLat(lat);
        double lng = 2.2;
        pub.setLng(lng);
        stompSession.send("/pub/walk-logs", pub);
    }
}