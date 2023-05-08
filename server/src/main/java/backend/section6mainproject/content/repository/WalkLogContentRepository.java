package backend.section6mainproject.content.repository;

import backend.section6mainproject.content.entity.WalkLogContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkLogContentRepository extends JpaRepository<WalkLogContent, Long> {
}
