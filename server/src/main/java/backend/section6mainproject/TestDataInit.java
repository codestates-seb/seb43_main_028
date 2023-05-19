package backend.section6mainproject;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WalkLogRepository walkLogRepository;
    private final WalkLogContentRepository walkLogContentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member firstMember = new Member();
        firstMember.setEmail("test@gmail.com");
        firstMember.setRoles(List.of("USER"));
        firstMember.setPassword(passwordEncoder.encode("testdot01!"));
        firstMember.setNickname("거터");
        firstMember.setIntroduction("안녕하세요");
        memberRepository.save(firstMember);

        WalkLog walkLog = new WalkLog();
        walkLog.setMember(firstMember);
        walkLog.setMessage("안녕하십니까");
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        walkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        walkLogRepository.save(walkLog);

        WalkLogContent walkLogContent = new WalkLogContent();
        walkLogContent.setWalkLog(walkLog);
        walkLogContent.setText("첫번째 걷기 기록중 글 기록");
        walkLogContentRepository.save(walkLogContent);

    }
}
