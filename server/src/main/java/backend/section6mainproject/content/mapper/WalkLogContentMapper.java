package backend.section6mainproject.content.mapper;

import backend.section6mainproject.Section6MainProjectApplication;
import backend.section6mainproject.content.dto.WalkLogContentDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.service.StorageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Mapper(componentModel = "spring", uses = StorageService.class)
public interface WalkLogContentMapper {

    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    WalkLogContent walkLogContentPostDTOToWalkLogContent(WalkLogContentDTO.Post post);

    @Mapping(source = "imageFileName", target = "imageUrl", qualifiedByName = "signBucket")
    WalkLogContentDTO.Response walkLogContentToWalkLogContentResponseDTO(WalkLogContent walkLogContent);
}
