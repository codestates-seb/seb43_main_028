package backend.section6mainproject.coordinate.mapper;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.AnonymousCoordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AnonymousCoordinateMapper {
    AnonymousCoordinateServiceDTO.Input controllerPubDTOTOServiceInputDTO(AnonymousCoordinateControllerDTO.Pub pub);

    @Mapping(source = "userId", target = "walkLog.userId")
    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    AnonymousCoordinate serviceInputDTOToEntity(AnonymousCoordinateServiceDTO.Input input);

    @Mapping(source = "walkLog.userId", target = "userId")
    AnonymousCoordinateServiceDTO.Output entityToServiceOutputDTO(AnonymousCoordinate coordinate);

    AnonymousCoordinateControllerDTO.Sub serviceOutputDTOToControllerSubDTO(AnonymousCoordinateServiceDTO.Output output);

    @Named("coordinateEntityToServiceDTO")
    List<AnonymousCoordinateServiceDTO.Output> entitiesToServiceOutputDTOs(List<AnonymousCoordinate> coordinates);

    @Named("coordinateServiceDTOToControllerDTO")
    List<AnonymousCoordinateControllerDTO.Sub> serviceOutputDTOsToControllerSubDTOs(List<AnonymousCoordinateServiceDTO.Output> outputs);
}
