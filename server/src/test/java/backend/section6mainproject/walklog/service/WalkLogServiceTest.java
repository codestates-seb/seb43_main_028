package backend.section6mainproject.walklog.service;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WalkLogServiceTest {

    @Mock
    private WalkLogRepository walkLogRepository;

    @Mock
    private MemberService memberService;

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
        given(memberService.findVerifiedMember(Mockito.anyLong())).willReturn(member);
        given(walkLogRepository.save(Mockito.any(WalkLog.class))).willReturn(walkLog);
        // when
        WalkLog createdWalkLog = walkLogService.createWalkLog(memberId);

        // then
        assertNotNull(createdWalkLog);
        assertEquals(member, createdWalkLog.getMember());
    }
}