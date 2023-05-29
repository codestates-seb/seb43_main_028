package backend.section6mainproject.member.service;

import backend.section6mainproject.auth.utils.CustomAuthorityUtils;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.MemberStubData;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.utils.CustomBeanUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.section6mainproject.walklog.entity.WalkLog;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private MemberMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomAuthorityUtils authorityUtils;
    @Mock
    private CustomBeanUtils<Member> beanUtils;

    private MemberStubData stubData;

    @BeforeEach
    void init() {
        stubData = new MemberStubData();
    }
    @Test
    void createMemberTest() throws Exception {
        //given
        MemberServiceDTO.CreateInput createInput = stubData.getCreateInput();
        MemberServiceDTO.CreateOutput createOutput = stubData.getCreateOutput();
        Member member = stubData.getMember();

        given(mapper.createInputToMember(Mockito.any(MemberServiceDTO.CreateInput.class))).willReturn(member);
        given(authorityUtils.getRoles()).willReturn(member.getRoles());
        given(memberRepository.save(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToCreateOutput(Mockito.any(Member.class))).willReturn(createOutput);

        //when
        MemberServiceDTO.CreateOutput result = memberService.createMember(createInput);

        //then
        MatcherAssert.assertThat(result, is(equalTo(createOutput)));

    }

    @Test
    void findMemberTest() throws Exception {
        Member member = stubData.getMember();
        MemberServiceDTO.Output output = stubData.getMemberOutput();

        given(memberRepository.findById(Mockito.anyLong())).willReturn(Optional.of(member));
        given(mapper.memberToOutput(Mockito.any(Member.class))).willReturn(output);

        MemberServiceDTO.Output actualMemberOutput = memberService.findMember(1L);

        assertEquals(output.getIntroduction(), actualMemberOutput.getIntroduction());

    }

    @Test
    void updateMemberTest() throws Exception {
        // Given
        MockMultipartFile profileImage = stubData.getImage();
        MemberServiceDTO.UpdateInput updateInput = stubData.getUpdateInput();
        updateInput.setProfileImage(profileImage);
        Member member = stubData.getUpdatedMember();

        when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        when(mapper.updateInputToMember(Mockito.any(MemberServiceDTO.UpdateInput.class))).thenReturn(member);
        when(storageService.store(Mockito.any(MultipartFile.class), Mockito.anyString(), Mockito.anyBoolean())).thenReturn("newProfileImage");
        when(beanUtils.copyNonNullProperties(Mockito.any(Member.class), Mockito.any(Member.class))).thenReturn(member);

        // When
        MemberServiceDTO.Output output = memberService.updateMember(updateInput);

        // Then
        assertEquals(updateInput.getNickname(), member.getNickname());
        assertEquals(updateInput.getIntroduction(), member.getIntroduction());
    }

    @Test
    void updateMemberPasswordTest() throws Exception {
        Long memberId = 1L;
        String newPassword = "newPassword";
        MemberServiceDTO.UpdatePwInput updatePwInput = new MemberServiceDTO.UpdatePwInput();
        updatePwInput.setMemberId(memberId);
        updatePwInput.setPassword(newPassword);

        Member priorMember = new Member();
        priorMember.setMemberId(memberId);
        priorMember.setPassword("oldPassword");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(priorMember));
        given(memberRepository.save(Mockito.any(Member.class))).willReturn(new Member());

        memberService.updateMemberPassword(updatePwInput);
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).save(priorMember);
    }


    @Test
    void deleteMemberTest() throws Exception {

        Member member = stubData.getMember();
        when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        memberService.deleteMember(member.getMemberId());

        verify(memberRepository, times(1)).delete(Mockito.any(Member.class));
    }

    @Test
    void findRecordingWalkLog() {
        // given
        Member member = stubData.getMemberWithWalkLogRecording();
        given(memberRepository.findById(Mockito.anyLong())).willReturn(Optional.of(member));

        // when
        Long walkLogId = memberService.findRecordingWalkLog(1L);

        // then
        Optional<WalkLog> result = member.getWalkLogs().stream().filter(walkLog -> walkLog.getWalkLogId() == walkLogId).findAny();
        Assertions.assertNotNull(result.get());
        MatcherAssert.assertThat(result.get().getWalkLogStatus(), is(WalkLog.WalkLogStatus.RECORDING));
    }
}