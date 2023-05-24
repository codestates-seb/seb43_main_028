package backend.section6mainproject.member.entity;

import backend.section6mainproject.audit.Auditable;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static backend.section6mainproject.walklog.entity.WalkLog.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Where(clause = "member_status <> 'MEMBER_QUIT'")
@SQLDelete(sql = "UPDATE member SET member_status = 'MEMBER_QUIT', nickname = CONCAT('del', member_id) WHERE member_id = ?") // 탈퇴된 회원의 경우 del + memberId 형식으로 닉네임을 변경해줌으로 기존 닉네임의 점유권을 상실시킨다.
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 50, nullable = false, updatable = false, unique = true)
    private String email;

    private String password;

    @Column(length = 16, unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private WalkLogPublicSetting defaultWalkLogPublicSetting = WalkLogPublicSetting.PRIVATE;

    @Column(length = 100) // 자기소개 길이 변경
    private String introduction;

    private String profileImage; //"directory/algorithm"등의 이미지이름 (+ 임시주소) == signBucket에서 처리
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
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
