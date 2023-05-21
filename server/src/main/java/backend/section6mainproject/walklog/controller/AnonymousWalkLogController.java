package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.mapper.AnonymousWalkLogMapper;
import backend.section6mainproject.walklog.service.AnonymousWalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/anonymous/walk-logs")
@Validated
@RequiredArgsConstructor
public class AnonymousWalkLogController {
    private final static String WALK_LOG_DEFAULT_URL = "/anonymous/walk-logs";
    private final AnonymousWalkLogService walkLogService;
    private final AnonymousWalkLogMapper mapper;

    @PostMapping
    public ResponseEntity postWalkLog() {
        AnonymousWalkLogServiceDTO.CreateOutput createOutput = walkLogService.createWalkLog();
        AnonymousWalkLogControllerDTO.PostResponse postResponse = new AnonymousWalkLogControllerDTO.PostResponse(createOutput.getUserId());
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALK_LOG_DEFAULT_URL + "/" + createOutput.getUserId())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(postResponse);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<?> getWalkLog(@NotNull @PathVariable("user-id") String userId) {
        AnonymousWalkLogServiceDTO.Output output = walkLogService.findWalkLog(userId);
        return ResponseEntity.ok(mapper.ServiceOutputDTOToControllerResponseDTO(output));
    }
}
