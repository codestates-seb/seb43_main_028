package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        Optional<WalkLog> first = findWalkLogByMemberId(memberId).stream()
                .filter(walkLog -> walkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.RECORDING))
                .findFirst();
        if(first.isPresent())
            throw new BusinessLogicException(ExceptionCode.WALK_LOG_ALREADY_RECORDING);
        WalkLog walkLog = new WalkLog();
        Member findVerifiedMember = memberService.findVerifiedMember(memberId);
        walkLog.setMember(findVerifiedMember);
        return walkLogRepository.save(walkLog);
    }
    @Override
    public WalkLog updateWalkLog(WalkLog walkLog){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLog.getWalkLogId());

        if(!findWalkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.STOP))
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_CHANGE_WALK_LOG);

        WalkLog updatedWalkLog = beanUtils.copyNonNullProperties(walkLog, findWalkLog);

        return walkLogRepository.save(updatedWalkLog);
    }
    @Override
    public WalkLog exitWalkLog(WalkLog walkLog){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLog.getWalkLogId());
        if(!findWalkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.RECORDING))
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_EXIT_WALK_LOG);
        WalkLog updatedWalkLog = beanUtils.copyNonNullProperties(walkLog, findWalkLog);
        updatedWalkLog.setEndTime(LocalDateTime.now());
        updatedWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        return walkLogRepository.save(updatedWalkLog);
    }
    @Override
    public WalkLog findWalkLog(Long walkLogId){
        return findVerifiedWalkLog(walkLogId);
    }

    @Override
    public Page<WalkLog> findWalkLogs(int page, int size){
        Optional<List<WalkLog>> allByWalkLogPublicSetting = walkLogRepository.findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        List<WalkLog> walkLogs = allByWalkLogPublicSetting.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), walkLogs.size());
        return new PageImpl<>(walkLogs.subList(start, end), pageRequest, walkLogs.size());

    }
    @Override
    public void deleteWalkLog(Long walkLogId){
        WalkLog findWalkLog = findVerifiedWalkLog(walkLogId);
        walkLogRepository.delete(findWalkLog);
    }

    @Override
    public WalkLog findVerifiedWalkLog(Long walkLogId) {
        Optional<WalkLog> findWalkLogById = walkLogRepository.findById(walkLogId);
        return findWalkLogById.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND));
    }
    private List<WalkLog> findWalkLogByMemberId(Long memberId){
        Optional<List<WalkLog>> walkLogsByMemberId = walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(memberId);
        return walkLogsByMemberId.orElseThrow(() -> new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND));
    }
}
