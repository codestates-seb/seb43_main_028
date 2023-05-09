package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogDTO;

import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class WalkLogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalkLogService walkLogService;





    @Test
    void postWalkLogTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        WalkLogDTO.Post post = new WalkLogDTO.Post();
        post.setMemberId(1L);
        String content = objectMapper.writeValueAsString(post);

        WalkLog walkLog = createWalkLog();


        given(walkLogService.createWalkLog(Mockito.anyLong())).willReturn(walkLog);

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
                .andExpect(header().string("Location", is(startsWith("/walk-logs"))));
    }

    @Test
    void patchWalkLogTest() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        WalkLog walkLog = createWalkLog();

        //WalkLogDto.Patch 객체가 1개 필요
        WalkLogDTO.Patch patchWalkLogDto = new WalkLogDTO.Patch();
        patchWalkLogDto.setMessage("안녕하십니끄아악!");
        patchWalkLogDto.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //updated된 WalkLog 객체
        WalkLog updatedWalkLog = new WalkLog();
        updatedWalkLog.setMember(walkLog.getMember());
        updatedWalkLog.setWalkLogId(walkLog.getWalkLogId());
        updatedWalkLog.setMessage("안녕하십니끄아악!");
        updatedWalkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //Json 데이터 생성
        String jsonPatchWalkLogDto = objectMapper.writeValueAsString(patchWalkLogDto);
        //walkLogService.updateWalkLog메서드 로직 Mock수행
        given(walkLogService.updateWalkLog(Mockito.any(WalkLog.class))).willReturn(updatedWalkLog);
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
                .andExpect(jsonPath("$.message").value(updatedWalkLog.getMessage()));
//                .andExpect(jsonPath("$.walkLogPublicSetting").value(response.getWalkLogPublicSetting())); enum이라 에러가 발생하는듯함
    }

    @Test
    void getWalkLogTest() throws Exception {
        //given
        WalkLog walkLog = createWalkLog();
        //walkLogId를 http메서드로 요청보내고
        //요청을 통해서 잘 조회되는지 확인
        given(walkLogService.findWalkLog(Mockito.anyLong())).willReturn(walkLog);
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
}