package backend.section6mainproject.content.mapper;

import backend.section6mainproject.content.dto.WalkLogContentDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalkLogContentMapper {
    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    WalkLogContent walkLogContentPostDTOToWalkLogContent(WalkLogContentDTO.Post post);

    WalkLogContentDTO.Response walkLogContentToWalkLogContentResponseDTO(WalkLogContent walkLogContent);
}
