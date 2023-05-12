package backend.section6mainproject.member.mapper;

import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StorageService.class})
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    Member memberPostRequestToMember(MemberDTO.PostRequest postRequest); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberDTO.PostRequest memberPostDTOToPostRequest(MemberDTO.Post post); //API계층에서 서비스 계층으로 넘길때 사용됨
    Member memberPatchRequestToMember(MemberDTO.PatchRequest patchRequest); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberDTO.PatchRequest memberPatchDTOToPatchRequest(MemberDTO.Patch patch); //API계층에서 서비스 계층으로 넘길때 사용됨

    @Mapping(source = "profileImage", target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "totalWalkLog", expression = "java(member.getWalkLogs().size())")
    @Mapping(target = "totalWalkLogContent", expression = "java(member.getWalkLogs().stream().mapToInt(el -> el.getWalkLogContents().size()).sum())")
    MemberDTO.ProfileResponse memberToMemberResponseDto(Member member);
}
