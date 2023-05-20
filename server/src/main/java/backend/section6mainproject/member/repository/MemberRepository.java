package backend.section6mainproject.member.repository;

import backend.section6mainproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndMemberStatus(String email, Member.MemberStatus memberStatus);
    Optional<Member> findByMemberIdAndMemberStatus(long memberId, Member.MemberStatus memberStatus);


}
