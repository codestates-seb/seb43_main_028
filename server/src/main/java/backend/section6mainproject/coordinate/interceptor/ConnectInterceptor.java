package backend.section6mainproject.coordinate.interceptor;

import backend.section6mainproject.auth.jwt.JwtTokenizer;
import backend.section6mainproject.member.service.MemberService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConnectInterceptor implements ChannelInterceptor {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        Object uri = SimpAttributesContextHolder.getAttributes().getAttribute("uri");
        if (headers.get("stompCommand").toString().equals("CONNECT") && uri != null) {
            String accessToken = getAccessToken(headers);
            if(accessToken == null) throw new AccessDeniedException(HttpStatus.FORBIDDEN.toString());
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            Map<String, Object> claims = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody();
            Long memberId = Long.parseLong(((String) claims.get("sub")));
            Long walkLogId = memberService.findRecordingWalkLog(memberId);
            SimpAttributesContextHolder.getAttributes().setAttribute("walkLogId", walkLogId);
        }
        return message;
    }

    private String getAccessToken(MessageHeaders headers) {
        MultiValueMap nativeHeaders = headers.get("nativeHeaders", MultiValueMap.class);
        List authList = (List) nativeHeaders.get("Authorization");
        if (authList != null) {
            Object authorization = authList.get(0);
            if(authorization != null) return authorization.toString();
        }
        return null;
    }
}
