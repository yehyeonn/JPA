package com.lec.spring.domain;

import com.lec.spring.listener.Auditable;
import com.lec.spring.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor    // NonNull 가진 생성자 새로 생성(alt + 7 로 확인)
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity    // user 라는 테이블을 못 만들기 때문에(에러는 아님.. 그냥 투덜) 테이블 이름을 지정할 수 있음
@Table(
        name = "T_USER"     // DB 테이블명
        , indexes = {@Index(columnList = "name")}   // 컬럼에 대한 index 생성
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "name"})}) // unique 제약사항, 이 unique는 두 조합이 unique 여야한다는 뜻(복합키)
// Entity 는 DDL 을 통해 테이블을 생성한다는 뜻
@EntityListeners(value = {UserEntityListener.class})    // User Entity 의 EventListener 를 MyEntityListener 로 지정한다는 뜻, 여러개 지정할 땐 꼭 {} 사용!
                        // AuditingEntityListener : 스프링에서 제공하는 기본 Entity! 작성날짜와 수정 날짜 바꿔줌!! 단 어떤 게 뭔지 지정해줘야함
public class User extends BaseEntity/* implements Auditable */{    // 상속. 이미 getter, setter 있기 때문에 별도 생성 필요 X
                    // AuditingEntityListener 지우고 상속        implements Auditable 도 BaseEntitiy 가 처리해서 필요 X

    @Id // Entity 를 통해 테이블화를 하기 때문에 pk 지정이 필요 필수!!!!
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 마치 MySQL 의 AUTO_INCREMENT 와 같은 동작을 수행
    private Long id;
// @Entity와 @Id 는 꼭!!! 필요함

    @NonNull
    private String name;
    @NonNull
    @Column(unique = true)
    private String email;

//    @Column(
//            name = "crtdat",
//            nullable = false)   // = NotNull
//    @Column(updatable = false)  // update 동작 시 해당 컬럼 생략
//    @CreatedDate    // AuditingEntityListener 가 Listener 로 적용시 사용
//    private LocalDateTime createdAt;        // created_at 으로 컬럼 저장됨
//
////    @Column(insertable = false)     // insert 동작 시 해당 컬럼 생략
//    @LastModifiedDate   // AuditingEntityListener 가 Listener 로 적용시 사용
//    private LocalDateTime updatedAt;
    // createdAt, updatedAt 모두 필요 없음(BaseEntity 상속 받았기 때문에 이미 존재함)

    // User:Address => 1:N
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Address> address; Entity 로 선언 된 것은 릴레이션 잡아야함!!

    @Transient  // jakarta.persistence          // DB 에 반영 안 하는 필드 속성.(영속성 부여 X)
    private String testData;        // test_data

    @Enumerated(value = EnumType.STRING)    // 0, 1 로 저장 되지 않고 String 자체로 저장하게 설정
    private Gender gender;

    // User: UserHistory = 1:N
    @OneToMany(fetch = FetchType.EAGER) // 부모를 읽을 때 자식 Entity도 같이 읽어오게!
    @JoinColumn(name = "user_id"     // Entity 가 어떤 컬럼으로 join 하게 될지 지정해준다.
                // name = "user_id"     : join 할 컬럼명 지정가능!
                //  UserHistory 의 user_id 란 컬럼으로 join       근데 user_id는 이미 존재한다... =>   그래서 UserHistory 의 userId에 @Column(name = "user_id") 생성해 1:N 완성!
                , insertable = false, updatable = false
                    // User 에서 userHistory 값을 추가, 수정하지 못하도록 한다.
    )
    private List<UserHistory> userHistories = new ArrayList<>();    // NPE 방지! 없으면 새로운 배열


    // User:Review = 1:N
    @OneToMany
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

//    @PrePersist // user entity 가 insert 되기 전에 호출됨!
//    public void prePersist() {
//        System.out.println(">> prePersist");
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate  // update 되기 전
//    public void preUpdate() {
//        System.out.println(">>> preUpdate");
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreRemove  // delete 되기 전
//    public void preRemove() {
//        System.out.println(">>> preRemove");
//    }
//
//    @PostPersist    // insert 직후
//    public void postPersist() {
//        System.out.println(">>> postPersist");
//    }
//
//    @PostUpdate // update 직후
//    public void postUpdate() {
//        System.out.println(">>> postUpdate");
//    }
//
//    @PostRemove // delete 직후
//    public void postRemove() {
//        System.out.println(">>> postRemove");
//    }
//
//    @PostLoad   // select 직후
//    public void postLoad() {
//        System.out.println(">>> postLoad");
//    }
        // MyEntityListener 에서 사용할 거양
}
