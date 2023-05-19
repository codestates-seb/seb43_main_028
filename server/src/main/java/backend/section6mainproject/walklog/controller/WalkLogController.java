package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/walk-logs")
@RequiredArgsConstructor
@Slf4j
public class WalkLogController {

    private final static String WALK_LOG_DEFAULT_URL = "/walk-logs";

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@Valid @RequestBody WalkLogControllerDTO.Post post){
        WalkLogServiceDTO.CreateInput createInput = walkLogMapper.walkLogControllerPostDTOtoWalkLogServiceCreateInputDTO(post);
        WalkLogServiceDTO.CreateOutput createOutput = walkLogService.createWalkLog(createInput);
        WalkLogControllerDTO.PostResponse postResponse = walkLogMapper.walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(createOutput);
        Long walkLogId = postResponse.getWalkLogId();
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALK_LOG_DEFAULT_URL + "/" + walkLogId)
                .buildAndExpand(walkLogId)
                .toUri();
        return ResponseEntity.created(location).body(postResponse);
    }
    @PatchMapping("/{walk-log-id}")
    public ResponseEntity patchWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId,
                                       @Valid @RequestBody WalkLogControllerDTO.Patch patch){

        WalkLogServiceDTO.UpdateInput updateInput = walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(patch);
        updateInput.setWalkLogId(walkLogId);
        WalkLogControllerDTO.DetailResponse detailResponse =
                walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(walkLogService.updateWalkLog(updateInput));
        return new ResponseEntity<>(detailResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/{walk-log-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
                                     @Valid @RequestPart WalkLogControllerDTO.EndPost endPost,
                                     @RequestPart MultipartFile mapImage){
        WalkLogServiceDTO.ExitInput exitInput =
                walkLogMapper.walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(endPost);
        exitInput.setWalkLogId(walkLogId);
        exitInput.setMapImage(mapImage);
        WalkLogServiceDTO.Output output = walkLogService.exitWalkLog(exitInput);

        return new ResponseEntity(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(output),HttpStatus.OK);
    }
    @GetMapping("/{walk-log-id}")
    public ResponseEntity getWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        //get은 RequestBody가 존재하지 않는다 그럼에도 불구하고 Client To Controller DTO를 만들 필요성이 있을까?
        WalkLogControllerDTO.DetailResponse detailResponse =
                walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(walkLogService.findWalkLog(walkLogId));
        return new ResponseEntity<>(detailResponse, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity getWalkLogs(@Valid @ModelAttribute WalkLogControllerDTO.GetFeedRequest getFeedRequest){
        WalkLogServiceDTO.FindFeedInput findFeedInput =
                walkLogMapper.walkLogControllerGetMemberRequestDTOtoWalkLogServiceFindFeedInputDTO(getFeedRequest);
        Page<WalkLogControllerDTO.GetFeedResponse> responses = walkLogService.findFeedWalkLogs(findFeedInput)
                .map(walkLogMapper::walkLogServiceFindFeedOutputDTOtoWalkLogControllerGetFeedResponseDTO);

        return new ResponseEntity<>(new MultiResponseDto<>(responses.getContent(),responses), HttpStatus.OK);    }

    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
