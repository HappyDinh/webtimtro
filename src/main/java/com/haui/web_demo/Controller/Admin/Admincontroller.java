package com.haui.web_demo.Controller.Admin;

import com.haui.web_demo.Service.*;
import com.haui.web_demo.entities.*;
import com.haui.web_demo.responsity.Blogresponsity;
import com.haui.web_demo.responsity.Discountresponsity;
import com.haui.web_demo.responsity.Partnerresponsity;
import com.haui.web_demo.responsity.Reviewresponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller

public class Admincontroller {
    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private Blogresponsity blogresponsity;

    @Autowired
    private Partnerresponsity partnerresponsity;

    @Autowired
    private Discountresponsity discountresponsity;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private Reviewresponsity reviewresponsity;

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Product> listproduct = roomService.getAllRooms();
        List<Blog> listblog = blogresponsity.findAll();
        List<Userobject> listuser = userService.getAllUsers();
        List<Discounted> listdis = discountresponsity.findAll();
        List<Product> lastProducts = listproduct.subList(
                Math.max(listproduct.size() - 5, 0), listproduct.size()
        );

        List<Blog> lastblog = listblog.subList(
                Math.max(listblog.size() - 5, 0), listblog.size()
        );

        model.addAttribute("sizeuser", listuser.size());
        model.addAttribute("sizedis", listdis.size());
        model.addAttribute("sizeroom", listproduct.size());
        model.addAttribute("lastprduct", lastProducts);
        model.addAttribute("lastblog", lastblog);
        return "admin/index";
    }


    @GetMapping("/table_generals")
    public String Showgeneralstable(Model model){
        List<Userobject> userobjects = userService.getAllUsers();
        if (userobjects == null || userobjects.isEmpty()) {
            System.out.println("Danh sách người dùng trống hoặc null.");
        } else {
            System.out.println("Danh sách người dùng có " + userobjects.size() + " người.");
        }
        model.addAttribute("userList", userobjects);
        return "admin/table_generals";
    }



    @GetMapping("/table_data")
    public String Showproduct(Model model){
        List<Product> products = roomService.getAllRooms();
        if (products == null || products.isEmpty()) {
            System.out.println("Danh sách phòng trọ trống hoặc null.");
        } else {
            System.out.println("Danh sách phòng trọ có " + products.size() + " phòng.");
        }
        model.addAttribute("products", products);
        return "admin/table_data";
    }

    @GetMapping("/table_blog")
    public String Table_blog(Model model){
        List<Blog> blogList = blogresponsity.findAll();
        model.addAttribute("listblog", blogList);
        return "admin/table_blog";
    }

    @GetMapping("/table_partner")
    public String tabe_partner(Model model){
        List<Partner> listpartner = partnerresponsity.findAll();
        model.addAttribute("list", listpartner);
        return "admin/table_partner";
    }

    @GetMapping("/table_review")
    public String tabe_review(Model model){
        List<Reviewroom> list = reviewresponsity.findAll();
        model.addAttribute("list", list);
        return "admin/table_review";
    }

    @GetMapping("/table_discount")
    public String Discount(Model model){
        List<Discounted> listdiscount = discountresponsity.findAll();
        model.addAttribute("listdiscount", listdiscount);
        return "admin/table_discount";
    }

    // them nguoi dung
    @GetMapping("/adduser")
    public String showAddForm(Model model){
        Userobject user = new Userobject();
        model.addAttribute("user", user);
        return "/admin/adduser";
    }

    //Xóa người dùng
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteuser/{userID}")
    public String deleteUser(@PathVariable("userID") Long userID, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userID);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa người dùng: " + e.getMessage());
        }
        return "redirect:/table_generals";
    }


    //Xóa phòng trọ
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteroom/{id}")
    public String deleteRoom(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
        }
        return "redirect:/table_data";
    }

    //Xóa blog
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteblog_admin/{id}")
    public String deleteBlog(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deleteblog(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
        }
        return "redirect:/table_blog";
    }

    //Xóa review
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletereview_admin/{id}")
    public String deleteReview(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deletereview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
        }
        return "redirect:/table_review";
    }

    //Xóa partner
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletepartner_admin/{id}")
    public String deletepartner(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            partnerService.deletepartner(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
        }
        return "redirect:/table_partner";
    }

    //Xóa discount
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deletediscount_admin/{id}")
    public String deletediscount(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            discountService.detelediscount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
        }
        return "redirect:/table_discount";
    }

}

