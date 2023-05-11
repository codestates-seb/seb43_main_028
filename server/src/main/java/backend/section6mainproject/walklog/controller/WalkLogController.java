package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
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

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@RequestBody WalkLogDTO.Post walkLogPostDTO){
        Long memberId = walkLogPostDTO.getMemberId();
        WalkLogDTO.Created created = walkLogMapper.walkLogToWalkLogCreatedDTO(walkLogService.createWalkLog(memberId));
        return new ResponseEntity(created, HttpStatus.CREATED);
    }
    @PostMapping("/{walk-log-id}")
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
            @RequestBody WalkLogDTO.EndPost walkLogEndPostDTO){
        //현재 걷기 도중인
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
    @GetMapping
    public ResponseEntity getWalkLogs(@Positive @RequestParam int page){
        //객체지향적으로, 컨트롤러에는 엔티티가 존재해서는 안됨을 명심
        Page<WalkLog> walkLogs = walkLogService.findWalkLogs( page - 1, PAGE_SIZE);
        List<WalkLogDTO.Response> responses = walkLogMapper.walkLogsToWalkLogResponseDTOs(walkLogs.toList());
        return new ResponseEntity<>(new MultiResponseDto<>(responses,walkLogs), HttpStatus.OK);
    }
    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
