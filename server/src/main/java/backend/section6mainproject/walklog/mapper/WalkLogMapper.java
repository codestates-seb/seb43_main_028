package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogDto;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalkLogMapper {
    WalkLog walkLogPatchDtoToWalkLog(WalkLogDto.Patch walkLogPatchDto);

    WalkLogDto.Response walkLogToWalkLogResponseDto(WalkLog walkLog);

    WalkLogDto.MemberResponse memberToWalkLogMemberResponseDto(Member member);

    WalkLogDto.ContentResponse walkLogContentToWalkLogContentResponseDto(WalkLogContent walkLogContent);
    WalkLogDto.CoordinateResponse coordinateToWalkLogCoordinateResponseDto(Coordinate coordinate);

}
