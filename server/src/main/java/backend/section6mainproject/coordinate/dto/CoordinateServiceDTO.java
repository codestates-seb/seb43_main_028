package backend.section6mainproject.coordinate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CoordinateServiceDTO {
    @Getter
    @Setter
    public static class CreateParam {
        @Setter
        private Long walkLogId;
        @NotNull
        private Double lat;
        @NotNull
        private Double lng;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateReturn {
        private long coordinateId;
        private long walkLogId;
        private Double lat;
        private Double lng;
        private LocalDateTime createdAt;
    }
}
