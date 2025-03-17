package com.haui.web_demo.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user")
public class Userobject {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long userID;

 @Column(name = "username")
 private String username;


 @Column(name = "phone")
 private String phone;

 @Column(name = "zalo")
 private String zalo;

 @Column(name = "dateDK")
 @DateTimeFormat(pattern = "yyyy-MM-dd")
 @Temporal(TemporalType.DATE)
 private Date dateDK;

 @Column(name = "password", nullable = false)
 private String password;


 @Column(name = "avatar")
 private String avatar;


 @Column(name = "address")
 private String address;

 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
 private Set<User_Role> userRoles;

 // Trường này dùng trong quá trình upload, không lưu vào database
 @Transient
 private MultipartFile avatarFile;

 // Getters và Setters
 //public String getAvatarPath() {
//  return "/img/" + avatar; // Tạo đường dẫn tương đối cho ảnh
// }

 public Userobject() {
  super();
  this.userRoles = new HashSet<>(); // Khởi tạo Set để tránh NullPointerException
 }

 public Userobject(long userID, String username, String phone, String zalo, Date dateDK, String password, String avatar, String address) {
  this.userID = userID;
  this.username = username;
  this.phone = phone;
  this.zalo = zalo;
  this.dateDK = dateDK;
  this.password = password;

  this.avatar = avatar;
  this.address = address;
 }



 // Getter và Setter cho userID
 public long getUserID() {
  return userID;
 }

 public void setUserID(long userID) {
  this.userID = userID;
 }

 // Getter và Setter cho username
 public String getUsername() {
  return username;
 }

 public void setUsername(String username) {
  this.username = username;
 }


 // Getter và Setter cho phone
 public String getPhone() {
  return phone;
 }

 public void setPhone(String phone) {
  this.phone = phone;
 }

 // Getter và Setter cho zalo
 public String getZalo() {
  return zalo;
 }

 public void setZalo(String zalo) {
  this.zalo = zalo;
 }

 // Getter và Setter cho dateDK
 public Date getDateDK() {
  return dateDK;
 }

 public void setDateDK(Date dateDK) {
  this.dateDK = dateDK;
 }

 // Getter và Setter cho password
 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }


 // Getter và Setter cho avatar
 public String getAvatar() {
  return avatar;
 }

 public void setAvatar(String avatar) {
  this.avatar = avatar;
 }

 // Getter và Setter cho address
 public String getAddress() {
  return address;
 }

 public void setAddress(String address) {
  this.address = address;
 }

 public Set<User_Role> getUserRoles() {
  if (userRoles == null) {
   userRoles = new HashSet<>(); // Khởi tạo nếu null
  }
  return userRoles;
 }

 public void setUserRoles(Set<User_Role> userRoles) {
  this.userRoles = userRoles;
 }

 @Override
 public String toString() {
  return "Userobject{" +
          "userID=" + userID +
          ", username='" + username + '\'' +
          ", phone='" + phone + '\'' +
          ", zalo='" + zalo + '\'' +
          ", dateDK=" + dateDK +
          ", password='" + password + '\'' +
          ", avatar='" + avatar + '\'' +
          ", address='" + address + '\'' +
          ", userRoles=" + userRoles +
          '}';
 }

 public boolean hasRole(String roleName) {
  for (User_Role userRole : this.userRoles) {
   if (userRole.getRole().getName().equals(roleName)) {
    return true;
   }
  }
  return false;
 }
}
