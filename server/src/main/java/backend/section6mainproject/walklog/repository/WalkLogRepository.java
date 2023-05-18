package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    Page<WalkLog> findAllByMember_MemberId(Pageable pageable,Long memberId);
    List<WalkLog> findAllByMember_MemberIdOrderByWalkLogIdDesc(Long memberId);
    @Query("FROM WalkLog w WHERE w.member.memberId = :memberId AND YEAR(w.createdAt) = :year AND MONTH(w.createdAt) = :month ")
    List<WalkLog> findMyWalkLogFromMonthForCalendar(Long memberId, int year, int month);
    @Query("FROM WalkLog w WHERE w.member.memberId = :memberId AND YEAR(w.createdAt) = :year")
    Page<WalkLog> findAllByMyWalkLogFromYear(Pageable pageable, Long memberId, int year);
    @Query("FROM WalkLog w WHERE w.member.memberId = :memberId AND YEAR(w.createdAt) = :year AND MONTH(w.createdAt) = :month ")
    Page<WalkLog> findMyWalkLogFromMonth(Pageable pageable,Long memberId, int year, int month);
    @Query("FROM WalkLog w WHERE w.member.memberId = :memberId AND YEAR(w.createdAt) = :year AND MONTH(w.createdAt) = :month AND DAY(w.createdAt) = :day")
    Page<WalkLog> findAllByMyWalkLogFromDay(Pageable pageable,Long memberId, int year, int month, int day);

}
