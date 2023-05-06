package backend.section6mainproject.member.mapper;

import backend.section6mainproject.member.dto.MemberDto;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    //기존에 대문자로 되어있는 첫글자들을 소문자로 수정
    Member memberPostDtoToMember(MemberDto.Post post);
    Member memberPatchDtoToMember(MemberDto.Patch patch);
    MemberDto.Response memberToMemberResponseDto(Member member);
}
