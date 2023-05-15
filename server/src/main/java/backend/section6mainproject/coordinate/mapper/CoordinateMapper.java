package backend.section6mainproject.coordinate.mapper;

import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoordinateMapper {

    // legacy codes

    CoordinateControllerDTO.Sub coordinateToCoordinateSubDTO(Coordinate coordinate);

    @Named("coordinatesToCoordinateSubDTOs")
    List<CoordinateControllerDTO.Sub> coordinatesToCoordinateSubDTOs(List<Coordinate> coordinates);

    // refactored codes

    // request flow

    CoordinateServiceDTO.Input controllerPubDTOTOServiceInputDTO(CoordinateControllerDTO.Pub pub);

    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    Coordinate serviceInputDTOToEntity(CoordinateServiceDTO.Input input);

    // response flow

    @Mapping(source = "walkLog.walkLogId", target = "walkLogId")
    CoordinateServiceDTO.Output entityToServiceOutputDTO(Coordinate coordinate);

    CoordinateControllerDTO.Sub serviceOutputDTOToControllerSubDTO(CoordinateServiceDTO.Output aOutput);

    @Named("coordinateEntityToServiceDTO")
    List<CoordinateServiceDTO.Output> entitiesToServiceOutputDTOs(List<Coordinate> coordinates);
    @Named("coordinateServiceDTOToControllerDTO")
    List<CoordinateControllerDTO.Sub> serviceOutputDTOsToControllerSubDTOs(List<CoordinateServiceDTO.Output> aOutputs);





}
