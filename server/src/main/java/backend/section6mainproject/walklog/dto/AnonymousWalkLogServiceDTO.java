package backend.section6mainproject.walklog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AnonymousWalkLogServiceDTO {
    @Getter
    @AllArgsConstructor
    public static class CreateOutput {
        private String userId;
    }
}
