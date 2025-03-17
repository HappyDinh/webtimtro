package com.haui.web_demo.Controller.User;

import com.haui.web_demo.Controller.common.FileUploadController;
import com.haui.web_demo.Service.*;
import com.haui.web_demo.entities.*;
import com.haui.web_demo.responsity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller

public class Usercontroller {
    @Autowired
    private RoomService roomService;

    @Autowired
    private Roomresponsity roomresponsity;

    @Autowired
    private Blogresponsity blogresponsity;

    @Autowired
    private Partnerresponsity partnerresponsity;

    @Autowired
    private Userresponsity userresponsity;

    @Autowired
    private FileUploadController fileUploadController;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private Reviewresponsity reviewresponsity;


    @GetMapping("/indexuser")
    public String Indexuser(@RequestParam(value = "type", required = false, defaultValue = "all") String type, Model model, @Param("keyword") String keyword, @RequestParam(name = "pageno", defaultValue = "1") int pageno, @RequestParam(name = "limitsize", defaultValue = "9") int limitsize) {

//        Page<Product> productPage = roomService.getAllRooms(pageno, limitsize);
        Page<Product> productPage;

        if (type.equals("all")) {
            productPage = roomService.getAllRooms(pageno, limitsize);
        } else {
            productPage = roomService.getByType(type, pageno, limitsize);
        }

        // Thêm đối tượng Page đã lọc vào model để truyền cho view
        model.addAttribute("totalPage", productPage.getTotalPages());
        model.addAttribute("currentPage", pageno);
        model.addAttribute("productPage", productPage);
        model.addAttribute("productlist", productPage.getContent());
        model.addAttribute("selectedType", type); // Để xác định loại đang chọn trên giao diện

        List<Discounted> listDicount = roomService.getAllDiscounted();
        model.addAttribute("listdiscount", listDicount);

        return "user/indexuser";
    }


    @GetMapping("/menu")
    public String menu(Model model, @RequestParam(name = "pageno", defaultValue = "1") int pageno, @RequestParam(name = "limitsize", defaultValue = "4") int limitsize){
        Page<Product> menuPage = roomService.getAllRooms(pageno, limitsize);

        List<Product> lp = roomService.getAllRooms();

        List<Product> latestRooms = menuPage.stream()
                .skip(Math.max(0, menuPage.getSize() - 6))  // Lấy 5 sản phẩm cuối cùng
                .collect(Collectors.toList());

        // Gửi danh sách 6 sản phẩm này vào model
        model.addAttribute("latestRooms",latestRooms);


        // Thêm đối tượng Page đã lọc vào model để truyền cho view
        model.addAttribute("menutotalPage", menuPage.getTotalPages());
        model.addAttribute("menucurrentPage", pageno);
        model.addAttribute("menuPage", menuPage);
        model.addAttribute("menulist", menuPage.getContent());
        model.addAttribute("listsize", lp.size());

        List<Discounted> listDicount = roomService.getAllDiscounted();
        model.addAttribute("menulistdiscount", listDicount);

        return ("user/menu");
    }

    @GetMapping("/blog")
    public String blog(Model model){
        List<Blog> listblog = blogresponsity.findAll();

        List<Product> listproduct = roomService.getAllRooms();

        List<Product> lastThreeProducts = listproduct.subList(
                Math.max(listproduct.size() - 3, 0), listproduct.size()
        );

        // Gửi danh sách 3 sản phẩm này vào model
        model.addAttribute("lastThreeProducts", lastThreeProducts);

        model.addAttribute("listblog", listblog);
        return ("user/blog/blog");
    }

    @GetMapping("/addblog")
    public String addblog(Model model){
        Blog newblog = new Blog();
        model.addAttribute("newblog", newblog);
        return "user/blog/addblog";
    }

