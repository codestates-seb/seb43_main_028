package backend.section6mainproject.member.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.member.dto.MemberDTO;
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
    public ResponseEntity postMember(@Valid @RequestBody MemberDTO.Post memberPostDto) {
        Long memberId = memberService.createMember(memberMapper.memberPostDtoToMember(memberPostDto));
        URI location = UriComponentsBuilder
                .newInstance()
                .path(MEMBER_DEFAULT_URL + "/" + memberId)
                .buildAndExpand(memberId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping(path = "/{member-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive Long memberId,
                                      @Valid @RequestPart MemberDTO.Patch patch,
                                      @RequestPart MultipartFile profileImage) {
        patch.setMemberId(memberId);

        MemberDTO.Response response = memberMapper.memberToMemberResponseDto(memberService.updateMember(memberMapper.memberPatchDtoToMember(patch), profileImage));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {
        return new ResponseEntity<>(memberMapper.memberToMemberResponseDto(memberService.findMember(memberId)), HttpStatus.OK);
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
