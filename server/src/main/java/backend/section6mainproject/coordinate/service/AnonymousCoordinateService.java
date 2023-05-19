package backend.section6mainproject.coordinate.service;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;

public interface AnonymousCoordinateService {
    AnonymousCoordinateServiceDTO.Output createCoordinate(AnonymousCoordinateServiceDTO.Input input);

}
