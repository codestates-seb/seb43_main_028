package backend.section6mainproject.auth;

import backend.section6mainproject.auth.dto.LoginDTO;
import backend.section6mainproject.member.MemberStubData;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MemberRepository memberRepository;
    private StubData stubData;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        stubData = new StubData();
        objectMapper = new ObjectMapper();
    }
    @Test
    void login() throws Exception {
        // given
        Member member = stubData.getMember();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));

        LoginDTO loginDTO = stubData.getLoginDTO();
        String content = objectMapper.writeValueAsString(loginDTO);

        // when
        ResultActions actions = mockMvc.perform(
                post("/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("Refresh"));
    }

    public static class StubData {
        private Long memberId = 1L;
        private String email = "test01@gmail.com";
        private String plainPassword = "testdot01!";
        private String nickname = "거터";
        private String updatedNickname = "나무";
        private Member.MemberStatus memberStatus = Member.MemberStatus.MEMBER_ACTIVE;
        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting = WalkLog.WalkLogPublicSetting.PRIVATE;
        public LoginDTO getLoginDTO() {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setEmail(email);
            loginDTO.setPassword(plainPassword);
            loginDTO.setAutoLogin(true);
            return loginDTO;
        }
        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            member.setEmail(email);
            member.setNickname(nickname);
            member.setMemberStatus(memberStatus);
            member.setDefaultWalkLogPublicSetting(defaultWalkLogPublicSetting);
            member.setRoles(List.of("USER"));
            member.setPassword(plainPassword);
            return member;
        }
    }

}
