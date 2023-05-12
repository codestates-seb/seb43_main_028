package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    Optional<Page<WalkLog>> findAllByWalkLogPublicSetting(Pageable pageable, WalkLog.WalkLogPublicSetting walkLogPublicSetting);
    Optional<List<WalkLog>> findAllByMember_MemberIdOrderByWalkLogIdDesc(Long memberId);

   Page<WalkLog> findAllByWalkLogPublicSettingAndCreatedAtBetween(Pageable pageable,
                                                                  WalkLog.WalkLogPublicSetting walkLogPublicSetting,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end);

}
