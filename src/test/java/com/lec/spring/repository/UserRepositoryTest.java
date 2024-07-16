package com.lec.spring.repository;

import com.lec.spring.domain.Address;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import com.lec.spring.domain.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@SpringBootTest // 가동 시에 하위 bean 객체 생성 후 실행한다는 뜻
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

//    @Test
    void crud() {
        System.out.println("\n-- TEST#crud() ---------------------------------------------");
//        userRepository.findAll().forEach(System.out::println);   // SELECT 전체 조회 > SELECT * FROM T_USER; List 리턴
//        User user = new User(); // 인스턴스 생성(Java 객체)
//        System.out.println("user: " + user);
//
//        userRepository.save(user);  // INSERT, 저장하기 -> 영속화된 객체로 다룸!
//        userRepository.findAll().forEach(System.out::println);// SELECT
//        System.out.println("user: " + user);    // 그래서 id 에 A.I 되어 적용된 값이 입력 (이제 자바객체 아니다~)

        // 테스트에 사용할 변수들
        List<User> users = null;
        User user1 = null, user2 = null, user3 = null, user4 = null, user5 = null;
        List<Long> ids = null;

        // #002 findAll()
        users = userRepository.findAll();
        users.forEach(System.out::println);

        System.out.println();

        // #003 findXX() + Sort.by() 사용 (Sort : 정렬)
        users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));   // name 가준 역순, ORDER BY 연산자 사용
        users.forEach(System.out::println);

        // #004 findByXX(Iterable)
        ids = List.of(1L, 3L, 5L);  // 1,3,5 아이디를 가진 리스트
        users = userRepository.findAllById(ids);    // IN 연산자 사용
        users.forEach(System.out::println);

        // #005 save(entity) 저장하기
        user1 = new User("jack", "jack@rednight.com");  // 자바객체
        userRepository.save(user1); // INSERT 발생

        userRepository.findAll().forEach(System.out::println);

        // #006 saveAll(Iterable)
        user1 = new User("jack", "jack@redknight.com");
        user2 = new User("steve", "steve@redknight.com");
        userRepository.saveAll(List.of(user1, user2));

        userRepository.findAll().forEach(System.out::println);

        // #008 findById(id)
        // 리턴타입은 Optional<Entity>!!!
        Optional<User> user;
        user = userRepository.findById(1L); // where 절 사용

        System.out.println(user);   // Optional 객체
        System.out.println(user.get()); // get 해서 꺼내기 가능

        user = userRepository.findById(10L);
        System.out.println(user);   // Optional 은 null 허용 X => 예외 발생하지 않고 Optional.empty 출력됨

        user1 = userRepository.findById(10L).orElse(null);
        System.out.println(user1);  // null

        // #009 flush()
        // flush() 는 SQL쿼리의 변화를 주는게 아니라 DB 반영 시점을 조정한다. 로그 출력으로는 변화를 확인하기 힘들다
        userRepository.save(new User("new martin", "newmartin@redknight.com"));
        userRepository.flush(); // 안정적으로 가려면 crud 후 바로 flush!

        // saveAndFlush() = save() + flush();
        userRepository.saveAndFlush(new User("베리베리", "베리@berry.com"));
        userRepository.findAll().forEach(System.out::println);

        // #010 count()
        Long count = userRepository.count();
        System.out.println(count);

        // #011 existsById()    특정 아이디가 존재하는지 여부(boolean 리턴) => count + where 사용해 출력!
        boolean exists = userRepository.existsById(1L);
        System.out.println(exists);

        // #012 delete(entity)
        // 삭제하기(영속성 객체 사용해 삭제해야함)
//        userRepository.delete(userRepository.findById(1L).orElse(null));   // Optional 리턴(T 객체) => 영속성 객체
        // delete 는 해당 조건의 객체가 있는지 존재 여부를 확인하기 위해 select 를 한 번 진행함(SELECT + DELETE) 그래서 총 4번 수행(수정도 마찬가지)
        userRepository.findAll().forEach(System.out::println);

        // delete 메소드는 null 값 허용 X => 예외 발생시켜서 핸들링 할 것
//        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));

        // #013 deleteById(id)
//        userRepository.deleteById(1L);  // 없는 경우는 select 만 수행하고 끝남
//        userRepository.deleteById(2L);  // 있으면 delete 실행

        // #014 deleteAll()
        System.out.println(new String("--").repeat(10));
//        userRepository.deleteAll(); // SELECT 1회 + DELETE x n

        // #015 deleteAll(Iterable)
