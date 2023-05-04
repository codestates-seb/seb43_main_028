package backend.section6mainproject.member.repository;

import backend.section6mainproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
