package backend.section6mainproject.content;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class WalkLogContentStubData {
    private String text = "걷기 중 순간기록 중입니다.";
    private Long walkLogContentId = 1L;
    private Long walkLogId = 1L;
    private String imageUrl = "/test/image/test.jpg";

    public Long getWalkLogId() {
        return walkLogId;
    }

    public Long getWalkLogContentId() {
        return walkLogContentId;
    }

    public WalkLogContentControllerDTO.Post getPost() {
        WalkLogContentControllerDTO.Post post = new WalkLogContentControllerDTO.Post();
        post.setText(text);
        return post;
    }

    public WalkLogContentControllerDTO.Patch getPatch() {
        WalkLogContentControllerDTO.Patch patch = new WalkLogContentControllerDTO.Patch();
        patch.setText(text);
        return patch;
    }

    public WalkLogContentControllerDTO.PostResponse getPostResponse() {
        return new WalkLogContentControllerDTO.PostResponse(walkLogContentId);
    }

    public WalkLogContentControllerDTO.Response getResponse() {
        return new WalkLogContentControllerDTO.Response(walkLogContentId, LocalDateTime.now(), text, imageUrl);
    }

    public WalkLogContentServiceDTO.CreateInput getCreateInput() throws IOException {
        WalkLogContentServiceDTO.CreateInput createInput = new WalkLogContentServiceDTO.CreateInput();
        createInput.setWalkLogId(walkLogId);
        createInput.setText(text);
        createInput.setContentImage(getImage());
        return createInput;
    }
    public WalkLogContentServiceDTO.CreateOutput getCreateOutput() {
        return new WalkLogContentServiceDTO.CreateOutput(walkLogContentId);
    }

    public WalkLogContentServiceDTO.Output getOutput() {
        return new WalkLogContentServiceDTO.Output(walkLogContentId, LocalDateTime.now(), text, imageUrl);
    }

    public WalkLogContent getWalkLogContent() {
        WalkLogContent walkLogContent = new WalkLogContent();
        WalkLog walkLog = new WalkLog();
        walkLog.setWalkLogId(walkLogId);
        walkLogContent.setWalkLog(walkLog);
        return walkLogContent;
    }

    public MockMultipartFile getImage() throws IOException {
        String fileName = "test";
        String contentType = "jpg";
        String fileFullName = fileName + "." + contentType;
        FileInputStream inputStream = new FileInputStream("src/test/resources/testImage/" + fileFullName);
        return new MockMultipartFile("contentImage", fileFullName, contentType, inputStream);
    }
}
