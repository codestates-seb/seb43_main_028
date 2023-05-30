package backend.section6mainproject.content.service;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;

public interface WalkLogContentService {
    WalkLogContentServiceDTO.CreateOutput createWalkLogContent(WalkLogContentServiceDTO.CreateInput createInput);

    WalkLogContentServiceDTO.Output updateWalkLogContent(WalkLogContentServiceDTO.UpdateInput updateInput);

    void deleteWalkLogContent(Long walkLogContentId);

    WalkLogContentServiceDTO.Output findVerifiedWalkLogContent(Long walkLogContentId);
}
