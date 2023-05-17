package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

//브랜치 병합 후 점검할것
@Mapper(componentModel = "spring",uses = {CoordinateMapper.class, WalkLogContentMapper.class})
public interface WalkLogMapper {
    WalkLogServiceDTO.CreateInput walkLogControllerPostDTOtoWalkLogServiceCreateInputDTO(WalkLogControllerDTO.Post walkLogControllerPostDTO);
    WalkLogServiceDTO.CreateOutput walkLogToWalkLogServiceCreatedOutputDTO(WalkLog walkLog);

    WalkLogControllerDTO.PostResponse walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(WalkLogServiceDTO.CreateOutput walkLogServiceCreateOutputDTO);

    WalkLogServiceDTO.UpdateInput walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(WalkLogControllerDTO.Patch walkLogControllerPatchDTO);
    WalkLog walkLogServiceUpdateInputDTOtoWalkLog(WalkLogServiceDTO.UpdateInput updateInput);
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(target = "coordinates",qualifiedByName = "coordinateEntityToServiceDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    WalkLogServiceDTO.Output walkLogToWalkLogServiceOutputDTO(WalkLog walkLog);
    @Mapping(target = "coordinates",qualifiedByName = "coordinateServiceDTOToControllerDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentServiceDTOToControllerDTO")
    WalkLogControllerDTO.DetailResponse walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(WalkLogServiceDTO.Output output);

    WalkLogServiceDTO.CalenderFindsInput walkLogControllerGetCalenderRequestsDTOtoWalkLogServiceCalenderFindsInputDTO(WalkLogControllerDTO.GetCalendarRequests getCalendarRequests);
    WalkLogServiceDTO.CalenderFindsOutput walkLogToWalkLogServiceCalenderFindsOutputDTO(WalkLog walkLog);
    List<WalkLogServiceDTO.CalenderFindsOutput> walkLogsToWalkLogServiceCalenderFindsOutputDTOs(List<WalkLog> walkLogs);
    WalkLogControllerDTO.CalendarResponse WalkLogServiceCalenderFindsOutputDTOToWalkLogControllerCalendarResponseDTO(WalkLogServiceDTO.CalenderFindsOutput calenderFindsOutput);
    List<WalkLogControllerDTO.CalendarResponse> WalkLogServiceCalenderFindsOutputDTOsToWalkLogControllerCalendarResponseDTOs(List<WalkLogServiceDTO.CalenderFindsOutput> calenderFindsOutputs);

    WalkLogServiceDTO.ExitInput walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(WalkLogControllerDTO.EndPost endPost);
    WalkLog walkLogServiceExitInputDTOtoWalkLog(WalkLogServiceDTO.ExitInput exitInput);

    WalkLogServiceDTO.FindsInput walkLogControllerGetRequestsDTOtoWalkLogServiceFindsInputDTO(WalkLogControllerDTO.GetRequests getRequests);

    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    @Mapping(target = "startedAt", source = "createdAt")
    WalkLogServiceDTO.FindsOutput walkLogToWalkLogServiceFindsOutputDTO(WalkLog walkLog); //구현중
    List<WalkLogServiceDTO.FindsOutput> walkLogsToWalkLogServiceFindsOutputDTOs(List<WalkLog> walkLogs); //임시


}
