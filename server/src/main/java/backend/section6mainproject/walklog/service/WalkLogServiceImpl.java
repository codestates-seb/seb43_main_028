package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        updatedWalkLog.setEndAt(LocalDateTime.now());
        updatedWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        return walkLogRepository.save(updatedWalkLog);
    }
    @Override
    public WalkLog findWalkLog(Long walkLogId){
        return findVerifiedWalkLog(walkLogId);
    }

    @Override
    public Page<WalkLog> findWalkLogs(int page, int size, String date){
        PageRequest pageRequest = PageRequest.of(page - 1, size,Sort.by("walkLogId").descending());
        if(date.equals("2000-01-01")) {
            Optional<Page<WalkLog>> response = walkLogRepository.findAllByWalkLogPublicSetting(pageRequest, WalkLog.WalkLogPublicSetting.PUBLIC);
            return response.orElseThrow(() ->
                    new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND));
        }
        if (date.substring(date.length()-2).equals("00")){
            date = date.substring(0,date.length()-2) + "01";
            LocalDate parse = LocalDate.parse(date);
            LocalDateTime start = LocalDateTime.of(parse.withDayOfMonth(1), LocalTime.of(0,0,0));
            LocalDateTime end = LocalDateTime.of(parse.withDayOfMonth(parse.lengthOfMonth()), LocalTime.of(23,59,59));
            return walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetween(pageRequest, WalkLog.WalkLogPublicSetting.PUBLIC,start,end);
        }
        String[] split = date.split("-");
        Integer year = Integer.parseInt(split[0]);
        Integer month = Integer.parseInt(split[1]);
            Integer day = Integer.parseInt(split[2]);
            if(year >= 2000 && year <= 9999 || month >=1 && month <=12 || day >= 1 && day <= 31){
                LocalDateTime start = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(0,0,0));
                LocalDateTime end = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(23,59,59));
                return walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetween(pageRequest,
                        WalkLog.WalkLogPublicSetting.PUBLIC,
                        start,
                        end);
            }else throw new RuntimeException("양식이 일치하지 않습니다.");
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
