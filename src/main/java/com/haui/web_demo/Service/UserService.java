package com.haui.web_demo.Service;

import com.haui.web_demo.entities.Role;
import com.haui.web_demo.entities.Userobject;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Userobject findByUsername(String username);

    Userobject findByPhone(String phone);

    List<Userobject> getAllUsers();

    Optional<Userobject> GetUserByID(Long id);

    Userobject saveUser(Userobject user);

    void deleteUser(Long id);

    public Page<Userobject> getUsers(String keyword, int page, int size);

    public Userobject updateUser(Long id, String name, String phone, String zalo, String password, String address, String dateDK, MultipartFile avatar, String role);

    Page<Userobject> getPaginatedData(int page, int size);

    List<Userobject> getUsersByRole(String role);

    public Role findByNameRole(String roleName);


}
