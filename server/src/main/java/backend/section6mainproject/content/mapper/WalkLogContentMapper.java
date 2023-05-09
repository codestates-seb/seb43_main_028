package backend.section6mainproject.content.mapper;

import backend.section6mainproject.content.dto.WalkLogContentDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.helper.image.StorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = StorageService.class)
public interface WalkLogContentMapper {

    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    WalkLogContent walkLogContentPostDTOToWalkLogContent(WalkLogContentDTO.Post post);

    @Mapping(source = "imageKey", target = "imageUrl", qualifiedByName = "signBucket")
    WalkLogContentDTO.Response walkLogContentToWalkLogContentResponseDTO(WalkLogContent walkLogContent);
    @Named("walkLogContentsToWalkLogContentResponseDTOs")
    List<WalkLogContentDTO.Response> walkLogContentsToWalkLogContentResponseDTOs(List<WalkLogContent> walkLogContents);
}
