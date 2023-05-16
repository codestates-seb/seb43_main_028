package backend.section6mainproject.member.controller;

import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.entity.WalkLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberMapper mapper;

    @MockBean
    private MemberService memberService;


    @Test
    void postMemberTest() throws Exception {
        //given 테스트에 필요한 객체들을 준비한다.
        MemberControllerDTO.Post post = new MemberControllerDTO.Post();
        post.setEmail("test@gmail.com");
        post.setPassword("testdot01!");
        post.setNickname("거터");

        String content = objectMapper.writeValueAsString(post);

        MemberServiceDTO.CreateInput input = new MemberServiceDTO.CreateInput();
        MemberServiceDTO.CreateOutput createOutput = new MemberServiceDTO.CreateOutput();
        createOutput.setMemberId(1L);

        //when mock객체를 이용하여 테스트한다.
        given(mapper.postToCreateInput(Mockito.any())).willReturn(input);
        given(memberService.createMember(Mockito.any())).willReturn(createOutput);
        given(mapper.createOutputToPostResponse(Mockito.any())).willReturn(new MemberControllerDTO.PostResponse(createOutput.getMemberId()));

        //then 결과를 확정한다.
        ResultActions result = mockMvc.perform(
                post("/members/sign")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        //then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(1L));
    }

    @Test
    void getMemberTest() throws Exception{
        //given
        Long memberId = 1L;
        MemberServiceDTO.Output output = makeMemberOutput();
        MemberControllerDTO.Response response = makeMemberResponse();
        //when
        given(memberService.findMember(Mockito.anyLong())).willReturn(output);
        given(mapper.outputToResponse(Mockito.any(MemberServiceDTO.Output.class))).willReturn(response);


        //then
        ResultActions actions = mockMvc.perform(
                get("/members/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void deleteMemberTest() throws Exception {
        Long memberId = 1L;
        doNothing().when(memberService).deleteMember(Mockito.anyLong());
        //then
        ResultActions actions = mockMvc.perform(
                delete("/members/" + memberId))
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteMember(Mockito.anyLong());
    }

    @Test
    void patchMemberTest() throws Exception, IOException {
        //given
        Long memberId = 1L;
        String introduction = "처음뵙겠습니다.";
        String nickname = "깃허볼래";
        WalkLog.WalkLogPublicSetting setting = WalkLog.WalkLogPublicSetting.PUBLIC;

        MemberControllerDTO.Patch originalPatch = new MemberControllerDTO.Patch();
        originalPatch.setIntroduction(introduction);
        originalPatch.setNickname(nickname);
        originalPatch.setDefaultWalkLogPublicSetting(setting);
        MemberServiceDTO.Output output = makeMemberOutput();

        MemberControllerDTO.Response response = makeMemberResponse();
        response.setNickname(nickname);
        response.setIntroduction(introduction);
        response.setDefaultWalkLogPublicSetting(setting.toString());

        String patch = objectMapper.writeValueAsString(originalPatch);
        FileInputStream inputStream = new FileInputStream("src/test/resources/imageSource/" + "블랙홀.jpeg");
        // Create a MockMultipartFile for the profileImage
        MockMultipartFile profileImageFile = new MockMultipartFile(
                "profileImage", "블랙홀.jpeg", "jpeg", inputStream
        );

        MockPart part = new MockPart("patch", patch.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(mapper.patchToUpdateInput(any())).thenReturn(new MemberServiceDTO.UpdateInput());
        when(memberService.updateMember(any())).thenReturn(output);
        given(mapper.outputToResponse(Mockito.any(MemberServiceDTO.Output.class))).willReturn(response);

        // when
        // Perform the request and validate the response
       ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH,"/members/{member-id}", memberId)
                        .file(profileImageFile)
                        .part(part)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()));
    }
    private MemberServiceDTO.Output makeMemberOutput() {
        Long memberId = 1L;
        MemberServiceDTO.Output output = new MemberServiceDTO.Output(memberId, "test@gmail.com", "거터", null, "PRIVATE", null, 0, 0);
        return output;
    }

    private MemberControllerDTO.Response makeMemberResponse() {
        Long memberId = 1L;
        MemberControllerDTO.Response response = new MemberControllerDTO.Response(memberId, "test@gmail.com", "거터", null, "PRIVATE", null, 0, 0);
        return response;
    }

}