//        userRepository.deleteAll(userRepository.findAllById(List.of(1L, 3L)));  // findAllById 에서 이미 없기 때문에 한 번만 실행
        // SELECT x n회 + DELETE x n회

        // deleteAll() 은 성능이슈 발생!
        // #016 deleteInBatch()
//        userRepository.deleteInBatch(userRepository.findAllById(List.of(3L, 4L, 5L)));  // Or 연산자 사용해 한 번의 명령으로 실행

        // #017 deleteAllInBatch 전체 삭제
//        userRepository.deleteAllInBatch();

        // Batch 가 없는 delete -> DELETE x n회
        // Batch 가 있는 delete -> 한 번의 DELETE

        // #018 findXXX(Pageable)  페이징
        // PageRequest 는 Pageable 의 구현체  org.springframework.data.domain.PageRequest
        // 리턴값은 Page<T>  org.springframework.data.domain.Page   => page 객체
        // 주의: page 값은 0-base 다
        Page<User> page = userRepository.findAll(PageRequest.of(3,3));    // 한 페이지 당 3개씩, 2번째 페이지(0-base)

        System.out.println("page: " + page);
        System.out.println("totalElements: " + page.getTotalElements());        // 전체 개수
        System.out.println("totalPages: " + page.getTotalPages());              // 총 페이지
        System.out.println("numberOfElements: " + page.getNumberOfElements());  // 현재 페이지에 있는 객체의 개수
        System.out.println("sort: " + page.getSort());      // 정렬?
        System.out.println("size: " + page.getSize());  // 페이징 할때 나누는 size

        page.getContent().forEach(System.out::println);  // 해당 페이지 내부의 데이터(들) (List 출력)

        // #019 QueryByExample 사용
        ExampleMatcher matcher = ExampleMatcher.matching()            // 검색 조건 담는 객체
//                .withIgnorePaths("name")                // name 컬럼은 매칭하지 않음
                .withMatcher("email", endsWith())       // email 컬럼은 뒷부분으로 매칭하여 검색      => 뒤가 ~로 끝나는 것
                ;
