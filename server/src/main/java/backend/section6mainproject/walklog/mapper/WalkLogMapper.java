package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface WalkLogMapper {
    WalkLog walkLogPatchDtoToWalkLog(WalkLogDTO.Patch walkLogPatchDto);

    WalkLogDTO.Response walkLogToWalkLogResponseDto(WalkLog walkLog);


    WalkLogDTO.MemberResponse memberToWalkLogMemberResponseDto(Member member);

    WalkLogDTO.ContentResponse walkLogContentToWalkLogContentResponseDto(WalkLogContent walkLogContent);
    WalkLogDTO.CoordinateResponse coordinateToWalkLogCoordinateResponseDto(Coordinate coordinate);

}
