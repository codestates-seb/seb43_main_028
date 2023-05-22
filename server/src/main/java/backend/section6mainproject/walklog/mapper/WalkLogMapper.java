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

@Mapper(componentModel = "spring",uses = {CoordinateMapper.class, WalkLogContentMapper.class, StorageService.class})
public interface WalkLogMapper {
    //Controller To Service
    WalkLogServiceDTO.UpdateInput walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(WalkLogControllerDTO.Patch walkLogControllerPatchDTO);


    WalkLogServiceDTO.ExitInput walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(WalkLogControllerDTO.EndPost endPost);

    WalkLogServiceDTO.FindInput walkLogControllerGetRequestDTOtoWalkLogServiceFindInputDTO(WalkLogControllerDTO.GetMemberRequest getMemberRequest);
    WalkLogServiceDTO.FindFeedInput walkLogControllerGetMemberRequestDTOtoWalkLogServiceFindFeedInputDTO(WalkLogControllerDTO.GetFeedRequest getFeedRequest);
    WalkLogServiceDTO.CalenderFindInput walkLogControllerGetCalenderRequestDTOtoWalkLogServiceCalenderFindInputDTO(WalkLogControllerDTO.GetCalendarRequest getCalendarRequest);



    //Service To Entity
    WalkLog walkLogServiceUpdateInputDTOtoWalkLog(WalkLogServiceDTO.UpdateInput updateInput);

    @Mapping(target = "mapImage", ignore = true)
    WalkLog walkLogServiceExitInputDTOtoWalkLog(WalkLogServiceDTO.ExitInput exitInput);



    //Entity To Service
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "mapImage",target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "coordinates",qualifiedByName = "coordinateEntityToServiceDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    WalkLogServiceDTO.Output walkLogToWalkLogServiceOutputDTO(WalkLog walkLog);
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    @Mapping(target = "startedAt", source = "createdAt")
    @Mapping(source = "mapImage",target = "mapImage", qualifiedByName = "signBucket")
    WalkLogServiceDTO.FindOutput walkLogToWalkLogServiceFindOutputDTO(WalkLog walkLog);
    WalkLogServiceDTO.CreateOutput walkLogToWalkLogServiceCreatedOutputDTO(WalkLog walkLog);
    WalkLogServiceDTO.CalenderFindOutput walkLogToWalkLogServiceCalenderFindOutputDTO(WalkLog walkLog);

    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "member.profileImage",target = "profileImage",qualifiedByName = "signBucket")
    @Mapping(source = "mapImage",target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "coordinates",qualifiedByName = "coordinateEntityToServiceDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    WalkLogServiceDTO.GetOutput walkLogToWalkLogServiceGetOutPutDTO(WalkLog walkLog);
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "member.profileImage",target = "profileImage")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentEntityToServiceDTO")
    @Mapping(target = "startedAt", source = "createdAt")
    @Mapping(source = "mapImage",target = "mapImage",qualifiedByName = "signBucket")
    WalkLogServiceDTO.FindFeedOutput walkLogToWalkLogServiceFindFeedOutputDTO(WalkLog walkLog);

    List<WalkLogServiceDTO.CalenderFindOutput> walkLogsToWalkLogServiceCalenderFindOutputDTOs(List<WalkLog> walkLogs);




    //Service To Controller

    WalkLogControllerDTO.Response walkLogServiceFindOutputDTOtoWalkLogControllerResponseDTO(WalkLogServiceDTO.FindOutput findOutput);

    WalkLogControllerDTO.PostResponse walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(WalkLogServiceDTO.CreateOutput walkLogServiceCreateOutputDTO);
    @Mapping(target = "coordinates",qualifiedByName = "coordinateServiceDTOToControllerDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentServiceDTOToControllerDTO")
    @Mapping(source = "imageUrl",target = "mapImage")
    WalkLogControllerDTO.DetailResponse walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(WalkLogServiceDTO.Output output);

    @Mapping(target = "coordinates",qualifiedByName = "coordinateServiceDTOToControllerDTO")
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentServiceDTOToControllerDTO")
    WalkLogControllerDTO.GetResponse walkLogServiceGetOutPutDTOtoWalkLogControllerGetResponseDTO(WalkLogServiceDTO.GetOutput getOutput);
    WalkLogControllerDTO.GetFeedResponse walkLogServiceFindFeedOutputDTOtoWalkLogControllerGetFeedResponseDTO(WalkLogServiceDTO.FindFeedOutput findFeedOutput);
    WalkLogControllerDTO.CalendarResponse WalkLogServiceCalenderFindOutputDTOToWalkLogControllerCalendarResponseDTO(WalkLogServiceDTO.CalenderFindOutput calenderFindOutput);
    List<WalkLogControllerDTO.CalendarResponse> WalkLogServiceCalenderFindOutputDTOsToWalkLogControllerCalendarResponseDTOs(List<WalkLogServiceDTO.CalenderFindOutput> calenderFindOutputs);


}
