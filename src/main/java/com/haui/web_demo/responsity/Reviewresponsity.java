package com.haui.web_demo.responsity;

import com.haui.web_demo.entities.Reviewroom;
import com.haui.web_demo.entities.Userobject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Reviewresponsity extends JpaRepository<Reviewroom, Long> {
    List<Reviewroom> findAllBy();
    List<Reviewroom> findByUser(Userobject user);
}
