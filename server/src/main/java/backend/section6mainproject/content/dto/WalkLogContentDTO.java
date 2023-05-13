package backend.section6mainproject.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class WalkLogContentDTO {
    @Getter
    public static class Post {
        @Setter
        private Long walkLogId;
        private String text;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long walkLogContentId;
        private LocalDateTime createdAt;
        private String text;
        private String imageUrl;
    }
}
