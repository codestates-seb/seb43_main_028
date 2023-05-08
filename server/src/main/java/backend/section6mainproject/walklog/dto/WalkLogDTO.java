package backend.section6mainproject.walklog.dto;



import backend.section6mainproject.coordinate.dto.CoordinateDTO;
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
        private Long memberId;
        private String nickname;


        private WalkLogPublicSetting walkLogPublicSetting;
        private List<CoordinateDTO.Sub> coordinates;
        private List<ContentResponse> walkLogContents;
    }
//패스워드와 나머지 객체들은 반환할 필요가 없어서 제거하고 Dto생성



    @Getter
    @Setter
    @NoArgsConstructor
    public static class ContentResponse {
        //순환참조 방지를 위해 Dto를 따로 만들었습니다.

        private Long walkLogContentId;

        private String text;
    }
}
