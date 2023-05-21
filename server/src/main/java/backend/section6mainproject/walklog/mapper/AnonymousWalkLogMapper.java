package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.coordinate.mapper.AnonymousCoordinateMapper;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AnonymousCoordinateMapper.class})
public interface AnonymousWalkLogMapper {
    @Mapping(target = "coordinates", qualifiedByName = "coordinateEntityToServiceDTO")
    AnonymousWalkLogServiceDTO.Output entityToServiceOutputDTO(AnonymousWalkLog walkLog);

    @Mapping(target = "coordinates", qualifiedByName = "coordinateServiceDTOToControllerDTO")
    AnonymousWalkLogControllerDTO.Response ServiceOutputDTOToControllerResponseDTO(AnonymousWalkLogServiceDTO.Output output);
}
