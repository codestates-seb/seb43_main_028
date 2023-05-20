package backend.section6mainproject.member.controller;

import backend.section6mainproject.advice.StompExceptionAdvice;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.section6mainproject.util.ApiDocumentUtils.*;
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
        WalkLogControllerDTO.GetMemberRequest getMemberRequest = createWalkLogControllerDTOgetRequests();
        WalkLogServiceDTO.FindInput findInput = createWalkLogServiceDTOfindsInput();
        ArrayList<WalkLogServiceDTO.FindOutput> findOutputs = new ArrayList<>();
        WalkLogServiceDTO.FindOutput findOutput = new WalkLogServiceDTO.FindOutput();
        findOutputs.add(findOutput);
        findOutputs.add(findOutput);
        WalkLogControllerDTO.Response response = new WalkLogControllerDTO.Response();
        response.setWalkLogId(1L);
        response.setMessage("메세지");
        response.setMapImage("/test/image/test.jpg");
        response.setStartedAt(LocalDateTime.now());
        response.setEndAt(LocalDateTime.now());
        ArrayList<WalkLogContentServiceDTO.Output> outputs = new ArrayList<>();
        WalkLogContentServiceDTO.Output output = new WalkLogContentServiceDTO.Output(1L, LocalDateTime.now(), "메세지1", "/test/image/test.jpg");
        WalkLogContentServiceDTO.Output output2 = new WalkLogContentServiceDTO.Output(2L, LocalDateTime.now(), "메세지2", "/test/image/test.jpg");
        outputs.add(output);
        outputs.add(output2);
        response.setWalkLogContents(outputs);
        given(walkLogMapper.walkLogControllerGetRequestDTOtoWalkLogServiceFindInputDTO(Mockito.any(WalkLogControllerDTO.GetMemberRequest.class)))
                .willReturn(findInput);
        given(walkLogService.findMyWalkLogs(Mockito.any(WalkLogServiceDTO.FindInput.class)))
                .willReturn(new PageImpl<>(findOutputs));
        given(walkLogMapper.walkLogServiceFindOutputDTOtoWalkLogControllerResponseDTO(Mockito.any(WalkLogServiceDTO.FindOutput.class)))
                .willReturn(response);
        String urlTemplate = "/members/{member-id}/walk-logs";

        //when
        ResultActions perform = mockMvc.perform(
                get(urlTemplate,memberId)
                        .param("page",String.valueOf(getMemberRequest.getPage()))
                        .param("size",String.valueOf(getMemberRequest.getSize()))
                        .param("year",String.valueOf(getMemberRequest.getYear()))
                        .param("month",String.valueOf(getMemberRequest.getMonth()))
                        .param("day",String.valueOf(getMemberRequest.getDay()))
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate));
                //then
        perform.andExpect(status().isOk())
        .andExpect(jsonPath("$.data.size()").value(findOutputs.size()))
        .andExpect(jsonPath("$.data[0].walkLogId").value(response.getWalkLogId()))
        .andExpect(jsonPath("$.data[0].message").value(response.getMessage()))
                .andDo(document(
                        "get-my-walk-logs",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("한페이지의 객체 개수").optional(),
                                parameterWithName("year").description("년도").optional(),
                                parameterWithName("month").description("월").optional(),
                                parameterWithName("day").description("일").optional()
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("걷기기록 데이터를 리스트로 가지고 있습니다"),
                                fieldWithPath("data.[].walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("data.[].mapImage").type(JsonFieldType.STRING).description("지도 이미지 임시 URL"),
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

    private static WalkLogControllerDTO.GetMemberRequest createWalkLogControllerDTOgetRequests() {
        WalkLogControllerDTO.GetMemberRequest getMemberRequest = new WalkLogControllerDTO.GetMemberRequest();
        getMemberRequest.setSize(3);
        getMemberRequest.setPage(1);
        getMemberRequest.setYear(LocalDateTime.now().getYear());
        getMemberRequest.setMonth(LocalDateTime.now().getMonthValue());
        getMemberRequest.setDay(LocalDateTime.now().getDayOfMonth());
        return getMemberRequest;
    }
    private static WalkLogServiceDTO.FindInput createWalkLogServiceDTOfindsInput() {
        WalkLogServiceDTO.FindInput findInput = new WalkLogServiceDTO.FindInput();
        findInput.setSize(1);
        findInput.setPage(3);
        findInput.setYear(LocalDateTime.now().getYear());
        findInput.setMonth(LocalDateTime.now().getMonthValue());
        findInput.setDay(LocalDateTime.now().getDayOfMonth());
        return findInput;
    }

    @Test
    void getMyWalkLogsForCalendarTest() throws Exception {
        LocalDateTime date = LocalDateTime.of(2023,5,20,12,12);
        Long memberId = 1L;
        WalkLogControllerDTO.GetCalendarRequest request = new WalkLogControllerDTO.GetCalendarRequest();
        request.setYear(LocalDateTime.now().getYear());
        request.setMonth(LocalDateTime.now().getMonthValue());
        WalkLogControllerDTO.CalendarResponse calendarResponse = new WalkLogControllerDTO.CalendarResponse();
        calendarResponse.setWalkLogId(1L);
        calendarResponse.setCreatedAt(date);
        WalkLogControllerDTO.CalendarResponse calendarResponse2 = new WalkLogControllerDTO.CalendarResponse();
        calendarResponse2.setWalkLogId(2L);
        calendarResponse2.setCreatedAt(date);
        List<WalkLogControllerDTO.CalendarResponse> calendarResponses = new ArrayList<>();
        calendarResponses.add(calendarResponse);
        calendarResponses.add(calendarResponse2);
        given(walkLogMapper.walkLogControllerGetCalenderRequestDTOtoWalkLogServiceCalenderFindInputDTO(Mockito.any(WalkLogControllerDTO.GetCalendarRequest.class)))
                .willReturn(new WalkLogServiceDTO.CalenderFindInput());
        given(walkLogService.findMyMonthWalkLogs(Mockito.any(WalkLogServiceDTO.CalenderFindInput.class)))
                .willReturn(new ArrayList<>());
        given(walkLogMapper.WalkLogServiceCalenderFindOutputDTOsToWalkLogControllerCalendarResponseDTOs(Mockito.anyList()))
                .willReturn(calendarResponses);
        String urlTemplate = "/members/{member-id}/walk-logs/calendar";
        ResultActions perform = mockMvc.perform(
                get(urlTemplate,memberId)
                        .param("year",String.valueOf(request.getYear()))
                        .param("month",String.valueOf(request.getMonth()))
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(calendarResponses.size()))
                .andExpect(jsonPath("$[0].walkLogId").value(calendarResponse.getWalkLogId()))
//                .andExpect(jsonPath("$[0].createdAt").value(calendarResponse2.getCreatedAt()))
                .andDo(document(
                        "get-my-walk-logs-for-calendar",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestParameters(
                                parameterWithName("year").description("년도"),
                                parameterWithName("month").description("월")
                        ),
                        responseFields(
                                fieldWithPath("[].walkLogId").type(JsonFieldType.NUMBER).description("걷기 기록 식별자"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("걷기 기록이 생성된 시각")
                        )
                ));
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
