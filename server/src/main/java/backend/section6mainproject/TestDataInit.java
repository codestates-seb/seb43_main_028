package backend.section6mainproject;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WalkLogRepository walkLogRepository;
    private final WalkLogContentRepository walkLogContentRepository;

    @PostConstruct
    public void init() {
        Member firstMember = new Member();
        firstMember.setEmail("admin@gmail.com");
        firstMember.setPassword("1234");
        firstMember.setNickname("거터볼래");
        firstMember.setIntroduction("안녕하세요");
        memberRepository.save(firstMember);
//달력테스트를 위해 추가했습니다.
        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        for(int i=0;i<9;i++) {
            WalkLog walkLog = new WalkLog();
            walkLog.setMember(firstMember);
            walkLog.setMessage("안녕하십니까" + (i+1));
            walkLog.setCreatedAt(LocalDateTime.of(LocalDate.parse("2023-0" + (i+1) + "-01"), LocalTime.now()));
            walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
            walkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
            walkLogs.add(walkLog);
        }
        walkLogRepository.saveAll(walkLogs);
        WalkLogContent walkLogContent = new WalkLogContent();
        walkLogContent.setWalkLog(walkLogs.get(0));
        walkLogContent.setText("첫번째 걷기 기록중 글 기록");
        walkLogContentRepository.save(walkLogContent);

    }
}
