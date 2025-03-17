package com.haui.web_demo.responsity;


import com.haui.web_demo.entities.Blog;
import com.haui.web_demo.entities.Product;
import com.haui.web_demo.entities.Userobject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Blogresponsity extends JpaRepository<Blog, Long> {
    List<Blog> findAllBy();
    List<Blog> findByUser(Userobject user);
}
