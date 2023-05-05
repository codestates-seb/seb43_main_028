package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConnectionInterceptor implements HandshakeInterceptor {
    private final MemberService memberService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 인증 로직 구현 후, 토큰에서 memberId를 가져오게 할 예정이다.
        Long memberId = 1L;
        Member findMember = memberService.findVerifiedMember(memberId);
        List<WalkLog> walkLogs = findMember.getWalkLogs();

        // 기록 상태를 나타내는 상태값 구현후 변경 예정, 임시로 마지막 walkLog의 Id를 가져오게 했다.
        Long walkLogId = walkLogs.get(walkLogs.size() - 1).getWalkLogId();
        attributes.put("walkLogId", walkLogId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
