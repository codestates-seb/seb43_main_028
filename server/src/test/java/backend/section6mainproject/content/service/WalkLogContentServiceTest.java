package backend.section6mainproject.content.service;

import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.service.WalkLogService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        given(mapper.serviceCreateInputDTOToEntity(Mockito.any(WalkLogContentServiceDTO.CreateInput.class))).willReturn(stubData.getWalkLogContent());
        given(walkLogService.findVerifiedWalkLog(Mockito.anyLong())).willReturn(new WalkLog());
        given(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("");
        given(walkLogContentRepository.save(Mockito.any(WalkLogContent.class))).willReturn(new WalkLogContent());
        given(mapper.entityToServiceCreateOutputDTO(Mockito.any(WalkLogContent.class))).willReturn(stubData.getCreateOutput());

        // when
        WalkLogContentServiceDTO.CreateOutput result = walkLogContentService.createWalkLogContent(stubData.getCreateInput());

        // then
        MatcherAssert.assertThat(result.getWalkLogContentId(), is(equalTo(stubData.getCreateOutput().getWalkLogContentId())));
    }


}