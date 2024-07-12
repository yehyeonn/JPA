package com.lec.spring.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// ApplicationContextAware 를 implement 하면
// ApplicationContext (IoC 컨테이너)를 가져올수 있다.

@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        BeanUtils.applicationContext = applicationContext;
    }

    // 해당 T 클래스에 맞는 Bean을 ApplictionContext 에서 받아오는 메소드 작성
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
