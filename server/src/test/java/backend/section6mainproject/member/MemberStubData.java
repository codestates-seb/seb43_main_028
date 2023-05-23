package backend.section6mainproject.member;

import backend.section6mainproject.member.dto.MemberControllerDTO;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberStubData {
    private Long memberId = 1L;
    private String email = "test01@gmail.com";
    private String plainPassword = "testdot01!";
    private String nickname = "거터";
    private String updatedNickname = "나무";
    private Member.MemberStatus memberStatus = Member.MemberStatus.MEMBER_ACTIVE;
    private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting = WalkLog.WalkLogPublicSetting.PRIVATE;
    private String introduction = "안녕하세요";
    private String updatedIntroduction = "물레물레물레";
    private String profileImage = "/test/image/test.jpg";
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
        member.setProfileImage(profileImage);
        return member;
    }

    public Member getUpdatedMember() {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail(email);
        member.setNickname(updatedNickname);
        member.setMemberStatus(memberStatus);
        member.setDefaultWalkLogPublicSetting(defaultWalkLogPublicSetting);
        member.setIntroduction(updatedIntroduction);
        member.setWalkLogs(walkLogs);
        member.setRoles(roles);
        member.setPassword(plainPassword);
        member.setProfileImage(profileImage);
        return member;
    }
    public MemberControllerDTO.Post getPost(){
        MemberControllerDTO.Post post = new MemberControllerDTO.Post();
        post.setEmail(email);
        post.setPassword(plainPassword);
        post.setNickname(nickname);
        return post;
    }
    public MemberControllerDTO.GetNewPw getGetNewPw() {
        MemberControllerDTO.GetNewPw getNewPw = new MemberControllerDTO.GetNewPw();
        getNewPw.setEmail(email);
        return getNewPw;
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
        MemberServiceDTO.Output output = new MemberServiceDTO.Output(memberId,
                email, nickname, introduction, defaultWalkLogPublicSetting.toString(),
                null, 1L,0, 0, LocalDateTime.now());
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
        String fileName = "test";
        String contentType = "image/jpeg";
        String fileFullName = fileName + ".jpg";
        FileInputStream inputStream = new FileInputStream("src/test/resources/testImage/" + fileFullName);
        return new MockMultipartFile("profileImage", fileFullName, contentType, inputStream);
    }

    public Member getMemberWithWalkLogRecording() {
        Member member = new Member();
        member.setMemberId(memberId);
        WalkLog walkLog1 = new WalkLog();
        walkLog1.setWalkLogId(1L);
        walkLog1.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        WalkLog walkLog2 = new WalkLog();
        walkLog2.setWalkLogId(2L);
        walkLog2.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        member.getWalkLogs().add(walkLog1);
        member.getWalkLogs().add(walkLog2);
        return member;
    }
}
