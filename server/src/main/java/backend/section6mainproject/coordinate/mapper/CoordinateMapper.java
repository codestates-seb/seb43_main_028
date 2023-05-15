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

    CoordinateControllerDTO.Sub coordinateToCoordinateSubDTO(Coordinate coordinate);

    @Named("coordinatesToCoordinateSubDTOs")
    List<CoordinateControllerDTO.Sub> coordinatesToCoordinateSubDTOs(List<Coordinate> coordinates);

    CoordinateServiceDTO.CreateParam controllerPubDTOTOServiceCreateParamDTO(CoordinateControllerDTO.Pub pub);

    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    Coordinate serviceCreateParamDTOToEntity(CoordinateServiceDTO.CreateParam createParam);

    @Mapping(source = "walkLog.walkLogId", target = "walkLogId")
    CoordinateServiceDTO.CreateReturn entityToServiceCreateReturnDTO(Coordinate coordinate);

    CoordinateControllerDTO.Sub serviceCreateReturnDTOToControllerSubDTO(CoordinateServiceDTO.CreateReturn createReturn);

    @Named("coordinateEntityToServiceDTO")
    List<CoordinateServiceDTO.CreateReturn> entitiesToServiceCreateReturnDTOs(List<Coordinate> coordinates);
    @Named("coordinateServiceDTOToControllerDTO")
    List<CoordinateControllerDTO.Sub> serviceCreateReturnDTOsToControllerSubDTOs(List<CoordinateServiceDTO.CreateReturn> createReturns);




}
