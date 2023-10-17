package com.colonb.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public String register(@RequestBody HashMap<String,String> param){
        return userService.register(param);
    }

    @PostMapping("login")
    public String login(@RequestBody HashMap<String,String> param, HttpSession session){
        return userService.login(param,session);
    }

}
