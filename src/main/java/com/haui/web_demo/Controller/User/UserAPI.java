//package com.haui.web_demo.Controller.User;
//
//import com.haui.web_demo.entities.Userobject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/users")
//public class UserAPI {
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public List<Userobject> getAllUsers(){
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<Userobject> getUserByID(@PathVariable Long id){
//        return userService.getUserByID(id);
//    }
//
//    @PostMapping
//    public Userobject createUser(@RequestBody Userobject user){
//        return userService.saveUser(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Long id){
//        userService.deleteUserByID(id);
//    }
//}
