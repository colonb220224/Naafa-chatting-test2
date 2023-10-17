package com.colonb.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class MainController {

    @GetMapping("")
    public String index(HttpSession session, Model model){
        if (Objects.nonNull(session.getAttribute("username"))){
            model.addAttribute("username",session.getAttribute("username"));
        }
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @GetMapping("register")
    public String register(){
        return "register";
    }

}
