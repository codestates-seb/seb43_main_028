package backend.section6mainproject.member.controller;

import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.member.MemberStubData;
import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.service.MemberService;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.mapper.WalkLogMapper;
import backend.section6mainproject.walklog.service.WalkLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.section6mainproject.util.ApiDocumentUtils.*;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(controllers = MemberController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean({JpaMetamodelMappingContext.class, StompExceptionAdvice.class})
@AutoConfigureRestDocs
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private MemberStubData stubData;

    @MockBean
    private WalkLogMapper walkLogMapper;
    @MockBean
    private WalkLogService walkLogService;
    @MockBean
    private MemberMapper mapper;
    @MockBean
    private MemberService memberService;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        stubData = new MemberStubData();
    }


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

        given(mapper.postToCreateInput(Mockito.any())).willReturn(input);
        given(memberService.createMember(Mockito.any())).willReturn(createOutput);
        given(mapper.createOutputToPostResponse(Mockito.any())).willReturn(new MemberControllerDTO.PostResponse(createOutput.getMemberId()));



        // when
        ResultActions result = mockMvc.perform(
                post("/members/sign")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andDo(document(
                        "post-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디로 사용되는 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자")
                        )
                ));
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

        String patch = objectMapper.writeValueAsString(originalPatch);
        // Create a MockMultipartFile for the profileImage
        MockMultipartFile profileImageFile = stubData.getImage();


        MockPart part = new MockPart("patch", patch.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(mapper.patchToUpdateInput(any())).thenReturn(new MemberServiceDTO.UpdateInput());
        when(memberService.updateMember(any())).thenReturn(output);
        given(mapper.outputToResponse(Mockito.any(MemberServiceDTO.Output.class))).willReturn(response);

        String urlTemplate = "/members/{member-id}";

        // when
        // Perform the request and validate the response
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, urlTemplate, memberId)
                .file(profileImageFile)
                .part(part)
                .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andDo(document(
                        "patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestParts(
                                partWithName("profileImage").description("회원 프로필 이미지"),
                                partWithName("patch").description("회원 수정용 JSON 데이터")
                        ),
                        requestPartFields("patch",
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").optional(),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개").optional(),
                                fieldWithPath("defaultWalkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 디폴트 공개 설정(PUBLIC, PRIVATE)").optional()
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                                fieldWithPath("defaultWalkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 디폴트 공개 설정"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("프로필 이미지 임시 URL"),
                                fieldWithPath("totalWalkLog").type(JsonFieldType.NUMBER).description("총 걷기 기록 수"),
                                fieldWithPath("totalWalkLogContent").type(JsonFieldType.NUMBER).description("총 걷기 중 순간 기록 수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("회원 생성 시각")
                        )
                ));
    }

    @Test
    void getMemberTest() throws Exception{
        //given
        Long memberId = 1L;
        MemberServiceDTO.Output output = makeMemberOutput();
        MemberControllerDTO.Response response = makeMemberResponse();

        given(memberService.findMember(Mockito.anyLong())).willReturn(output);
        given(mapper.outputToResponse(Mockito.any(MemberServiceDTO.Output.class))).willReturn(response);

        String urlTemplate = "/members/{member-id}";

        // when
        ResultActions actions = mockMvc.perform(
                get(urlTemplate, memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andDo(document(
                        "get-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                                fieldWithPath("defaultWalkLogPublicSetting").type(JsonFieldType.STRING).description("걷기 기록 디폴트 공개 설정"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("프로필 이미지 임시 URL"),
                                fieldWithPath("totalWalkLog").type(JsonFieldType.NUMBER).description("총 걷기 기록 수"),
                                fieldWithPath("totalWalkLogContent").type(JsonFieldType.NUMBER).description("총 걷기 중 순간 기록 수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("회원 생성 시각")
                        )
                ));
    }
    @Test
    void getMyWalkLogsTest() throws Exception {
        //given
        Long memberId = 1L;
        WalkLogControllerDTO.GetRequests getRequests = createWalkLogControllerDTOgetRequests();
        WalkLogServiceDTO.FindsInput findsInput = createWalkLogServiceDTOfindsInput();
        WalkLogControllerDTO.Response response = new WalkLogControllerDTO.Response();
        ArrayList<WalkLogServiceDTO.FindsOutput> findsOutputs = new ArrayList<>();
        WalkLogServiceDTO.FindsOutput findsOutput = new WalkLogServiceDTO.FindsOutput();
        findsOutputs.add(findsOutput);
        findsOutputs.add(findsOutput);
        response.setWalkLogId(1L);
        response.setMessage("메세지");

        given(walkLogMapper.walkLogControllerGetRequestsDTOtoWalkLogServiceFindsInputDTO(Mockito.any(WalkLogControllerDTO.GetRequests.class)))
                .willReturn(findsInput);
        given(walkLogService.findMyWalkLogs(Mockito.any(WalkLogServiceDTO.FindsInput.class)))
                .willReturn(new PageImpl<>(findsOutputs));
        given(walkLogMapper.walkLogServiceFindsOutputDTOtoWalkLogControllerResponseDTO(Mockito.any(WalkLogServiceDTO.FindsOutput.class)))
                .willReturn(response);
        //when
        ResultActions perform = mockMvc.perform(
                get("/members/"+memberId+"/walk-logs")
                        .param("page",String.valueOf(getRequests.getPage()))
                        .param("size",String.valueOf(getRequests.getSize()))
                        .param("year",String.valueOf(getRequests.getYear()))
                        .param("month",String.valueOf(getRequests.getMonth()))
                        .param("day",String.valueOf(getRequests.getDay())));
                //then
        perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.data.size()").value(findsOutputs.size()))
        .andExpect(jsonPath("$.data[0].walkLogId").value(response.getWalkLogId()))
        .andExpect(jsonPath("$.data[0].message").value(response.getMessage()));
    }

    private static WalkLogControllerDTO.GetRequests createWalkLogControllerDTOgetRequests() {
        WalkLogControllerDTO.GetRequests getRequests = new WalkLogControllerDTO.GetRequests();
        getRequests.setSize(3);
        getRequests.setPage(1);
        getRequests.setYear(LocalDateTime.now().getYear());
        getRequests.setMonth(LocalDateTime.now().getMonthValue());
        getRequests.setDay(LocalDateTime.now().getDayOfMonth());
        return getRequests;
    }
    private static WalkLogServiceDTO.FindsInput createWalkLogServiceDTOfindsInput() {
        WalkLogServiceDTO.FindsInput findsInput = new WalkLogServiceDTO.FindsInput();
        findsInput.setSize(1);
        findsInput.setPage(3);
        findsInput.setYear(LocalDateTime.now().getYear());
        findsInput.setMonth(LocalDateTime.now().getMonthValue());
        findsInput.setDay(LocalDateTime.now().getDayOfMonth());
        return findsInput;
    }

    @Test
    void getMyWalkLogsForCalendarTest() throws Exception {
        Long memberId = 1L;
        WalkLogControllerDTO.GetCalendarRequests request = new WalkLogControllerDTO.GetCalendarRequests();
        request.setYear(LocalDateTime.now().getYear());
        request.setMonth(LocalDateTime.now().getMonthValue());
        WalkLogControllerDTO.CalendarResponse calendarResponse = new WalkLogControllerDTO.CalendarResponse();
        calendarResponse.setWalkLogId(1L);
        WalkLogControllerDTO.CalendarResponse calendarResponse2 = new WalkLogControllerDTO.CalendarResponse();
        calendarResponse2.setWalkLogId(2L);
        List<WalkLogControllerDTO.CalendarResponse> calendarResponses = new ArrayList<>();
        calendarResponses.add(calendarResponse);
        calendarResponses.add(calendarResponse2);
        given(walkLogMapper.walkLogControllerGetCalenderRequestsDTOtoWalkLogServiceCalenderFindsInputDTO(Mockito.any(WalkLogControllerDTO.GetCalendarRequests.class)))
                .willReturn(new WalkLogServiceDTO.CalenderFindsInput());
        given(walkLogService.findMyMonthWalkLogs(Mockito.any(WalkLogServiceDTO.CalenderFindsInput.class)))
                .willReturn(new ArrayList<>());
        given(walkLogMapper.WalkLogServiceCalenderFindsOutputDTOsToWalkLogControllerCalendarResponseDTOs(Mockito.anyList()))
                .willReturn(calendarResponses);
        ResultActions perform = mockMvc.perform(
                get("/members/"+memberId+"/walk-logs/calendar")
                        .param("year",String.valueOf(request.getYear()))
                        .param("month",String.valueOf(request.getMonth())));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(calendarResponses.size()))
                .andExpect(jsonPath("$[0].walkLogId").value(calendarResponse.getWalkLogId()))
                .andExpect(jsonPath("$[1].walkLogId").value(calendarResponse2.getWalkLogId()));

    }

    @Test
    void deleteMemberTest() throws Exception {
        // given
        Long memberId = 1L;
        String urlTemplate = "/members/{member-id}";

        // when
        ResultActions actions = mockMvc.perform(
                delete(urlTemplate, memberId)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );


        // then
        verify(memberService, times(1)).deleteMember(Mockito.anyLong());
        actions
                .andExpect(status().isNoContent())
                .andDo(document(
                        "delete-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        )
                ));
    }

    @Test
    void patchMemberPasswordTest() throws Exception {
        // given
        Long memberId = 1L;
        String newPassword = "starstar0101$";
        MemberServiceDTO.UpdatePwInput pwInput = new MemberServiceDTO.UpdatePwInput();
        pwInput.setMemberId(memberId);
        pwInput.setPassword(newPassword);
        MemberControllerDTO.PatchPw patchPw = new MemberControllerDTO.PatchPw();
        patchPw.setPassword("starstar0101$");
        String content = objectMapper.writeValueAsString(patchPw);

        given(mapper.patchPwToUpdatePwInput(Mockito.any(MemberControllerDTO.PatchPw.class))).willReturn(pwInput);

        String urlTemplate = "/members/{member-id}/pw";

        // when
        ResultActions actions = mockMvc.perform(
                patch(urlTemplate, memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate)
        );

        // then
        verify(memberService, times(1)).updateMemberPassword(Mockito.any(MemberServiceDTO.UpdatePwInput.class));
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "patch-member-pw",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                        )
                ));

    }
    private MemberServiceDTO.Output makeMemberOutput() {
        Long memberId = 1L;
        MemberServiceDTO.Output output = new MemberServiceDTO.Output(memberId, "test@gmail.com", "거터", null, "PRIVATE", null, 0, 0, LocalDateTime.now());
        return output;
    }

    private MemberControllerDTO.Response makeMemberResponse() {
        Long memberId = 1L;
        String introduction = "처음뵙겠습니다.";
        String nickname = "깃허볼래";
        WalkLog.WalkLogPublicSetting setting = WalkLog.WalkLogPublicSetting.PUBLIC;
        MemberControllerDTO.Response response = new MemberControllerDTO.Response(memberId, "test@gmail.com", "거터", null, WalkLog.WalkLogPublicSetting.PRIVATE, null, 0, 0, LocalDateTime.now());
        response.setNickname(nickname);
        response.setImageUrl("/test/image/test.jpg");
        response.setIntroduction(introduction);
        response.setDefaultWalkLogPublicSetting(setting);
        return response;
    }

}
