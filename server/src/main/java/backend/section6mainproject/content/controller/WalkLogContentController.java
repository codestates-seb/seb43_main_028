package backend.section6mainproject.content.controller;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.service.WalkLogContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/walk-logs/{walk-log-id}/contents")
@Validated
@RequiredArgsConstructor
public class WalkLogContentController {
    private static final String WALK_LOG_CONTENT_DEFAULT_URL = "/walk-logs/{walk-log-id}/contents/";
    private final WalkLogContentService walkLogContentService;
    private final WalkLogContentMapper mapper;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<WalkLogContentControllerDTO.PostResponse> postContent(@Positive @PathVariable("walk-log-id") long walkLogId,
                                         @Valid @RequestPart WalkLogContentControllerDTO.Post content,
                                         @RequestPart(required = false) MultipartFile contentImage) {
        WalkLogContentServiceDTO.CreateInput createInput = mapper.controllerPostDTOTOServiceCreateInputDTO(content);
        createInput.setWalkLogId(walkLogId);
        createInput.setContentImage(contentImage);
        WalkLogContentServiceDTO.CreateOutput walkLogContent = walkLogContentService.createWalkLogContent(createInput);
        URI uri = UriComponentsBuilder.newInstance()
                .path(WALK_LOG_CONTENT_DEFAULT_URL + walkLogContent.getWalkLogContentId())
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(mapper.serviceCreateOutputDTOToControllerCreateResponseDTO(walkLogContent));
    }

    @PatchMapping(path = "/{content-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<WalkLogContentControllerDTO.Response> patchContent(@Positive @PathVariable("content-id") long walkLogContentId,
                                                                             @Valid @RequestPart WalkLogContentControllerDTO.Patch content,
                                                                             @RequestPart(required = false) MultipartFile contentImage) {
        WalkLogContentServiceDTO.UpdateInput updateInput = mapper.controllerPatchDTOToServiceUpdateInputDTO(content);
        updateInput.setWalkLogContentId(walkLogContentId);
        updateInput.setContentImage(contentImage);
        WalkLogContentServiceDTO.Output output = walkLogContentService.updateWalkLogContent(updateInput);
        return ResponseEntity.ok(mapper.serviceOutputDTOToControllerResponseDTO(output));
    }

    @DeleteMapping("/{content-id}")
    public ResponseEntity<?> deleteContent(@Positive @PathVariable("content-id") long walkLogContentId) {
        walkLogContentService.deleteWalkLogContent(walkLogContentId);
        return ResponseEntity.noContent().build();
    }

}
