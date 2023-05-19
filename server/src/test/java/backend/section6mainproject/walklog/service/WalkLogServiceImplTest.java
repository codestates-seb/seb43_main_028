package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalkLogServiceImplTest {

    @Mock
    private WalkLogRepository walkLogRepository;

    @Mock
    private MemberService memberService;
    @Mock
    private WalkLogMapper walkLogMapper;
    @Mock
    private CustomBeanUtils<WalkLog> beanUtils;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private WalkLogServiceImpl walkLogService;

    @Test
    public void createWalkLogTest() {
        // given
        WalkLog walkLog = createWalkLog(1L);

        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput(walkLog.getMember().getMemberId());
        WalkLogServiceDTO.CreateOutput createOutput = new WalkLogServiceDTO.CreateOutput();
        createOutput.setWalkLogId(walkLog.getWalkLogId());
        List<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(walkLog);
        Member member = new Member();
        member.setMemberId(2L);


        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(member);
        given(walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(Mockito.anyLong()))
                .willReturn(new ArrayList<>());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogMapper.walkLogToWalkLogServiceCreatedOutputDTO(Mockito.any(WalkLog.class))).willReturn(createOutput);
        // when
        WalkLogServiceDTO.CreateOutput result = walkLogService.createWalkLog(createInput);


        // then
        assertNotNull(createInput);
        assertEquals(result.getWalkLogId(), walkLog.getWalkLogId());
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogAlreadyRecordingTest() {
        //given
        WalkLog walkLog = createWalkLog(1L);
        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput(walkLog.getMember().getMemberId());
        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(walkLog);
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(walkLog.getMember());
        given(walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(Mockito.anyLong())).willReturn(walkLogs);
        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.createWalkLog(createInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog already recording");
    }

    @Test
    public void updateWalkLogTest(){

        // given
        //WalkLog객체 추가
        WalkLog walkLog = createWalkLog(1L);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        WalkLogServiceDTO.UpdateInput updateInput = new WalkLogServiceDTO.UpdateInput();
        updateInput.setWalkLogId(walkLog.getWalkLogId());
        updateInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        updateInput.setMessage("안녕하세용1");

        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setWalkLogId(walkLog.getWalkLogId());
        output.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        output.setMessage("안녕하세용1");



        given(walkLogMapper.walkLogServiceUpdateInputDTOtoWalkLog(Mockito.any(WalkLogServiceDTO.UpdateInput.class))).willReturn(new WalkLog());
        given(beanUtils.copyNonNullProperties(Mockito.any(WalkLog.class),Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(walkLogMapper.walkLogToWalkLogServiceOutputDTO(Mockito.any(WalkLog.class))).willReturn(output);
        //when
        WalkLogServiceDTO.Output result = walkLogService.updateWalkLog(updateInput);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getWalkLogId()).isEqualTo(walkLog.getWalkLogId());
        assertThat(result.getMessage()).isEqualTo(output.getMessage());
//        assertThat(result.getWalkLogPublicSetting()).isEqualTo(String.valueOf(output.getWalkLogPublicSetting()));
    }
    @Test
    public void shouldThrowExceptionWhenWalkLogNotFoundTest() {
        //given
        WalkLogServiceDTO.UpdateInput updateInput = new WalkLogServiceDTO.UpdateInput();
        updateInput.setWalkLogId(1L);

        given(walkLogRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Not Found");
    }

//    @Test 테스트 리팩토링을 새로 열어서 변경하도록 하겠습니다.
    public void exitWalkLogTest(){

        // given
        //WalkLog객체 추가
        WalkLog walkLog = createWalkLog(1L);
        walkLog.setMapImage("");
        //수정용 WalkLog객체 생성
        WalkLogServiceDTO.ExitInput exitInput = new WalkLogServiceDTO.ExitInput();
        exitInput.setMessage("hi!");
        exitInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        exitInput.setWalkLogId(1L);
        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setMessage("hi!");
        output.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        output.setWalkLogId(1L);
        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(walkLogMapper.walkLogServiceExitInputDTOtoWalkLog(Mockito.any(WalkLogServiceDTO.ExitInput.class))).willReturn(walkLog);
        given(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("mapImage");
        given(beanUtils.copyNonNullProperties(Mockito.any(WalkLog.class),Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogMapper.walkLogToWalkLogServiceOutputDTO(Mockito.any(WalkLog.class))).willReturn(output);

        //when
        WalkLogServiceDTO.Output result = walkLogService.exitWalkLog(exitInput);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getWalkLogId()).isEqualTo(exitInput.getWalkLogId());
        assertThat(result.getMessage()).isEqualTo(exitInput.getMessage());
        assertThat(result.getWalkLogPublicSetting()).isEqualTo(exitInput.getWalkLogPublicSetting());
    }
    @Test
    public void shouldThrowExceptionWhenWalkLogRecordingTest() {
        //given
        WalkLogServiceDTO.UpdateInput updateInput = new WalkLogServiceDTO.UpdateInput();
        updateInput.setWalkLogId(1L);
        WalkLog walkLog = createWalkLog(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Change");
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogNotRecordingTest() {
        //given
        WalkLogServiceDTO.ExitInput exitInput = new WalkLogServiceDTO.ExitInput();
        exitInput.setWalkLogId(1L);
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(exitInput.getWalkLogId());
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.exitWalkLog(exitInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Exit");
    }

    @Test
    public void findWalkLogTest(){
        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setWalkLogId(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new WalkLog()));
        given(walkLogMapper.walkLogToWalkLogServiceOutputDTO(Mockito.any(WalkLog.class))).willReturn(output);

        WalkLogServiceDTO.Output result = walkLogService.findWalkLog(1L);

        Assertions.assertEquals(output, result);
    }

    @Test
    public void deleteWalkLogTest() {
        // Given
        WalkLog walkLog = createWalkLog(1L);

        // When
        when(walkLogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(walkLog));
        doNothing().when(walkLogRepository).delete(Mockito.any(WalkLog.class));
        walkLogService.deleteWalkLog(walkLog.getWalkLogId());

        // Then
        verify(walkLogRepository, times(1)).delete(Mockito.any(WalkLog.class));
    }
    @Test
    public void findMyWalkLogsTest(){
        WalkLogServiceDTO.FindsInput findsInput = createFindsInput();
        WalkLogServiceDTO.FindsOutput findsOutput = new WalkLogServiceDTO.FindsOutput();
        findsOutput.setWalkLogId(1L);
        findsOutput.setMessage("dd");
        findsOutput.setStartedAt(LocalDateTime.now());
        findsOutput.setEndAt(LocalDateTime.now());
        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        WalkLog walkLog = createWalkLog(1L);
        WalkLog walkLog2 = createWalkLog(2L);
        walkLogs.add(walkLog);
        walkLogs.add(walkLog2);

        given(walkLogRepository.findAllByMyWalkLogFromDay(Mockito.any(PageRequest.class),anyLong(),anyInt(),anyInt(),anyInt()))
                .willReturn(new PageImpl<>(walkLogs));
        given(walkLogMapper.walkLogToWalkLogServiceFindsOutputDTO(Mockito.any(WalkLog.class))).willReturn(findsOutput);
        Page<WalkLogServiceDTO.FindsOutput> result = walkLogService.findMyWalkLogs(findsInput);

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getWalkLogId()).isEqualTo(findsOutput.getWalkLogId());
        assertThat(result.getContent().get(0).getMessage()).isEqualTo(findsOutput.getMessage());

    }
    @Test
    public void checkInputErrorTestWrongDay(){
        WalkLogServiceDTO.FindsInput findsInput = createFindsInput();
        findsInput.setMonth(null);
        findsInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findsInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Month or Year Not Input");
    }
    @Test
    public void checkInputErrorTestWrongMonth(){
        WalkLogServiceDTO.FindsInput findsInput = createFindsInput();
        findsInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findsInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Not Input Year");
    }
    @Test
    public void findMyMonthWalkLogsTest(){
        WalkLogServiceDTO.CalenderFindsInput calenderFindsInput = new WalkLogServiceDTO.CalenderFindsInput();
        calenderFindsInput.setMemberId(1L);
        calenderFindsInput.setYear(LocalDateTime.now().getYear());
        calenderFindsInput.setMonth(LocalDateTime.now().getMonthValue());
        List<WalkLogServiceDTO.CalenderFindsOutput> calenderFindsOutputs = new ArrayList<>();
        WalkLogServiceDTO.CalenderFindsOutput calenderFindsOutput = new WalkLogServiceDTO.CalenderFindsOutput();
        calenderFindsOutput.setWalkLogId(1L);
        WalkLogServiceDTO.CalenderFindsOutput calenderFindsOutput2 = new WalkLogServiceDTO.CalenderFindsOutput();
        calenderFindsOutput2.setWalkLogId(2L);
        calenderFindsOutputs.add(calenderFindsOutput);
        calenderFindsOutputs.add(calenderFindsOutput2);
        given(walkLogRepository.findMyWalkLogFromMonthForCalendar(anyLong(),anyInt(),anyInt())).willReturn(new ArrayList<>());
        given(walkLogMapper.walkLogsToWalkLogServiceCalenderFindsOutputDTOs(Mockito.anyList())).willReturn(calenderFindsOutputs);

        List<WalkLogServiceDTO.CalenderFindsOutput> result = walkLogService.findMyMonthWalkLogs(calenderFindsInput);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getWalkLogId()).isEqualTo(calenderFindsOutput.getWalkLogId());
        assertThat(result.get(1).getWalkLogId()).isEqualTo(calenderFindsOutput2.getWalkLogId());
    }


    private static WalkLogServiceDTO.FindsInput createFindsInput() {
        WalkLogServiceDTO.FindsInput findsInput = new WalkLogServiceDTO.FindsInput();
        findsInput.setMemberId(1L);
        findsInput.setPage(1);
        findsInput.setSize(3);
        findsInput.setYear(LocalDateTime.now().getYear());
        findsInput.setMonth(LocalDateTime.now().getMonthValue());
        findsInput.setDay(LocalDateTime.now().getDayOfMonth());
        return findsInput;
    }

    private static WalkLog createWalkLog(Long num) {
        Long memberId = num;
        Member member = new Member();
        member.setMemberId(num);
        member.setEmail("admin"+num+"@gmail.com");
        member.setPassword("12345");
        member.setNickname("닉네임"+num);
        member.setIntroduction("안녕하세요"+num);
        //WalkLog 객체 추가
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(num);
        walkLog.setMessage("메세지"+num);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        return walkLog;
    }
}