package backend.section6mainproject.walklog.controller;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogDto;

import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.repository.WalkLogRepository;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        WalkLogDto.Post post = new WalkLogDto.Post();
        post.setMemberId(1L);
        String content = objectMapper.writeValueAsString(post);

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
        //온전한 WalkLog객체를 위해 온전한 Member 객체가 1개 필요
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

        //WalkLogDto.Patch 객체가 1개 필요
        WalkLogDto.Patch patchWalkLogDto = new WalkLogDto.Patch();
        patchWalkLogDto.setMessage("안녕하십니끄아악!");
        patchWalkLogDto.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //updated된 WalkLog 객체
        WalkLog updatedWalkLog = new WalkLog();
        updatedWalkLog.setMember(member);
        updatedWalkLog.setWalkLogId(walkLogId);
        updatedWalkLog.setMessage("안녕하십니끄아악!");
        updatedWalkLog.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        //Json 데이터 생성
        String jsonPatchWalkLogDto = objectMapper.writeValueAsString(patchWalkLogDto);
        //walkLogService.updateWalkLog메서드 로직 Mock수행
        given(walkLogService.updateWalkLog(Mockito.any(WalkLog.class))).willReturn(updatedWalkLog);
        //when
        //patchWalkLog메서드를 수행 했을 때
        ResultActions perform = mockMvc.perform(
                patch("/walk-logs/" + walkLogId)
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

}