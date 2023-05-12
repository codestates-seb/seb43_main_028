package backend.section6mainproject.walklog.dto;




import backend.section6mainproject.content.dto.WalkLogContentDTO;
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
        private List<CoordinateDTO.Sub> coordinates;
        private List<WalkLogContentDTO.Response> walkLogContents;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SimpleResponse{
        private Long walkLogId; // 이름이 조금 변경되도 괜찮은지?
        private String mapImage; // 구현예정
        private LocalDateTime startedAt;
        private LocalDateTime endAt;
        private String message;
        private List<WalkLogContentDTO.Response> walkLogContents; //좌표 관련된 정보는 필요 없으신지? //Response내부에도 질문있음

    }
}
