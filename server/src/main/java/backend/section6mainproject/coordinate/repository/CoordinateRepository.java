package backend.section6mainproject.coordinate.repository;

import backend.section6mainproject.coordinate.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}
