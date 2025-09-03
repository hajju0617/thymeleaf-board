package com.project.thymeleafboard;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @GetMapping("/test")
    @ResponseBody
    public String home() {
        return "서버 테스트용";
    }

    // 루트 URL 용.
    @GetMapping("/")
    public String root() {
        return "redirect:/article/list";
    }

    @GetMapping("/**")
    public String handleUnknownUrls() {
        return "redirect:/article/list";
    }
}
