package backend.section6mainproject.walklog.service;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberServiceImpl;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WalkLogServiceImplTest {

    @Mock
    private WalkLogRepository walkLogRepository;

    @Mock
    private MemberServiceImpl memberServiceImpl;

    @InjectMocks
    private WalkLogService walkLogService;

    @Test
    public void testCreateWalkLog() {
        // given
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("admin1@gmail.com");
        member.setPassword("12345");
        member.setNickname("거터볼래1");
        member.setIntroduction("안녕하세요1");
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        given(memberServiceImpl.findVerifiedMember(Mockito.anyLong())).willReturn(member);
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(walkLog);
        // when
        WalkLog createdWalkLog = walkLogService.createWalkLog(memberId);

        // then
        assertNotNull(createdWalkLog);
        assertEquals(member, createdWalkLog.getMember());
    }

    @Test
    public void testUpdateWalkLog(){

        // given
        //멤버 객체 추가
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

        //수정용 WalkLog객체 생성
        WalkLog patchWalkLog = new WalkLog();

        patchWalkLog.setMessage("안녕하십니끄아악!");
        patchWalkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        patchWalkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(Mockito.anyLong())).willReturn(Optional.of(walkLog));
        given(walkLogRepository.save(walkLog)).willReturn(walkLog);

        //when
        WalkLog updatedWalkLog = walkLogService.updateWalkLog(patchWalkLog);

        //then
        assertThat(updatedWalkLog).isNotNull();
        assertThat(updatedWalkLog.getWalkLogId()).isEqualTo(walkLog.getWalkLogId());
        assertThat(patchWalkLog.getMessage()).isEqualTo(updatedWalkLog.getMessage());
        assertThat(patchWalkLog.getWalkLogPublicSetting()).isEqualTo(updatedWalkLog.getWalkLogPublicSetting());
    }

    @Test
    public void shouldThrowExceptionWhenWalkLogNotFound() {
        //given
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(1L);

        given(walkLogRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            walkLogService.updateWalkLog(walkLog);
        });
        //then
        assertThat(exception.getMessage()).isEqualTo("WalkLog를 찾을 수 없습니다");
    }
}