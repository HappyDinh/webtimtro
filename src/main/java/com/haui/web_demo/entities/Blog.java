package com.haui.web_demo.entities;


import jakarta.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blogID")
    private Long blogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private Userobject user;

    @Column(name = "blog_name")
    private String blog_name;

    @Column(name = "blog_content", length = 545)
    private String blog_content;

    @Column(name = "blog_img")
    private String blog_img;

    @Transient
    private MultipartFile blogimg;

    public Blog() {
    }

    public Blog(Long blogId, String blog_name, String blog_content, String blog_img) {
        this.blogId = blogId;
        this.blog_name = blog_name;
        this.blog_content = blog_content;
        this.blog_img = blog_img;
    }

    public Long getBlogId() {
        return blogId;
    }

    public Userobject getUser() {
        return user;
    }

    public void setUser(Userobject user) {
        this.user = user;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlog_name() {
        return blog_name;
    }

    public void setBlog_name(String blog_name) {
        this.blog_name = blog_name;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public String getBlog_img() {
        return blog_img;
    }

    public void setBlog_img(String blog_img) {
        this.blog_img = blog_img;
    }

    public MultipartFile getBlogimg() {
        return blogimg;
    }

    public void setBlogimg(MultipartFile blogimg) {
        this.blogimg = blogimg;
    }
}
