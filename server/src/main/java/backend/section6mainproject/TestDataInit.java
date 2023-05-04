package backend.section6mainproject;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member firstMember = new Member();
        firstMember.setEmail("admin@gmail.com");
        firstMember.setPassword("1234");
        firstMember.setNickname("거터볼래");
        firstMember.setIntroduction("안녕하세요");
        memberRepository.save(firstMember);
    }
}
