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
    private WalkLogStubData stubData;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private WalkLogServiceImpl walkLogService;
    @BeforeEach
    void init() {
        stubData = new WalkLogStubData();
    }

    @Test
    public void createWalkLogTest() {
        // given

        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());
        WalkLogServiceDTO.CreateInput createInput = stubData.getCreateInput();
        WalkLogServiceDTO.CreateOutput createOutput = stubData.getCreateOutput();



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

    @Test
    public void shouldThrowExceptionWhenWalkLogAlreadyRecordingTest() {
        //given
        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());
        WalkLogServiceDTO.CreateInput createInput = stubData.getCreateInput();
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
        WalkLog walkLog = stubData.getStopWalkLog(stubData.getMember());
        WalkLogServiceDTO.UpdateInput updateInput = stubData.getUpdateInput();
        WalkLogServiceDTO.Output output = stubData.getOutput();


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
        WalkLogServiceDTO.UpdateInput updateInput = stubData.getUpdateInput();

        given(walkLogRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Not Found");
    }

    @Test
    public void exitWalkLogTest() throws IOException {

        // given
        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());
        WalkLogServiceDTO.ExitInput exitInput = stubData.getExitInput();
        WalkLogServiceDTO.Output output = stubData.getOutput();

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
        WalkLogServiceDTO.UpdateInput updateInput = stubData.getUpdateInput();
        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(updateInput);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Change");
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogNotRecordingTest() throws IOException {
        //given
        WalkLogServiceDTO.ExitInput exitInput = stubData.getExitInput();
        WalkLog walkLog = stubData.getStopWalkLog(stubData.getMember());

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
        WalkLogServiceDTO.Output output = stubData.getOutput();

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new WalkLog()));
        given(walkLogMapper.walkLogToWalkLogServiceOutputDTO(Mockito.any(WalkLog.class))).willReturn(output);

        WalkLogServiceDTO.Output result = walkLogService.findWalkLog(1L);

        Assertions.assertEquals(output, result);
    }

    @Test
    public void deleteWalkLogTest() {
        // Given
        WalkLog walkLog = stubData.getStopWalkLog(stubData.getMember());

        // When
        when(walkLogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(walkLog));
        doNothing().when(walkLogRepository).delete(Mockito.any(WalkLog.class));
        walkLogService.deleteWalkLog(walkLog.getWalkLogId());

        // Then
        verify(walkLogRepository, times(1)).delete(Mockito.any(WalkLog.class));
    }
    @Test
    public void findMyWalkLogsTest(){
        WalkLogServiceDTO.FindInput findInput = stubData.getFindInput();
        WalkLogServiceDTO.FindOutput findOutput = stubData.getFindOutput();

        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(stubData.getStopWalkLog(stubData.getMember()));
        walkLogs.add(stubData.getAnotherWalkLog(stubData.getMember()));

        given(walkLogRepository.findAllByMyWalkLogFromDay(Mockito.any(PageRequest.class),anyLong(),anyInt(),anyInt(),anyInt()))
                .willReturn(new PageImpl<>(walkLogs));
        given(walkLogMapper.walkLogToWalkLogServiceFindOutputDTO(Mockito.any(WalkLog.class))).willReturn(findOutput);
        Page<WalkLogServiceDTO.FindOutput> result = walkLogService.findMyWalkLogs(findInput);

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getWalkLogId()).isEqualTo(findOutput.getWalkLogId());
        assertThat(result.getContent().get(0).getMessage()).isEqualTo(findOutput.getMessage());

    }
    @Test
    public void findFeedWalkLogsTest(){
        WalkLogServiceDTO.FindFeedInput findFeedInput = stubData.getFindFeedInput();
        WalkLogServiceDTO.FindFeedOutput findFeedOutput = stubData.getFindFeedOutput();

        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(stubData.getStopWalkLog(stubData.getMember()));
        walkLogs.add(stubData.getAnotherWalkLog(stubData.getMember()));

        given(walkLogRepository.findAllByWalkLogPublicSetting(Mockito.any(PageRequest.class),Mockito.any(WalkLog.WalkLogPublicSetting.class)))
                .willReturn(new PageImpl<>(walkLogs));
        given(walkLogMapper.walkLogToWalkLogServiceFindFeedOutputDTO(Mockito.any(WalkLog.class))).willReturn(findFeedOutput);

        Page<WalkLogServiceDTO.FindFeedOutput> result = walkLogService.findFeedWalkLogs(findFeedInput);

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getWalkLogId()).isEqualTo(findFeedOutput.getWalkLogId());
        assertThat(result.getContent().get(0).getMessage()).isEqualTo(findFeedOutput.getMessage());

    }
    @Test
    public void checkInputErrorTestWrongDay(){
        WalkLogServiceDTO.FindInput findInput = stubData.getFindInput();
        findInput.setMonth(null);
        findInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Month or Year Not Input");
    }
    @Test
    public void checkInputErrorTestWrongMonth(){
        WalkLogServiceDTO.FindInput findInput = stubData.getFindInput();
        findInput.setYear(null);

        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.findMyWalkLogs(findInput);
        });
        assertThat(exception.getMessage()).isEqualTo("Not Input Year");
    }
    @Test
    public void findMyMonthWalkLogsTest(){
        WalkLogServiceDTO.CalenderFindInput calenderFindInput = stubData.getCalenderFindInput();
        List<WalkLogServiceDTO.CalenderFindOutput> calenderFindOutputs = new ArrayList<>();
        calenderFindOutputs.add(stubData.getCalenderFindOutput(1L));
        calenderFindOutputs.add(stubData.getCalenderFindOutput(2L));


        given(walkLogRepository.findMyWalkLogFromMonthForCalendar(anyLong(),anyInt(),anyInt())).willReturn(new ArrayList<>());
        given(walkLogMapper.walkLogsToWalkLogServiceCalenderFindOutputDTOs(Mockito.anyList())).willReturn(calenderFindOutputs);

        List<WalkLogServiceDTO.CalenderFindOutput> result = walkLogService.findMyMonthWalkLogs(calenderFindInput);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getWalkLogId()).isEqualTo(calenderFindOutputs.get(0).getWalkLogId());
        assertThat(result.get(1).getWalkLogId()).isEqualTo(calenderFindOutputs.get(1).getWalkLogId());
    }
}