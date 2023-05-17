package backend.section6mainproject.member.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final WalkLogService walkLogService;
    private final WalkLogMapper walkLogMapper;

    public MemberController(MemberMapper memberMapper, MemberService memberService, WalkLogService walkLogService, WalkLogMapper walkLogMapper) {
        this.memberMapper = memberMapper;
        this.memberService = memberService;
        this.walkLogService = walkLogService;
        this.walkLogMapper = walkLogMapper;
    }

    @PostMapping("/sign")
    public ResponseEntity postMember(@Valid @RequestBody MemberControllerDTO.Post post) {
        MemberServiceDTO.CreateInput createInput = memberMapper.postToCreateInput(post);
        MemberServiceDTO.CreateOutput memberIdResponse = memberService.createMember(createInput);

        MemberControllerDTO.PostResponse response = memberMapper.createOutputToPostResponse(memberIdResponse);
        Long memberId = response.getMemberId();

        URI location = UriComponentsBuilder
                .newInstance()
                .path(MEMBER_DEFAULT_URL + "/" + memberId)
                .build()
                .toUri();

        // 테스트 통과를 위해 위 코드들 주석처리

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping(path = "/{member-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive Long memberId,
                                      @Valid @RequestPart MemberControllerDTO.Patch patch,
                                      @RequestPart MultipartFile profileImage) {
        MemberServiceDTO.UpdateInput updateInput = memberMapper.patchToUpdateInput(patch);
        updateInput.setProfileImage(profileImage);
        updateInput.setMemberId(memberId);
        MemberServiceDTO.Output preResponse = memberService.updateMember(updateInput);

        MemberControllerDTO.Response response = memberMapper.outputToResponse(preResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@PatchMapping("/{member-id}/pw")
    public ResponseEntity patchMemberPassword(@PathVariable("member-id") @Positive Long memberId,
                                              @RequestBody MemberControllerDTO.PatchPw patchPw) {
        return null; // 서비스 코드 작성 후 구현 예정
    }*/



    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {
        MemberControllerDTO.Response response = memberMapper.outputToResponse(memberService.findMember(memberId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{member-id}/walk-logs")
    public ResponseEntity getMyWalkLogs(@PathVariable("member-id") @Positive Long memberId,
                                        @Positive @RequestParam int page,
                                        @RequestParam(value = "size",required = false,defaultValue = "10") int size,
                                        @RequestParam(value = "year",required = false,defaultValue = "9999") int year,
                                        @RequestParam(value = "month",required = false,defaultValue = "99") int month,
                                        @RequestParam(value = "day",required = false,defaultValue = "99") int day){
        Page<WalkLog> walkLogs = walkLogService.findMyWalkLogs(memberId, page, size, year, month, day);
        List<WalkLogDTO.SimpleResponse> response = walkLogMapper.walkLogsToWalkLogSimpleResponseDTOs(walkLogs.toList());
        return new ResponseEntity<>(new MultiResponseDto<>(response,walkLogs), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive Long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
