package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/walk-logs")
@RequiredArgsConstructor
public class WalkLogController {

    private final static String WALK_LOG_DEFAULT_URL = "/walk-logs";

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@RequestBody WalkLogDTO.Post walkLogPostDTO){
        //멤버 인증 로직을 한 다음 멤버를 가져오는 로직(추후 작성예정)
        Long memberId = walkLogPostDTO.getMemberId();
        WalkLogDTO.Created created = walkLogMapper.walkLogToWalkLogCreatedDTO(walkLogService.createWalkLog(memberId));
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALK_LOG_DEFAULT_URL + "/" + created)
                .buildAndExpand(created)
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @PostMapping("/{walk-log-id}")
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
            @RequestBody WalkLogDTO.EndPost walkLogEndPostDTO){
        //멤버 인증 로직은 추후에 반영
        walkLogEndPostDTO.setWalkLogId(walkLogId);

        WalkLogDTO.Response response = walkLogMapper.walkLogToWalkLogResponseDTO(
                walkLogService.exitWalkLog(
                walkLogMapper.walkLogEndPostDTOtoWalkLog(walkLogEndPostDTO)));
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PatchMapping("/{walk-log-id}")
    public ResponseEntity patchWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId,
                                       @RequestBody WalkLogDTO.Patch walkLogPatchDTO){
        walkLogPatchDTO.setWalkLogId(walkLogId);
        WalkLog updatedWalkLog = walkLogService.updateWalkLog(walkLogMapper.walkLogPatchDTOToWalkLog(walkLogPatchDTO));
        WalkLogDTO.Response response = walkLogMapper.walkLogToWalkLogResponseDTO(updatedWalkLog);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("/{walk-log-id}")
    public ResponseEntity getWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        WalkLogDTO.Response response = walkLogMapper.walkLogToWalkLogResponseDTO(walkLogService.findWalkLog(walkLogId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
