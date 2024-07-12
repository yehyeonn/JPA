package com.lec.spring.listener;

import java.time.LocalDateTime;

public interface Auditable {

    // 추상메소드 (lombok 이 만들어놨을 getter&setter)
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);

}
