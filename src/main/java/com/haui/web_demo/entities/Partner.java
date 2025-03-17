package com.haui.web_demo.entities;

import jakarta.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "partner")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partnerID")
    private Long partnerID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private Userobject user;

    @Column(name = "partnername")
    private String partner_name;

    @Column(name = "partner_content")
    private String partner_content;

    @Column(name = "partner_address")
    private String partner_address;

    @Column(name = "partner_price")
    private Double partner_price;

    @Column(name = "partner_acreage")
    private Double partner_acreage;

    @Column(name = "partner_img")
    private String partner_img;

    @Transient
    private MultipartFile partnerimg;

    public Partner() {
    }

    public Partner(Long partnerID, Userobject user, String partner_name, String partner_content, String partner_address, Double partner_price, Double partner_acreage, String partner_img) {
        this.partnerID = partnerID;
        this.user = user;
        this.partner_name = partner_name;
        this.partner_content = partner_content;
        this.partner_address = partner_address;
        this.partner_price = partner_price;
        this.partner_acreage = partner_acreage;
        this.partner_img = partner_img;
    }

    public Long getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Long partnerID) {
        this.partnerID = partnerID;
    }

    public Userobject getUser() {
        return user;
    }

    public void setUser(Userobject user) {
        this.user = user;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_content() {
        return partner_content;
    }

    public void setPartner_content(String partner_content) {
        this.partner_content = partner_content;
    }

    public String getPartner_address() {
        return partner_address;
    }

    public void setPartner_address(String partner_address) {
        this.partner_address = partner_address;
    }

    public Double getPartner_price() {
        return partner_price;
    }

    public void setPartner_price(Double partner_price) {
        this.partner_price = partner_price;
    }

    public Double getPartner_acreage() {
        return partner_acreage;
    }

    public void setPartner_acreage(Double partner_acreage) {
        this.partner_acreage = partner_acreage;
    }

    public String getPartner_img() {
        return partner_img;
    }

    public void setPartner_img(String partner_img) {
        this.partner_img = partner_img;
    }

    public MultipartFile getPartnerimg() {
        return partnerimg;
    }

    public void setPartnerimg(MultipartFile partnerimg) {
        this.partnerimg = partnerimg;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "partnerID=" + partnerID +
                ", user=" + user +
                ", partner_name='" + partner_name + '\'' +
                ", partner_content='" + partner_content + '\'' +
                ", partner_address='" + partner_address + '\'' +
                ", partner_price=" + partner_price +
                ", partner_acreage=" + partner_acreage +
                ", partner_img='" + partner_img + '\'' +
                ", partnerimg=" + partnerimg +
                '}';
    }
}
