package backend.section6mainproject.walklog.dto;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class WalkLogServiceDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateInput {
        private Long memberId;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateOutput {
        private Long walkLogId;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateInput {
        private Long walkLogId;
        private String message;
        private WalkLog.WalkLogPublicSetting walkLogPublicSetting;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ExitInput {
        private Long walkLogId;
        private String message;
        private WalkLog.WalkLogPublicSetting walkLogPublicSetting;
        private MultipartFile mapImage;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindsInput {
        private Long memberId;
        private Integer page;
        private Integer size;
        private Integer day;
        private Integer month;
        private Integer year;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CalenderFindsInput {
        private Long memberId;
        private int year;
        private int month;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CalenderFindsOutput {
        private Long walkLogId;
        private LocalDateTime createdAt;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindsOutput {
        private Long walkLogId;
        private String mapImage; // 구현예정
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private String message;
        private List<WalkLogContentServiceDTO.Output> walkLogContents;

    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class Output {
        private Long walkLogId;
        private LocalDateTime createdAt;
        private LocalDateTime endAt;
        private String imageUrl;

        private String message;
        private Long memberId;
        private String nickname;


        private WalkLog.WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateServiceDTO.Output> coordinates;
        private List<WalkLogContentServiceDTO.Output> walkLogContents;

    }

}
