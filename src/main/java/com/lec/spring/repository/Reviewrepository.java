package com.lec.spring.repository;

import com.lec.spring.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Reviewrepository extends JpaRepository<Review, Long> {

}
