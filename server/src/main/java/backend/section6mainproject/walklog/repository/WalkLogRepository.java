package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    List<WalkLog> findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting walkLogPublicSetting);
    List<WalkLog> findAllByWalkLogPublicSettingAndMember_MemberId(WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                  Long memberId);
    List<WalkLog> findAllByMember_MemberIdOrderByWalkLogIdDesc(Long memberId);
    @Query("FROM WalkLog w WHERE w.member.memberId = :memberId AND YEAR(w.createdAt) = :year AND MONTH(w.createdAt) = :month ")
    List<WalkLog> findMyWalkLogByMonth(Long memberId,int year,int month);
    List<WalkLog> findAllByWalkLogPublicSettingAndCreatedAtBetweenAndMember_MemberId(
                                                                                     WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                                     LocalDateTime start,
                                                                                     LocalDateTime end,
                                                                                     Long memberId);

}
