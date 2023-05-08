package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.coordinate.repository.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoordinateServiceImpl implements CoordinateService {
    private final CoordinateRepository coordinateRepository;
    @Override
    public Coordinate createCoordinate(Coordinate coordinate) {
        return coordinateRepository.save(coordinate);
    }
}
