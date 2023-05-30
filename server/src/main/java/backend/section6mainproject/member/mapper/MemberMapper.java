package backend.section6mainproject.member.mapper;

import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StorageService.class, WalkLogRepository.class, WalkLogContentRepository.class})
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    MemberServiceDTO.CreateInput postToCreateInput(MemberControllerDTO.Post post); //API계층에서 서비스 계층으로 넘길때 사용됨
    Member createInputToMember(MemberServiceDTO.CreateInput createInput); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberServiceDTO.UpdateInput patchToUpdateInput(MemberControllerDTO.Patch patch); //API계층에서 서비스 계층으로 넘길때 사용됨
    MemberServiceDTO.UpdatePwInput patchPwToUpdatePwInput(MemberControllerDTO.PatchPw patchPw); // API계층에서 서비스 계층으로 패스워드를 넘길때 사용됨
    @Mapping(target = "profileImage", ignore = true)
    Member updateInputToMember(MemberServiceDTO.UpdateInput updateInput); //서비스용DTO를 엔티티로 바꾼다. 서비스 계층에서만 사용됨
    MemberServiceDTO.FindNewPwInput getNewPwToFindNewPw(MemberControllerDTO.GetNewPw getNewPw);
    @Mapping(source = "profileImage", target = "imageUrl", qualifiedByName = "PreSignedUrlForThumbnail")
    @Mapping(target = "totalWalkLog", source = "memberId", qualifiedByName = "countWalkLog")
    @Mapping(target = "totalWalkLogContent", source = "memberId", qualifiedByName = "countWalkLogContent")
    MemberServiceDTO.Output memberToOutput(Member member); // 서비스 계층에서 API계층으로 넘길때 사용됨
    MemberControllerDTO.Response outputToResponse(MemberServiceDTO.Output output); //API계층에서 클라이언트로 넘길때 사용됨

    MemberControllerDTO.PostResponse createOutputToPostResponse(MemberServiceDTO.CreateOutput memberId);

    MemberServiceDTO.CreateOutput memberToCreateOutput(Member member);

}
