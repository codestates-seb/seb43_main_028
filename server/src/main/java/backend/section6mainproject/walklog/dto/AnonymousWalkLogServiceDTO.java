package backend.section6mainproject.walklog.dto;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import backend.section6mainproject.coordinate.dto.CoordinateServiceDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class AnonymousWalkLogServiceDTO {
    @Getter
    @AllArgsConstructor
    public static class CreateOutput {
        private String userId;
    }

    @Getter
    @AllArgsConstructor
    public static class Output {
        private String userId;
        private LocalDateTime createdAt;
        private List<AnonymousCoordinateServiceDTO.Output> coordinates;
    }
}
