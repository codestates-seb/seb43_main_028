package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
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
    private final CustomBeanUtils<WalkLog> beanUtils;

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

        WalkLog updatedWalkLog = beanUtils.copyNonNullProperties(walkLog, findWalkLog);

        return walkLogRepository.save(updatedWalkLog);
    }
    public WalkLog exitWalkLog(WalkLog walkLog){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLog.getWalkLogId());
        if(!findWalkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.RECORDING))
            throw new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_RECORDING);

        WalkLog updatedWalkLog = beanUtils.copyNonNullProperties(walkLog, findWalkLog);
        updatedWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        return walkLogRepository.save(updatedWalkLog);
    }
    @Override
    public WalkLog findWalkLog(Long walkLogId){
        return findVerifiedWalkLog(walkLogId);
    }

    @Override
    public void deleteWalkLog(Long walkLogId){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLogId);
        walkLogRepository.delete(findWalkLog);
    }

    @Override
    public WalkLog findVerifiedWalkLog(Long walkLogId) {
        Optional<WalkLog> findWalkLogById = walkLogRepository.findById(walkLogId);

        WalkLog walkLog =
                findWalkLogById.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND)); //잘못된 문구 수정
        return walkLog;
    }
}
