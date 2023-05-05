package backend.section6mainproject.walklog.dto;


import lombok.Getter;
import lombok.Setter;

public class WalkLogDto {
    @Getter
    @Setter
    public static class Post {
        private Long memberId;
    }

}
