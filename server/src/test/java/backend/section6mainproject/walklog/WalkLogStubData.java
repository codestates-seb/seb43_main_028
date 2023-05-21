package backend.section6mainproject.walklog;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class WalkLogStubData {

    private  Long memberId = 1L;
    private  Long walkLogId = 1L;
    private Long anotherWalkLogId = 2L;
    private  String message = "안녕하세요";
    private  String nickname = "테스트닉네임";
    private  String mapImage = "/test/image/test.jpg";
    private  int page = 1;
    private  int size = 10;
    private  WalkLog.WalkLogStatus walkLogStatus = WalkLog.WalkLogStatus.STOP;

    public Long getWalkLogId() {
        return walkLogId;
    }

    public Member getMember() {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("email@email.com");
        member.setPassword("1q2w3e4r!abc");
        member.setNickname(nickname);
        member.setIntroduction("자기소개테스트");
        return member;
    }
    public WalkLog getRecordingWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(walkLogId);
        walkLog.setMessage(message);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        return walkLog;
    }
    public WalkLog getStopWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(walkLogId);
        walkLog.setMessage(message);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }
    public WalkLog getAnotherWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(anotherWalkLogId);
        walkLog.setMessage(message);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }

    public WalkLogControllerDTO.PostResponse getResponse() {
        WalkLogControllerDTO.PostResponse response = new WalkLogControllerDTO.PostResponse();
        response.setWalkLogId(1L);
        return response;
    }
    public WalkLogControllerDTO.Patch getPatch(){
        WalkLogControllerDTO.Patch patchWalkLogDto = new WalkLogControllerDTO.Patch();
        patchWalkLogDto.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        patchWalkLogDto.setMessage(message);
        return patchWalkLogDto;
    }
    public WalkLogControllerDTO.DetailResponse getDetailResponse(){
        WalkLogControllerDTO.DetailResponse detailResponse = new WalkLogControllerDTO.DetailResponse();
        detailResponse.setWalkLogId(walkLogId);
        detailResponse.setMemberId(memberId);
        detailResponse.setNickname(nickname);
        detailResponse.setMapImage(mapImage);
        detailResponse.setCreatedAt(LocalDateTime.now());
        detailResponse.setEndAt(LocalDateTime.now());
        detailResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        detailResponse.setMessage(message);
        return detailResponse;
    }
    public WalkLogControllerDTO.GetFeedRequest getFeedRequest(){
        WalkLogControllerDTO.GetFeedRequest getFeedRequest = new WalkLogControllerDTO.GetFeedRequest();
        getFeedRequest.setPage(1);
        getFeedRequest.setSize(10);
        return getFeedRequest;
    }
    public WalkLogControllerDTO.GetFeedResponse getFeedResponse() throws IOException {
        WalkLogControllerDTO.GetFeedResponse getFeedResponse = new WalkLogControllerDTO.GetFeedResponse();
        getFeedResponse.setWalkLogId(1L);
        getFeedResponse.setMessage(message);
        getFeedResponse.setNickname(nickname);
        getFeedResponse.setMapImage(mapImage);
        getFeedResponse.setStartedAt(LocalDateTime.now());
        getFeedResponse.setEndAt(LocalDateTime.now());
        getFeedResponse.setProfileImage(mapImage);
        return getFeedResponse;
    }

    public WalkLogControllerDTO.EndPost getEndPost(){
        WalkLogControllerDTO.EndPost endPostDTO = new WalkLogControllerDTO.EndPost();
        endPostDTO.setMessage(message);
        endPostDTO.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        return endPostDTO;
    }
    public WalkLogServiceDTO.CreateInput getCreateInput(){
        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput();
        createInput.setMemberId(memberId);
        return createInput;
    }
    public WalkLogServiceDTO.CreateOutput getCreateOutput(){
        WalkLogServiceDTO.CreateOutput createOutput = new WalkLogServiceDTO.CreateOutput();
        createOutput.setWalkLogId(walkLogId);
        return createOutput;
    }
    public WalkLogServiceDTO.UpdateInput getUpdateInput(){
        WalkLogServiceDTO.UpdateInput updateInput = new WalkLogServiceDTO.UpdateInput();
        updateInput.setWalkLogId(walkLogId);
        updateInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        updateInput.setMessage(message);
        return updateInput;
    }
    public WalkLogServiceDTO.Output getOutput(){
        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setWalkLogId(walkLogId);
        output.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        output.setMessage(message);
        return output;
    }
    public WalkLogServiceDTO.GetOutput getGetOutput(){
        WalkLogServiceDTO.GetOutput getOutput = new WalkLogServiceDTO.GetOutput();
        getOutput.setWalkLogId(walkLogId);
        getOutput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        getOutput.setMessage(message);
        return getOutput;

    }
    public WalkLogControllerDTO.GetResponse getGetResponse(){
        WalkLogControllerDTO.GetResponse getResponse = new WalkLogControllerDTO.GetResponse();
        getResponse.setWalkLogId(walkLogId);
        getResponse.setMemberId(memberId);
        getResponse.setNickname(nickname);
        getResponse.setMapImage(mapImage);
        getResponse.setCreatedAt(LocalDateTime.now());
        getResponse.setEndAt(LocalDateTime.now());
        getResponse.setProfileImage(mapImage);
        getResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        getResponse.setMessage(message);
        return getResponse;    }
    public WalkLogServiceDTO.ExitInput getExitInput() throws IOException {
        WalkLogServiceDTO.ExitInput exitInput = new WalkLogServiceDTO.ExitInput();
        exitInput.setMessage(message);
        exitInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        exitInput.setWalkLogId(1L);
        exitInput.setMapImage(getImage());
        return exitInput;
    }
    public WalkLogServiceDTO.FindInput getFindInput() {
        WalkLogServiceDTO.FindInput findInput = new WalkLogServiceDTO.FindInput();
        findInput.setMemberId(memberId);
        findInput.setPage(page);
        findInput.setSize(size);
        findInput.setYear(LocalDateTime.now().getYear());
        findInput.setMonth(LocalDateTime.now().getMonthValue());
        findInput.setDay(LocalDateTime.now().getDayOfMonth());
        return findInput;
    }
    public WalkLogServiceDTO.FindOutput getFindOutput(){
        WalkLogServiceDTO.FindOutput findOutput = new WalkLogServiceDTO.FindOutput();
        findOutput.setWalkLogId(walkLogId);
        findOutput.setMessage(message);
        findOutput.setStartedAt(LocalDateTime.now());
        findOutput.setEndAt(LocalDateTime.now());
        return findOutput;
    }

    public WalkLogServiceDTO.FindFeedInput getFindFeedInput(){
        WalkLogServiceDTO.FindFeedInput findFeedInput = new WalkLogServiceDTO.FindFeedInput();
        findFeedInput.setPage(page);
        findFeedInput.setSize(size);
        return findFeedInput;
    }

    public WalkLogServiceDTO.FindFeedOutput getFindFeedOutput(){
        WalkLogServiceDTO.FindFeedOutput findFeedOutput = new WalkLogServiceDTO.FindFeedOutput();
        findFeedOutput.setWalkLogId(walkLogId);
        findFeedOutput.setMessage(message);
        return findFeedOutput;
    }

    public WalkLogServiceDTO.CalenderFindInput getCalenderFindInput(){
        WalkLogServiceDTO.CalenderFindInput calenderFindInput = new WalkLogServiceDTO.CalenderFindInput();
        calenderFindInput.setMemberId(memberId);
        calenderFindInput.setYear(LocalDateTime.now().getYear());
        calenderFindInput.setMonth(LocalDateTime.now().getMonthValue());
        return calenderFindInput;
    }
    public WalkLogServiceDTO.CalenderFindOutput getCalenderFindOutput(Long id){
        WalkLogServiceDTO.CalenderFindOutput calenderFindOutput = new WalkLogServiceDTO.CalenderFindOutput();
        calenderFindOutput.setWalkLogId(id);
        return calenderFindOutput;
    }

        public MockMultipartFile getImage() throws IOException {
        String fileName = "test";
        String contentType = "image/jpeg";
        String fileFullName = fileName + ".jpg";
        FileInputStream inputStream = new FileInputStream("src/test/resources/testImage/" + fileFullName);
        return new MockMultipartFile("mapImage", fileFullName, contentType, inputStream);
    }

    public Member getMemberForRepo() {
        Member member = new Member();
        member.setEmail("email@email.com");
        member.setPassword("1q2w3e4r!abc");
        member.setNickname(nickname);
        member.setRoles(List.of("USER"));
        member.setIntroduction("자기소개테스트");
        return member;
    }
    public WalkLog getWalkLogForRepo(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setMessage(message);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }
}
