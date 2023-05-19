package backend.section6mainproject.walklog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AnonymousWalkLogControllerDTO {
    @Getter
    @AllArgsConstructor
    public static class PostResponse{
        private String userId;
    }
}
