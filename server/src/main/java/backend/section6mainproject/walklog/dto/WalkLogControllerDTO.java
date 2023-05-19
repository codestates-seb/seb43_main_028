package backend.section6mainproject.walklog.dto;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;

import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

public class WalkLogControllerDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostResponse{
        private Long walkLogId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Patch{
        @Size(max = 100) //한줄메시지기 때문에 길지않아도 된다고 판단했습니다.
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class EndPost {
        @Size(max = 100)
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMemberRequest {
        @NotNull
        @Positive
        private int page;
        private Integer size = 10;
        private Integer day;
        private Integer month;
        private Integer year;


    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetFeedRequest {
        @NotNull
        @Positive
        private int page;
        private Integer size = 10;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetFeedResponse {
        private Long walkLogId;
        private String mapImage;
        private String nickname;
        private String profileImage;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private String message;
        private List<WalkLogContentServiceDTO.Output> walkLogContents;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetCalendarRequest {
        @NotNull
//        @Min(value = 2023)
        private Integer year;
        @NotNull
        @Range(min = 1, max = 12)
        private Integer month;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CalendarResponse {
        private Long walkLogId;
        private LocalDateTime createdAt;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private Long walkLogId;
        private String mapImage;
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private String message;
        private List<WalkLogContentServiceDTO.Output> walkLogContents;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DetailResponse {

        private Long walkLogId;
        private LocalDateTime createdAt;
        private LocalDateTime endAt;
        private String mapImage;
        @Size(max = 100)
        private String message;
        private Long memberId;
        private String nickname;


        private WalkLog.WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateControllerDTO.Sub> coordinates;
        private List<WalkLogContentControllerDTO.Response> walkLogContents;

    }
}
