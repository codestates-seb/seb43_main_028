package backend.section6mainproject.content.repository;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkLogContentRepository extends JpaRepository<WalkLogContent, Long> {
    List<WalkLogContent> findAllByWalkLog_WalkLogId(Long walkLogId);

}
