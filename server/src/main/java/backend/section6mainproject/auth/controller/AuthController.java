package backend.section6mainproject.auth.controller;

import backend.section6mainproject.auth.AuthMapper;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final AuthMapper authMapper;
    @GetMapping("/members/refresh")
    public ResponseEntity<?> getRefreshToken() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        Authentication auth = Optional.ofNullable(authentication)
                .orElseThrow(() -> new AccessDeniedException(HttpStatus.FORBIDDEN.toString()));
        long memberId = Long.parseLong(auth.getPrincipal().toString());
        MemberServiceDTO.Output output = memberService.findMember(memberId);
        return ResponseEntity.ok(authMapper.ServiceOutputDTOToProfileDTO(output));
    }
}
