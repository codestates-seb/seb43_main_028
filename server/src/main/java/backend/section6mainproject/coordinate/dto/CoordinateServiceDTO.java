package backend.section6mainproject.coordinate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CoordinateServiceDTO {
    @Getter
    @Setter
    public static class Input {

        private Long walkLogId;
        private Double lat;
        private Double lng;
    }

    @Getter
    @AllArgsConstructor
    public static class Output {
        private long coordinateId;
        private long walkLogId;
        private Double lat;
        private Double lng;
        private LocalDateTime createdAt;
    }
}
