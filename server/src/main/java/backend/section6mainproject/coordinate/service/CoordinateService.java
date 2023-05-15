package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;

public interface CoordinateService {
    CoordinateServiceDTO.CreateReturn createCoordinate(CoordinateServiceDTO.CreateParam coordinateDTO);
}
