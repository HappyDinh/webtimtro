package com.haui.web_demo.Service;

import com.haui.web_demo.Controller.common.FileUploadController;
import com.haui.web_demo.entities.Role;
import com.haui.web_demo.entities.User_Role;
import com.haui.web_demo.entities.Userobject;
import com.haui.web_demo.responsity.RoleRepository;
import com.haui.web_demo.responsity.Userresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private Userresponsity userresponsity;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FileUploadController fileUploadController;

    @Override
    public Userobject findByUsername(String username) {
        return userresponsity.findByUsername(username);
    }

    @Override
    public Userobject findByPhone(String phone){
        return userresponsity.findByPhone(phone);
    }

    @Override
    public List<Userobject> getAllUsers() {
        return userresponsity.findAll();
    }

    @Override
    public Optional<Userobject> GetUserByID(Long id) {
        return userresponsity.findById(id);
    }

    @Override
    public Userobject saveUser(Userobject user) {
        return userresponsity.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userresponsity.deleteById(id);
    }

    public Page<Userobject> getUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        return userresponsity.findByUsernameContaining(keyword, pageable);
    }

    @Override
    public Userobject updateUser(Long id, String name, String phone, String zalo, String password, String address, String dateDK, MultipartFile avatar, String role) {
        Userobject userobject = GetUserByID(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userobject.setUsername(name);
        userobject.setPhone(phone);
        userobject.setZalo(zalo);
        userobject.setPassword(password);
        userobject.setAddress(address);

        Date date = null;
        // Kiểm tra nếu dateDK không rỗng và tiến hành chuyển đổi
        if (dateDK != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = dateFormat.parse(dateDK);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Tìm vai trò từ RoleService
        Role userRole = roleRepository.findByName(role);  // Lấy đối tượng Role theo tên

        if (userRole == null) {
            userRole = roleRepository.findByName("USER"); // Nếu không tìm thấy, gán mặc định là "USER"
        }

        // Tạo User_Role và liên kết User với Role
        User_Role userRoleMapping = new User_Role();
        userRoleMapping.setUser(userobject);  // Liên kết người dùng
        userRoleMapping.setRole(userRole);    // Liên kết vai trò

        // Thêm User_Role vào danh sách userRoles của Userobject
        userobject.getUserRoles().clear();  // Xóa các UserRole cũ nếu có
        userobject.getUserRoles().add(userRoleMapping);  // Thêm vai trò mới

        // Xử lý avatar nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                String avatarFilename = fileUploadController.uploadFile(avatar); // Phương thức uploadFile trong fileUploadController
                userobject.setAvatar(avatarFilename); // Lưu tên file vào avatar
            } catch (IOException e) {
                e.printStackTrace(); // Hoặc có thể log lỗi tùy theo yêu cầu
                throw new RuntimeException("Failed to upload avatar file", e);
            }
        }

        return userresponsity.save(userobject);
    }

    public Page<Userobject> getPaginatedData(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return userresponsity.findAll(pageable);
    }

    public List<Userobject> getUsersByRole(String roleName) {
        return userresponsity.findUsersByRoleName(roleName);
    }

    @Override
    public Role findByNameRole(String roleName) {
        return roleRepository.findByName(roleName);
    }


}
