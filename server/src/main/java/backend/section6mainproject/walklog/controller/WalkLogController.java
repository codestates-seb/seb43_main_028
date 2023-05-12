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

import javax.validation.constraints.Positive;
import java.time.LocalDate;
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
    @PatchMapping("/{walk-log-id}")
    public ResponseEntity patchWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId,
                                       @RequestBody WalkLogDTO.Patch walkLogPatchDTO){
        walkLogPatchDTO.setWalkLogId(walkLogId);
        WalkLog updatedWalkLog = walkLogService.updateWalkLog(walkLogMapper.walkLogPatchDTOToWalkLog(walkLogPatchDTO));
        WalkLogDTO.DetailResponse detailResponse = walkLogMapper.walkLogToWalkLogDetailResponseDTO(updatedWalkLog);

        return new ResponseEntity<>(detailResponse, HttpStatus.OK);

    }

    @PostMapping("/{walk-log-id}")
    public ResponseEntity endWalkLog(@PathVariable("walk-log-id") @Positive Long walkLogId,
            @RequestBody WalkLogDTO.EndPost walkLogEndPostDTO){
        //현재 걷기 도중인
        //멤버 인증 로직은 추후에 반영
        walkLogEndPostDTO.setWalkLogId(walkLogId);

        WalkLogDTO.DetailResponse detailResponse = walkLogMapper.walkLogToWalkLogDetailResponseDTO(
                walkLogService.exitWalkLog(
                walkLogMapper.walkLogEndPostDTOtoWalkLog(walkLogEndPostDTO)));
        return new ResponseEntity(detailResponse,HttpStatus.OK);
    }
    @GetMapping("/{walk-log-id}")
    public ResponseEntity getWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        WalkLogDTO.DetailResponse detailResponse = walkLogMapper.walkLogToWalkLogDetailResponseDTO(walkLogService.findWalkLog(walkLogId));
        return new ResponseEntity<>(detailResponse, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity getWalkLogs(@Positive @RequestParam int page,
                                      @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                      @RequestParam(value = "day",required = false,defaultValue = "99") int day,
                                      @RequestParam(value = "month",required = false,defaultValue = "99") int month,
                                      @RequestParam(value = "year",required = false,defaultValue = "9999") int year){
        //객체지향적으로, 컨트롤러에는 엔티티가 존재해서는 안됨을 명심//RequestParam의 경우에는 String로 들어오는 만큼 dto에서 유효성 검사를 철저하게 만들것
        Page<WalkLog> walkLogs = walkLogService.findWalkLogs(page,size,year,month,day);
        List<WalkLogDTO.SimpleResponse> response = walkLogMapper.walkLogsToWalkLogSimpleResponseDTOs(walkLogs.toList());
        return new ResponseEntity<>(new MultiResponseDto<>(response,walkLogs), HttpStatus.OK);
    }
    @DeleteMapping("/{walk-log-id}")
    public ResponseEntity deleteWalkLog(@PathVariable("walk-log-id") @Positive long walkLogId){
        walkLogService.deleteWalkLog(walkLogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
