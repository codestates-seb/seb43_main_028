package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.WalkLogStubData;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private WalkLogStubData walkLogStubData;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private WalkLogServiceImpl walkLogService;
    @BeforeEach
    void init() {
        walkLogStubData = new WalkLogStubData();
    }

    @DisplayName("걷기기록 생성 테스트")
    @Test
    public void createWalkLogTest() {
        // given

        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());
        WalkLogServiceDTO.CreateInput createInput = walkLogStubData.getCreateInput();
        WalkLogServiceDTO.CreateOutput createOutput = walkLogStubData.getCreateOutput();



        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(walkLog.getMember());
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

    @DisplayName("걷기기록 생성시 기록중인 걷기기록이 있을 때 에러발생 테스트")
    @Test
    public void shouldThrowExceptionWhenWalkLogAlreadyRecordingTest() {
        //given
        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());
        WalkLogServiceDTO.CreateInput createInput = walkLogStubData.getCreateInput();
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

    @DisplayName("걷기기록 수정 테스트")
    @Test
    public void updateWalkLogTest(){

        // given
        //WalkLog객체 추가
        WalkLog walkLog = walkLogStubData.getStopWalkLog(walkLogStubData.getMember());
        WalkLogServiceDTO.UpdateInput updateInput = walkLogStubData.getUpdateInput();
        WalkLogServiceDTO.Output output = walkLogStubData.getOutput();


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
    }

    @DisplayName("걷기기록 수정시 요청보낸 걷기기록이 없을 경우 에러발생 테스트")
    @Test
    public void shouldThrowExceptionWhenWalkLogNotFoundTest() {
        //given
        WalkLogServiceDTO.UpdateInput updateInput = walkLogStubData.getUpdateInput();

        given(walkLogRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Not Found");
    }

    @DisplayName("걷기기록 수정시 기록중인 걷기기록이 있을경우 에러발생 테스트")
    @Test
    public void shouldThrowExceptionWhenWalkLogRecordingTest() {
        //given
        WalkLogServiceDTO.UpdateInput updateInput = walkLogStubData.getUpdateInput();
        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Change");
    }

    @DisplayName("걷기기록 종료 테스트")
    @Test
    public void exitWalkLogTest() throws IOException {

        // given
        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());
        WalkLogServiceDTO.ExitInput exitInput = walkLogStubData.getExitInput();
        WalkLogServiceDTO.Output output = walkLogStubData.getOutput();

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(walkLogMapper.walkLogServiceExitInputDTOtoWalkLog(Mockito.any(WalkLogServiceDTO.ExitInput.class))).willReturn(walkLog);
        given(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString(), Mockito.anyBoolean())).willReturn("mapImage");
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

    @DisplayName("걷기기록 종료시 해당 걷기기록의 상태가 기록종료 상태일경우 에러발생 테스트")
    @Test
    public void shouldThrowExceptionWhenWalkLogNotRecordingTest() throws IOException {
        //given
        WalkLogServiceDTO.ExitInput exitInput = walkLogStubData.getExitInput();
        WalkLog walkLog = walkLogStubData.getStopWalkLog(walkLogStubData.getMember());

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.exitWalkLog(exitInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Exit");
    }

    @DisplayName("걷기기록 1개 조회 테스트")
    @Test
    public void findWalkLogTest(){
        WalkLogServiceDTO.GetOutput getOutput = walkLogStubData.getGetOutput();

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new WalkLog()));
        given(walkLogMapper.walkLogToWalkLogServiceGetOutPutDTO(Mockito.any(WalkLog.class))).willReturn(getOutput);

        WalkLogServiceDTO.GetOutput result = walkLogService.findWalkLog(1L);

        Assertions.assertEquals(getOutput, result);
    }

    @DisplayName("걷기기록 삭제 테스트")
    @Test
    public void deleteWalkLogTest() {
        // Given
        WalkLog walkLog = walkLogStubData.getStopWalkLog(walkLogStubData.getMember());

        // When
        when(walkLogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(walkLog));
        doNothing().when(walkLogRepository).delete(Mockito.any(WalkLog.class));
        walkLogService.deleteWalkLog(walkLog.getWalkLogId());

        // Then
        verify(walkLogRepository, times(1)).delete(Mockito.any(WalkLog.class));
    }
    @DisplayName("내 걷기기록 전체조회 테스트")
    @Test
    public void findMyWalkLogsTest(){
        WalkLogServiceDTO.FindInput findInput = walkLogStubData.getFindInput();
        WalkLogServiceDTO.FindOutput findOutput = walkLogStubData.getFindOutput();

        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(walkLogStubData.getStopWalkLog(walkLogStubData.getMember()));
        walkLogs.add(walkLogStubData.getAnotherWalkLog(walkLogStubData.getMember()));

        given(walkLogRepository.findAllByMyWalkLogFromDay(Mockito.any(PageRequest.class),anyLong(),anyInt(),anyInt(),anyInt()))
                .willReturn(new PageImpl<>(walkLogs));
        given(walkLogMapper.walkLogToWalkLogServiceFindOutputDTO(Mockito.any(WalkLog.class))).willReturn(findOutput);
        Page<WalkLogServiceDTO.FindOutput> result = walkLogService.findMyWalkLogs(findInput);

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getWalkLogId()).isEqualTo(findOutput.getWalkLogId());
        assertThat(result.getContent().get(0).getMessage()).isEqualTo(findOutput.getMessage());

    }
    @DisplayName("걷기기록 전체조회(피드조회용) 테스트")
    @Test
    public void findFeedWalkLogsTest(){
        WalkLogServiceDTO.FindFeedInput findFeedInput = walkLogStubData.getFindFeedInput();
        WalkLogServiceDTO.FindFeedOutput findFeedOutput = walkLogStubData.getFindFeedOutput();

        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(walkLogStubData.getStopWalkLog(walkLogStubData.getMember()));
        walkLogs.add(walkLogStubData.getAnotherWalkLog(walkLogStubData.getMember()));

        given(walkLogRepository.findAllByWalkLogPublicSetting(Mockito.any(PageRequest.class),Mockito.any(WalkLog.WalkLogPublicSetting.class)))
                .willReturn(new PageImpl<>(walkLogs));
        given(walkLogMapper.walkLogToWalkLogServiceFindFeedOutputDTO(Mockito.any(WalkLog.class))).willReturn(findFeedOutput);

        Page<WalkLogServiceDTO.FindFeedOutput> result = walkLogService.findFeedWalkLogs(findFeedInput);

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getWalkLogId()).isEqualTo(findFeedOutput.getWalkLogId());
        assertThat(result.getContent().get(0).getMessage()).isEqualTo(findFeedOutput.getMessage());

    }
    @DisplayName("내 걷기기록 전체조회 Day만을 입력했을 경우 에러 발생 테스트")
    @Test
    public void checkInputErrorTestWrongDay(){
        WalkLogServiceDTO.FindInput findInput = walkLogStubData.getFindInput();
        findInput.setMonth(null);
        findInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Month or Year Not Input");
    }
    @DisplayName("내 걷기기록 전체조회 Month만을 입력했을 경우 에러 발생 테스트")
    @Test
    public void checkInputErrorTestWrongMonth(){
        WalkLogServiceDTO.FindInput findInput = walkLogStubData.getFindInput();
        findInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Not Input Year");
    }
    @DisplayName("내 걷기 기록 전체조회(달력조회용) 테스트")
    @Test
    public void findMyMonthWalkLogsTest(){
        WalkLogServiceDTO.CalenderFindInput calenderFindInput = walkLogStubData.getCalenderFindInput();
        List<WalkLogServiceDTO.CalenderFindOutput> calenderFindOutputs = new ArrayList<>();
        calenderFindOutputs.add(walkLogStubData.getCalenderFindOutput(1L));
        calenderFindOutputs.add(walkLogStubData.getCalenderFindOutput(2L));


        given(walkLogRepository.findMyWalkLogFromMonthForCalendar(anyLong(),anyInt(),anyInt())).willReturn(new ArrayList<>());
        given(walkLogMapper.walkLogsToWalkLogServiceCalenderFindOutputDTOs(Mockito.anyList())).willReturn(calenderFindOutputs);

        List<WalkLogServiceDTO.CalenderFindOutput> result = walkLogService.findMyMonthWalkLogs(calenderFindInput);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getWalkLogId()).isEqualTo(calenderFindOutputs.get(0).getWalkLogId());
        assertThat(result.get(1).getWalkLogId()).isEqualTo(calenderFindOutputs.get(1).getWalkLogId());
    }
}