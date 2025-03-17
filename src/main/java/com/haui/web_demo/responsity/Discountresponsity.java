package com.haui.web_demo.responsity;

import com.haui.web_demo.entities.Discounted;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Discountresponsity extends JpaRepository<Discounted, Long> {
    List<Discounted> findAllBy();
}
