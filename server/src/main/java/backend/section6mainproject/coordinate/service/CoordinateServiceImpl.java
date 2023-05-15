package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.coordinate.repository.CoordinateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoordinateServiceImpl implements CoordinateService {
    private final CoordinateRepository coordinateRepository;
    private final CoordinateMapper mapper;

    @Override
    public CoordinateServiceDTO.Output createCoordinate(CoordinateServiceDTO.Input coordinateDTO) {
        Coordinate coordinate = mapper.serviceInputDTOToEntity(coordinateDTO);
        Coordinate savedCoordinate = coordinateRepository.save(coordinate);
        return mapper.entityToServiceOutputDTO(savedCoordinate);

    }
}
