package backend.section6mainproject.member.mapper;

import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StorageService.class})
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    Member memberPostDtoToMember(MemberDTO.Post post);

    Member memberPatchDtoToMember(MemberDTO.Patch patch);
    @Mapping(source = "profileImage", target = "imageUrl", qualifiedByName = "signBucket")
    @Mapping(target = "totalWalkLog", expression = "java(member.getWalkLogs().size())")
    @Mapping(target = "totalWalkLogContent", expression = "java(member.getWalkLogs().stream().mapToInt(el -> el.getWalkLogContents().size()).sum())")
    MemberDTO.Response memberToMemberResponseDto(Member member);
}
