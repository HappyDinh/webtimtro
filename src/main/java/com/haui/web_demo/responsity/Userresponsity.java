package com.haui.web_demo.responsity;

import com.haui.web_demo.entities.Userobject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Userresponsity extends JpaRepository<Userobject, Long> {
    Userobject findByUsername(String username);
    Userobject findByPhone(String phone);

    @Override
    Page<Userobject> findAll(Pageable pageable);


    @Query("SELECT u FROM Userobject u JOIN u.userRoles ur JOIN ur.role r WHERE r.name = :roleName")
    List<Userobject> findUsersByRoleName(@Param("roleName") String roleName);

    // Tìm kiếm user theo tên
    Page<Userobject> findByUsernameContaining(String keyword, Pageable pageable);
}
