package com.haui.web_demo.entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "reviewroom")
public class Reviewroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private Userobject user;

    @Column(name = "review_name")
    private String review_name;

    @Column(name = "review_content", length = 545)
    private String review_content;

    @Column(name = "reivew_img")
    private String review_img;

    @Transient
    private MultipartFile reviewimg;

    public Reviewroom() {

    }

    public Reviewroom(Long reviewId, Userobject user, String review_name, String review_content, String review_img, MultipartFile reviewimg) {
        this.reviewId = reviewId;
        this.user = user;
        this.review_name = review_name;
        this.review_content = review_content;
        this.review_img = review_img;
        this.reviewimg = reviewimg;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Userobject getUser() {
        return user;
    }

    public void setUser(Userobject user) {
        this.user = user;
    }

    public String getReview_name() {
        return review_name;
    }

    public void setReview_name(String review_name) {
        this.review_name = review_name;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getReview_img() {
        return review_img;
    }

    public void setReview_img(String review_img) {
        this.review_img = review_img;
    }

    public MultipartFile getReviewimg() {
        return reviewimg;
    }

    public void setReviewimg(MultipartFile reviewimg) {
        this.reviewimg = reviewimg;
    }
}
