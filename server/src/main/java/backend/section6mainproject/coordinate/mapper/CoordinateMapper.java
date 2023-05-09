package backend.section6mainproject.coordinate.mapper;

import backend.section6mainproject.coordinate.dto.CoordinateDTO;
import backend.section6mainproject.coordinate.entity.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoordinateMapper {
    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    Coordinate coordinatePubDTOToCoordinate(CoordinateDTO.Pub pub);

    CoordinateDTO.Sub coordinateToCoordinateSubDTO(Coordinate coordinate);

    @Named("coordinatesToCoordinateSubDTOs")
    List<CoordinateDTO.Sub> coordinatesToCoordinateSubDTOs(List<Coordinate> coordinates);
}
