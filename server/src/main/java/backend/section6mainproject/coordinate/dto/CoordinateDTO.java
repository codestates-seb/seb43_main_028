package backend.section6mainproject.coordinate.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CoordinateDTO {
    @Getter
    public static class Pub {
        private Long walkLogId;
        @NotNull
        private Double lat;
        @NotNull
        private Double lng;
    }

    @Getter
    @AllArgsConstructor
    public static class Sub {
        private long coordinateId;
        private Double lat;
        private Double lng;
        private LocalDateTime createdAt;
    }
}
