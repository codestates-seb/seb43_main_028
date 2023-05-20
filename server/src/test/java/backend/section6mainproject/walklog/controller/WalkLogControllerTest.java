package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;
import backend.section6mainproject.walklog.WalkLogStubData;
import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;

import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.section6mainproject.util.ApiDocumentUtils.getRequestPreProcessor;
import static backend.section6mainproject.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@WebMvcTest(controllers = WalkLogController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean({JpaMetamodelMappingContext.class, StompExceptionAdvice.class})
@AutoConfigureRestDocs
public class WalkLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalkLogMapper walkLogMapper;
    private WalkLogStubData walkLogStubData;
    private WalkLogContentStubData walkLogContentStubData;
    private ObjectMapper objectMapper;
    @MockBean
    private WalkLogService walkLogService;
    private Double num = 2.424;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        walkLogStubData = new WalkLogStubData();
        walkLogContentStubData = new WalkLogContentStubData();
    }
    @Test
    void postWalkLogTest() throws Exception {
        WalkLogControllerDTO.PostResponse response = walkLogStubData.getResponse();
        UsernamePasswordAuthenticationToken principal = UsernamePasswordAuthenticationToken
                .authenticated(1L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        WalkLogControllerDTO.PostResponse response = stubData.getResponse();

        given(walkLogService.createWalkLog(Mockito.any(WalkLogServiceDTO.CreateInput.class))).willReturn(new WalkLogServiceDTO.CreateOutput());
        given(walkLogMapper.walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(Mockito.any(WalkLogServiceDTO.CreateOutput.class)))
                .willReturn(response);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/walk-logs")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(principal)
                );
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walkLogId").value(response.getWalkLogId()))
                .andDo(document(
                        "post-walk-log",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                fieldWithPath("walkLogId").type(JsonFieldType.NUMBER).description("걷기기록 식별자")
                        )
                ));
    }

    @Test
    void patchWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());
        WalkLogControllerDTO.Patch patch = walkLogStubData.getPatch();
        WalkLogControllerDTO.DetailResponse detailResponse = walkLogStubData.getDetailResponse();
        WalkLogContentControllerDTO.Response response = walkLogContentStubData.getResponse();
        ArrayList<WalkLogContentControllerDTO.Response> responses = new ArrayList<>();
        responses.add(response);
        detailResponse.setWalkLogContents(responses);
        List<CoordinateControllerDTO.Sub> coordinates = createCoordinateControllerDTOsub(num);
        detailResponse.setCoordinates(coordinates);

        String jsonPatchWalkLogDto = objectMapper.writeValueAsString(patch);

        given(walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(Mockito.any(WalkLogControllerDTO.Patch.class))).willReturn(new WalkLogServiceDTO.UpdateInput());
        given(walkLogService.updateWalkLog(Mockito.any(WalkLogServiceDTO.UpdateInput.class))).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class))).willReturn(detailResponse);

        String urlTemplate = "/walk-logs/{walk-log-id}";

        //when
        ResultActions perform = mockMvc.perform(
                patch(urlTemplate,walkLog.getWalkLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonPatchWalkLogDto)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()))
                .andExpect(jsonPath("$.walkLogPublicSetting").value(String.valueOf(detailResponse.getWalkLogPublicSetting())))
                .andDo(document(
                        "patch-walk-log",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기 기록 식별자")
                        ),
                        requestParameters(
                                parameterWithName("message").description("한줄 메시지").optional(),
                                parameterWithName("walkLogPublicSetting").description("걷기 기록 공개 설정(PUBLIC, PRIVATE)").optional()
                        ),
                        responseFields(
                                fieldWithPath("walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("mapImage").type(JsonFieldType.STRING).description("지도 이미지 임시 URL"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("걷기 기록이 생성된 시각"),
                                fieldWithPath("endAt").type(JsonFieldType.STRING).description("걷기 기록 종료한 시각"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("걷기 종료후 생성한 한줄 메시지"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("walkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 공개 설정(PUBLIC, PRIVATE)"),

                                fieldWithPath("coordinates").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 좌표 기록들"),
                                fieldWithPath("coordinates.[].coordinateId").type(JsonFieldType.NUMBER).description("좌표 기록 식별자"),
                                fieldWithPath("coordinates.[].lat").type(JsonFieldType.NUMBER).description("좌표 위도"),
                                fieldWithPath("coordinates.[].lng").type(JsonFieldType.NUMBER).description("좌표 경도"),
                                fieldWithPath("coordinates.[].createdAt").type(JsonFieldType.STRING).description("좌표 기록 생성된 시간"),

                                fieldWithPath("walkLogContents").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 순간 기록들"),
                                fieldWithPath("walkLogContents.[].walkLogContentId").type(JsonFieldType.NUMBER).description("순간 기록 식별자"),
                                fieldWithPath("walkLogContents.[].createdAt").type(JsonFieldType.STRING).description("순간 기록 생성된 시간"),
                                fieldWithPath("walkLogContents.[].text").type(JsonFieldType.STRING).description("순간 기록 메세지"),
                                fieldWithPath("walkLogContents.[].imageUrl").type(JsonFieldType.STRING).description("순간 기록 이미지 임시 URL")
                        )
                ));
    }

    @Test
    void getWalkLogsTest() throws Exception {
        //given
        WalkLogControllerDTO.GetFeedRequest getFeedRequest = walkLogStubData.getFeedRequest();
        WalkLogControllerDTO.GetFeedResponse getFeedResponse = walkLogStubData.getFeedResponse();
        WalkLogContentControllerDTO.Response response = walkLogContentStubData.getResponse();
        ArrayList<WalkLogContentControllerDTO.Response> responses = new ArrayList<>();
        responses.add(response);
        getFeedResponse.setWalkLogContents(responses);

        ArrayList<WalkLogServiceDTO.FindFeedOutput> findFeedOutputs = new ArrayList<>();
        findFeedOutputs.add(walkLogStubData.getFindFeedOutput());
        findFeedOutputs.add(walkLogStubData.getFindFeedOutput());

        given(walkLogMapper.walkLogControllerGetMemberRequestDTOtoWalkLogServiceFindFeedInputDTO(Mockito.any(WalkLogControllerDTO.GetFeedRequest.class)))
                .willReturn(new WalkLogServiceDTO.FindFeedInput());
        given(walkLogService.findFeedWalkLogs(Mockito.any(WalkLogServiceDTO.FindFeedInput.class)))
                .willReturn(new PageImpl<>(findFeedOutputs));
        given(walkLogMapper.walkLogServiceFindFeedOutputDTOtoWalkLogControllerGetFeedResponseDTO(Mockito.any(WalkLogServiceDTO.FindFeedOutput.class)))
                .willReturn(getFeedResponse);
        //when
        ResultActions perform = mockMvc.perform(
                get("/walk-logs")
                        .param("page",String.valueOf(getFeedRequest.getPage()))
                        .param("size",String.valueOf(getFeedRequest.getSize())));
        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(findFeedOutputs.size()))
                .andExpect(jsonPath("$.data[0].walkLogId").value(getFeedResponse.getWalkLogId()))
                .andExpect(jsonPath("$.data[0].message").value(getFeedResponse.getMessage()))
                .andDo(document(
                        "get-walk-logs",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("걷기기록 데이터를 리스트로 가지고 있습니다"),
                                fieldWithPath("data.[].walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("data.[].mapImage").type(JsonFieldType.STRING).description("지도 이미지 임시 URL"),
                                fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.[].profileImage").type(JsonFieldType.STRING).description("프로필 이미지 임시 URL"),
                                fieldWithPath("data.[].startedAt").type(JsonFieldType.STRING).description("걷기 기록이 생성된 시각"),
                                fieldWithPath("data.[].endAt").type(JsonFieldType.STRING).description("걷기 기록 종료한 시각"),
                                fieldWithPath("data.[].message").type(JsonFieldType.STRING).description("걷기 종료후 생성한 한줄 메시지"),

                                fieldWithPath("data.[].walkLogContents").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 순간 기록들"),
                                fieldWithPath("data.[].walkLogContents.[].walkLogContentId").type(JsonFieldType.NUMBER).description("순간 기록 식별자"),
                                fieldWithPath("data.[].walkLogContents.[].createdAt").type(JsonFieldType.STRING).description("순간 기록 생성된 시간"),
                                fieldWithPath("data.[].walkLogContents.[].text").type(JsonFieldType.STRING).description("순간 기록 메세지"),
                                fieldWithPath("data.[].walkLogContents.[].imageUrl").type(JsonFieldType.STRING).description("순간 기록 이미지 임시 URL"),

                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보를 가지고 있습니다"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("한 페이지에 표시하는 데이터의 개수"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지")
                                )
                ));
    }
    @Test
    void endWalkLogTest() throws Exception {
        //given
        WalkLogControllerDTO.EndPost endPostDTO = walkLogStubData.getEndPost();
        WalkLogControllerDTO.DetailResponse detailResponse = walkLogStubData.getDetailResponse();
        WalkLogContentControllerDTO.Response response = walkLogContentStubData.getResponse();
        ArrayList<WalkLogContentControllerDTO.Response> responses = new ArrayList<>();
        responses.add(response);
        detailResponse.setWalkLogContents(responses);
        List<CoordinateControllerDTO.Sub> coordinates = createCoordinateControllerDTOsub(num);
        detailResponse.setCoordinates(coordinates);

        String jsonEndWalkLogDTO = objectMapper.writeValueAsString(endPostDTO);
        MockMultipartFile mapImageFile = walkLogStubData.getImage();
        MockPart part = new MockPart("endPost", jsonEndWalkLogDTO.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        given(walkLogMapper.walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(Mockito.any(WalkLogControllerDTO.EndPost.class))).
                willReturn(new WalkLogServiceDTO.ExitInput());
        given(walkLogService.exitWalkLog(Mockito.any(WalkLogServiceDTO.ExitInput.class))).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class)))
                .willReturn(detailResponse);

        String urlTemplate = "/walk-logs/{walk-log-id}";

        //when
        ResultActions perform = mockMvc
                .perform(MockMvcRequestBuilders.multipart(HttpMethod.POST,urlTemplate, walkLogStubData.getWalkLogId())
                        .file(mapImageFile)
                        .part(part)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)

                );

        //then
        perform
                //예상되는 결과는 status가 isok
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()))
                .andDo(document(
                        "end-walk-log",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기 기록 식별자")
                        ),
                        requestParts(
                                partWithName("mapImage").description("걸은 위치를 이어 그린 지도").optional(),
                                partWithName("endPost").description("걷기 기록종료시 필요한 JSON 데이터")
                        ),
                        requestPartFields("endPost",
                                fieldWithPath("message").type(JsonFieldType.STRING).description("한줄 메세지").optional(),
                                fieldWithPath("walkLogPublicSetting").type(JsonFieldType.STRING).
                                        description("걷기 기록 공개 설정(PUBLIC, PRIVATE)").optional()
                        ),
                        responseFields(
                                fieldWithPath("walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("mapImage").type(JsonFieldType.STRING).description("지도 이미지 임시 URL"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("걷기 기록이 생성된 시각"),
                                fieldWithPath("endAt").type(JsonFieldType.STRING).description("걷기 기록 종료한 시각"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("걷기 종료후 생성한 한줄 메시지"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("walkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 공개 설정(PUBLIC, PRIVATE)"),

                                fieldWithPath("coordinates").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 좌표 기록들"),
                                fieldWithPath("coordinates.[].coordinateId").type(JsonFieldType.NUMBER).description("좌표 기록 식별자"),
                                fieldWithPath("coordinates.[].lat").type(JsonFieldType.NUMBER).description("좌표 위도"),
                                fieldWithPath("coordinates.[].lng").type(JsonFieldType.NUMBER).description("좌표 경도"),
                                fieldWithPath("coordinates.[].createdAt").type(JsonFieldType.STRING).description("좌표 기록 생성된 시간"),

                                fieldWithPath("walkLogContents").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 순간 기록들"),
                                fieldWithPath("walkLogContents.[].walkLogContentId").type(JsonFieldType.NUMBER).description("순간 기록 식별자"),
                                fieldWithPath("walkLogContents.[].createdAt").type(JsonFieldType.STRING).description("순간 기록 생성된 시간"),
                                fieldWithPath("walkLogContents.[].text").type(JsonFieldType.STRING).description("순간 기록 메세지"),
                                fieldWithPath("walkLogContents.[].imageUrl").type(JsonFieldType.STRING).description("순간 기록 이미지 임시 URL")
                        )
                ));
    }

    private static List<CoordinateControllerDTO.Sub> createCoordinateControllerDTOsub(Double num) {
        List<CoordinateControllerDTO.Sub> coordinates = new ArrayList<>();
        CoordinateControllerDTO.Sub sub = new CoordinateControllerDTO.Sub(1L, num, num, LocalDateTime.now());
        CoordinateControllerDTO.Sub sub2 = new CoordinateControllerDTO.Sub(2L, num, num, LocalDateTime.now());
        coordinates.add(sub);
        coordinates.add(sub2);
        return coordinates;
    }

    @Test
    void getWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = walkLogStubData.getRecordingWalkLog(walkLogStubData.getMember());
        WalkLogControllerDTO.DetailResponse detailResponse = walkLogStubData.getDetailResponse();
        WalkLogContentControllerDTO.Response response = walkLogContentStubData.getResponse();
        ArrayList<WalkLogContentControllerDTO.Response> responses = new ArrayList<>();
        responses.add(response);
        detailResponse.setWalkLogContents(responses);
        List<CoordinateControllerDTO.Sub> coordinates = createCoordinateControllerDTOsub(num);
        detailResponse.setCoordinates(coordinates);

        given(walkLogService.findWalkLog(walkLog.getWalkLogId())).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class)))
                .willReturn(detailResponse);

        String urlTemplate = "/walk-logs/{walk-log-id}";
        //when
        ResultActions perform = mockMvc.perform(
                get(urlTemplate, walkLog.getWalkLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate));
        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.walkLogId").value(walkLog.getWalkLogId()))
