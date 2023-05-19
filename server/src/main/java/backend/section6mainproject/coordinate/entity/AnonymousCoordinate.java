package backend.section6mainproject.coordinate.entity;

import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AnonymousCoordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coordinateId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private Double lat;
    @Column(nullable = false)
    private Double lng;

    @ManyToOne
    @JoinColumn(name = "WALK_LOG_ID")
    private AnonymousWalkLog walkLog;
}
