package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberServiceImpl;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConnectionInterceptor implements HandshakeInterceptor {
    private final MemberServiceImpl memberServiceImpl;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 인증 로직 구현 후, 토큰에서 memberId를 가져오게 할 예정이다.
        Long memberId = 1L;
        Member findMember = memberServiceImpl.findVerifiedMember(memberId);
        List<WalkLog> walkLogs = findMember.getWalkLogs();
        Long walkLogId = null;
        for (int i = walkLogs.size() - 1; i >= 0; i--) {
            if(walkLogs.get(i).getWalkLogStatus() == WalkLog.WalkLogStatus.RECORDING) {
                walkLogId = walkLogs.get(i).getWalkLogId();
                break;
            }
        }
        if(walkLogId == null) throw new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND);//인증 구현 후 ExceptionCode에 추가예정 "WalkLog is not recording"
        attributes.put("walkLogId", walkLogId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
