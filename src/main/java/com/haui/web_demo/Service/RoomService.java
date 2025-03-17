package com.haui.web_demo.Service;

import com.haui.web_demo.entities.Discounted;
import com.haui.web_demo.entities.Product;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.Discountresponsity;
import com.haui.web_demo.responsity.Roomresponsity;
import com.haui.web_demo.responsity.Userresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private Roomresponsity roomresponsity;

    @Autowired
    private Discountresponsity discountresponsity;

    @Autowired
    private Userresponsity userresponsity;

    public List<Product> getRoomsByIds(List<Long> roomIds) {
        return roomresponsity.findByIdIn(roomIds);
    }

    public Product saveroom(Product product){
        return roomresponsity.save(product);
    }

    public List<Product> getAllRooms(){
        return roomresponsity.findAll();
    }

    public void deleteRoom(Long id){
        roomresponsity.deleteById(id);
    }

    public Page<Product> getAllRooms(Integer pageno, Integer limitsize) {
        Pageable pageable = PageRequest.of(pageno - 1, limitsize);
        return roomresponsity.findAll(pageable);  // Dùng findAll() để trả về phân trang
    }

    public List<Product> getRoomsByUserId(Long userID) {
        Userobject user = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        return roomresponsity.findByUser(user);
    }


    public Page<Product> getAllRoomswithVideo(Integer pageno, Integer limitsize) {
        Pageable pageable = PageRequest.of(pageno - 1, limitsize);
        return roomresponsity.findRoomsWithVideo(pageable);  // Dùng findAll() để trả về phân trang
    }

    public String getnamefromproduct(Long productID){
        Product product = roomresponsity.findById(productID).orElseThrow(() -> new RuntimeException("Not found product"));
        return product.getUser().getUsername();
    }

    public Page<Product> getByType(String type, Integer pageno, Integer limitsize){
        Pageable pageable = PageRequest.of(pageno - 1, limitsize);
        return roomresponsity.findByType(type, pageable);
    }

    public List<Discounted> getAllDiscounted(){
        return discountresponsity.findAllBy();
    }

    public List<Product> findByArea(String area) {
        return roomresponsity.findByArea(area);
    }

}
