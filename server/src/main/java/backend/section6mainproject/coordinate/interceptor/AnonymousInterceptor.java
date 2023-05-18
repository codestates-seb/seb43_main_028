package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateControllerDTO;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.support.ChannelInterceptor;

public class AnonymousInterceptor implements ChannelInterceptor {
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        MessageHeaders headers = message.getHeaders();
        String destination = (String)headers.get("simpDestination");
        if(!destination.startsWith("/sub/anonymous")) return;


        String userId = (String)SimpAttributesContextHolder.getAttributes().getAttribute("userId");
        String newId = ((AnonymousCoordinateControllerDTO.Pub) message.getPayload()).getUserId();
        if(userId == null) SimpAttributesContextHolder.getAttributes().setAttribute("userId", newId);
    }
}
