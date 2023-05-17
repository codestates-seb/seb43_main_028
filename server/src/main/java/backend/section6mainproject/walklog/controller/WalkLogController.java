package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/walk-logs")
@RequiredArgsConstructor
@Slf4j
public class WalkLogController {

    private final static String WALK_LOG_DEFAULT_URL = "/walk-logs";

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@Valid @RequestBody WalkLogControllerDTO.Post walkLogControllerPostDto){
        WalkLogServiceDTO.CreateInput createInput = walkLogMapper.walkLogControllerPostDTOtoWalkLogServiceCreateInputDTO(walkLogControllerPostDto);
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
                                       @Valid @RequestBody WalkLogControllerDTO.Patch walkLogControllerPatchDto){

        WalkLogServiceDTO.UpdateInput updateInput = walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(walkLogControllerPatchDto);
        updateInput.setWalkLogId(walkLogId);
        WalkLogControllerDTO.DetailResponse detailResponse =
                walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(walkLogService.updateWalkLog(updateInput));
        return new ResponseEntity<>(detailResponse, HttpStatus.OK);
    }

    @PostMapping("/{walk-log-id}")
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
                                     @Valid @RequestBody WalkLogControllerDTO.EndPost walkLogEndPostDTO){
        //현재 걷기 도중인
        //멤버 인증 로직은 추후에 반영
        WalkLogServiceDTO.ExitInput exitInput = walkLogMapper.walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(walkLogEndPostDTO);
        exitInput.setWalkLogId(walkLogId);
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
    public ResponseEntity getWalkLogs(@Valid @ModelAttribute WalkLogControllerDTO.GetRequests getRequests){
            return new ResponseEntity<>(walkLogService.findTotalWalkLogs(),HttpStatus.OK);
    }
    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
