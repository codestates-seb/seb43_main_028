package backend.section6mainproject.coordinate.mapper;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.entity.AnonymousCoordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AnonymousCoordinateMapper {
    AnonymousCoordinateServiceDTO.Input controllerPubDTOTOServiceInputDTO(AnonymousCoordinateControllerDTO.Pub pub);

    @Mapping(source = "userId", target = "walkLog.userId")
    AnonymousCoordinate serviceInputDTOToEntity(AnonymousCoordinateServiceDTO.Input input);

    @Mapping(source = "walkLog.userId", target = "userId")
    AnonymousCoordinateServiceDTO.Output entityToServiceOutputDTO(AnonymousCoordinate coordinate);

    AnonymousCoordinateControllerDTO.Sub serviceOutputDTOToControllerSubDTO(AnonymousCoordinateServiceDTO.Output output);
}
