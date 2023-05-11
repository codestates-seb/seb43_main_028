package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkLogRepository extends JpaRepository<WalkLog, Long> {
    List<WalkLog> findAllByWalkLogPublicSetting(WalkLog.WalkLogPublicSetting walkLogPublicSetting);
}
