package backend.section6mainproject.coordinate;

import backend.section6mainproject.coordinate.interceptor.AnonymousInterceptor;
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
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SubscribeInterceptor(), new AnonymousInterceptor());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/walk-logs")
                .addInterceptors(new ConnectionInterceptor(memberService))
                .setAllowedOrigins("*");
        registry.addEndpoint("/ws/anonymous/walk-logs")
                .setAllowedOrigins("*");
    }


}
