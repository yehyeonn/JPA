package com.lec.spring.domain.service;

import com.lec.spring.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Test
    void converterErrorTest1(){
        bookService.getAll();
        System.out.println("🧸".repeat(30));
        bookRepository.findAll().forEach(System.out::println);  // converter 가 없다면 프로그램에서 다른 걸로 인식해 update 발생한다.
    }
}