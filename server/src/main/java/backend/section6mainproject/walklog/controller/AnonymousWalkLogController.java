package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.service.AnonymousWalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/anonymous/walk-logs")
@Validated
@RequiredArgsConstructor
public class AnonymousWalkLogController {
    private final static String WALK_LOG_DEFAULT_URL = "/anonymous/walk-logs";
    private final AnonymousWalkLogService walkLogService;

    @PostMapping
    public ResponseEntity postWalkLog(@Valid @RequestBody WalkLogControllerDTO.Post walkLogControllerPostDto) {
        AnonymousWalkLogServiceDTO.CreateOutput createOutput = walkLogService.createWalkLog();
        AnonymousWalkLogControllerDTO.PostResponse postResponse = new AnonymousWalkLogControllerDTO.PostResponse(createOutput.getUserId());
        URI location = UriComponentsBuilder
                .newInstance()
                .path(WALK_LOG_DEFAULT_URL + "/" + createOutput.getUserId())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(postResponse);
    }
}
