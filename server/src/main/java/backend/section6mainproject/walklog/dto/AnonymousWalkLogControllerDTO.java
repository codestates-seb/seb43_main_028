package backend.section6mainproject.walklog.dto;

import backend.section6mainproject.coordinate.dto.AnonymousCoordinateControllerDTO;
import backend.section6mainproject.coordinate.dto.AnonymousCoordinateServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class AnonymousWalkLogControllerDTO {
    @Getter
    @AllArgsConstructor
    public static class PostResponse{
        private String userId;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private LocalDateTime createdAt;
        private List<AnonymousCoordinateControllerDTO.Sub> coordinates;
    }
}
