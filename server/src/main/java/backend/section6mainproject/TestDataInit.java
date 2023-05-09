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
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(firstMember);
        walkLog.setMessage("안녕하십니까");
        walkLogRepository.save(walkLog);
        WalkLogContent walkLogContent = new WalkLogContent();
        walkLogContent.setWalkLog(walkLog);
        walkLogContent.setText("첫번째 걷기 기록중 글 기록");
        walkLogContentRepository.save(walkLogContent);

    }
}
