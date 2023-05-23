package backend.section6mainproject.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class ProfileControllerDTO {
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long memberId;
        private String email;
        private String nickname;
        private String introduction;
        private String defaultWalkLogPublicSetting;
        private String imageUrl;
        private Long recordingWalkLogId;
        private long totalWalkLog;
        private long totalWalkLogContent;
        private LocalDateTime createdAt; // 회원가입일시 공개
    }
}
