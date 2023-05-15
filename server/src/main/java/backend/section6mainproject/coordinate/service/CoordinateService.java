package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;

public interface CoordinateService {
    CoordinateServiceDTO.Output createCoordinate(CoordinateServiceDTO.Input coordinateDTO);

}
