package backend.section6mainproject.walklog.dto;




import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

public class WalkLogDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        private Long memberId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Created {
        private Long walkLogId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class EndPost {
        private Long walkLogId;
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Patch{
        private Long walkLogId;
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailResponse {

        private Long walkLogId;
        private LocalDateTime createdAt;
        private LocalDateTime endAt;

        private String message;
        private Long memberId;
        private String nickname;


        private WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateControllerDTO.Sub> coordinates;
        private List<WalkLogContentControllerDTO.Response> walkLogContents;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SimpleResponse{
        private Long walkLogId;
        private String mapImage; // 구현예정
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private String message;
        private List<WalkLogContentControllerDTO.Response> walkLogContents;

    }
}
