package backend.section6mainproject.member.mapper;

import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", uses = {StorageService.class})
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    MemberServiceDTO.PostInService memberPostRequestToService(MemberControllerDTO.PostInController postInController); //API계층에서 서비스 계층으로 넘길때 사용됨
    Member memberPostRequestToMember(MemberServiceDTO.PostInService postInService); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberServiceDTO.PatchInService memberPatchRequestToService(MemberControllerDTO.PatchInController patchInController); //API계층에서 서비스 계층으로 넘길때 사용됨
    @Mapping(target = "profileImage", ignore = true)
    Member memberPatchRequestToMember(MemberServiceDTO.PatchInService patchInService); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    @Mapping(source = "profileImage", target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "totalWalkLog", expression = "java(member.getWalkLogs().size())")
    @Mapping(target = "totalWalkLogContent", expression = "java(member.getWalkLogs().stream().mapToInt(el -> el.getWalkLogContents().size()).sum())")
    MemberServiceDTO.ProfileResponseForController memberToMemberResponseDto(Member member); // 서비스 계층에서 API계층으로 넘길때 사용됨
    MemberControllerDTO.ProfileResponseForClient memberProfileResponseToClient(MemberServiceDTO.ProfileResponseForController profileResponseForController); //API계층에서 클라이언트로 넘길때 사용됨

    MemberControllerDTO.CreatedIdForClient makeMemberIdForClient(MemberServiceDTO.CreatedToController memberId);

    MemberServiceDTO.CreatedToController makeMemberIdAfterPostMember(Member member);

}
