package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymousWalkLogRepository extends JpaRepository<AnonymousWalkLog, Long> {
    Optional<AnonymousWalkLog> findByUserId(String userId);
}
