package backend.section6mainproject.content.controller;

import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.service.WalkLogContentService;
import backend.section6mainproject.util.ApiDocumentUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static backend.section6mainproject.util.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WalkLogContentController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean({JpaMetamodelMappingContext.class, StompExceptionAdvice.class})
@AutoConfigureRestDocs
class WalkLogContentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalkLogContentMapper mapper;
    @MockBean
    private WalkLogContentService walkLogContentService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private WalkLogContentStubData stubData;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        stubData = new WalkLogContentStubData();
    }
    @Test
    void postContent() throws Exception {
        // given
        given(mapper.controllerPostDTOTOServiceCreateInputDTO(Mockito.any(WalkLogContentControllerDTO.Post.class)))
                .willReturn(new WalkLogContentServiceDTO.CreateInput());
        given(walkLogContentService.createWalkLogContent(Mockito.any(WalkLogContentServiceDTO.CreateInput.class)))
                .willReturn(new WalkLogContentServiceDTO.CreateOutput(1L));
        given(mapper.serviceCreateOutputDTOToControllerCreateResponseDTO(Mockito.any(WalkLogContentServiceDTO.CreateOutput.class)))
                .willReturn(stubData.getPostResponse());

        WalkLogContentControllerDTO.Post post = stubData.getPost();
        String content = objectMapper.writeValueAsString(post);
        MockMultipartFile contentImage = stubData.getImage();

        MockPart part = new MockPart("content", content.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String urlTemplate = "/walk-logs/{walk-log-id}/contents";

        // when
        ResultActions actions = mockMvc.perform(

                MockMvcRequestBuilders.multipart(HttpMethod.POST, urlTemplate, stubData.getWalkLogId())
                        .file(contentImage)
                        .part(part)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walkLogContentId").value(stubData.getWalkLogContentId()))
                .andDo(document(
                        "post-walk-log-content",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기 기록 식별자")
                        ),
                        requestParts(
                                partWithName("contentImage").description("걷기 중 순간기록 사진").optional(),
                                partWithName("content").description("걷기 중 순간기록 JSON 데이터")
                        ),
                        requestPartFields("content",
                                fieldWithPath("text").type(JsonFieldType.STRING).description("걷기 중 순간기록 텍스트").optional()
                                ),
                        responseFields(
                                fieldWithPath("walkLogContentId").type(JsonFieldType.NUMBER).description("걷기중 순간기록 식별자")
                        )
                ));
    }

    @Test
    void patchContent() throws Exception {
        // given
        WalkLogContentControllerDTO.Patch patch = stubData.getPatch();
        WalkLogContentControllerDTO.Response response = stubData.getResponse();

        given(mapper.controllerPatchDTOToServiceUpdateInputDTO(Mockito.any(WalkLogContentControllerDTO.Patch.class)))
                .willReturn(new WalkLogContentServiceDTO.UpdateInput());
        given(walkLogContentService.updateWalkLogContent(Mockito.any(WalkLogContentServiceDTO.UpdateInput.class)))
                .willReturn(new WalkLogContentServiceDTO.Output(0L, null, null, null));
        given(mapper.serviceOutputDTOToControllerResponseDTO(Mockito.any(WalkLogContentServiceDTO.Output.class)))
                .willReturn(response);

        String content = objectMapper.writeValueAsString(patch);
        MockMultipartFile contentImage = stubData.getImage();

        MockPart part = new MockPart("content", content.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String urlTemplate = "/walk-logs/{walk-log-id}/contents/{content-id}";

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(
                                HttpMethod.PATCH,
                                urlTemplate,
                                stubData.getWalkLogId(), stubData.getWalkLogContentId())
                        .file(contentImage)
                        .part(part)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walkLogContentId").value(response.getWalkLogContentId()))
                .andExpect(jsonPath("$.text").value(response.getText()))
                .andDo(document(
                        "patch-walk-log-content",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기 기록 식별자"),
                                parameterWithName("content-id").description("걷기 중 순간기록 식별자")
                        ),
                        requestParts(
                                partWithName("contentImage").description("걷기 중 순간기록 사진").optional(),
                                partWithName("content").description("걷기 중 순간기록 JSON 데이터")
                        ),
                        requestPartFields("content",
                                fieldWithPath("text").type(JsonFieldType.STRING).description("걷기 중 순간기록 텍스트").optional()
                        ),
                        responseFields(
                                fieldWithPath("walkLogContentId").type(JsonFieldType.NUMBER).description("걷기 중 순간기록 식별자"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("순간기록 생성시각"),
                                fieldWithPath("text").type(JsonFieldType.STRING).description("걷기 중 순간기록 텍스트"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("걷기 중 순간기록 사진 임시 URL")
                        )
                ));
    }

    @Test
    void deleteContent() throws Exception {
        // given
        Long walkLogId = stubData.getWalkLogId();
        Long walkLogContentId = stubData.getWalkLogContentId();

        String urlTemplate = "/walk-logs/{walk-log-id}/contents/{content-id}";

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.delete(urlTemplate, walkLogId, walkLogContentId)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document(
                        "delete-walk-log-content",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기 기록 식별자"),
                                parameterWithName("content-id").description("걷기 중 순간기록 식별자")
                        )
                ));
    }
}