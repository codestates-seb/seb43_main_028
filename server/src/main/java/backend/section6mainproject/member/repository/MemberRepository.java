package backend.section6mainproject.member.repository;

import backend.section6mainproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT * FROM member WHERE email = ?", nativeQuery = true)
    Optional<Member> findByEmailAll(String email);

    Optional<Member> findByNickname(String nickname);

    @Query(value = "SELECT * FROM member WHERE nickname = ?", nativeQuery = true)
    Optional<Member> findByNicknameAll(String nickname);
    @Modifying
    @Query("DELETE FROM Member m WHERE m.memberStatus = 'MEMBER_QUIT' AND m.quittedAt <= :cutoffDateTime")
    void deleteMemberCompletely(LocalDateTime cutoffDateTime);

}
