package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousWalkLogRepository extends JpaRepository<AnonymousWalkLog, Long> {
}
