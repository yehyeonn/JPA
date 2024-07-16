package com.lec.spring.repository;

// Repository 생성
// JpaRepository<Entity타입, Id타입> 상속 ← 바로 이게 Spring Data JPA 가 지원해주는 영역!
//   상속받은 것만으로도 많은 JPA 메소드를 편리하게 사용 가능하게 된다.
//   상속받은 것만으로도 이미 Spring Context 에 생성된다. → 주입 가능!

import com.lec.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1.
//    User findByName(String name);       // 단일객체를 리턴하는 경우만 가능.
//    List<User> findByName(String name);     // 여러 객체가 SELECT 되는 경우.
    Optional<User> findByName(String name);

    // 2.
    User findByEmail(String email);
    User getByEmail(String email);
    User readByEmail(String email);
    User queryByEmail(String email);
    User searchByEmail(String email);
    User streamByEmail(String email);           // 뭘로 하던지 관계 XXX find와 무엇인지만 들어가면 됨 중간은 상관 X
    User findUsersByEmail(String email);        // Ok

    // 3. find 아무말... 가능
    User findYouinahByEmail(String email);

    // 4. 잘못된 네이밍
//    User findByByName(String name);     // 서버 가동 자체 불가능 'ByName' 이란 property 를 찾기 때문

    // 5. First, Top
    List<User> findFirst1ByName(String name);
    List<User> findFirst2ByName(String name);
    List<User> findTop1ByName(String name);
    List<User> findTop2ByName(String name);

    // 6.
    // Last 는 없는 키워드! find 와 By 사이의 키워드 아닌 것들은 무시된다.
    List<User> findLast1ByName(String name);        // 걍 findByName() 과 동일

    // 7. And, Or
    List<User> findByEmailAndName(String email, String name);
    List<User> findByEmailOrName(String email, String name);

    // 8. After, Before -> 비교절
    List<User> findByCreatedAtAfter(LocalDateTime dateTime);    // dateTime 이후
    List<User> findByIdAfter(Long id);  // 주어진 id 보다 큰 값
    List<User> findByNameBefore(String name);  // 주어진 name 보다 작은 값

    // 9. GreaterThan, GreaterThanEqual, LessThan, LessThanEqual -> 비교절
    //      >, >=, <, <=
    List<User> findByCreatedAtGreaterThan(LocalDateTime dateTime);
    List<User> findByNameGreaterThanEqual(String name);

    // 10. Between
    List<User> findByCreatedAtBetween(LocalDateTime dateTime1, LocalDateTime dateTime2);
    List<User> findByIdBetween(Long id1, Long id2);

    // k BETWEEN a AND b    =>   k >= a AND k <= b
    List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Long id1, Long id2);

    // 11. Null, Empty
    List<User> findByIdIsNotNull();

//    List<User> findByIdIsNotEmpty();        // 스프링 가동 시 에러

    // IsEmpty / IsNotEmpty can only be used on collection properties
    // Query method 의 Empty 는 문자열의 Empty 와 다르다!
    // Query method 의 Empty 는 collection 에서의 Empty, not Empty 를 체크한다.(List 의 요소가 있는지 없는지)

//    List<User> findByAddressIsNotEmpty();   // Address 가 Empty 가 아닌 것

    // 12. In, NotIn
    List<User> findByNameIn(List<String> names);    // IN은 (a, b, c) 식으로 작성되기 때문에 list가 매개변수

    // 13. StartingWith, EndingWith, Contains
    // 문자열에 대한 검색 쿼리, LIKE 사용
    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByEmailContains(String email);

    // 14. Like
    List<User> findByEmailLike(String email);   // 와일드카드까지 포함한 값이 매개변수로 와야함

    // 15. Is, Equals
    // 특별한 역할 안함. 생략 가능. 가독성 차원에서 남겨진 키워드들.
    Set<User> findByNameIs(String name);    // findByName 과 동일

    // 아래 4개는 동일하게 동작하는 메소드다.
    // Set<User> findByName(String name);
    // Set<User> findUserByName(String name);
    // Set<User> findUserByNameIs(String name);
    // Set<User> findUserByNameEquals(String name);

    // 16. OrderBy => sort
    List<User> findTopByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDesc(String name);

    // 17. 정렬기준 추가
    List<User> findFirstByNameOrderByIdDescEmailDesc(String name);

    // 18. sort 객체를 매개변수로 사용한 정렬
    List<User> findFirstByName(String name, Sort sort);

    // 19. Paging
    Page<User> findByName(String name, Pageable pageable);

    // 20. Enum 처리
    @Query(value = "SELECT * FROM t_user LIMIT 1" , nativeQuery = true)
    Map<String, Object> findRowRecord();      // Map : 컬럼 이름과 컬럼 value


}
