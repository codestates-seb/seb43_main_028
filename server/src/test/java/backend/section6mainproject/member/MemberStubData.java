package backend.section6mainproject.member;

import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberStubData {
    private MemberRepository memberRepository;
    private Long memberId = 1L;
    private String email = "test@gmail.com";
    private String plainPassword = "testdot01!";
    private String nickname = "거터";
    private Member.MemberStatus memberStatus = Member.MemberStatus.MEMBER_ACTIVE;
    private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting = WalkLog.WalkLogPublicSetting.PRIVATE;
    private String introduction = "안녕하세요";
    private String profileImage = null;
    private List<String> roles = new ArrayList<>();
    private List<WalkLog> walkLogs = new ArrayList<>();

    public Member getMember() {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail(email);
        member.setNickname(nickname);
        member.setMemberStatus(memberStatus);
        member.setDefaultWalkLogPublicSetting(defaultWalkLogPublicSetting);
        member.setIntroduction(introduction);
        member.setWalkLogs(walkLogs);
        member.setRoles(roles);
        member.setPassword(plainPassword);
        return member;
    }

    public MemberServiceDTO.CreateInput getCreateInput() throws Exception {
        MemberServiceDTO.CreateInput createInput = new MemberServiceDTO.CreateInput();
        createInput.setEmail(email);
        createInput.setPassword(plainPassword);
        createInput.setNickname(nickname);
        return createInput;
    }

    public MemberServiceDTO.CreateOutput getCreateOutput() throws Exception {
        MemberServiceDTO.CreateOutput createOutput = new MemberServiceDTO.CreateOutput();
        createOutput.setMemberId(memberId);
        return createOutput;
    }
    public MemberServiceDTO.Output getMemberOutput() throws Exception {
        MemberServiceDTO.Output output = new MemberServiceDTO.Output(memberId, email, nickname, introduction, defaultWalkLogPublicSetting.toString(), null, 0, 0, LocalDateTime.now());
        return output;
    }
    public MemberServiceDTO.UpdateInput getUpdateInput() throws Exception {
        MemberServiceDTO.UpdateInput input = new MemberServiceDTO.UpdateInput();
        input.setIntroduction("물레물레물레");
        input.setNickname("나무");
        input.setProfileImage(getImage());
        return input;
    }
    public MockMultipartFile getImage() throws IOException {
        String fileName = "블랙홀";
        String contentType = "Jpeg";
        String fileFullName = fileName + "." + contentType;
        FileInputStream inputStream = new FileInputStream("src/test/resources/imageSource/" + fileFullName);
        return new MockMultipartFile("profileImage", fileFullName, contentType, inputStream);
    }
}
