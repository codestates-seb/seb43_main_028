package backend.section6mainproject.coordinate;

import backend.section6mainproject.auth.jwt.JwtTokenizer;
import backend.section6mainproject.coordinate.interceptor.ConnectInterceptor;
import backend.section6mainproject.coordinate.interceptor.ConnectionInterceptor;
import backend.section6mainproject.coordinate.interceptor.SubscribeInterceptor;
import backend.section6mainproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    private final MemberService memberService;
    private final JwtTokenizer jwtTokenizer;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SubscribeInterceptor(), new ConnectInterceptor(jwtTokenizer, memberService));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/walk-logs")
                .addInterceptors(new ConnectionInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
        registry.addEndpoint("/ws/anonymous/walk-logs")
                .setAllowedOrigins("*")
                .withSockJS();
    }


}
