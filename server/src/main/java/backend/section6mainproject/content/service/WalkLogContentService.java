package backend.section6mainproject.content.service;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;

public interface WalkLogContentService {
    WalkLogContentServiceDTO.CreateOutput createWalkLogContent(WalkLogContentServiceDTO.CreateInput createInput);
}
