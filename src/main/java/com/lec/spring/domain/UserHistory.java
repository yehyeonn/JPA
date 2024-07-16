package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// User 의 히스토리 정보 저장 용도의 Entity
// '수정하기 전의 데이터' 가 아니라
// '수정될 내용' 을 History 에 담는 예제.
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
//@EntityListeners(value = AuditingEntityListener.class)
public class UserHistory extends BaseEntity /*implements Auditable*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // UserHistory 의 id

//    @Column(name = "user_id", insertable = false, updatable = false)
//    private Long userId;    // User 의 id  아래 @ManyToOne 해서 필요 X
    private String name;    // User 의 name
    private String email;   // User 의 email

    @ManyToOne
    @ToString.Exclude
    private User user;

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

}
