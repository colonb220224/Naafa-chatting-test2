package com.colonb.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

//    private final ChatRoomRepository repository;
    private final RedisChatRoomRepository redisChatRoomRepository;


    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){

        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("/rooms");

        mv.addObject("list", redisChatRoomRepository.findAllRoom());

        return mv;
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public String create(@RequestParam String name, RedirectAttributes rttr){

        log.info("# Create Chat Room , name: " + name);
        //rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));
        rttr.addFlashAttribute("roomName", redisChatRoomRepository.createChatRoom(name));
        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public ModelAndView getRoom(String roomId, ModelAndView mav){

        log.info("# get Chat Room, roomID : " + roomId);

        mav.addObject("room", redisChatRoomRepository.findRoomById(roomId));
        mav.addObject("message", redisChatRoomRepository.loadMessage(roomId));
        System.out.println("mav" + mav);
        mav.setViewName("/room");

        return mav;
    }

}
