package backend.section6mainproject.member.mapper;

import backend.section6mainproject.member.dto.MemberDto;
import backend.section6mainproject.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member MemberPostDtoToMember(MemberDto.Post post);
    Member MemberPatchDtoToMember(MemberDto.Patch patch);
    MemberDto.Response MemberToMemberResponseDto(Member member);
}
