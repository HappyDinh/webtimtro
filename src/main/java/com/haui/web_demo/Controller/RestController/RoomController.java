package com.haui.web_demo.Controller.RestController;

import com.haui.web_demo.Service.RoomService;
import com.haui.web_demo.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép tất cả nguồn gửi request
public class RoomController {
//    @PostMapping("/receive_rooms")
//    public ResponseEntity<?> receiveRooms(@RequestBody Map<String, Object> data) {
//        System.out.println("Dữ liệu nhận từ Flask: " + data);
//
//        // Xử lý dữ liệu ở đây nếu cần, ví dụ lưu vào database
//
//        return ResponseEntity.ok(Map.of("message", "Dữ liệu đã nhận thành công!", "receivedData", data));
//    }
    @Autowired
    private RoomService roomService;

    private List<Product> room_result = new ArrayList<>();
    private String mes = null;
    private String sugg = null;


    @PostMapping("/receive_rooms")
    public String receiveRooms(@RequestBody Map<String, Object> flaskData, Model model) {
        // Trích xuất phần "flask_data" từ dữ liệu nhận được
        System.out.println("Dữ liệu nhận từ Flask: " + flaskData);

        // Lấy đối tượng flask_data
        Map<String, Object> flaskDataMap = (Map<String, Object>) flaskData.get("flask_data");

        List<Long> list_room_id = new ArrayList<>();
        if (flaskDataMap != null && flaskDataMap.get("List_room_id") instanceof List<?>) {
            for (Object id : (List<?>) flaskDataMap.get("List_room_id")) {
                if (id instanceof Number) {
                    list_room_id.add(((Number) id).longValue());
                }
            }
        }

        String message = flaskDataMap != null ? (String) flaskDataMap.get("message") : null;
        String suggestion = flaskDataMap != null ? (String) flaskDataMap.get("suggestion") : null;
        mes = message;
        sugg = suggestion;

        // Nếu danh sách ID hợp lệ thì lấy danh sách phòng trọ
        List<Product> rooms = (list_room_id != null && !list_room_id.isEmpty())
                ? roomService.getRoomsByIds(list_room_id)
                : List.of(); // Trả về danh sách rỗng nếu ID không hợp lệ


        room_result = rooms;

        // Truyền dữ liệu vào model để hiển thị trên HTML
        model.addAttribute("rooms_result", rooms);


        model.addAttribute("message", message);
        model.addAttribute("suggestion", suggestion);

        // Trả về trang kết quả
        return "user/result_fill";
    }


    @GetMapping("/receive_rooms")
    public String showRooms(Model model) {
        // Truyền dữ liệu đã lưu vào model để hiển thị trên HTML
        model.addAttribute("rooms_result", room_result);
        model.addAttribute("message", mes);
        model.addAttribute("suggestion", sugg);

        return "user/result_fill";
    }

}
