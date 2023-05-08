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
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new RuntimeException("회원이 존재하지 않습니다")); //잘못된 오류문구 수정
        return findMember;
    }
}
