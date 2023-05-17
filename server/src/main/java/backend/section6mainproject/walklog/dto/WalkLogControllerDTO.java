package backend.section6mainproject.walklog.dto;

import backend.section6mainproject.content.dto.WalkLogContentControllerDTO;
import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.coordinate.dto.CoordinateControllerDTO;

import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

public class WalkLogControllerDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        private Long memberId;
    }

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
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class EndPost {
        private String message;
        private WalkLogPublicSetting walkLogPublicSetting;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRequests {
        private int page;
        private Integer size = 10;
        private Integer day;
        private Integer month;
        private Integer year;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetCalendarRequests {
        private Integer year;
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
    public static class DetailResponse {

        private Long walkLogId;
        private LocalDateTime createdAt;
        private LocalDateTime endAt;

        private String message;
        private Long memberId;
        private String nickname;


        private WalkLog.WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateControllerDTO.Sub> coordinates;
        private List<WalkLogContentControllerDTO.Response> walkLogContents;

    }
}
