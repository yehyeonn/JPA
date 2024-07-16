package com.lec.spring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable     // Entity 에서 Embed 할 수 있는 클래스임을 선언(Embed : 내포하다) --> Entity 안에 선언 가능해짐
public class Address {
//    @Id
//    private Long id;
    private String city;
    private String district;
    @Column(name = "address_detail")
    private String detail;
    private String zipCode;


}
