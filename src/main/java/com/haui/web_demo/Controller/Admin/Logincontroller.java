package com.haui.web_demo.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Logincontroller {
    @GetMapping("/login")
    public String Login(){
        return "/admin/login";
    }


}
