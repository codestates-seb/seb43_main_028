package backend.section6mainproject.member.controller;

import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.member.service.MemberServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity postMember(@Valid @RequestBody MemberDTO.Post memberPostDto) {
        Member member = mapper.memberPostDtoToMember(memberPostDto);
        Member createdMember = memberService.createMember(member);
        Long memberId = createdMember.getMemberId();
        URI location = UriComponentsBuilder
                .newInstance()
                .path(MEMBER_DEFAULT_URL + "/" + memberId)
                .buildAndExpand(memberId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive Long memberId,
                                      @RequestBody @Valid MemberDTO.Patch patch) {
        patch.setMemberId(memberId);

        Member member = mapper.memberPatchDtoToMember(patch);
        Member updatedMember = memberService.updateMember(member);

        MemberDTO.Response response = mapper.memberToMemberResponseDto(updatedMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive Long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
