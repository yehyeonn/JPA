package com.lec.spring.listener;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserHistory;
import com.lec.spring.repository.UserHistoryRepository;
import com.lec.spring.support.BeanUtils;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

// ★ Entity Listener 는 Spring Bean 을 주입 받지 못.한.다!   => Autowired 해도 userHistoryRepository가 null
//@Component
public class UserEntityListener {

    // ★ Entity Listener 는 Spring Bean 을 주입 받지 못.한.다!
//    @Autowired  // 이 주입을 위해 @Component 사용(필드 인제ㅐㄱ션 비추)
//    private UserHistoryRepository userHistoryRepository;

//    public UserEntityListener(UserHistoryRepository userHistoryRepository) {    // 생성자 하나만 있기 때문에 Autowired 붙어있는 것도 동일하다~
//        this.userHistoryRepository = userHistoryRepository; // 추천~
//    }

    @PostUpdate
    @PostPersist // 복수 가능
    public void addUserHistory(Object o) {
        System.out.println(">> UserEntityListener#addUserHistory()");

        // 스프링 bean 객체 주입 받기
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;
        // UserHistory 에 UPDATE 될 User 정보를 담아서 저장 (INSERT)
        UserHistory userHistory = new UserHistory();
//        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());
        userHistory.setUser(user);

        userHistoryRepository.save(userHistory);    // INSERT
    }
}
