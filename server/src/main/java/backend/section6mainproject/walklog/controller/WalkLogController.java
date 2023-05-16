package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/walk-logs")
@RequiredArgsConstructor
public class WalkLogController {

    private final static Integer PAGE_SIZE = 10;
    private final static String WALK_LOG_DEFAULT_URL = "/walk-logs";

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@RequestBody WalkLogControllerDTO.Post walkLogControllerPostDto){
        WalkLogServiceDTO.CreateInput createInput = walkLogMapper.walkLogControllerPostDTOtoWalkLogServiceCreateInputDTO(walkLogControllerPostDto);
        WalkLogServiceDTO.CreateOutput createOutput = walkLogService.createWalkLog(createInput);
        WalkLogControllerDTO.PostResponse postResponse = walkLogMapper.walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(createOutput);
        Long walkLogId = postResponse.getWalkLogId();
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALK_LOG_DEFAULT_URL + "/" + walkLogId)
                .buildAndExpand(walkLogId)
                .toUri();
        return ResponseEntity.created(location).body(postResponse); //되는지 체크
    }
    @PatchMapping("/{walk-log-id}")
    public ResponseEntity patchWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId,
                                       @RequestBody WalkLogControllerDTO.Patch walkLogControllerPatchDto){

        WalkLogServiceDTO.UpdateInput updateInput = walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(walkLogControllerPatchDto);
        updateInput.setWalkLogId(walkLogId);
        WalkLogControllerDTO.DetailResponse detailResponse =
                walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(walkLogService.updateWalkLog(updateInput));
        return new ResponseEntity<>(detailResponse, HttpStatus.OK);
    }

    @PostMapping("/{walk-log-id}")
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
                                     @RequestBody WalkLogControllerDTO.EndPost walkLogEndPostDTO){
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
    public ResponseEntity getWalkLogs(@ModelAttribute WalkLogControllerDTO.GetRequests getRequests){
        //객체지향적으로, 컨트롤러에는 엔티티가 존재해서는 안됨을 명심//RequestParam의 경우에는 String로 들어오는 만큼 dto에서 유효성 검사를 철저하게 만들것
        Page<WalkLogServiceDTO.FindsOutput> walkLogs = walkLogService.findWalkLogs(walkLogMapper.walkLogControllerGetRequestsDTOtoWalkLogServiceFindsInputDTO(getRequests));
        PageInfo pageInfo = walkLogService.createPageInfo(walkLogs);
        return new ResponseEntity<>(new MultiResponseDto<>(walkLogs.toList(),pageInfo), HttpStatus.OK);
    }
    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
