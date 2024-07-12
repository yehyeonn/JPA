package com.lec.spring.repository;

import com.lec.spring.domain.Author;
import com.lec.spring.domain.Book;
import com.lec.spring.domain.Writing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WritingRepository writingRepository;

    @Test
    @Transactional
    void manyToManyTest() {
        System.out.println("\n-- TEST#manyToManyTest() ---------------------------------------------");

        Book book1 = givenBook("고양이책1");
        Book book2 = givenBook("고양이책2");
        Book book3 = givenBook("멍멍이책1");
        Book book4 = givenBook("멍멍이책2");

        Author author1 = givenAuthor("유이나");
        Author author2 = givenAuthor("이다예");

        // ManyToMany 설정
//        book1.addAuthor(author1);   // 저자 유이나
//        book2.addAuthor(author2);   // 저자 이다예
//        book3.addAuthor(author1, author2);  // 공동저자
//        book4.addAuthor(author1, author2);  // 공동저자
//
//        // 유이나가 작성한 책들
//        author1.addBook(book1, book3, book4);
//        // 이다예가 작성한 책들
//        author1.addBook(book2, book3, book4);

        Writing writing1 = givenWriting(book1, author1);
        Writing writing2 = givenWriting(book2, author2);
        Writing writing3 = givenWriting(book3, author1);
        Writing writing4 = givenWriting(book3, author2);
        Writing writing5 = givenWriting(book4, author1);
        Writing writing6 = givenWriting(book4, author2);

        // Book 에 Writing 을 매핑
        book1.addWritings(writing1);
        book2.addWritings(writing2);
        book3.addWritings(writing3, writing4);
        book4.addWritings(writing5, writing6);

        //Author 에 Writing 을 매핑
        author1.addWritings(writing1, writing3, writing5);
        author2.addWritings(writing2, writing4, writing6);

        // book, author 를 save --> 중간테이블에 INSERT 발생
        bookRepository.saveAll(List.of(book1, book2, book3, book4));
        System.out.println("!!".repeat(30));
        authorRepository.saveAll(List.of(author1, author2));

        System.out.println("@".repeat(30));
        // 세번째 책의 저자들  Book ---> Author
//        bookRepository.findAll().get(2).getAuthoers().forEach(System.out::println);
        bookRepository.findAll().get(2).getWritings().forEach(w -> System.out.println(w.getAuthor()));

        System.out.println("~".repeat(30));
        // 첫번째 저자의 책들  Author ---> Book
//        authorRepository.findAll().get(0).getBooks().forEach(System.out::println);
        authorRepository.findAll().get(0).getWritings().forEach(w -> System.out.println(w.getBook()));

        System.out.println("\n------------------------------------------------------------\n");
    }

    // 테스트용 데이터 입력 메소드
    private Book givenBook(String name) {
        Book book = new Book();
        book.setName(name);

        return bookRepository.save(book); // INSERT
    }

    private Author givenAuthor(String name) {
        Author author = new Author();
        author.setName(name);

        return authorRepository.save(author); // INSERT
    }

    private Writing givenWriting(Book book, Author author) {
        Writing writing = new Writing();
        writing.setBook(book);
        writing.setAuthor(author);

        return writingRepository.save(writing);
    }






}