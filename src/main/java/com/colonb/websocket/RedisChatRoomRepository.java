package com.colonb.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RedisChatRoomRepository {
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        opsHashChatRoom.delete(CHAT_ROOMS, "339f87eb-ca12-41e7-9fad-05d483eb66ea");
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    public void saveMessage(ChatMessage chatMessage) {
        // 1. DB 저장
//        Message message = new Message(chatMessage.getWriter(), chatMessage.getRoomId(), chatMessage.getMessage());
//        messageRepository.save(message);

        // 2. 직렬화
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        // 3. redis 저장
        redisTemplate.opsForList().rightPush(chatMessage.getRoomId(), chatMessage);

        // 4. expire 을 이용해서, Key 를 만료시킬 수 있음
        redisTemplate.expire(chatMessage.getRoomId(), 5, TimeUnit.MINUTES);
    }

    // 5. 대화 조회 - Redis & DB
    public List<ChatMessage> loadMessage(String roomId) {
        List<ChatMessage> messageList = new ArrayList<>();

        // Redis 에서 해당 채팅방의 메시지 100개 가져오기
        List<ChatMessage> redisMessageList = redisTemplate.opsForList().range(roomId, 0, 99);

        // 6. Redis 에서 가져온 메시지가 없다면, DB 에서 메시지 100개 가져오기
//        if (redisMessageList == null || redisMessageList.isEmpty()) {
//            List<ChatMessage> dbMessageList = messageRepository.findTop100ByRoomIdOrderByCreatedAtAsc(roomId);
//
//            for (ChatMessage message : dbMessageList) {
//                ChatMessage chatMessage = new ChatMessage(message);
//                messageList.add(chatMessage);
//                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Message.class));      // 직렬화
//                redisTemplateMessage.opsForList().rightPush(roomId, chatMessage);                                // redis 저장
//            }
//        } else {

        messageList.addAll(redisMessageList);
//        }

        return messageList;
    }
}
