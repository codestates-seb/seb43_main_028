package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.walklog.dto.WalkLogDto;
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

    private final static String WALKLOG_DEFULT_URL = "/walk-logs";

    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;
    @PostMapping
    public ResponseEntity postWalkLog(@RequestBody WalkLogDto.Post walkLogPostDto){

        Long memberId = walkLogPostDto.getMemberId();
        WalkLog createdWalkLog = walkLogService.createWalkLog(memberId);
        Long walkLogId = createdWalkLog.getWalkLogId();
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALKLOG_DEFULT_URL + "/" + walkLogId)
                .buildAndExpand(walkLogId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{walk-log-id}")
    public ResponseEntity patchWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId,
                                       @RequestBody WalkLogDto.Patch walkLogPatchDto){
        WalkLog walkLog = walkLogMapper.walkLogPatchDtoToWalkLog(walkLogPatchDto);
        walkLog.setWalkLogId(walkLogId);
        WalkLog updatedWalkLog = walkLogService.updateWalkLog(walkLog);
        WalkLogDto.Response response = walkLogMapper.walkLogToWalkLogResponseDto(updatedWalkLog);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
