package backend.section6mainproject.member.controller;

import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
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

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberMapper mapper;
    private final MemberService memberService;

    public MemberController(MemberMapper mapper, MemberService memberService) {
        this.mapper = mapper;
        this.memberService = memberService;
    }

    @PostMapping("/sign")
    public ResponseEntity postMember(@Valid @RequestBody MemberDTO.Post post) {
        MemberDTO.PostRequestForService postRequestForService = mapper.memberPostRequestToService(post);
        MemberDTO.Created memberIdResponse = memberService.createMember(postRequestForService);

        MemberDTO.CreatedIdForClient response = mapper.makeMemberIdForClient(memberIdResponse);

        /*URI location = UriComponentsBuilder
                .newInstance()
                .path(MEMBER_DEFAULT_URL + "/" + memberId)
                .buildAndExpand(memberId)
                .toUri();*/

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{member-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive Long memberId,
                                      @Valid @RequestPart MemberDTO.Patch patch,
                                      @RequestPart MultipartFile profileImage) {
        patch.setMemberId(memberId);
        MemberDTO.PatchRequestForService patchRequestForService = mapper.memberPatchRequestToService(patch, profileImage);

        MemberDTO.ProfileResponseForController preResponse = memberService.updateMember(patchRequestForService);

        MemberDTO.ProfileResponseForClient response = mapper.memberProfileResponseToClient(preResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {
       MemberDTO.ProfileResponseForClient response = mapper.memberProfileResponseToClient(memberService.findMember(memberId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive Long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
