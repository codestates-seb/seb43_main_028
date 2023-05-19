package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;

import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class WalkLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalkLogMapper walkLogMapper;
    @MockBean
    private WalkLogService walkLogService;

    @Test
    void postWalkLogTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        WalkLogControllerDTO.Post post = new WalkLogControllerDTO.Post();
        post.setMemberId(1L);
        WalkLogControllerDTO.PostResponse response = new WalkLogControllerDTO.PostResponse();
        response.setWalkLogId(1L);
        String content = objectMapper.writeValueAsString(post);

        given(walkLogMapper.walkLogControllerPostDTOtoWalkLogServiceCreateInputDTO(Mockito.any(WalkLogControllerDTO.Post.class))).willReturn(new WalkLogServiceDTO.CreateInput());
        given(walkLogService.createWalkLog(Mockito.any(WalkLogServiceDTO.CreateInput.class))).willReturn(new WalkLogServiceDTO.CreateOutput());
        given(walkLogMapper.walkLogServiceCreateOutPutDTOtoWalkLogControllerPostResponseDTO(Mockito.any(WalkLogServiceDTO.CreateOutput.class)))
                .willReturn(response);
        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/walk-logs")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walkLogId").value(response.getWalkLogId()));
    }

    @Test
    void patchWalkLogTest() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        WalkLog walkLog = createWalkLog();
        //WalkLogDto.Patch 객체가 1개 필요
        WalkLogControllerDTO.Patch patchWalkLogDto = new WalkLogControllerDTO.Patch();
        patchWalkLogDto.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        patchWalkLogDto.setMessage("안녕하세용");
        WalkLogControllerDTO.DetailResponse detailResponse = new WalkLogControllerDTO.DetailResponse();
        detailResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        detailResponse.setMessage("안녕하세용");
        //Json 데이터 생성
        String jsonPatchWalkLogDto = objectMapper.writeValueAsString(patchWalkLogDto);
        //walkLogService.updateWalkLog메서드 로직 Mock수행
        given(walkLogMapper.walkLogControllerPatchDTOtoWalkLogServiceUpdateInputDTO(Mockito.any(WalkLogControllerDTO.Patch.class))).willReturn(new WalkLogServiceDTO.UpdateInput());
        given(walkLogService.updateWalkLog(Mockito.any(WalkLogServiceDTO.UpdateInput.class))).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class))).willReturn(detailResponse);
        //when
        //patchWalkLog메서드를 수행 했을 때
        ResultActions perform = mockMvc.perform(
                patch("/walk-logs/" + walkLog.getWalkLogId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonPatchWalkLogDto));

        //then
        perform
                //예상되는 결과는 status가 isok
                .andExpect(status().isOk())
                //WalkLogDto.Patch객체로 전달받은 데이터들이 변경되었는지확인
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()))
                .andExpect(jsonPath("$.walkLogPublicSetting").value(String.valueOf(detailResponse.getWalkLogPublicSetting())));
    }

    @Test
    void getWalkLogsTest() throws Exception {
        //given
        WalkLogControllerDTO.GetFeedRequest getFeedRequest = new WalkLogControllerDTO.GetFeedRequest();
        getFeedRequest.setPage(1);
        getFeedRequest.setSize(10);
        ArrayList<WalkLogServiceDTO.FindFeedOutput> findFeedOutputs = new ArrayList<>();
        WalkLogServiceDTO.FindFeedOutput findFeedOutput = new WalkLogServiceDTO.FindFeedOutput();
        findFeedOutputs.add(findFeedOutput);
        findFeedOutputs.add(findFeedOutput);
        WalkLogControllerDTO.GetFeedResponse getFeedResponse = new WalkLogControllerDTO.GetFeedResponse();
        getFeedResponse.setWalkLogId(1L);
        getFeedResponse.setMessage("안녕하세요");
        getFeedResponse.setNickname("테스트");
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
        ObjectMapper objectMapper = new ObjectMapper();
        WalkLog walkLog = createWalkLog();

        WalkLogControllerDTO.EndPost endPostDTO = new WalkLogControllerDTO.EndPost();
        endPostDTO.setMessage("안녕하십니깟!");
        endPostDTO.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //update된 WalkLogControllerDetailResponse 객체
        WalkLogControllerDTO.DetailResponse detailResponse = createDetailResponse(walkLog);
        detailResponse.setMessage("안녕하십니깟!");
        detailResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //Json 데이터 생성
        String jasonEndWalkLogDTO = objectMapper.writeValueAsString(endPostDTO);
        FileInputStream inputStream = new FileInputStream("src/test/resources/testImage/" + "test.jpg");
        MockMultipartFile mapImageFile = new MockMultipartFile(
                "mapImage", "test.jpg", "jpg", inputStream
        );
        MockPart part = new MockPart("endPost", jasonEndWalkLogDTO.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //walkLogService.updateWalkLog메서드 로직 Mock수행
        given(walkLogMapper.walkLogControllerEndPostDTOtoWalkLogServiceExitInputDTO(Mockito.any(WalkLogControllerDTO.EndPost.class))).
                willReturn(new WalkLogServiceDTO.ExitInput());
        given(walkLogService.exitWalkLog(Mockito.any(WalkLogServiceDTO.ExitInput.class))).willReturn(new WalkLogServiceDTO.Output());
        given(walkLogMapper.walkLogServiceOutputDTOtoWalkLogControllerDetailResponseDTO(Mockito.any(WalkLogServiceDTO.Output.class))).willReturn(detailResponse);
        //when
        //patchWalkLog메서드를 수행 했을 때
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST,"/walk-logs/{walk-log-id}", walkLog.getWalkLogId())
                        .file(mapImageFile)
                        .part(part)
                );

        //then
        perform
                //예상되는 결과는 status가 isok
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(detailResponse.getMessage()));
    }

    private static WalkLogControllerDTO.DetailResponse createDetailResponse(WalkLog walkLog) {
        WalkLogControllerDTO.DetailResponse detailResponse = new WalkLogControllerDTO.DetailResponse();
        detailResponse.setWalkLogId(walkLog.getWalkLogId());
        detailResponse.setNickname(walkLog.getMember().getNickname());
        detailResponse.setMemberId(walkLog.getMember().getMemberId());
        detailResponse.setMessage(walkLog.getMessage());
        detailResponse.setWalkLogPublicSetting(walkLog.getWalkLogPublicSetting());
        detailResponse.setCreatedAt(walkLog.getCreatedAt());
        detailResponse.setEndAt(walkLog.getEndAt());
        return detailResponse;
    }

    @Test
    void getWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = createWalkLog();
        WalkLogControllerDTO.DetailResponse detailResponse = walkLogToWalkLogDetailResponseDTO(walkLog);
        //walkLogId를 http메서드로 요청보내고
        //요청을 통해서 잘 조회되는지 확인
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
    void getAllWalkLogsTest(){
        //전체 조회
        //given
        //walkLog 2개가 주어졌을 때
        List<WalkLog> walkLogListWithDifferentDates = createWalkLogListWithDifferentDates();
        //when
        //파라미터에 따로 date 값을 안넣고 요청을 보냈을 시
        //then
        // 두 데이터 값이 온전히 조회가 되는지


    }

    @Test
    void getWalkLogsByMonthTest(){
        //특정 월 조회
    }

    @Test
    void getWalkLogsByDayTest(){
        //특정 일 조회
    }
    @Test
    void deleteWalkLogTest() throws Exception {
        //WalkLogRepository에 저장되어있던 1번 데이터가 WalkLogService.deleteWalkLog에 의해서 삭제되어야함
        //given
        doNothing().when(walkLogService).deleteWalkLog(Mockito.anyLong());
        //when
        ResultActions perform = mockMvc.perform(
                        delete("/walk-logs/1"))
                .andExpect(status().isNoContent());
        //then
        verify(walkLogService, times(1)).deleteWalkLog(Mockito.anyLong());

    }

    private WalkLogControllerDTO.DetailResponse walkLogToWalkLogDetailResponseDTO(WalkLog walkLog) {
        WalkLogControllerDTO.DetailResponse detailResponse = new WalkLogControllerDTO.DetailResponse();
        detailResponse.setWalkLogId(walkLog.getWalkLogId());
        detailResponse.setMessage(walkLog.getMessage());
        detailResponse.setMemberId(walkLog.getMember().getMemberId());
        detailResponse.setNickname(walkLog.getMember().getNickname());
        return detailResponse;
    }

    private WalkLog createWalkLog() {
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("admin1@gmail.com");
        member.setPassword("12345");
        member.setNickname("거터볼래1");
        member.setIntroduction("안녕하세요1");
        //온전한 WalkLog 객체
        Long walkLogId = 1L;
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(walkLogId);
        walkLog.setMessage("안녕하십니까");
        return walkLog;
    }
    private List<WalkLog> createWalkLogListWithDifferentDates(){
        ArrayList<WalkLog> walkLogs = new ArrayList<>();
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("admin1@gmail.com");
        member.setPassword("12345");
        member.setNickname("거터볼래1");
        member.setIntroduction("안녕하세요1");
        //온전한 WalkLog 객체
        Long walkLogId = 1L;
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(walkLogId);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        walkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        walkLogs.add(walkLog);

        Long walkLogId2 = 2L;
        WalkLog walkLog2 = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(walkLogId2);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        walkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        walkLogs.add(walkLog2);
        return walkLogs;
    }


}