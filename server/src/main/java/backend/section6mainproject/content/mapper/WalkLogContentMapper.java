package backend.section6mainproject.content.mapper;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.helper.image.StorageService;
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
    @Mapping(source = "imageKey", target = "imageUrl", qualifiedByName = "signBucket")
    WalkLogContentServiceDTO.Output entityToServiceOutputDTO(WalkLogContent walkLogContent);

    WalkLogContentControllerDTO.Response serviceOutputDTOToControllerResponseDTO(WalkLogContentServiceDTO.Output output);

    @Named("walkLogContentEntityToServiceDTO")
    List<WalkLogContentServiceDTO.Output> entitiesToServiceOutputDTOs(List<WalkLogContent> walkLogContents);
    @Named("walkLogContentServiceDTOToControllerDTO")
    List<WalkLogContentControllerDTO.Response> serviceOutputDTOsToControllerResponseDTOs(List<WalkLogContentServiceDTO.Output> outputs);


    // legacy codes

    @Mapping(source = "imageKey", target = "imageUrl", qualifiedByName = "signBucket")
    WalkLogContentControllerDTO.Response walkLogContentToWalkLogContentResponseDTO(WalkLogContent walkLogContent);
    @Named("walkLogContentsToWalkLogContentResponseDTOs")
    List<WalkLogContentControllerDTO.Response> walkLogContentsToWalkLogContentResponseDTOs(List<WalkLogContent> walkLogContents);
}
