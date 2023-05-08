package backend.section6mainproject.walklog.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

public class WalkLogDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        private Long memberId;
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
    public static class Response{

        private Long walkLogId;
        private LocalDateTime endTime;

        private String message;
        private WalkLogDto.MemberResponse member;

        private WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateResponse> coordinates;
        private List<ContentResponse> walkLogContents;
    }
//패스워드와 나머지 객체들은 반환할 필요가 없어서 제거하고 Dto생성

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MemberResponse {
        private Long memberId;
        private String nickname;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CoordinateResponse {

        //순환참조 방지를 위해 Dto를 따로 생성했습니다.
        private Long coordinateId;
        private LocalDateTime createdAt;
        private Double lat;
        private Double lng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ContentResponse {
        //순환참조 방지를 위해 Dto를 따로 만들었습니다.

        private Long walkLogContentId;

        private String text;
    }
}
