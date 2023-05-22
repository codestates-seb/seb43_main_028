package backend.section6mainproject.walklog;

import backend.section6mainproject.content.WalkLogContentStubData;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.walklog.dto.WalkLogControllerDTO;
import backend.section6mainproject.walklog.dto.WalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WalkLogStubData {

    private WalkLogContentStubData walkLogContentStubData;
    public static final String MEMBER_PASSWORD = "1q2w3e4r!abc";
    public static final String INTRODUCTION = "자기소개테스트";
    private  Long MEMBER_ID = 1L;
    private String MEMBER_EMAIL = "email@email.com";
    private  Long WALK_LOG_ID = 1L;
    private Long ANOTHER_WALK_LOG_ID = 2L;
    private  String MESSAGE = "안녕하세요";
    private  String NICKNAME = "테스트닉네임";
    private  String MAP_IMAGE = "/test/image/test.jpg";
    private String REGION = "03948";
    private  int PAGE = 1;
    private  int SIZE = 10;
    private LocalDateTime DATE = LocalDateTime.of(2023,5,22,12,12);
    private int YEAR = 2023;
    private int MONTH = 5;
    private int DAY = 22;

    public Long getWalkLogId() {
        return WALK_LOG_ID;
    }

    public Member getMember() {
        Member member = new Member();
        member.setMemberId(MEMBER_ID);
        member.setEmail(MEMBER_EMAIL);
        member.setPassword(MEMBER_PASSWORD);
        member.setNickname(NICKNAME);
        member.setIntroduction(INTRODUCTION);
        return member;
    }
    public WalkLog getRecordingWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(WALK_LOG_ID);
        walkLog.setMessage(MESSAGE);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.RECORDING);
        return walkLog;
    }
    public WalkLog getStopWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(WALK_LOG_ID);
        walkLog.setMessage(MESSAGE);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }
    public WalkLog getAnotherWalkLog(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setWalkLogId(ANOTHER_WALK_LOG_ID);
        walkLog.setMessage(MESSAGE);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }

    public WalkLogControllerDTO.PostResponse getPostResponse() {
        WalkLogControllerDTO.PostResponse response = new WalkLogControllerDTO.PostResponse();
        response.setWalkLogId(1L);
        return response;
    }
    public WalkLogControllerDTO.Patch getPatch(){
        WalkLogControllerDTO.Patch patchWalkLogDto = new WalkLogControllerDTO.Patch();
        patchWalkLogDto.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        patchWalkLogDto.setMessage(MESSAGE);
        return patchWalkLogDto;
    }
    public WalkLogControllerDTO.DetailResponse getDetailResponse(){
        WalkLogControllerDTO.DetailResponse detailResponse = new WalkLogControllerDTO.DetailResponse();
        detailResponse.setWalkLogId(WALK_LOG_ID);
        detailResponse.setMemberId(MEMBER_ID);
        detailResponse.setNickname(NICKNAME);
        detailResponse.setMapImage(MAP_IMAGE);
        detailResponse.setRegion(REGION);
        detailResponse.setCreatedAt(LocalDateTime.now());
        detailResponse.setEndAt(LocalDateTime.now());
        detailResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        detailResponse.setMessage(MESSAGE);
        return detailResponse;
    }
    public WalkLogControllerDTO.GetFeedRequest getFeedRequest(){
        WalkLogControllerDTO.GetFeedRequest getFeedRequest = new WalkLogControllerDTO.GetFeedRequest();
        getFeedRequest.setPage(PAGE);
        getFeedRequest.setSize(SIZE);
        return getFeedRequest;
    }
    public WalkLogControllerDTO.GetFeedResponse getFeedResponse() throws IOException {
        WalkLogControllerDTO.GetFeedResponse getFeedResponse = new WalkLogControllerDTO.GetFeedResponse();
        getFeedResponse.setWalkLogId(WALK_LOG_ID);
        getFeedResponse.setMessage(MESSAGE);
        getFeedResponse.setNickname(NICKNAME);
        getFeedResponse.setMapImage(MAP_IMAGE);
        getFeedResponse.setStartedAt(LocalDateTime.now());
        getFeedResponse.setEndAt(LocalDateTime.now());
        getFeedResponse.setProfileImage(MAP_IMAGE);
        return getFeedResponse;
    }

    public WalkLogControllerDTO.EndPost getEndPost(){
        WalkLogControllerDTO.EndPost endPostDTO = new WalkLogControllerDTO.EndPost();
        endPostDTO.setMessage(MESSAGE);
        endPostDTO.setRegion(REGION);
        endPostDTO.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        return endPostDTO;
    }
    public WalkLogServiceDTO.CreateInput getCreateInput(){
        WalkLogServiceDTO.CreateInput createInput = new WalkLogServiceDTO.CreateInput();
        createInput.setMemberId(MEMBER_ID);
        return createInput;
    }
    public WalkLogServiceDTO.CreateOutput getCreateOutput(){
        WalkLogServiceDTO.CreateOutput createOutput = new WalkLogServiceDTO.CreateOutput();
        createOutput.setWalkLogId(WALK_LOG_ID);
        return createOutput;
    }
    public WalkLogServiceDTO.UpdateInput getUpdateInput(){
        WalkLogServiceDTO.UpdateInput updateInput = new WalkLogServiceDTO.UpdateInput();
        updateInput.setWalkLogId(WALK_LOG_ID);
        updateInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        updateInput.setMessage(MESSAGE);
        return updateInput;
    }
    public WalkLogServiceDTO.Output getOutput(){
        WalkLogServiceDTO.Output output = new WalkLogServiceDTO.Output();
        output.setWalkLogId(WALK_LOG_ID);
        output.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        output.setMessage(MESSAGE);
        return output;
    }
    public WalkLogServiceDTO.GetOutput getGetOutput(){
        WalkLogServiceDTO.GetOutput getOutput = new WalkLogServiceDTO.GetOutput();
        getOutput.setWalkLogId(WALK_LOG_ID);
        getOutput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        getOutput.setMessage(MESSAGE);
        return getOutput;

    }
    public WalkLogControllerDTO.GetResponse getGetResponse(){
        WalkLogControllerDTO.GetResponse getResponse = new WalkLogControllerDTO.GetResponse();
        getResponse.setWalkLogId(WALK_LOG_ID);
        getResponse.setMemberId(MEMBER_ID);
        getResponse.setNickname(NICKNAME);
        getResponse.setMapImage(MAP_IMAGE);
        getResponse.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        getResponse.setCreatedAt(DATE);
        getResponse.setEndAt(DATE);
        getResponse.setProfileImage(MAP_IMAGE);
        getResponse.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PRIVATE);
        getResponse.setMessage(MESSAGE);
        return getResponse;
    }
    public WalkLogServiceDTO.ExitInput getExitInput() throws IOException {
        WalkLogServiceDTO.ExitInput exitInput = new WalkLogServiceDTO.ExitInput();
        exitInput.setMessage(MESSAGE);
        exitInput.setWalkLogPublicSetting(WalkLog.WalkLogPublicSetting.PUBLIC);
        exitInput.setWalkLogId(WALK_LOG_ID);
        exitInput.setMapImage(getImage());
        return exitInput;
    }
    public WalkLogServiceDTO.FindInput getFindInput() {
        WalkLogServiceDTO.FindInput findInput = new WalkLogServiceDTO.FindInput();
        findInput.setMemberId(MEMBER_ID);
        findInput.setPage(PAGE);
        findInput.setSize(SIZE);
        findInput.setYear(YEAR);
        findInput.setMonth(MONTH);
        findInput.setDay(DAY);
        return findInput;
    }
    public WalkLogServiceDTO.FindOutput getFindOutput(){
        walkLogContentStubData = new WalkLogContentStubData();
        WalkLogServiceDTO.FindOutput findOutput = new WalkLogServiceDTO.FindOutput();
        findOutput.setWalkLogId(WALK_LOG_ID);
        findOutput.setMessage(MESSAGE);
        findOutput.setStartedAt(DATE);
        findOutput.setEndAt(DATE);
        findOutput.setWalkLogContents(walkLogContentStubData.getOutputs());

        return findOutput;
    }

    public WalkLogServiceDTO.FindFeedInput getFindFeedInput(){
        WalkLogServiceDTO.FindFeedInput findFeedInput = new WalkLogServiceDTO.FindFeedInput();
        findFeedInput.setPage(PAGE);
        findFeedInput.setSize(SIZE);
        return findFeedInput;
    }

    public WalkLogServiceDTO.FindFeedOutput getFindFeedOutput(){
        WalkLogServiceDTO.FindFeedOutput findFeedOutput = new WalkLogServiceDTO.FindFeedOutput();
        findFeedOutput.setWalkLogId(WALK_LOG_ID);
        findFeedOutput.setMessage(MESSAGE);
        return findFeedOutput;
    }

    public WalkLogServiceDTO.CalenderFindInput getCalenderFindInput(){
        WalkLogServiceDTO.CalenderFindInput calenderFindInput = new WalkLogServiceDTO.CalenderFindInput();
        calenderFindInput.setMemberId(MEMBER_ID);
        calenderFindInput.setYear(YEAR);
        calenderFindInput.setMonth(MONTH);
        return calenderFindInput;
    }
    public WalkLogServiceDTO.CalenderFindOutput getCalenderFindOutput(Long id){
        WalkLogServiceDTO.CalenderFindOutput calenderFindOutput = new WalkLogServiceDTO.CalenderFindOutput();
        calenderFindOutput.setWalkLogId(id);
        return calenderFindOutput;
    }
    public WalkLogControllerDTO.GetMemberRequest getGetMemberRequest(){
        WalkLogControllerDTO.GetMemberRequest getMemberRequest = new WalkLogControllerDTO.GetMemberRequest();
        getMemberRequest.setPage(PAGE);
        getMemberRequest.setSize(SIZE);
        getMemberRequest.setYear(YEAR);
        getMemberRequest.setMonth(MONTH);
        getMemberRequest.setDay(DAY);
        return getMemberRequest;
    }
    public WalkLogControllerDTO.Response getResponse(){
        walkLogContentStubData = new WalkLogContentStubData();
        WalkLogControllerDTO.Response response = new WalkLogControllerDTO.Response();
        response.setWalkLogId(WALK_LOG_ID);
        response.setMessage(MESSAGE);
        response.setMapImage(MAP_IMAGE);
        response.setStartedAt(DATE);
        response.setEndAt(DATE);
        response.setWalkLogContents(walkLogContentStubData.getOutputs());
        return response;
    }
   public ArrayList<WalkLogServiceDTO.FindOutput> getFindOutputs(){
       ArrayList<WalkLogServiceDTO.FindOutput> findOutputs = new ArrayList<>();
        WalkLogServiceDTO.FindOutput findOutput1 = getFindOutput();
        WalkLogServiceDTO.FindOutput findOutput2 = getFindOutput();
        findOutput2.setWalkLogId(ANOTHER_WALK_LOG_ID);
        findOutputs.add(findOutput1);
        findOutputs.add(findOutput2);
        return findOutputs;
    }

    public WalkLogControllerDTO.GetCalendarRequest getGetCalendarRequest(){
        WalkLogControllerDTO.GetCalendarRequest request = new WalkLogControllerDTO.GetCalendarRequest();
        request.setYear(YEAR);
        request.setMonth(MONTH);
        return request;
    }

    public WalkLogControllerDTO.CalendarResponse getCalendarResponse(){
        WalkLogControllerDTO.CalendarResponse calendarResponse = new WalkLogControllerDTO.CalendarResponse();
        calendarResponse.setWalkLogId(WALK_LOG_ID);
        calendarResponse.setCreatedAt(DATE);
        return calendarResponse;
    }
    public List<WalkLogControllerDTO.CalendarResponse> getCalendarResponses(){
        List<WalkLogControllerDTO.CalendarResponse> calendarResponses = new ArrayList<>();
        WalkLogControllerDTO.CalendarResponse calendarResponse1 = getCalendarResponse();
        WalkLogControllerDTO.CalendarResponse calendarResponse2 = getCalendarResponse();
        calendarResponse2.setWalkLogId(ANOTHER_WALK_LOG_ID);
        calendarResponses.add(calendarResponse1);
        calendarResponses.add(calendarResponse2);
        return calendarResponses;
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
        member.setNickname(NICKNAME);
        member.setRoles(List.of("USER"));
        member.setIntroduction("자기소개테스트");
        return member;
    }
    public WalkLog getWalkLogForRepo(Member member){
        WalkLog walkLog = new WalkLog();
        walkLog.setMember(member);
        walkLog.setMessage(MESSAGE);
        walkLog.setWalkLogStatus(WalkLog.WalkLogStatus.STOP);
        return walkLog;
    }
}
