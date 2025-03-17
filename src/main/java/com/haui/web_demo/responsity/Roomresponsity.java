package com.haui.web_demo.responsity;

import com.haui.web_demo.entities.Product;
import com.haui.web_demo.entities.Userobject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface Roomresponsity extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByIdIn(List<Long> roomid);
    Page<Product> findByType(String type, Pageable pageable);
    List<Product> findByUser(Userobject user);

    // Truy vấn các phòng có video, phân trang
    @Query("SELECT p FROM Product p WHERE p.video IS NOT NULL")
    Page<Product> findRoomsWithVideo(Pageable pageable);

    // Lọc theo khu vực
    @Query("SELECT r FROM Product r WHERE :area IS NULL OR r.address_product LIKE %:area%")
    List<Product> findByArea(@Param("area") String area);

    // Lọc theo khoảng giá
    @Query("SELECT r FROM Product r WHERE r.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Lọc theo diện tích
    @Query("SELECT r FROM Product r WHERE r.acreage BETWEEN :minAcreage AND :maxAcreage")
    List<Product> findByAcreageRange(@Param("minAcreage") Double minAcreage, @Param("maxAcreage") Double maxAcreage);

    // Lọc theo thể loại
    @Query("SELECT r FROM Product r WHERE (:type IS NULL OR r.type = :type)")
    List<Product> findByType(@Param("type") String type);

}