    @PostMapping("/addblog")
    public String addblog(@RequestParam(value = "blog_content") String Description, @RequestParam(name = "blog_name") String nameblog, @RequestParam(value = "blog_img")MultipartFile image_blog,
                            @AuthenticationPrincipal CustomerUserDetails customerUserDetails) throws IOException {

        Blog blog = new Blog();
        String url_blog = null;
        if(image_blog != null){
            url_blog = fileUploadController.uploadFile(image_blog);
        }
        Userobject user = userService.findByUsername(customerUserDetails.getUsername());
        if (user == null) {
            return "errorPage";
        }
        blog.setBlog_img(url_blog);
        blog.setUser(user);
        blog.setBlog_name(nameblog);
        blog.setBlog_content(Description);
        blogService.createBlog(blog);
        return "redirect:/blog";
    }


    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/authordeleteblog/{id}")
    public String deleteblog(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal CustomerUserDetails currentUser) {
        try {
            // Lấy thông tin phòng từ DB
            Blog blog = blogresponsity.findById(id).orElseThrow(() -> new RuntimeException("Not found product"));

            Long userID_current = blog.getUser().getUserID();
            Long userID_cus = currentUser.getUserID();
            // Kiểm tra nếu người dùng hiện tại KHÔNG PHẢI chủ sở hữu phòng
            if (userID_current != null && !userID_current.equals(userID_cus)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa bài đăng này!");
                return "redirect:/authorroom";
            }

            // Nếu là chủ sở hữu, tiến hành xóa
            blogService.deleteblog(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/authorroom";
    }
    



    @GetMapping("/review")
    public String review(Model model){
        List<Reviewroom> listreview = reviewresponsity.findAll();
        model.addAttribute("listreview", listreview);
        return "user/review/review";
    }

    @GetMapping("/addreview")
    public String addreview(Model model){
        Reviewroom newreview = new Reviewroom();
        model.addAttribute("newreview", newreview);
        return "user/review/addreview";
    }

    @PostMapping("/addreview")
    public String addreview(@RequestParam(value = "review_content") String review_content, @RequestParam(name = "review_name") String review_name, @RequestParam(value = "review_img")MultipartFile review_img,
                            @AuthenticationPrincipal CustomerUserDetails customerUserDetails) throws IOException {

        Reviewroom newreview = new Reviewroom();
        String url_review = null;
        if(review_img != null){
            url_review = fileUploadController.uploadFile(review_img);
        }
        Userobject user = userService.findByUsername(customerUserDetails.getUsername());
        if (user == null) {
            return "errorPage";
        }

        newreview.setUser(user);
        newreview.setReview_name(review_name);
        newreview.setReview_content(review_content);
        newreview.setReview_img(url_review);
        reviewService.savereview(newreview);
        return "redirect:/review";
    }

    @GetMapping("/partner")
    public String partner(Model model){
        List<Partner> listpartner = partnerresponsity.findAll();
        List<Blog> lgblog = blogresponsity.findAll();
        System.out.println("Có danh sach" + listpartner);
        model.addAttribute("listpartner", listpartner);
        model.addAttribute("blogs", lgblog);
        return "user/partner/partner";
    }

    @GetMapping("/addpartner")
    public String addpartner(Model model){
        Partner newpartner = new Partner();
        model.addAttribute("newpartner", newpartner);
        return "user/partner/addpartner";
    }

    @PostMapping("/addpartner")
    public String addpartner(@RequestParam(value = "partner_content") String Description, @RequestParam(name = "partner_name") String namepartner, @RequestParam(value = "partner_img")MultipartFile image_partner,
                             @RequestParam(value = "partner_acreage") Double partner_acreage, @RequestParam(value = "partner_price") Double partner_price, @RequestParam(name = "partner_address") String partner_address, @AuthenticationPrincipal CustomerUserDetails customerUserDetails) throws IOException {

        Partner partner = new Partner();
        String url_partner = null;
        if(image_partner != null){
            url_partner = fileUploadController.uploadFile(image_partner);
        }
        Userobject user = userService.findByUsername(customerUserDetails.getUsername());
        if (user == null) {
            return "errorPage";
        }

        partner.setPartner_name(namepartner);
        partner.setPartner_img(url_partner);
        partner.setPartner_price(partner_price);
        partner.setUser(user);
        partner.setPartner_content(Description);
        partner.setPartner_acreage(partner_acreage);
        partner.setPartner_address(partner_address);
        partnerService.savepartner(partner);
        return "redirect:/partner";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/deletereview/{id}")
    public String deletereview(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal CustomerUserDetails currentUser) {
        try {
            // Lấy thông tin phòng từ DB
            Reviewroom reviewroom = reviewresponsity.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

            Long userID_current = reviewroom.getUser().getUserID();
            Long userID_cus = currentUser.getUserID();
            // Kiểm tra nếu người dùng hiện tại KHÔNG PHẢI chủ sở hữu phòng
            if (userID_current != null && !userID_current.equals(userID_cus)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa bài đăng này!");
                return "redirect:/authorroom";
            }

            // Nếu là chủ sở hữu, tiến hành xóa
            reviewService.deletereview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/authorroom";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/deletepartner/{id}")
    public String deletepartner(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal CustomerUserDetails currentUser) {
        try {
            // Lấy thông tin phòng từ DB
            Partner partner = partnerresponsity.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

            Long userID_current = partner.getUser().getUserID();
            Long userID_cus = currentUser.getUserID();
            // Kiểm tra nếu người dùng hiện tại KHÔNG PHẢI chủ sở hữu phòng
            if (userID_current != null && !userID_current.equals(userID_cus)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa bài đăng này!");
                return "redirect:/authorroom";
            }

            // Nếu là chủ sở hữu, tiến hành xóa
            partnerService.deletepartner(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/authorroom";
    }




    @GetMapping("/audio")
    public String audio(Model model, @RequestParam(name = "pageno", defaultValue = "1") int pageno, @RequestParam(name = "limitsize", defaultValue = "4") int limitsize){
        Page<Product> list = roomService.getAllRooms(pageno, limitsize);
        List<Product> recentProducts = list.stream()
                .skip(Math.max(0, list.getSize() - 5))  // Lấy 5 sản phẩm cuối cùng
                .collect(Collectors.toList());

        // Gửi danh sách 5 sản phẩm này vào model
        model.addAttribute("recentProducts", recentProducts);

        Page<Product> audioPage = roomService.getAllRoomswithVideo(pageno, limitsize);

        // Thêm đối tượng Page đã lọc vào model để truyền cho view
        model.addAttribute("audiototalPage", audioPage.getTotalPages());
        model.addAttribute("audiocurrentPage", pageno);
        model.addAttribute("audioPage", audioPage);
        model.addAttribute("audiolist", audioPage.getContent());

        return "user/audio";
    }

    @GetMapping("/detailroom")
    public String detailroom(@RequestParam(value = "RoomID") Long roomid, Model model){
        if(roomid == null){
            return "redirect:/error";
        }
        Product room = roomresponsity.findById(roomid).orElseThrow(() -> new RuntimeException("Not found product"));
        List<String> item = Arrays.asList("Diện tích", "Khoảng cách", "Thể loại", "VSKK", "Điều hòa", "Nóng lạnh", "Chung chủ", "Wifi", "Thú cưng");
        List<String> value = Arrays.asList(String.valueOf(room.getAcreage()), String.valueOf(room.getDistance()), room.getType(), room.isClosed() ? "Có" : "Không", room.isAirconditioner() ? "Có" : "Không", room.isHotcold() ? "Có" : "Không", room.isCommonowner() ? "Có" : "Không", room.isWifi() ? "Có" : "Không", room.isPets() ? "Cho phép" : "Không cho phép");
        // Gộp thành Map
        Map<String, String> roomInfo = new LinkedHashMap<>();
        for (int i = 0; i < item.size(); i++) {
            roomInfo.put(item.get(i), value.get(i));
        }
        model.addAttribute("room", room);
        model.addAttribute("infors", roomInfo);
        return "user/shop_detail";
    }

    @GetMapping("/ownroom")
    public String ownroom(@RequestParam(value = "UserID") Long userID, Model model){
        if(userID == null){
            return "redirect:/error";
        }
        Userobject user = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        List<Product> listown = roomService.getRoomsByUserId(userID);
        List<Blog> listblog = blogService.getbyuser(userID);
        List<Partner> listpartner = partnerService.getByUser(userID);
        List<Reviewroom> listreview = reviewService.getReviewByUser(userID);

        model.addAttribute("user", user);
        model.addAttribute("listown", listown);
        model.addAttribute("listblog", listblog);
        model.addAttribute("listreview", listreview);
        model.addAttribute("listpartner", listpartner);

        return "user/ownroom";
    }

    @GetMapping("/about")
    public String about(){
        return "user/about";
    }

    @GetMapping("/loginuser")
    public String loginuser(){
        return "user/loginuser";
    }

    @GetMapping("/register")
    public String register(Model model){
        Userobject userobject = new Userobject();
        model.addAttribute("userobject", userobject);
        return "user/register";
    }

    @GetMapping("/addroom")
    public String Postroom(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return "user/addroom";
    }

    @GetMapping("/detail_blog")
    public String detail_blog(@RequestParam(value = "blogID") Long blogID, Model model){
        if(blogID == null){
            return "redirect:/error";
        }
        Blog blog = blogresponsity.findById(blogID).orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("blog", blog);
        return "/user/blog/detail";
    }


//    @PreAuthorize("hasRole('AUTHOR')")
//    @PostMapping("/authordeleteroom/{id}")
//    public String deleteRoom(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
//        try {
//            roomService.deleteRoom(id);
//            redirectAttributes.addFlashAttribute("successMessage", "Xóa  thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa : " + e.getMessage());
//        }
//        return "redirect:/ownroom";
//    }
    @GetMapping("/authorroom")
    public String author(Model model, @AuthenticationPrincipal CustomerUserDetails User){
        Long userID = User.getUserID();
        if(userID == null){
            return "redirect:/error";
        }
        Userobject user = userresponsity.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
        List<Product> listown = roomService.getRoomsByUserId(userID);
        List<Blog> listblog = blogService.getbyuser(userID);
        List<Partner> listpartner = partnerService.getByUser(userID);
        List<Reviewroom> listreview = reviewService.getReviewByUser(userID);

        model.addAttribute("user", user);
        model.addAttribute("listown", listown);
        model.addAttribute("listblog", listblog);
        model.addAttribute("listreview", listreview);
        model.addAttribute("listpartner", listpartner);

        return "user/authorroom";
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/authordeleteroom/{id}")
    public String deleteRoom(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal CustomerUserDetails currentUser) {
        try {
            // Lấy thông tin phòng từ DB
            Product room = roomresponsity.findById(id).orElseThrow(() -> new RuntimeException("Not found product"));

            Long userID_current = room.getUser().getUserID();
            Long userID_cus = currentUser.getUserID();
            // Kiểm tra nếu người dùng hiện tại KHÔNG PHẢI chủ sở hữu phòng
            if (userID_current != null && !userID_current.equals(userID_cus)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa bài đăng này!");
                return "redirect:/authorroom";
            }

            // Nếu là chủ sở hữu, tiến hành xóa
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }

        return "redirect:/authorroom";
    }


    @GetMapping("/search")
    public String searchRooms(@RequestParam(value = "area", required = false) String area, Model model) {
        List<Product> rooms = roomService.findByArea(area);
        String mes = null;
        String sug = null;
        model.addAttribute("rooms_result", rooms);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sug);
        return "user/result_fill"; // Trang hiển thị kết quả
    }

    // Lọc theo giá
    @GetMapping("/filter_price")
    public String filterByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice, Model model) {
        List<Product> rooms = roomresponsity.findByPriceRange(minPrice, maxPrice);
        String mes = null;
        String sug = null;
        model.addAttribute("rooms_result", rooms);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sug);
        return "user/result_fill"; // Trả về trang hiển thị danh sách phòng
    }

    // Lọc theo diện tích
    @GetMapping("/filter_acreage")
    public String filterByAcreage(@RequestParam Double minAcreage, @RequestParam Double maxAcreage, Model model) {
        List<Product> rooms = roomresponsity.findByAcreageRange(minAcreage, maxAcreage);
        String mes = null;
        String sug = null;
        model.addAttribute("rooms_result", rooms);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sug);
        return "user/result_fill";
    }

    // Lọc theo thể loại
    @GetMapping("/filter_type")
    public String filterByType(@RequestParam(required = false) String type, Model model) {
        List<Product> rooms = roomresponsity.findByType(type);
        String mes = null;
        String sug = null;
        model.addAttribute("rooms_result", rooms);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sug);
        return "user/result_fill";
    }

    // Lọc theo add
    @GetMapping("/filter_add")
    public String findByArea(@RequestParam(required = false) String area, Model model) {
        List<Product> rooms = roomresponsity.findByArea(area);
        String mes = null;
        String sug = null;
        model.addAttribute("rooms_result", rooms);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sug);
        return "user/result_fill";
    }
}
