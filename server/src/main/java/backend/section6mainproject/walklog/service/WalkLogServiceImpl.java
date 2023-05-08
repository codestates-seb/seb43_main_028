package backend.section6mainproject.walklog.service;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkLogServiceImpl implements WalkLogService {

    private final WalkLogRepository walkLogRepository;
    private final MemberService memberService;

    @Override
    public WalkLog createWalkLog(Long memberId){
        WalkLog walkLog = new WalkLog();
        Member findVerifiedMember = memberService.findVerifiedMember(memberId);
        walkLog.setMember(findVerifiedMember);
        return walkLogRepository.save(walkLog);
    }
    @Override
    public WalkLog updateWalkLog(WalkLog walkLog){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLog.getWalkLogId());

        Optional.ofNullable(walkLog.getWalkLogPublicSetting())
                .ifPresent(walkLogPublicSetting -> findWalkLog.setWalkLogPublicSetting(walkLogPublicSetting));
        Optional.ofNullable(walkLog.getMessage())
                .ifPresent(message -> findWalkLog.setMessage(message));

        return walkLogRepository.save(findWalkLog);
    }
    @Override
    public WalkLog findWalkLog(Long walkLogId){
        return findVerifiedWalkLog(walkLogId);
    }
    @Override
    public WalkLog findVerifiedWalkLog(Long walkLogId) {
        Optional<WalkLog> findWalkLogById = walkLogRepository.findById(walkLogId);

        WalkLog walkLog =
                findWalkLogById.orElseThrow(() ->
                        new RuntimeException("WalkLog를 찾을 수 없습니다")); //잘못된 문구 수정
        return walkLog;
    }
}