//Example.of(probe, ExampleMatcher)  <-- 여기서 probe 란 실제 Entity 는 아니란 말입니다.  match 를 위해서 쓰인 객체
        Example<User> example = Example.of(new User("ma", "knight.com"), matcher);      // probe

        userRepository.findAll(example).forEach(System.out::println);

        // #020
        user1 = new User();
        user1.setEmail("blue");

        // email 필드에서 주어진 문자열을 담고 있는 것을 검색
        matcher = ExampleMatcher.matching().withMatcher("email", contains());
        example = Example.of(user1, matcher);
        userRepository.findAll(example).forEach(System.out::println);

        // UPDATE !!
        // save() 는 INSERT 뿐 아니라 UPDATE 도 수행.

        userRepository.save(new User("유인아", "You-in-Ah@베리베리.com"));     // INSERT => @Id 가 null 인 경우는 insert

        user1 = userRepository.findById(1L).orElseThrow(RuntimeException::new);     // 영속성객체
        user1.setEmail("마우스가@움직여요.com");
        userRepository.save(user1);     // SELECT + UPDATE, id = 1L 인 User 를 수정 => @Id 가 이미 존재하는 경우는 update

        userRepository.findAll().forEach(System.out::println);

        System.out.println("------------------------------------------------------------\n");
    }

    // 테스트에 사용할 변수들
    List<User> users;
    User user, user1, user2, user3, user4, user5;
    List<Long> ids;
    Optional<User> optUser;

    // @BeforeEach : 매 테스트 메소드가 실행되기 직전에 실행
    //  @BeforeEach 메소드의 매개변수에 TestInfo 객체를 지정하면
    // JUnit Jupiter 에선 '현재 실행할 test' 의 정보가 담긴 TestInfo 객체를 주입해준다
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("─".repeat(40));

        users = null;
        user = user1 = user2 = user3 = user4 = user5 = null;
        ids = null;
        optUser = null;
    }

    // @AfterEach : 매 테스트 메소드가 종료된 직후에 실행
    @AfterEach
    void afterEach() {
        System.out.println("-".repeat(40) + "\n");
    }


    /***********************************************************************
     * Query Method
     */

    // 다양한 Query Return Types
    //   https://docs.spring.io/spring-data/jpa/reference/repositories/query-return-types-reference.html
    //   => void, Primitive, Wrapper, T, Iterator<T>, Collection<T>, List<T>, Optional<T>, Option<T>, Stream<T> ...
    // 쿼리 메소드의 리턴타입은 SELECT 결과가  '1개인지' 혹은 '복수개인지' 에 따라, 위에서 가용한 범위내에서 설정해서 선언

    @Test
    void qryMethod01() {
        System.out.println(userRepository.findByName("dennis"));
        // 티런타입이 User 이면 에러다. <- 여러 개를 리턴하는 경우
//        System.out.println(userRepository.findByName("martin"));
//        userRepository.findByName("martin").forEach(System.out::println);
    }

    // 쿼리 메소드의 naming
    //  https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html
    //     find…By, read…By, get…By, query…By, search…By, stream…By
    @Test
    void qryMethod002() {
        String email = "martin@redknight.com";
        System.out.println("findByEmail: " + userRepository.findByEmail(email));
        System.out.println("getByEmail : " + userRepository.getByEmail(email));
        System.out.println("readByEmail : " + userRepository.readByEmail(email));
        System.out.println("queryByEmail : " + userRepository.queryByEmail(email));
        System.out.println("searchByEmail : " + userRepository.searchByEmail(email));
        System.out.println("streamByEmail : " + userRepository.streamByEmail(email));
        System.out.println("findUserByEmail : " + userRepository.findUsersByEmail(email));
    }

    @Test
    void qryMethod03() {
        System.out.println(userRepository.findYouinahByEmail("martin@redknight.com"));
    }

    // First, Top
    //   First 와 Top 은 둘다 동일 (가독성 차원에서 제공되는 것임)
    @Test
    void qryMethod005() {
        String name = "martin";
        System.out.println("findTop1ByName : " + userRepository.findFirst1ByName(name));
        System.out.println("findTop2ByName : " + userRepository.findTop2ByName(name));
        System.out.println("findFirst1ByName : " + userRepository.findFirst1ByName(name));
        System.out.println("findFirst2ByName : " + userRepository.findFirst2ByName(name));
    }

    // And, Or
    @Test
    void qryMethod007() {
        String email = "martin@redknight.com";
        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@redknight.com", "martin"));
        System.out.println("findByEmailAndName : " + userRepository.findByEmailAndName("martin@redknight.com", "dennis"));      // [] 출력
        System.out.println("findByEmailOrName : " + userRepository.findByEmailOrName("martin@redknight.com", "dennis"));
    }

    // After, Before
    // After, Before 는 주로 '시간'에 대해서 쓰이는 조건절에 쓰인다.  (가독성 측면)
    // 그러나, 꼭 '시간'만을 위해서 쓰이지는 않는다 .   '숫자', '문자열' 등에서도 쓰일수 있다.
    @Test
    void qryMethod008() {
        System.out.println("findByCreatedAtAfter : " + userRepository.findByCreatedAtAfter(
                LocalDateTime.now().minusDays(1L)
        ));
        System.out.println("findByIdAfter : " + userRepository.findByIdAfter(4L));
        System.out.println("findByNameBefore : " + userRepository.findByNameBefore("martin"));
    }

    // GreaterThan, GreaterThanEqual, LessThan, LessThanEqual
    @Test
    void qryMethod009() {
        System.out.println("findByCreatedAtGreaterThan : " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByNameGreaterThanEqual : " + userRepository.findByNameGreaterThanEqual("martin"));
    }


    // Between
    @Test
    void qryMethod010() {
        System.out.println("findByCreatedAtBetween : "
                + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween : " + userRepository.findByIdBetween(1L, 3L));
        System.out.println("findByIdGreaterThanEqualAndIdLessThanEqual : "
                + userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(1L, 3L));
    }


    // Empty 와 Null
    //   - IsEmpty, Empty
    //   - IsNotEmpty, NotEmpty,
    //   - NotNull, IsNotNull
    //   - Null, IsNull
    @Test
    void qryMethod011() {
        System.out.println("findByIdIsNotNull : " + userRepository.findByIdIsNotNull());    // 예측: WHERE id IS NOT NULL

//        System.out.println("findByIdIsNotEmpty : " + userRepository);

//        System.out.println("findByAddressIsNotEmpty : " + userRepository.findByAddressIsNotEmpty()); // 예측: [], exists 사용(서브쿼리)
    }

    // In, NotIn
    @Test
    void qryMethod012() {
        System.out.println("findByNameIn : "
                + userRepository.findByNameIn(List.of("martin", "dennis")));
        // WHERE name IN (?, ?)
    }


    // StartingWith, EndingWith, Contains
    // 문자열에 대한 검색쿼리, LIKE 사용
    @Test
    void qryMethod013() {
        System.out.println("findByNameStartingWith : " + userRepository.findByNameStartingWith("mar"));
        System.out.println("findByNameEndingWith : " + userRepository.findByNameEndingWith("s"));
        System.out.println("findByEmailContains : " + userRepository.findByEmailContains("red"));
    }


    // Like
    // 사실 위의 키워드는 Like 를 한번 더 wrapping 한거다.
    // Like 검색 시 %, _ 와 같은 와일드 카드 사용.
    @Test
    void qryMethod014() {
        System.out.println("findByEmailLike : " + userRepository.findByEmailLike("%" + "dragon" + "%"));
    }


    // Is, Equals
    // 특별한 역할은 하지 않는다. 생략가능. 가독성 차원에서 남겨진 키워드입니다.
    @Test
    void qryMethod015() {
        System.out.println(userRepository);
    }


    /***********************************************************************
     * Query Method - Sorting & Paging
     */


    // Naming 기반 정렬
    // Query method 에 OrderBy 를 붙임
    @Test
    void qryMethod016() {
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("martin"));    // Last 는 없지롱

        System.out.println("findTopByNameOrderByIdDesc : " + userRepository.findTopByNameOrderByIdDesc("martin"));
    }


    // 정렬기준 추가
    @Test
    void qryMethod017() {
        System.out.println("findFirstByNameOrderByIdDesc : "
                + userRepository.findFirstByNameOrderByIdDesc("martin"));
        System.out.println("findFirstByNameOrderByIdDescEmailDesc : "
                + userRepository.findFirstByNameOrderByIdDescEmailDesc("martin"));
    }


    // 매개변수(Sort) 기반 정렬
    // Query method 에 Sort 매개변수로 정렬옵션 제공
    @Test
    void qryMethod018() {
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"))));
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));
    }

    // ↑ 무엇이 더 나을까?
    //   Naming 기반 정렬 vs. 매개변수(Sort) 기반 정렬
    //   'Naming 기반 정렬' 은 유연성이 떨어지고..
    //      정렬 조건이 많아지면 길어지면 메소드도 많이 생성해야 하고 메소드 이름이 길어지니까 가독성이 안좋다.
    //   '매개변수 기반 정렬' 은 메소드 하나로 여러 정렬 조건을 다룰수 있다.
    //     메소드 하나만 정의해놓고, 사용하는 쪽에서 정렬조건을 제공할수 있다
    //     유연성, 자유도, 가독성 좋다.

    @Test
    void qryMethod018_2() {
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
        System.out.println("findFirstByName + Sort : "
                + userRepository);
    }

    private Sort getSort() {
        return Sort.by(
                Sort.Order.desc("id"),
                Sort.Order.asc("email").ignoreCase(),   // 대소문자 구분 X
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("updatedAt"));
    }

    @Test
    void qryMethod018_3() {
        System.out.println("findFirstByName + Sort : "
                + userRepository.findFirstByName("martin", getSort()));
    }

    // 19 Paging + Sort
    // PageRequest.of(page, size, Sort) page는 0-base
    // PageRequest 는 Pageable의 구현체
    @Test
    void qryMethod019() {
        Page<User> userPage = userRepository.findByName("martin", PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id"))));

        System.out.println("page: " + userPage); // Page 를 함 찍어보자
        System.out.println("totalElements: " + userPage.getTotalElements());        // 2
        System.out.println("totalPages: " + userPage.getTotalPages());              // 2
        System.out.println("numberOfElements: " + userPage.getNumberOfElements());  // 1    현재 페이지에 보이는 객체 개수
        System.out.println("sort: " + userPage.getSort());
        System.out.println("size: " + userPage.getSize()); // 페이징 할때 나누는 size   1
        userPage.getContent().forEach(System.out::println);  // 페이지 내의 데이터 List<>
    }

    @Test
    void insertAndUpdateTest() {
        System.out.println("\n-- TEST#insertAndUpdateTest() ---------------------------------------------");

        user = User.builder()
                .name("martin")
                .email("martin2@blueknight.com")
                .build();

        userRepository.save(user);  // INSERT

        user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("U E NA");
        userRepository.save(user2); // UPDATE

        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void enumTest() {
        System.out.println("\n-- TEST#enumTest() ---------------------------------------------");

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setGender(Gender.MALE);
        userRepository.save(user);  // UPDATE
        userRepository.findAll().forEach(System.out::println);

        // enum 타입이 실제 어떤 값으로 DB에 저장되어 있는지 확인
        System.out.println(userRepository.findRowRecord().get("gender"));   // 0 과 1로 나옴 =>  @Enumerated(value = EnumType.STRING) 로 이제 String 으로 나옴

        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void listenerTest() {

        user = new User();
        user.setEmail("베리베리@mail.com");
        user.setName("유인아");

        userRepository.save(user);  // INSERT

        // SELECT
        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        user2.setName("marrrrrtin");
        userRepository.save(user2);  // SELECT, UPDATE

        userRepository.deleteById(4L);  // SELECT, DELETE

        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void prePersistTest() {
        System.out.println("\n-- TEST#prePersistTest() ---------------------------------------------");
        User user = new User();
        user.setEmail("martin2@redknight.com");
        user.setName("martin2");

//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now()); // 이렇게 반복하지 말고 prePersist 사용하자!

        userRepository.save(user);  // INSERT

        System.out.println(userRepository.findByEmail("martin2@redknight.com"));

        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void preUpdateTest() throws InterruptedException {
        Thread.sleep(1000); // 1초 뒤에 UPDATE 시도
        System.out.println("\n-- TEST#preUpdateTest() ---------------------------------------------");

        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        System.out.println("as-is : " + user);   // 수정전
        user.setName("martin2");

        userRepository.save(user);      // UPDATE
        System.out.println("to-be: " + userRepository.findAll().get(0));


        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void userHistoryTest() {
        System.out.println("\n-- TEST#userHistoryTest() ---------------------------------------------");

        User user = new User();
        user.setEmail("martin-new@greendragon.com");
        user.setName("martin-new");

        userRepository.save(user);  // INSERT

        user.setName("U E NA");
        userRepository.save(user);  // UPDATE. User UPDATE 전에 UserHistory 에 INSERT 발생

        userHistoryRepository.findAll().forEach(System.out::println);


        System.out.println("\n------------------------------------------------------------\n");
    }

    @Test
    void userRelationTest() {
        System.out.println("\n-- TEST#userRelationTest() ---------------------------------------------");

        User user = new User();
        user.setName("David");
        user.setEmail("david@reddragon.com");
        user.setGender(Gender.MALE);

        userRepository.save(user);  // User 와 UserHistory 에 INSERT

        user.setName("베리냥");
        userRepository.save(user);      // User 에 UPDATE, UserHistory 에 INSERT.

        System.out.println("^".repeat(30));

        user.setEmail("berry@mail.com");
        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);

        System.out.println("%".repeat(30));

        // 특정 userId 로 UserHistory 조회
//        Long userId = userRepository.findByEmail("berry@mail.com").getId();
//        List<UserHistory> result = userHistoryRepository.findByUserId(userId);
//        result.forEach(System.out::println);

        List<UserHistory> result = userRepository.findByEmail("berry@mail.com").getUserHistories();

        result.forEach(System.out::println);    // LazyInitializationException 발생!

        System.out.println("@".repeat(30));
        System.out.println(userHistoryRepository.findAll().get(0).getUser());

        System.out.println("\n------------------------------------------------------------\n");
    }
    //------------------------------------------------------------------
    // Embedded 테스트
    @Test
    void embededTest1(){
        User user = new User();
        user.setName("유인아");
        user.setHomeAddress(new Address("서울", "구로구", "대림동", "11111"));
        user.setCompanyAddress(new Address("경기도", "고양시", "고양고양", "23333"));
        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);

        System.out.println("👻".repeat(30));

        userHistoryRepository.findAll().forEach(System.out::println);
    }

    @Test
    void embededTest2(){
        User user1 = new User();
        user1.setName("유인아");
        user1.setHomeAddress(new Address("서울", "구로구", "대림동", "11111"));
        user1.setCompanyAddress(new Address("경기도", "고양시", "고양고양", "23333"));
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("신현아");
        user2.setHomeAddress(null);             // Address 가 null 인 경우
        user2.setCompanyAddress(null);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("홍가연");
        user3.setHomeAddress(new Address());    // Address 가 empty 인 경우
        user3.setCompanyAddress(new Address());
        userRepository.save(user3);

        userRepository.findAll().forEach(System.out::println);

        System.out.println("👻".repeat(30));

        userHistoryRepository.findAll().forEach(System.out::println);

        // DB에 저장된 내용 확인
        userRepository.findAllRowRecord().forEach(a -> System.out.println(a.entrySet()));   // null
    }
}