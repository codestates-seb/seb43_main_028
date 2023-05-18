package backend.section6mainproject.walklog.entity;

import backend.section6mainproject.audit.Auditable;
import backend.section6mainproject.content.entity.AnonymousWalkLogContent;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.coordinate.entity.AnonymousCoordinate;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AnonymousWalkLog extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkLogId;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private LocalDateTime endAt = LocalDateTime.now();
    @Column(length = 50)
    private String message;
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private WalkLog.WalkLogStatus walkLogStatus = WalkLog.WalkLogStatus.RECORDING;
    @OneToMany(mappedBy = "WalkLog", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AnonymousCoordinate> anonymousCoordinates = new ArrayList<>();
    @OneToMany(mappedBy = "walkLog", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AnonymousWalkLogContent> anonymousWalkLogContents = new ArrayList<>();



}
