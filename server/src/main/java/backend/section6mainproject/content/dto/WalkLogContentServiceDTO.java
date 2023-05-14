package backend.section6mainproject.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class WalkLogContentServiceDTO {
    @Getter
    @Setter
    public static class Input {
        private Long walkLogId;
        private String text;
        private MultipartFile contentImage;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateOutput {
        private Long walkLogContentId;
    }
    @Getter
    @AllArgsConstructor
    public static class Output {
        private Long walkLogContentId;
        private LocalDateTime createdAt;
        private String text;
        private String imageUrl;

    }
}
