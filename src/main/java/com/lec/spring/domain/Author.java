package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    // Author:Book = N:M
//    @ManyToMany
//    @ToString.Exclude
//    private List<Book> books = new ArrayList<>();
//
//    public void addBook(Book... books){ // books 는 배열!
//        Collections.addAll(this.books, books);
//    }

    //  Author:Writing = 1:M
    @OneToMany
    @JoinColumn(name = "author_id")
    @ToString.Exclude // 순환참조 방지
    private List<Writing> writings = new ArrayList<>();

    public void addWritings(Writing... writing) {
        Collections.addAll(this.writings, writing);
    }



}
