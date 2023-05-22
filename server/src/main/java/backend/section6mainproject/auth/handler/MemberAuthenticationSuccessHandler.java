package backend.section6mainproject.auth.handler;

import backend.section6mainproject.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticationSuccessHandlerUtils authenticationSuccessHandlerUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = (Member) authentication.getPrincipal();

        String accessToken = authenticationSuccessHandlerUtils.delegateAccessToken(member);
        response.setHeader("Authorization", "Bearer " + accessToken);

        if((Boolean) request.getAttribute("autoLogin")){
            String refreshToken = authenticationSuccessHandlerUtils.delegateRefreshToken(member);
            ResponseCookie cookie = ResponseCookie.from("Refresh", refreshToken)
                    .domain(".would-you-walk.com")
                    .path("/")
                    .httpOnly(true)
                    .maxAge(Duration.ofDays(7))
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
        }

        setMemberIdToBody(response, member);
    }

    private void setMemberIdToBody(HttpServletResponse response, Member member) throws IOException {
        Map<String, Long> loginResponse = new HashMap<>();
        loginResponse.put("memberId", member.getMemberId());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }

}
