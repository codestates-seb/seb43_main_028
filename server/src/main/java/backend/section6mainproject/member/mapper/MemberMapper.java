package backend.section6mainproject.member.mapper;

import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", uses = {StorageService.class})
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    MemberDTO.PostRequestForService memberPostRequestToService(MemberDTO.Post post); //API계층에서 서비스 계층으로 넘길때 사용됨
    Member memberPostRequestToMember(MemberDTO.PostRequestForService postRequestForService); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberDTO.PatchRequestForService memberPatchRequestToService(MemberDTO.Patch patch, MultipartFile profileImage); //API계층에서 서비스 계층으로 넘길때 사용됨
    @Mapping(target = "profileImage", ignore = true)
    Member memberPatchRequestToMember(MemberDTO.PatchRequestForService patchRequestForService); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    @Mapping(source = "profileImage", target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "totalWalkLog", expression = "java(member.getWalkLogs().size())")
    @Mapping(target = "totalWalkLogContent", expression = "java(member.getWalkLogs().stream().mapToInt(el -> el.getWalkLogContents().size()).sum())")
    MemberDTO.ProfileResponseForController memberToMemberResponseDto(Member member); // 서비스 계층에서 API계층으로 넘길때 사용됨
    MemberDTO.ProfileResponseForClient memberProfileResponseToClient(MemberDTO.ProfileResponseForController profileResponseForController); //API계층에서 클라이언트로 넘길때 사용됨

    MemberDTO.CreatedIdForClient makeMemberIdForClient(MemberDTO.Created memberId);

    MemberDTO.Created makeMemberIdAfterPostMember(Member member);

}
