package backend.section6mainproject.member.entity;

import backend.section6mainproject.audit.Auditable;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 50, nullable = false, updatable = false, unique = true)
    private String email;

    private String password;

    @Column(length = 50, unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private WalkLogPublicSetting defaultWalkLogPublicSetting = WalkLogPublicSetting.PRIVATE;

    @Column(length = 500)
    private String introduction;

    @OneToMany(mappedBy = "member")
    private List<WalkLog> walkLogs = new ArrayList<>();

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_QUIT("탈퇴 상태");
        @Getter
        private String status;
        MemberStatus(String status) {
            this.status = status;
        }
    }
}
