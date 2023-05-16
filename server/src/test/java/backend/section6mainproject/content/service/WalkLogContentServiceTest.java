package backend.section6mainproject.content.service;

import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.service.WalkLogService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WalkLogContentServiceTest {

    @InjectMocks
    private WalkLogContentServiceImpl walkLogContentService;
    @Mock
    private WalkLogContentRepository walkLogContentRepository;
    @Mock
    private WalkLogService walkLogService;
    @Mock
    private StorageService storageService;
    @Mock
    private WalkLogContentMapper mapper;
    private WalkLogContentStubData stubData;

    @BeforeEach
    void init() {
        stubData = new WalkLogContentStubData();
    }

    @Test
    void createWalkLogContent() throws IOException {
        // given
        WalkLogContentServiceDTO.CreateInput createInput = stubData.getCreateInput();
        WalkLogContentServiceDTO.CreateOutput createOutput = stubData.getCreateOutput();

        given(mapper.serviceCreateInputDTOToEntity(Mockito.any(WalkLogContentServiceDTO.CreateInput.class))).willReturn(stubData.getWalkLogContent());
        given(walkLogService.findVerifiedWalkLog(Mockito.anyLong())).willReturn(new WalkLog());
        given(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("");
        given(walkLogContentRepository.save(Mockito.any(WalkLogContent.class))).willReturn(new WalkLogContent());
        given(mapper.entityToServiceCreateOutputDTO(Mockito.any(WalkLogContent.class))).willReturn(createOutput);


        // when
        WalkLogContentServiceDTO.CreateOutput result = walkLogContentService.createWalkLogContent(createInput);

        // then
        MatcherAssert.assertThat(result, is(equalTo(createOutput)));
    }

    @Test
    void createWalkLogContentNotValidInput() {
        // given
        WalkLogContentServiceDTO.CreateInput createInput = new WalkLogContentServiceDTO.CreateInput();
        createInput.setWalkLogId(1L);

        // when //then
        Assertions.assertThrows(BusinessLogicException.class, () -> walkLogContentService.createWalkLogContent(createInput));
    }

    @Test
    void updateWalkLogContent() throws IOException {
        // given
        WalkLogContentServiceDTO.UpdateInput updateInput = stubData.getUpdateInput();
        WalkLogContentServiceDTO.Output output = stubData.getOutput();

        given(walkLogContentRepository.findById(Mockito.anyLong())).willReturn(Optional.of(stubData.getWalkLogContent()));
        given(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("");
        given(walkLogContentRepository.save(Mockito.any(WalkLogContent.class))).willReturn(new WalkLogContent());
        given(mapper.entityToServiceOutputDTO(Mockito.any(WalkLogContent.class))).willReturn(output);

        // when
        WalkLogContentServiceDTO.Output result = walkLogContentService.updateWalkLogContent(updateInput);

        // then
        MatcherAssert.assertThat(result, is(equalTo(output)));
    }

}