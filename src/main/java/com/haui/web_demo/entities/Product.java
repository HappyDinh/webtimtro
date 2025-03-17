package com.haui.web_demo.entities;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Table(name = "room")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private Userobject user;

    @Column(name = "Title")
    private String nameproduct;

    @Column(name = "area")
    private String area;

    @Column(name = "Address")
    private String address_product;

    @Column(name = "Distance")
    private Double distance;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "Acreage")
    private Double acreage;

    @Column(name = "Type")
    private String type;

    @Column(name = "Description", length = 455)
    private String description;

    @Column(name = "Closed")
    private boolean closed;

    @Column(name = "wifi")
    private boolean wifi;

    @Column(name = "Pets")
    private boolean pets;

    @Column(name = "Hotcold")
    private boolean hotcold;

    @Column(name = "Airconditioner")
    private boolean airconditioner;

    @Column(name = "Commonowner")
    private boolean commonowner;

    @Column(name = "Image", columnDefinition = "LONGTEXT")
    private String imageUrls; // Lưu chuỗi URL ảnh cách nhau bởi dấu phẩy

    @Transient // Không lưu trong CSDL, chỉ để sử dụng trong code
    private List<String> imageUrlList;

    @Column(name = "Video")
    private String video;

    @Transient  // Không lưu vào database
    private MultipartFile videoFile;

    public Product() {
    }

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Userobject getUser() {
        return user;
    }

    public void setUser(Userobject user) {
        this.user = user;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress_product() {
        return address_product;
    }

    public void setAddress_product(String address_product) {
        this.address_product = address_product;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getAcreage() {
        return acreage;
    }

    public void setAcreage(Double acreage) {
        this.acreage = acreage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isHotcold() {
        return hotcold;
    }

    public void setHotcold(boolean hotcold) {
        this.hotcold = hotcold;
    }

    public boolean isAirconditioner() {
        return airconditioner;
    }

    public void setAirconditioner(boolean airconditioner) {
        this.airconditioner = airconditioner;
    }

    public boolean isCommonowner() {
        return commonowner;
    }

    public void setCommonowner(boolean commonowner) {
        this.commonowner = commonowner;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
        this.imageUrlList = null; // Đặt lại giá trị để đảm bảo đồng bộ
    }

    public List<String> getImageUrlList() {
        if (imageUrlList == null && imageUrls != null && !imageUrls.isEmpty()) {
            imageUrlList = Arrays.asList(imageUrls.split(",")); // Tách chuỗi thành danh sách
        }
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            this.imageUrls = String.join(",", imageUrlList); // Gộp danh sách thành chuỗi
        }
        this.imageUrlList = imageUrlList;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }
}
