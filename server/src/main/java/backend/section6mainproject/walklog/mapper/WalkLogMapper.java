package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {CoordinateMapper.class, WalkLogContentMapper.class})
public interface WalkLogMapper {
    WalkLog walkLogPatchDTOToWalkLog(WalkLogDTO.Patch walkLogPatchDTO);

    WalkLog walkLogEndPostDTOtoWalkLog(WalkLogDTO.EndPost walkLogEndPostDTO);
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(target = "coordinates",qualifiedByName = "coordinatesToCoordinateSubDTOs")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentsToWalkLogContentResponseDTOs")
    WalkLogDTO.Response walkLogToWalkLogResponseDTO(WalkLog walkLog);
    WalkLogDTO.Created walkLogToWalkLogCreatedDTO(WalkLog walkLog);

}
