package com.haui.web_demo.entities;

import com.haui.web_demo.entities.Product;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "discount")
@Entity
public class Discounted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountID")
    private Long DiscountID;

    @Column(name = "Discountname")
    private String discountname;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoomID", nullable = false)
    private Product product;

    @Column(name = "Discount")
    private Double discount;

    @Column(name = "Start_time")
    private Date start_time;

    @Column(name = "End_time")
    private Date end_time;


    public Discounted() {

    }

    public Discounted(Long discountID, String discountname, Product product, Double discount, Date start_time, Date end_time) {
        DiscountID = discountID;
        this.discountname = discountname;
        this.product = product;
        this.discount = discount;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Long getDiscountID() {
        return DiscountID;
    }

    public void setDiscountID(Long discountID) {
        DiscountID = discountID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDiscountname() {
        return discountname;
    }

    public void setDiscountname(String discountname) {
        this.discountname = discountname;
    }


    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "discounted{" +
                "DiscountID=" + DiscountID +
                ", product=" + product +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
