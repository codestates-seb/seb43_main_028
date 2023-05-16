package backend.section6mainproject.walklog.mapper;

import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.coordinate.mapper.CoordinateMapper;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

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
    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentsToWalkLogContentResponseDTOs")
    WalkLogServiceDTO.Output walkLogToWalkLogServiceOutputDTO(WalkLog walkLog);
    WalkLogControllerDTO.DetailResponse walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(WalkLogServiceDTO.Output output);


    WalkLogServiceDTO.ExitInput walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(WalkLogControllerDTO.EndPost endPost);
    WalkLog walkLogServiceExitInputDTOtoWalkLog(WalkLogServiceDTO.ExitInput exitInput);

    WalkLog walkLogControllerPatchDTOtoWalkLog(WalkLogControllerDTO.Patch walkLogPatchDTO);

    WalkLog walkLogEndPostDTOtoWalkLog(WalkLogControllerDTO.EndPost walkLogEndPostDTO);
    WalkLogServiceDTO.FindsInput walkLogControllerGetRequestsDTOtoWalkLogServiceFindsInputDTO(WalkLogControllerDTO.GetRequests getRequests);
    List<WalkLogServiceDTO.CreateOutput> walkLogsToWalkLogServiceOutputDTOs(List<WalkLog> walkLogs);
    List<WalkLogControllerDTO.DetailResponse> walkLogsToWalkLogDetailResponseDTOs(List<WalkLog> walkLogs); //임시

    @Mapping(target = "walkLogContents",qualifiedByName = "walkLogContentsToWalkLogContentResponseDTOs")
    @Mapping(target = "startedAt", source = "createdAt")
    WalkLogServiceDTO.FindsOutput walkLogToWalkLogServiceFindsOutputDTO(WalkLog walkLog); //구현중
    List<WalkLogServiceDTO.FindsOutput> walkLogsToWalkLogServiceFindsOutputDTOs(List<WalkLog> walkLogs); //임시


}
