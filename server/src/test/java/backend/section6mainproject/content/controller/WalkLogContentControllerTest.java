package backend.section6mainproject.content.controller;

import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.service.WalkLogContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WalkLogContentController.class)
@MockBean({JpaMetamodelMappingContext.class, StompExceptionAdvice.class})
class WalkLogContentControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private WalkLogContentMapper mapper;
    @MockBean
    private WalkLogContentService walkLogContentService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private WalkLogContentStubData stubData;

    @BeforeEach
    void init() {
        stubData = new WalkLogContentStubData();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void postContent() throws Exception {
        // given
        given(mapper.controllerPostDTOTOServiceCreateInputDTO(Mockito.any(WalkLogContentControllerDTO.Post.class)))
                .willReturn(new WalkLogContentServiceDTO.CreateInput());
        given(walkLogContentService.createWalkLogContent(Mockito.any(WalkLogContentServiceDTO.CreateInput.class)))
                .willReturn(new WalkLogContentServiceDTO.CreateOutput(0L));
        given(mapper.serviceCreateOutputDTOToControllerCreateResponseDTO(Mockito.any(WalkLogContentServiceDTO.CreateOutput.class)))
                .willReturn(stubData.getPostResponse());

        WalkLogContentControllerDTO.Post post = stubData.getPost();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(post);
        MockMultipartFile contentImage = stubData.getImage();

        MockPart part = new MockPart("content", content.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, "/walk-logs/{walk-log-id}/contents", stubData.getWalkLogContentId())
                        .file(contentImage)
                        .part(part)
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walkLogContentId").value(stubData.getWalkLogContentId()));
    }


}