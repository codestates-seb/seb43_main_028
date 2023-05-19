package backend.section6mainproject.walklog.service;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    private final WalkLogContentRepository walkLogContentRepository;
    private final MemberService memberService;
    private final CustomBeanUtils<WalkLog> beanUtils;
    private final WalkLogMapper walkLogMapper;
    private final StorageService storageService;
    private final int FIRST_PAGE_SETTING= 1;

    @Override
    public WalkLogServiceDTO.CreateOutput createWalkLog(WalkLogServiceDTO.CreateInput createInput){
        Member findmember = memberService.findVerifiedMember(createInput.getMemberId());
        checkWalkLogRecording(findmember);
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(findmember);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
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
        //WalkLogContent들을 조회
        //만약 walkLogContrent들이 존재하면 리스트들을 돌면서 가장 첫번째 파일의 이미지 url주소를 mapImage에 반환
        String mapImage = storageService.store(exitInput.getMapImage(), "mapImage");
        WalkLog walkLog = walkLogMapper.walkLogServiceExitInputDTOtoWalkLog(exitInput);
        WalkLog exitedWalkLog =
                beanUtils.copyNonNullProperties(walkLog, findWalkLog);
        updateWalkLogExitSetting(mapImage, exitedWalkLog);

        return walkLogMapper.walkLogToWalkLogServiceOutputDTO(exitedWalkLog);
    }

    private void updateWalkLogExitSetting(String mapImage, WalkLog exitedWalkLog) {
        exitedWalkLog.setEndAt(LocalDateTime.now());
        exitedWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        exitedWalkLog.setMapImage(mapImage);
        walkLogRepository.save(exitedWalkLog);
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
    public List<WalkLogServiceDTO.CalenderFindOutput> findMyMonthWalkLogs(WalkLogServiceDTO.CalenderFindInput totalFindsInput){
        List<WalkLog> myWalkLogFromMonthForCalendar =
                walkLogRepository.findMyWalkLogFromMonthForCalendar(totalFindsInput.getMemberId(), totalFindsInput.getYear(), totalFindsInput.getMonth());
        return walkLogMapper.walkLogsToWalkLogServiceCalenderFindOutputDTOs(myWalkLogFromMonthForCalendar);
    }

    @Override
    public PageInfo createPageInfo(Page<WalkLogServiceDTO.FindOutput> findsOutputs) {
        return new PageInfo(findsOutputs.getNumber() + 1,
                findsOutputs.getSize(), findsOutputs.getTotalElements(), findsOutputs.getTotalPages());
    }

    @Override
    public Page<WalkLogServiceDTO.FindOutput> findMyWalkLogs(WalkLogServiceDTO.FindInput findInput){//년월일 int타입으로 나눠서 리팩토링하기
        Long memberId = findInput.getMemberId();
        Integer year = findInput.getYear();
        Integer month = findInput.getMonth();
        Integer day = findInput.getDay();
        PageRequest pageRequest =
                PageRequest.of(findInput.getPage()-FIRST_PAGE_SETTING, findInput.getSize(),Sort.by("walkLogId").descending());
        memberService.findVerifiedMember(memberId);
        checkInputError(year, month, day);
        if(year == null)
            return walkLogRepository.findAllByMember_MemberId(pageRequest, memberId)
                            .map(walkLogMapper::walkLogToWalkLogServiceFindOutputDTO);
        else if (month == null)
            return walkLogRepository.findAllByMyWalkLogFromYear(pageRequest,memberId,year)
                    .map(walkLogMapper::walkLogToWalkLogServiceFindOutputDTO);
        else if (day == null)
            return walkLogRepository.findMyWalkLogFromMonth(pageRequest,memberId, year, month)
                    .map(walkLogMapper::walkLogToWalkLogServiceFindOutputDTO);
        else
            return walkLogRepository.findAllByMyWalkLogFromDay(pageRequest,memberId, year, month, day)
                    .map(walkLogMapper::walkLogToWalkLogServiceFindOutputDTO);
    }

    private static void checkInputError(Integer year, Integer month, Integer day) {
        if(year == null){
            if(month != null)
                throw new BusinessLogicException(ExceptionCode.WRONG_MONTH_INPUT);
            if(day != null)
                throw new BusinessLogicException(ExceptionCode.WRONG_DAY_INPUT);
        }else if(month == null){
            if(day != null)
                throw new BusinessLogicException(ExceptionCode.WRONG_DAY_INPUT);
        }
    }
    @Override
    public Page<WalkLogServiceDTO.FindFeedOutput> findFeedWalkLogs(WalkLogServiceDTO.FindFeedInput findFeedInput){
        PageRequest pageRequest = PageRequest.of(findFeedInput.getPage() - 1, findFeedInput.getSize(),Sort.by("walkLogId").descending());
        return walkLogRepository.findAllByWalkLogPublicSetting(pageRequest, WalkLog.WalkLogPublicSetting.PUBLIC)
                .map(walkLogMapper::walkLogToWalkLogServiceFindFeedOutputDTO);
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
