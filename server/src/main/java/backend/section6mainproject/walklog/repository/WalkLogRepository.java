package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    List<WalkLog> findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting walkLogPublicSetting);
    List<WalkLog> findAllByWalkLogPublicSettingAndMember_MemberId(Pageable pageable,
                                                                  WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                  Long memberId);
    List<WalkLog> findAllByMember_MemberIdOrderByWalkLogIdDesc(Long memberId);

   List<WalkLog> findAllByWalkLogPublicSettingAndCreatedAtBetween(WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end);

   List<WalkLog> findAllByWalkLogPublicSettingAndCreatedAtBetweenAndMember_MemberId(WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                                    LocalDateTime start,
                                                                                    LocalDateTime end,
                                                                                    Long memberId);

}
