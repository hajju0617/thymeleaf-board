package com.project.thymeleafboard;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/test")
    @ResponseBody
    public String home() {
        return "서버 테스트용";
    }
}
