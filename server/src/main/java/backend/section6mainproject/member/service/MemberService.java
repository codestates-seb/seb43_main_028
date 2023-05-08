package backend.section6mainproject.member.service;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());

        //추후 패스워드 암호화 및 User Role 관련해서 작업필요

        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    private void verifyExistsEmail(String email) {
       Optional<Member> findMember = memberRepository.findByEmail(email);
       if(findMember.isPresent()) {
           throw new RuntimeException("이미 존재하는 회원의 아이디입니다.");
       }
    }

    /** 회원이 리포지토리에 존재하는지 여부를 검증하고, 회원을 반환하는 메서드.
     * <p>Member 엔티티의 walkLogs 필드는 fetchType이 Lazy로 설정되어 있으므로, 값을 실제로 조회할 때 데이터를 호출하게 되는데,
     *  ConnectionInterceptor가 영속성 컨텍스트 밖에 있는것으로 보여서 이 메서드에서 값을 호출했다.
     */
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new RuntimeException("회원이 존재하지 않습니다"));
        findMember.getWalkLogs().stream().forEach(walkLog -> walkLog.getWalkLogId());
        return findMember;
    }
}
