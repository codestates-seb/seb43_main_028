package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    Optional<List<WalkLog>> findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting walkLogPublicSetting);
    Optional<List<WalkLog>> findAllByMember_MemberIdOrderByWalkLogIdDesc(Long memberId);
}
