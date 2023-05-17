package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.HashMap;
import java.util.Map;

public class SubscribeInterceptor implements ChannelInterceptor {
    /**
     * 현재 기록중인 walkLog의 좌표만 확인할 수 있도록 subscribe url을 검증하는 메서드
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        if (headers.get("stompCommand").toString().equals("SUBSCRIBE")) {
            Object walkLogId = SimpAttributesContextHolder.getAttributes().getAttribute("walkLogId");
            Object destination = headers.get("simpDestination");
            if (!destination.equals("/sub/" + walkLogId)) {
                Map<String, Object> attr = new HashMap<>();
                attr.put("dest", destination);
                attr.put("requiredId", walkLogId);
                throw new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND, attr);
            }
        }

        return message;
    }
}
