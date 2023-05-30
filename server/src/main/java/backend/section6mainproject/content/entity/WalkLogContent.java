package backend.section6mainproject.content.entity;

import backend.section6mainproject.audit.Auditable;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WalkLogContent extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkLogContentId;
    @Column(length = 1000)
    private String text;
    private String imageKey;
    @ManyToOne
    @JoinColumn(name = "WALK_LOG_ID")
    private WalkLog walkLog;
}
