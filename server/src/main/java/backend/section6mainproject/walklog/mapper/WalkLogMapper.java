package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

//브랜치 병합 후 점검할것
@Mapper(componentModel = "spring",uses = {CoordinateMapper.class, WalkLogContentMapper.class, StorageService.class})
public interface WalkLogMapper {
    WalkLogServiceDTO.CreateOutput walkLogToWalkLogServiceCreatedOutputDTO(WalkLog walkLog);

    WalkLogControllerDTO.PostResponse walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(WalkLogServiceDTO.CreateOutput walkLogServiceCreateOutputDTO);

    WalkLogServiceDTO.UpdateInput walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(WalkLogControllerDTO.Patch walkLogControllerPatchDTO);
    WalkLog walkLogServiceUpdateInputDTOtoWalkLog(WalkLogServiceDTO.UpdateInput updateInput);
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "mapImage",target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "coordinates",qualifiedByName = "coordinateEntityToServiceDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    WalkLogServiceDTO.Output walkLogToWalkLogServiceOutputDTO(WalkLog walkLog);
    @Mapping(target = "coordinates",qualifiedByName = "coordinateServiceDTOToControllerDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentServiceDTOToControllerDTO")
    @Mapping(target = "mapImage",source = "imageUrl")
    WalkLogControllerDTO.DetailResponse walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(WalkLogServiceDTO.Output output);
    WalkLogControllerDTO.Response walkLogServiceFindOutputDTOtoWalkLogControllerResponseDTO(WalkLogServiceDTO.FindOutput findOutput);

    WalkLogServiceDTO.CalenderFindInput walkLogControllerGetCalenderRequestDTOtoWalkLogServiceCalenderFindInputDTO(WalkLogControllerDTO.GetCalendarRequest getCalendarRequest);
    WalkLogServiceDTO.CalenderFindOutput walkLogToWalkLogServiceCalenderFindOutputDTO(WalkLog walkLog);
    List<WalkLogServiceDTO.CalenderFindOutput> walkLogsToWalkLogServiceCalenderFindOutputDTOs(List<WalkLog> walkLogs);
    WalkLogControllerDTO.CalendarResponse WalkLogServiceCalenderFindOutputDTOToWalkLogControllerCalendarResponseDTO(WalkLogServiceDTO.CalenderFindOutput calenderFindOutput);
    List<WalkLogControllerDTO.CalendarResponse> WalkLogServiceCalenderFindOutputDTOsToWalkLogControllerCalendarResponseDTOs(List<WalkLogServiceDTO.CalenderFindOutput> calenderFindOutputs);

    WalkLogServiceDTO.ExitInput walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(WalkLogControllerDTO.EndPost endPost);
    @Mapping(target = "mapImage", ignore = true)
    WalkLog walkLogServiceExitInputDTOtoWalkLog(WalkLogServiceDTO.ExitInput exitInput);

    WalkLogServiceDTO.FindInput walkLogControllerGetRequestDTOtoWalkLogServiceFindInputDTO(WalkLogControllerDTO.GetMemberRequest getMemberRequest);

    WalkLogServiceDTO.FindFeedInput walkLogControllerGetMemberRequestDTOtoWalkLogServiceFindFeedInputDTO(WalkLogControllerDTO.GetFeedRequest getFeedRequest);
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "member.profileImage",target = "profileImage")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    @Mapping(target = "startedAt", source = "createdAt")
    WalkLogServiceDTO.FindFeedOutput walkLogToWalkLogServiceFindFeedOutputDTO(WalkLog walkLog);
    WalkLogControllerDTO.GetFeedResponse walkLogServiceFindFeedOutputDTOtoWalkLogControllerGetFeedResponseDTO(WalkLogServiceDTO.FindFeedOutput findFeedOutput);
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    @Mapping(target = "startedAt", source = "createdAt")
    WalkLogServiceDTO.FindOutput walkLogToWalkLogServiceFindOutputDTO(WalkLog walkLog); //구현중
    List<WalkLogServiceDTO.FindOutput> walkLogsToWalkLogServiceFindOutputDTOs(List<WalkLog> walkLogs); //임시


}
