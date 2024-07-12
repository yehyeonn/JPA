package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BookReviewInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:1 연결하기
//    private Long bookId;    // FK 역할

    @OneToOne(optional = false)   // BookReviewInfo:Book 은 1:1  <= Book 은 절대 null 을 허용하지 않는다.
    private Book book;  // 자식 -> 부모 Entity 를 직접 참조

    // NULL 을 허용하면 wrapper 객체 사용
    // NULL 을 허용하지 않을거면 primitive 객체 사용 -> DDL 에 NOT NULL 부여됨
    // 이번예제에서 아래 두개값은 기본값 0 을 사용하기 위해 primitive 를 사용합니다.
    //   --> 굳이 null check 안해도 된다.
    private float averageReviewScore;
    private int reviewCount;


}
