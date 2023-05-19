package backend.section6mainproject.coordinate.repository;

import backend.section6mainproject.coordinate.entity.AnonymousCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousCoordinateRepository extends JpaRepository<AnonymousCoordinate, Long> {
}
