package backend.section6mainproject.coordinate.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.support.ChannelInterceptor;

public class SubscribeInterceptor implements ChannelInterceptor {
    /**
     * 현재 기록중인 walkLog의 좌표만 확인할 수 있도록 subscribe url을 검증하는 메서드
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        if (headers.get("stompCommand").toString().equals("SUBSCRIBE")) {
            Object walkLogId = SimpAttributesContextHolder.getAttributes().getAttribute("walkLogId");
            if (!headers.get("simpDestination").equals("/sub/" + walkLogId)) {
                throw new RuntimeException("cannot access walkLog");
            }
        }

        return message;
    }
}
