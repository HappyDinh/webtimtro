package com.haui.web_demo.responsity;

import com.haui.web_demo.entities.Partner;
import com.haui.web_demo.entities.Userobject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Partnerresponsity extends JpaRepository<Partner, Long> {
    List<Partner> findAllBy();
    List<Partner> findByUser(Userobject user);
}
