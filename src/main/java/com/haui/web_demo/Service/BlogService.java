package com.haui.web_demo.Service;

import com.haui.web_demo.entities.Blog;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.Blogresponsity;
import com.haui.web_demo.responsity.Userresponsity;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private Blogresponsity blogresponsity;

    @Autowired
    private Userresponsity userresponsity;

    public Blog createBlog(Blog blog) {
        return blogresponsity.save(blog);
    }

    public void deleteblog(Long id){
        blogresponsity.deleteById(id);
    }

    public List<Blog> getbyuser(Long userID){
        Userobject userobject = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        return blogresponsity.findByUser(userobject);
    }
}
