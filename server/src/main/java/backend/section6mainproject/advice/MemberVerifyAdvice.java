package backend.section6mainproject.advice;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberVerifyAdvice {
    private final MemberRepository memberRepository;
    private final WalkLogRepository walkLogRepository;
    private final WalkLogContentRepository walkLogContentRepository;

    /**
     * 회원 수정, 회원 비밀번호 수정, 회원 삭제, 회원 걷기 목록 조회, 회원 걷기 목록 캘린더 형식 조회 주체 검증
     */
    @Before("execution(* patchMember*(..)) || execution(!void deleteMember(..)) || execution(* getMyWalkLogs*(..))")
    public void verifyMember(JoinPoint joinPoint) {
        log.info("verifying member is working : {}", joinPoint.getSignature());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long authenticatedMemberId = getAuthenticatedMemberId(request);
        long memberId = extractIdFromUri(request, "/members/");

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        optionalMember.ifPresentOrElse(member -> {
            if (member.getMemberId() != authenticatedMemberId) throw new AccessDeniedException(HttpStatus.FORBIDDEN.toString());
        }, () -> {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        });
    }

    /**
     * 걷기 기록 수정, 걷기 기록 삭제, 걷기 기록 종료, 걷기 기록 조회(PRIVATE), 걷기 중 순간기록 등록 주체 검증
     */
    @Before("execution(* patchWalkLog(..)) || execution(!void deleteWalkLog(..))|| execution(* endWalkLog(Long, ..)) || " +
            "execution(* getWalkLog(long)) || execution(* postContent(..))")
    public void verifyWalkLog(JoinPoint joinPoint) {
        log.info("verifying walkLog is working : {}", joinPoint.getSignature());
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long authenticatedMemberId = getAuthenticatedMemberId(request);
        long walkLogId = extractIdFromUri(request, "/walk-logs/");

        Optional<WalkLog> optionalWalkLog = walkLogRepository.findById(walkLogId);
        optionalWalkLog.ifPresentOrElse(walkLog -> {
            if(methodName.equals("getWalkLog") && walkLog.getWalkLogPublicSetting() == WalkLog.WalkLogPublicSetting.PUBLIC) return;
            if(walkLog.getMember() == null || walkLog.getMember().getMemberId() != authenticatedMemberId) {
                throw new AccessDeniedException(HttpStatus.FORBIDDEN.toString());
            }
        }, () -> {
            throw new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND);
        });
    }

    /**
     * 걷기 중 순간기록 수정, 걷기 중 순간기록 삭제 주체 검증
     */
    @Before("execution(* patchContent(..)) || execution(!void deleteContent(..))")
    public void verifyWalkLogContent(JoinPoint joinPoint) {
        log.info("verifying walkLogContent is working : {}", joinPoint.getSignature());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long authenticatedMemberId = getAuthenticatedMemberId(request);
        long walkLogContentId = extractIdFromUri(request, "/contents/");

        Optional<WalkLogContent> optionalContent = walkLogContentRepository.findById(walkLogContentId);
        optionalContent.ifPresentOrElse(content -> {
            if(content.getWalkLog() == null || content.getWalkLog().getMember() == null
            || content.getWalkLog().getMember().getMemberId() != authenticatedMemberId) {
                throw new AccessDeniedException(HttpStatus.FORBIDDEN.toString());
            }
        }, () -> {
            throw new BusinessLogicException(ExceptionCode.WALK_LOG_CONTENT_NOT_FOUND);
        });
    }

    private Long getAuthenticatedMemberId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return Long.parseLong(principal.getName());
        }
        return null;
    }


    /**
     * URI에서 id값을 추출하는 메서드
     * @param request
     * @param prefix 추출할 id를 지정하기 위한 prefix
     * @return id
     */
    private long extractIdFromUri(HttpServletRequest request, String prefix) {
        String servletPath = request.getServletPath();

        if (servletPath.contains(prefix)) {
            String substring = servletPath.substring(servletPath.indexOf(prefix) + prefix.length());
            if(substring.contains("/")) return Long.parseLong(substring.substring(0, substring.indexOf("/")));
            else return Long.parseLong(substring);
        } else {
            throw new RuntimeException("Cannot extract id");
        }
    }
}
