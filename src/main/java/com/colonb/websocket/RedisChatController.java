package com.colonb.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class RedisChatController {
    private final RedisPublisher redisPublisher;
    private final RedisChatRoomRepository redisChatRoomRepository;

//    private final RedisMessageService redisMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            redisChatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getWriter() + "님이 입장하셨습니다!!!!!");
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(redisChatRoomRepository.getTopic(message.getRoomId()), message);

        // DB & Redis 에 대화 저장
        redisChatRoomRepository.saveMessage(message);
    }
}
