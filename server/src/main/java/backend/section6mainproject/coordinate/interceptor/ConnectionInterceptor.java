package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uri = request.getURI().toString();
        attributes.put("uri", uri);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
