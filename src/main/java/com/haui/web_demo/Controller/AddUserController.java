package com.haui.web_demo.Controller;

import com.haui.web_demo.Controller.common.FileUploadController;
import com.haui.web_demo.Service.UserService;
import com.haui.web_demo.entities.Role;
import com.haui.web_demo.entities.User_Role;
import com.haui.web_demo.entities.Userobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AddUserController {

    @Autowired
    private UserService userService;


    @Autowired
    private FileUploadController fileUploadController;

    @PostMapping("/adduser")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("phone") String phone,
                          @RequestParam("zalo") String zalo,
                          @RequestParam("dateDK") String dateDK,
                          @RequestParam("password") String password,
                          @RequestParam("avatar") MultipartFile avatar,
                          @RequestParam("address") String address,
                          @RequestParam(value = "userRoles", defaultValue = "USER") String role,
                          Model model) {
        Date date = null;

        try {

            // Kiểm tra nếu dateDK không rỗng và tiến hành chuyển đổi
            if (dateDK != null && !dateDK.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(dateDK);  // Chuyển đổi chuỗi dateDK thành đối tượng Date
                } catch (ParseException e) {
                    model.addAttribute("error", "Lỗi khi chuyển đổi ngày tháng: " + e.getMessage());
                    return "redirect:/error"; // Trả về trang lỗi nếu gặp lỗi khi chuyển đổi ngày
                }
            }

            // Tìm vai trò từ RoleService
            Role userRole = userService.findByNameRole(role);  // Lấy đối tượng Role theo tên

            if (userRole == null) {
                userRole = userService.findByNameRole("USER");
            }


            // Tạo đối tượng Userobject từ các tham số đầu vào
            Userobject user = new Userobject();
            user.setUsername(username);
            user.setPhone(phone);
            user.setZalo(zalo);
            user.setDateDK(date);
            user.setPassword(password);
            user.setAddress(address);

            // Tạo User_Role và liên kết User với Role
            User_Role userRoleMapping = new User_Role();
            userRoleMapping.setUser(user);  // Liên kết người dùng
            userRoleMapping.setRole(userRole);  // Liên kết vai trò

            // Thêm User_Role vào danh sách userRoles của Userobject
            user.getUserRoles().add(userRoleMapping);

            // Upload avatar nếu có
            if (avatar != null && !avatar.isEmpty()) {
                String avatarFilename = fileUploadController.uploadFile(avatar);
                user.setAvatar(avatarFilename); // Đường dẫn tương đối
            }


            // Lưu người dùng
            userService.saveUser(user);
            model.addAttribute("message", "Người dùng được thêm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi thêm người dùng: " + e.getMessage());
        }


        // Lấy thông tin người dùng hiện tại từ Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra vai trò
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/table_generals"; // Chuyển hướng đến giao diện admin
        } else {
            return "redirect:/indexuser"; // Chuyển hướng đến giao diện user
        }
    }
}
