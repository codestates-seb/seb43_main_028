package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.entity.WalkLog;
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
    private CustomBeanUtils<WalkLog> beanUtils;


    @InjectMocks
    private WalkLogServiceImpl walkLogService;

    @Test
    public void createWalkLogTest() {
        // given
        WalkLog walkLog = createWalkLog();
        given(walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(Mockito.anyLong())).willReturn(Optional.of(new ArrayList<>()));
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(walkLog.getMember());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(walkLog);
        // when
        WalkLog createdWalkLog = walkLogService.createWalkLog(walkLog.getMember().getMemberId());


        // then
        assertNotNull(createdWalkLog);
        assertEquals(walkLog.getMember(), createdWalkLog.getMember());
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogAlreadyRecordingTest() {
        //given
        WalkLog walkLog = new WalkLog();
        Long memberId = 1L;
        walkLog.setWalkLogId(memberId);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        walkLogs.add(walkLog);

        given(walkLogRepository.findAllByMember_MemberIdOrderByWalkLogIdDesc(Mockito.anyLong())).willReturn(Optional.of(walkLogs));
        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.createWalkLog(memberId);
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
        //수정용 WalkLog객체 생성
        WalkLog patchWalkLog = new WalkLog();

        patchWalkLog.setMessage("안녕하십니끄아악!");
        patchWalkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        patchWalkLog.setWalkLogId(1L);
        patchWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(beanUtils.copyNonNullProperties(Mockito.any(WalkLog.class),Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(patchWalkLog);

        //when
        WalkLog updatedWalkLog = walkLogService.updateWalkLog(patchWalkLog);

        //then
        assertThat(updatedWalkLog).isNotNull();
        assertThat(updatedWalkLog.getWalkLogId()).isEqualTo(walkLog.getWalkLogId());
        assertThat(patchWalkLog.getMessage()).isEqualTo(updatedWalkLog.getMessage());
        assertThat(patchWalkLog.getWalkLogPublicSetting()).isEqualTo(updatedWalkLog.getWalkLogPublicSetting());
    }
    @Test
    public void shouldThrowExceptionWhenWalkLogNotFoundTest() {
        //given
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(walkLog);
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
        WalkLog patchWalkLog = new WalkLog();

        patchWalkLog.setMessage("안녕하십니끄아악!");
        patchWalkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        patchWalkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(beanUtils.copyNonNullProperties(Mockito.any(WalkLog.class),Mockito.any(WalkLog.class))).willReturn(new WalkLog());
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(patchWalkLog);

        //when
        patchWalkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        WalkLog updatedWalkLog = walkLogService.exitWalkLog(patchWalkLog);

        //then
        assertThat(updatedWalkLog).isNotNull();
        assertThat(updatedWalkLog.getWalkLogId()).isEqualTo(walkLog.getWalkLogId());
        assertThat(patchWalkLog.getMessage()).isEqualTo(updatedWalkLog.getMessage());
        assertThat(patchWalkLog.getWalkLogPublicSetting()).isEqualTo(updatedWalkLog.getWalkLogPublicSetting());
        assertThat(updatedWalkLog.getWalkLogStatus()).isEqualTo(WalkLog.WalkLogStatus.STOP);
    }
    @Test
    public void shouldThrowExceptionWhenWalkLogRecordingTest() {
        //given
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.updateWalkLog(walkLog);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Change");
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogNotRecordingTest() {
        //given
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(1L);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));

        //when
        RuntimeException exception = assertThrows(BusinessLogicException.class, () -> {
            walkLogService.exitWalkLog(walkLog);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog Can Not Exit");
    }

    @Test
    public void findWalkLogTest(){
        WalkLog expectedWalkLog = new WalkLog();
        expectedWalkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expectedWalkLog));

        WalkLog actualWalkLog = walkLogService.findWalkLog(1L);

        Assertions.assertEquals(expectedWalkLog, actualWalkLog);
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