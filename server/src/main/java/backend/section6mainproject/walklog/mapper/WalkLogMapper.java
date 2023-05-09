package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {CoordinateMapper.class})
public interface WalkLogMapper {
    WalkLog walkLogPatchDtoToWalkLog(WalkLogDTO.Patch walkLogPatchDto);

    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(target = "coordinates",qualifiedByName = "coordinatesToCoordinateSubDTOs")
    WalkLogDTO.Response walkLogToWalkLogResponseDto(WalkLog walkLog);

    WalkLogDTO.ContentResponse walkLogContentToWalkLogContentResponseDto(WalkLogContent walkLogContent);

}
