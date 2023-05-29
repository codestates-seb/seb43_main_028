package backend.section6mainproject.content.mapper;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.helper.image.StorageService;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = StorageService.class)
public interface WalkLogContentMapper {

    // request flow - for create
    WalkLogContentServiceDTO.CreateInput controllerPostDTOTOServiceCreateInputDTO(WalkLogContentControllerDTO.Post post);

    @Mapping(source = "walkLogId", target = "walkLog.walkLogId")
    WalkLogContent serviceCreateInputDTOToEntity(WalkLogContentServiceDTO.CreateInput createInput);

    // request flow - for update
    WalkLogContentServiceDTO.UpdateInput controllerPatchDTOToServiceUpdateInputDTO(WalkLogContentControllerDTO.Patch patch);



    // response flow - for create
    WalkLogContentServiceDTO.CreateOutput entityToServiceCreateOutputDTO(WalkLogContent walkLogContent);

    WalkLogContentControllerDTO.PostResponse serviceCreateOutputDTOToControllerCreateResponseDTO(WalkLogContentServiceDTO.CreateOutput createOutput);

    // response flow - for general
    @Mapping(source = "imageKey", target = "imageUrl", qualifiedByName = "PreSignedUrl")
    @Named("walkLogContentForOrigin")
    WalkLogContentServiceDTO.Output entityToServiceOutputDTO(WalkLogContent walkLogContent);

    @Mapping(source = "imageKey", target = "imageUrl", qualifiedByName = "PreSignedUrlForThumbnail")
    @Named("walkLogContentForThumbnail")
    WalkLogContentServiceDTO.Output entityToServiceOutputDTOForThumbnail(WalkLogContent walkLogContent);

    WalkLogContentControllerDTO.Response serviceOutputDTOToControllerResponseDTO(WalkLogContentServiceDTO.Output output);

    @Named("walkLogContentEntityToServiceDTO")
    @IterableMapping(qualifiedByName = "walkLogContentForOrigin")
    List<WalkLogContentServiceDTO.Output> entitiesToServiceOutputDTOs(List<WalkLogContent> walkLogContents);
    @Named("walkLogContentEntityToServiceDTOForThumbnail")
    @IterableMapping(qualifiedByName = "walkLogContentForThumbnail")
    List<WalkLogContentServiceDTO.Output> entitiesToServiceOutputDTOsForThumbnail(List<WalkLogContent> walkLogContents);
    @Named("walkLogContentServiceDTOToControllerDTO")
    List<WalkLogContentControllerDTO.Response> serviceOutputDTOsToControllerResponseDTOs(List<WalkLogContentServiceDTO.Output> outputs);


}
