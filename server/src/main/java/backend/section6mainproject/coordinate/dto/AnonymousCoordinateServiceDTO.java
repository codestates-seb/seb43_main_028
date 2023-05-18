package backend.section6mainproject.coordinate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AnonymousCoordinateServiceDTO {
    @Getter
    @Setter
    public static class Input {
        @Setter
        private String userId;
        @NotNull
        private Double lat;
        @NotNull
        private Double lng;
        private boolean userIdSaved;
    }

    @Getter
    @AllArgsConstructor
    public static class Output {
        private long coordinateId;
        private String userId;
        private Double lat;
        private Double lng;
        private LocalDateTime createdAt;
    }
}
