package com.colonb.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public String register(HashMap<String, String> param) {
        String username = param.get("username");
        String password = param.get("password");
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return "이미 존재하는 아이디입니다.";
        }
        Users user = Users.builder().username(username).password(password).build();
        userRepository.save(user);
        return "true";
    }

    public String login(HashMap<String, String> param, HttpSession session) {
        String username = param.get("username");
        String password = param.get("password");
        Optional<Users> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return "잘못된 아이디 또는 패스워드입니다";
        }
        Users user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return "잘못된 아이디 또는 패스워드입니다";
        }
        session.setAttribute("username", username);
        return "true";
    }
}
