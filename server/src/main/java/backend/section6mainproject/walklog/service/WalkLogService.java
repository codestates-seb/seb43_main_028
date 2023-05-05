package backend.section6mainproject.walklog.service;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkLogService {

    private final WalkLogRepository walkLogRepository;
    private final MemberService memberService;

    public WalkLog createWalkLog(Long memberId){
        WalkLog walkLog = new WalkLog();
        Member findVerifiedMember = memberService.findVerifiedMember(memberId);
        walkLog.setMember(findVerifiedMember);
        return walkLogRepository.save(walkLog);
    }
    public WalkLog findVerifiedWalkLog(long walkLogId) {
        Optional<WalkLog> findWalkLogById = walkLogRepository.findById(walkLogId);

        WalkLog walkLog =
                findWalkLogById.orElseThrow(() ->
                        new RuntimeException("이미 존재하는 WalkLog 입니다."));
        return walkLog;
    }
}
