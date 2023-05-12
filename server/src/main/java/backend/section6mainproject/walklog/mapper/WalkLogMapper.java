package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring",uses = {CoordinateMapper.class, WalkLogContentMapper.class})
public interface WalkLogMapper {
    WalkLog walkLogPatchDTOToWalkLog(WalkLogDTO.Patch walkLogPatchDTO);

    WalkLog walkLogEndPostDTOtoWalkLog(WalkLogDTO.EndPost walkLogEndPostDTO);
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(target = "coordinates",qualifiedByName = "coordinatesToCoordinateSubDTOs")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentsToWalkLogContentResponseDTOs")
    WalkLogDTO.DetailResponse walkLogToWalkLogDetailResponseDTO(WalkLog walkLog);
    WalkLogDTO.Created walkLogToWalkLogCreatedDTO(WalkLog walkLog);
    List<WalkLogDTO.DetailResponse> walkLogsToWalkLogDetailResponseDTOs(List<WalkLog> walkLogs); //임시

    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentsToWalkLogContentResponseDTOs")
    @Mapping(target = "startedAt", source = "createdAt")
    WalkLogDTO.SimpleResponse walkLogToWalkLogSimpleResponseDTO(WalkLog walkLog); //구현중
    List<WalkLogDTO.SimpleResponse> walkLogsToWalkLogSimpleResponseDTOs(List<WalkLog> walkLogs); //임시


}