//                .andExpect(jsonPath("$.endTime").value(equalTo(walkLog.getEndTime()))) jsonPath에서 시간뒤에 00이더 붙음
                .andExpect(jsonPath("$.message").value(walkLog.getMessage()))
                .andExpect(jsonPath("$.memberId").value(walkLog.getMember().getMemberId()))
                .andExpect(jsonPath("$.nickname").value(walkLog.getMember().getNickname()))
                .andDo(document(
                        "get-walk-log",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("걷기기록 식별자")
                        ),
                        responseFields(
                                fieldWithPath("walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("mapImage").type(JsonFieldType.STRING).description("지도 이미지 임시 URL"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("걷기 기록이 생성된 시각"),
                                fieldWithPath("endAt").type(JsonFieldType.STRING).description("걷기 기록 종료한 시각"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("걷기 종료후 생성한 한줄 메시지"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("walkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 공개 설정(PUBLIC, PRIVATE)"),

                                fieldWithPath("coordinates").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 좌표 기록들"),
                                fieldWithPath("coordinates.[].coordinateId").type(JsonFieldType.NUMBER).description("좌표 기록 식별자"),
                                fieldWithPath("coordinates.[].lat").type(JsonFieldType.NUMBER).description("좌표 위도"),
                                fieldWithPath("coordinates.[].lng").type(JsonFieldType.NUMBER).description("좌표 경도"),
                                fieldWithPath("coordinates.[].createdAt").type(JsonFieldType.STRING).description("좌표 기록 생성된 시간"),

                                fieldWithPath("walkLogContents").type(JsonFieldType.ARRAY).description("해당 걷기 기록에 기록된 순간 기록들"),
                                fieldWithPath("walkLogContents.[].walkLogContentId").type(JsonFieldType.NUMBER).description("순간 기록 식별자"),
                                fieldWithPath("walkLogContents.[].createdAt").type(JsonFieldType.STRING).description("순간 기록 생성된 시간"),
                                fieldWithPath("walkLogContents.[].text").type(JsonFieldType.STRING).description("순간 기록 메세지"),
                                fieldWithPath("walkLogContents.[].imageUrl").type(JsonFieldType.STRING).description("순간 기록 이미지 임시 URL")

                        )
                ));
    }

    @Test
    void deleteWalkLogTest() throws Exception {
        //given
        Long walkLogId = 1L;
        String urlTemplate = "/walk-logs/{walk-log-id}";
        doNothing().when(walkLogService).deleteWalkLog(Mockito.anyLong());
        //when
        ResultActions actions = mockMvc.perform(
                delete(urlTemplate, walkLogId)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );
        verify(walkLogService, times(1)).deleteWalkLog(Mockito.anyLong());

        actions
                .andExpect(status().isNoContent())
                .andDo(document(
                        "delete-walk-log",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("walk-log-id").description("회원 식별자")
                        )
                ));

    }
}