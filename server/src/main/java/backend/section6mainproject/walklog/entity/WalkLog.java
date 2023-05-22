package backend.section6mainproject.walklog.entity;

import backend.section6mainproject.audit.Auditable;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.coordinate.entity.Coordinate;
import backend.section6mainproject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WalkLog extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkLogId;
    @Column(nullable = false)
    private LocalDateTime endAt = LocalDateTime.now();
    @Column(length = 50)
    private String message;
    private String mapImage;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private WalkLogPublicSetting walkLogPublicSetting = WalkLogPublicSetting.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private WalkLogStatus walkLogStatus;
    @OneToMany(mappedBy = "walkLog", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Coordinate> coordinates = new ArrayList<>();
    @OneToMany(mappedBy = "walkLog", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WalkLogContent> walkLogContents = new ArrayList<>();

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
    }

    public void addWalkLogContent(WalkLogContent walkLogContent) {
        walkLogContents.add(walkLogContent);
    }

    public void setMember(Member member) {
        this.member = member;
        this.walkLogPublicSetting = member.getDefaultWalkLogPublicSetting();
    }

    public enum WalkLogPublicSetting {
        PUBLIC, PRIVATE
    }
    public enum WalkLogStatus{
        RECORDING,STOP
    }

}
