package backend.section6mainproject.member.controller;

import backend.section6mainproject.dto.MultiResponseDto;
import backend.section6mainproject.dto.PageInfo;
import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
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



    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {
        MemberControllerDTO.Response response = memberMapper.outputToResponse(memberService.findMember(memberId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{member-id}/walk-logs")
    public ResponseEntity getMyWalkLogs(@PathVariable("member-id") @Positive Long memberId,
                                        @Valid @ModelAttribute WalkLogControllerDTO.GetRequests getRequests){
        if(getRequests.isNoPage())
            return new ResponseEntity(walkLogService.findMyTotalWalkLogs(memberId),HttpStatus.OK);


        WalkLogServiceDTO.FindsInput findsInput = walkLogMapper.walkLogControllerGetRequestsDTOtoWalkLogServiceFindsInputDTO(getRequests);
        findsInput.setMemberId(memberId);
        Page<WalkLogServiceDTO.FindsOutput> myWalkLogs = walkLogService.findMyWalkLogs(findsInput);
        PageInfo pageInfo = walkLogService.createPageInfo(myWalkLogs);
        return new ResponseEntity<>(new MultiResponseDto<>(myWalkLogs.getContent(),pageInfo), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive Long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
