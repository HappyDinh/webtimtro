package com.haui.web_demo.Controller;

import com.haui.web_demo.Service.UserService;
import com.haui.web_demo.entities.Role;
import com.haui.web_demo.entities.User_Role;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.Userresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.haui.web_demo.Controller.common.FileUploadController;

import java.io.IOException;
import java.util.Optional;

@Controller
public class EditUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Userresponsity userresponsity;

    @Autowired
    private FileUploadController fileUploadController;

    //Lấy thông tin người dùng để chỉnh sửa
    @GetMapping("/edituser")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView edituser(@RequestParam(value = "userID", required = false) Long userID) {
        if (userID == null) {
            return new ModelAndView("redirect:/errorPage"); // Chuyển hướng đến trang lỗi nếu userID không tồn tại
        }

        Optional<Userobject> userobject = userService.GetUserByID(userID);
        if (userobject.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("admin/edituser");
            modelAndView.addObject("userobject", userobject.get());
            return modelAndView;
        }

        return new ModelAndView("redirect:/table_generals");
    }


    @PostMapping("/edituser/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveEditUser(@PathVariable("userID") Long userID,
                               @RequestParam("username") String username,
                               @RequestParam("phone") String phone,
                               @RequestParam("zalo") String zalo,
                               @RequestParam("dateDK") String dateDK,
                               @RequestParam("password") String password,
                               @RequestParam("avatar") MultipartFile avatar,
                               @RequestParam("address") String address,
                               @RequestParam(value = "userRoles", defaultValue = "USER") String role){
        userService.updateUser(userID, username, phone, zalo, password, address, dateDK, avatar, role);
        return "redirect:/table_generals";
    }



}
