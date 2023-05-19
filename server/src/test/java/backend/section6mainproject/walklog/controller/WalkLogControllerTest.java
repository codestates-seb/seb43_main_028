package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.walklog.WalkLogStubData;
import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;

import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
    private WalkLogStubData stubData;
    private ObjectMapper objectMapper;
    @MockBean
    private WalkLogService walkLogService;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        stubData = new WalkLogStubData();
    }
    @Test
    void postWalkLogTest() throws Exception {
        WalkLogControllerDTO.Post post = stubData.getPost();
        WalkLogControllerDTO.PostResponse response = stubData.getResponse();
        String content = objectMapper.writeValueAsString(post);
        UsernamePasswordAuthenticationToken principal = UsernamePasswordAuthenticationToken
                .authenticated(1L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

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
                .andExpect(jsonPath("$.walkLogId").value(response.getWalkLogId()));
    }

    @Test
    void patchWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());
        WalkLogControllerDTO.Patch patch = stubData.getPatch();
        WalkLogControllerDTO.DetailResponse detailResponse = stubData.getDetailResponse();

        String jsonPatchWalkLogDto = objectMapper.writeValueAsString(patch);

        given(walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(Mockito.any(WalkLogControllerDTO.Patch.class))).willReturn(new WalkLogServiceDTO.UpdateInput());
        given(walkLogService.updateWalkLog(Mockito.any(WalkLogServiceDTO.UpdateInput.class))).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class))).willReturn(detailResponse);
        //when
        ResultActions perform = mockMvc.perform(
                patch("/walk-logs/" + walkLog.getWalkLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonPatchWalkLogDto));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()))
                .andExpect(jsonPath("$.walkLogPublicSetting").value(String.valueOf(detailResponse.getWalkLogPublicSetting())));
    }

    @Test
    void getWalkLogsTest() throws Exception {
        //given
        WalkLogControllerDTO.GetFeedRequest getFeedRequest = stubData.getFeedRequest();
        WalkLogControllerDTO.GetFeedResponse getFeedResponse = stubData.getFeedResponse();

        ArrayList<WalkLogServiceDTO.FindFeedOutput> findFeedOutputs = new ArrayList<>();
        findFeedOutputs.add(stubData.getFindFeedOutput());
        findFeedOutputs.add(stubData.getFindFeedOutput());

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
                .andExpect(jsonPath("$.data[0].message").value(getFeedResponse.getMessage()));
    }
    @Test
    void endWalkLogTest() throws Exception {
        //given
        WalkLogControllerDTO.EndPost endPostDTO = stubData.getEndPost();
        WalkLogControllerDTO.DetailResponse detailResponse = stubData.getDetailResponse();

        String jsonEndWalkLogDTO = objectMapper.writeValueAsString(endPostDTO);
        MockMultipartFile mapImageFile = stubData.getImage();
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
                .perform(MockMvcRequestBuilders.multipart(HttpMethod.POST,urlTemplate, stubData.getWalkLogId())
                        .file(mapImageFile)
                        .part(part)
                );

        //then
        perform
                //예상되는 결과는 status가 isok
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()));
    }

    @Test
    void getWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = stubData.getRecordingWalkLog(stubData.getMember());
        WalkLogControllerDTO.DetailResponse detailResponse = stubData.getDetailResponse();

        given(walkLogService.findWalkLog(walkLog.getWalkLogId())).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class)))
                .willReturn(detailResponse);
        //when
        ResultActions perform = mockMvc.perform(
                get("/walk-logs/" + walkLog.getWalkLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.walkLogId").value(walkLog.getWalkLogId()))
//                .andExpect(jsonPath("$.endTime").value(equalTo(walkLog.getEndTime()))) jsonPath에서 시간뒤에 00이더 붙음
                .andExpect(jsonPath("$.message").value(walkLog.getMessage()))
                .andExpect(jsonPath("$.memberId").value(walkLog.getMember().getMemberId()))
                .andExpect(jsonPath("$.nickname").value(walkLog.getMember().getNickname()));
    }

    @Test
    void deleteWalkLogTest() throws Exception {
        //given
        doNothing().when(walkLogService).deleteWalkLog(Mockito.anyLong());
        //when
        ResultActions perform = mockMvc.perform(
                        delete("/walk-logs/1"))
                .andExpect(status().isNoContent());
        //then
        verify(walkLogService, times(1)).deleteWalkLog(Mockito.anyLong());

    }
}