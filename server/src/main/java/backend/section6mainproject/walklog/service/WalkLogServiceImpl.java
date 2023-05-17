package backend.section6mainproject.walklog.service;

import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
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
    private final WalkLogMapper walkLogMapper;

    @Override
    public WalkLogServiceDTO.CreateOutput createWalkLog(WalkLogServiceDTO.CreateInput createInput){
        Member findmember = memberService.findVerifiedMember(createInput.getMemberId());
        checkWalkLogRecording(findmember);
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(findmember);
        return walkLogMapper.walkLogToWalkLogServiceCreatedOutputDTO(walkLogRepository.save(walkLog));
    }

    private void checkWalkLogRecording(Member member) {
        Optional<WalkLog> first = findWalkLogByMemberId(member.getMemberId()).stream()
                .filter(walkLog -> walkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.RECORDING))
                .findFirst();
        if(first.isPresent())
            throw new BusinessLogicException(ExceptionCode.WALK_LOG_ALREADY_RECORDING);
    }

    @Override
    public WalkLogServiceDTO.Output updateWalkLog(WalkLogServiceDTO.UpdateInput updateInput){
        WalkLog findWalkLog = findVerifiedWalkLog(updateInput.getWalkLogId());
        checkWalkLogStatusStop(findWalkLog);
        WalkLog updatedWalkLog = beanUtils.copyNonNullProperties(walkLogMapper.walkLogServiceUpdateInputDTOtoWalkLog(updateInput), findWalkLog);
        walkLogRepository.save(updatedWalkLog);

        return walkLogMapper.walkLogToWalkLogServiceOutputDTO(updatedWalkLog);
    }

    private static void checkWalkLogStatusStop(WalkLog findWalkLog) {
        if(!findWalkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.STOP))
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_CHANGE_WALK_LOG);
    }

    @Override
    public WalkLogServiceDTO.Output exitWalkLog(WalkLogServiceDTO.ExitInput exitInput){
        WalkLog findWalkLog = findVerifiedWalkLog(exitInput.getWalkLogId());
        checkWalkLogStatusRecording(findWalkLog);
        WalkLog exitedWalkLog = updateWalkLogExitSetting(beanUtils.copyNonNullProperties(walkLogMapper.walkLogServiceExitInputDTOtoWalkLog(exitInput), findWalkLog));

        return walkLogMapper.walkLogToWalkLogServiceOutputDTO(exitedWalkLog);
    }

    private WalkLog updateWalkLogExitSetting(WalkLog updatedWalkLog) {
        updatedWalkLog.setEndAt(LocalDateTime.now());
        updatedWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLogRepository.save(updatedWalkLog);
    }

    private static void checkWalkLogStatusRecording(WalkLog findWalkLog) {
        if(!findWalkLog.getWalkLogStatus().equals(WalkLog.WalkLogStatus.RECORDING))
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_EXIT_WALK_LOG);
    }

    @Override
    public WalkLogServiceDTO.Output findWalkLog(Long walkLogId){
        WalkLogServiceDTO.Output output = walkLogMapper.walkLogToWalkLogServiceOutputDTO(findVerifiedWalkLog(walkLogId));
        return output;
    }

    @Override
    public Page<WalkLogServiceDTO.FindsOutput> findWalkLogs(WalkLogServiceDTO.FindsInput findsInput){//년월일 int타입으로 나눠서 리팩토링하기
        PageRequest pageRequest = PageRequest.of(findsInput.getPage(),findsInput.getSize(),Sort.by("walkLogId").descending());
        Integer day = findsInput.getDay();
        Integer month = findsInput.getMonth();
        Integer year = findsInput.getYear();
        if(year == null && month == null && day == null) {
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(walkLogRepository.findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC));
// 요청으로 들어온 page와 한 page당 원하는 데이터의 갯수
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;

        }
        if (day == 0){
            day++;
            LocalDate parse = LocalDate.of(year,month,day);
            LocalDateTime start = LocalDateTime.of(parse.withDayOfMonth(1), LocalTime.of(0,0,0));
            LocalDateTime end = LocalDateTime.of(parse.withDayOfMonth(parse.lengthOfMonth()), LocalTime.of(23,59,59));
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetween(WalkLog.WalkLogPublicSetting.PUBLIC, start, end));
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;
        }
        if(year >= 2000 && year <= 9999 || month >=1 && month <=12 || day >= 1 && day <= 31){
            LocalDate parse = LocalDate.of(year,month,day);
            LocalDateTime start = LocalDateTime.of(parse, LocalTime.of(0,0,0));
            LocalDateTime end = LocalDateTime.of(parse, LocalTime.of(23,59,59));
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(
                    walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetween(WalkLog.WalkLogPublicSetting.PUBLIC,
                            start,
                            end));
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;
        }else throw new RuntimeException("양식이 일치하지 않습니다."); //비즈니스로직을 작명해서 새로 추가하기
    }

    private static PageImpl<WalkLogServiceDTO.FindsOutput> listToPage(PageRequest pageRequest, List<WalkLogServiceDTO.FindsOutput> findsOutputs) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), findsOutputs.size());
        PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = new PageImpl<>(findsOutputs.subList(start, end), pageRequest, findsOutputs.size());
        return pageFindsOutput;
    }

    @Override
    public PageInfo createPageInfo(Page<WalkLogServiceDTO.FindsOutput> findsOutputs) {
        return new PageInfo(findsOutputs.getNumber() + 1,
                findsOutputs.getSize(), findsOutputs.getTotalElements(), findsOutputs.getTotalPages());
    }

    @Override
    public Page<WalkLogServiceDTO.FindsOutput> findMyWalkLogs(WalkLogServiceDTO.FindsInput findsInput){//년월일 int타입으로 나눠서 리팩토링하기
        Long memberId = findsInput.getMemberId();
        Integer year = findsInput.getYear();
        Integer month = findsInput.getMonth();
        Integer day = findsInput.getDay();
        memberService.findVerifiedMember(memberId);

        PageRequest pageRequest = PageRequest.of(findsInput.getPage(),findsInput.getSize(),Sort.by("walkLogId").descending());
        if(year == null && month == null && day == null) {
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(walkLogRepository.findAllByWalkLogPublicSettingAndMember_MemberId(pageRequest,
                    WalkLog.WalkLogPublicSetting.PUBLIC,
                    memberId));
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;

        }
        if (day == 0){
            day++;
            LocalDate parse = LocalDate.of(year,month,day);
            LocalDateTime start = LocalDateTime.of(parse.withDayOfMonth(1), LocalTime.of(0,0,0));
            LocalDateTime end = LocalDateTime.of(parse.withDayOfMonth(parse.lengthOfMonth()), LocalTime.of(23,59,59));
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(
                    walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetweenAndMember_MemberId(WalkLog.WalkLogPublicSetting.PUBLIC,
                            start,
                            end,
                            memberId));
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;
        }
        if(year >= 2000 && year <= 9999 || month >=1 && month <=12 || day >= 1 && day <= 31){
            LocalDate parse = LocalDate.of(year,month,day);
            LocalDateTime start = LocalDateTime.of(parse, LocalTime.of(0,0,0));
            LocalDateTime end = LocalDateTime.of(parse, LocalTime.of(23,59,59));
            List<WalkLogServiceDTO.FindsOutput> findsOutputs = walkLogMapper.walkLogsToWalkLogServiceFindsOutputDTOs(
                    walkLogRepository.findAllByWalkLogPublicSettingAndCreatedAtBetweenAndMember_MemberId(WalkLog.WalkLogPublicSetting.PUBLIC,
                            start,
                            end,
                            memberId));
            PageImpl<WalkLogServiceDTO.FindsOutput> pageFindsOutput = listToPage(pageRequest, findsOutputs);
            return pageFindsOutput;
        }else throw new RuntimeException("양식이 일치하지 않습니다."); //비즈니스로직을 작명해서 새로 추가하기
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
        return walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(memberId);

    }
}
