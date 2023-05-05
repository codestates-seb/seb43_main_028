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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Mockito.when(memberService.findVerifiedMember(memberId)).thenReturn(member);
        Mockito.when(walkLogRepository.save(Mockito.any(WalkLog.class))).thenAnswer(invocation -> {
            WalkLog walkLog = (WalkLog) invocation.getArguments()[0];
            walkLog.setWalkLogId(1L);
            return walkLog;
        });

        // when
        WalkLog walkLog = walkLogService.createWalkLog(memberId);

        // then
        assertNotNull(walkLog);
        assertEquals(member, walkLog.getMember());
    }
}