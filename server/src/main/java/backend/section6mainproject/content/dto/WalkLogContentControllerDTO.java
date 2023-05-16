package backend.section6mainproject.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class WalkLogContentControllerDTO {
    @Getter
    @Setter
    public static class Post {
        private String text;
    }

    @Getter
    @Setter
    public static class Patch {
        private String text;
    }

    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private Long walkLogContentId;
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
