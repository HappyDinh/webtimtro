package com.haui.web_demo.Controller.User;

import com.haui.web_demo.Controller.common.FileUploadController;
import com.haui.web_demo.Service.RoomService;
import com.haui.web_demo.Service.UserService;
import com.haui.web_demo.entities.CustomerUserDetails;
import com.haui.web_demo.entities.Product;
import com.haui.web_demo.entities.Userobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Addroom {
    @Autowired
    private RoomService roomService;

    @Autowired
    private FileUploadController fileUploadController;

    @Autowired
    private UserService userService;


    @PostMapping("/addroom")
    public String addroom(@ModelAttribute Product product,
                          @RequestParam(value = "images", required = false) MultipartFile[] images,
                          @RequestParam(value = "videoFile", required = false) MultipartFile video,
                          Model model, @AuthenticationPrincipal CustomerUserDetails customerUserDetails) throws IOException {

        if (product == null) {
            model.addAttribute("error", "Lỗi: Không nhận được thông tin phòng!");
            return "errorPage";
        }

        List<String> imageUrls = new ArrayList<>();
        String videoUrl = null;

        // Kiểm tra và upload ảnh nếu có
        if (images != null) {
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    String uploadedImageUrl = fileUploadController.uploadFile(image);
                    imageUrls.add(uploadedImageUrl);
                }
            }
        }

        // Kiểm tra và upload video nếu có
        if (video != null && !video.isEmpty()) {
            videoUrl = fileUploadController.uploadFile(video);
        }

        // Kiểm tra ít nhất một trong hai có dữ liệu
        if (imageUrls.isEmpty() && videoUrl == null) {
            model.addAttribute("error", "Bạn cần tải lên ít nhất một ảnh hoặc một video!");
            return "errorPage";
        }

        // Lấy thông tin User từ username
        Userobject user = userService.findByUsername(customerUserDetails.getUsername());
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy người dùng!");
            return "errorPage";
        }

        product.setUser(user); // Gán user vào sản phẩm


        // Lưu danh sách ảnh và video vào đối tượng Product
        product.setImageUrlList(imageUrls);
        product.setVideo(videoUrl); // Nếu video null thì giữ nguyên null

        // Lưu sản phẩm vào database
        roomService.saveroom(product);
        model.addAttribute("message", "Thêm phòng thành công!");

        return "redirect:/indexuser";
    }

}
