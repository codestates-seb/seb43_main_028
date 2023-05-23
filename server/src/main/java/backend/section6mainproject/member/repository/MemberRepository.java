package backend.section6mainproject.member.repository;

import backend.section6mainproject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT * FROM member WHERE email = ?", nativeQuery = true)
    Optional<Member> findByEmailAll(String email);

    Optional<Member> findByNickname(String nickname);

    @Query(value = "SELECT * FROM member WHERE nickname = ?", nativeQuery = true)
    Optional<Member> findByNicknameAll(String nickname);


}
