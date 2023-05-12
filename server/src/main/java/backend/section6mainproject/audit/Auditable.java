package backend.section6mainproject.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter//달력 테스트가 끝난 후 삭제부탁드립니다.
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
//    @CreatedDate
    @Column(nullable = true, updatable = false) //달력테스트가 끝난 후 nullable = false로 변경해주세요
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
