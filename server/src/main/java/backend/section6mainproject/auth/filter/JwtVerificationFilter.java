package backend.section6mainproject.auth.filter;

import backend.section6mainproject.auth.jwt.JwtTokenizer;
import backend.section6mainproject.auth.utils.CustomAuthorityUtils;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(request.getServletPath().equals("/members/refresh")){
                verifyRefreshToken(request, response);
            } else {
                verifyAccessToken(request);
            }
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
    private void verifyAccessToken(HttpServletRequest request) {
        Map<String, Object> claims = verifyAccessJws(request);
        setAuthenticationToContext(claims);
    }
    private void verifyRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        Long memberId = Long.parseLong(verifyRefreshJws(request));
        String accessToken = delegateAccessToken(memberId);
        response.setHeader("Authorization", "Bearer " + accessToken);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        String refresh = request.getHeader("Refresh");
        return (authorization == null || !authorization.startsWith("Bearer")) &&
                (!request.getServletPath().equals("/members/refresh") || !getRefreshToken(request).isPresent());
    }
    private Map<String, Object> verifyAccessJws(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        return jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody();
    }
    private String verifyRefreshJws(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request).get().getValue();
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        return (String) jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody().get("sub");
    }

    private Optional<Cookie> getRefreshToken(HttpServletRequest request) {
        Optional<Cookie[]> optionalCookies = Optional.ofNullable(request.getCookies());
        if (optionalCookies.isPresent()) {
            Cookie[] cookies = optionalCookies.get();
            return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("Refresh")).findAny();
        }
        return Optional.empty();
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String memberId = (String) claims.get("sub");
        List<String> roles = (List<String>) claims.get("roles");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null, authorityUtils.getAuthorities(roles));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    private String delegateAccessToken(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        setAuthenticationToContext(claims);

        String subject = member.getMemberId().toString();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

}
