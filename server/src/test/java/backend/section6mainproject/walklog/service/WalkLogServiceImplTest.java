package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
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
import org.mockito.junit.jupiter.MockitoExtension;

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


    @InjectMocks
    private WalkLogServiceImpl walkLogService;

    @Test
    public void createWalkLogTest() {
        // given
        WalkLog walkLog = createWalkLog();

        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput();
        createInput.setMemberId(walkLog.getMember().getMemberId());
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
        WalkLog walkLog = createWalkLog();
        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput();
        createInput.setMemberId(walkLog.getMember().getMemberId());
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
        WalkLog walkLog = createWalkLog();
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

    @Test
    public void exitWalkLogTest(){

        // given
        //WalkLog객체 추가
        WalkLog walkLog = createWalkLog();
        //수정용 WalkLog객체 생성
        WalkLogServiceDTO.ExitInput exitInput = new WalkLogServiceDTO.ExitInput();
        exitInput.setMessage("hi!");
        exitInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        exitInput.setWalkLogId(1L);
        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setMessage("hi!");
        output.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        output.setWalkLogId(1L);
        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new WalkLog()));
        given(walkLogMapper.walkLogServiceExitInputDTOtoWalkLog(Mockito.any(WalkLogServiceDTO.ExitInput.class))).willReturn(new WalkLog());
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
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(updateInput.getWalkLogId());

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
        WalkLog walkLog = createWalkLog();

        // When
        when(walkLogRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(walkLog));
        doNothing().when(walkLogRepository).delete(Mockito.any(WalkLog.class));
        walkLogService.deleteWalkLog(walkLog.getWalkLogId());

        // Then
        verify(walkLogRepository, times(1)).delete(Mockito.any(WalkLog.class));

    }
    private static WalkLog createWalkLog() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("admin1@gmail.com");
        member.setPassword("12345");
        member.setNickname("거터볼래1");
        member.setIntroduction("안녕하세요1");
        //WalkLog 객체 추가
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(1L);
        walkLog.setMessage("안녕하십니까");
        return walkLog;
    }
}