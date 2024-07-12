package com.lec.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class BookReviewInfo extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    // 1:1 연결하기
    private Long bookId;    // FK 역할

    // NULL 을 허용하면 wrapper 객체 사용
    // NULL 을 허용하지 않을거면 primitive 객체 사용 -> DDL 에 NOT NULL 부여됨
    // 이번예제에서 아래 두개값은 기본값 0 을 사용하기 위해 primitive 를 사용합니다.
    //   --> 굳이 null check 안해도 된다.

    private float averageReviewScore;
    private int reviewCount;


}
