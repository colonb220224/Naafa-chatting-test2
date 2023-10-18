package com.colonb.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String writer; // 메시지 보낸사람
    private String message; // 메시지

    public ChatMessage() {}

    public ChatMessage(MessageType type, String roomId, String writer, String message) {
        this.type = type;
        this.roomId = roomId;
        this.writer = writer;
        this.message = message;
    }
}