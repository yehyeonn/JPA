package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass   // 이 클래스의 속성을 상속 받는 쪽에 포함시켜 줌
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 단, 부모쪽의 toString 은 기본적으로 안 보여줌 그래서
    //@ToString(callSuper = true)
    //@EqualsAndHashCode(callSuper = true) 해야함!!

}
