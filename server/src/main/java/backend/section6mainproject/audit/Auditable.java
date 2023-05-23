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
@Setter //데이터 테스트가 끝나고 수정하겠습니다
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
//    @CreatedDate(데이터 테스트가 끝나고 수정하겠습니다)
    @Column //(nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 데이터 테스트가 끝나고 수정하겠습니다.

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
