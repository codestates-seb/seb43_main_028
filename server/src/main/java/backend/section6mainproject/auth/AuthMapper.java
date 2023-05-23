package backend.section6mainproject.auth;

import backend.section6mainproject.auth.dto.ProfileControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    ProfileControllerDTO.Response ServiceOutputDTOToProfileDTO(MemberServiceDTO.Output output);
}
