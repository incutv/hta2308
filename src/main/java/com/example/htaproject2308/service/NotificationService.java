package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Notification;
import com.example.htaproject2308.mapper.NotificationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NotificationMapper notificationMapper;

    @Transactional
    public void sendNotification(Long receiverId) {
        // 알림정보를 디비에 저장
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setMessage("New notification message");
        notification.setRead(false);

        //notificationMapper.saveNotification(notification);

        publishNotification(notification);
    }

    // Redis에 메세지 발행
    private void publishNotification(Notification notification) {
        redisTemplate.convertAndSend("notificationTopic", notification);
    }

    private void sendRealTimeNotification(Notification notification) {
        SseEmitter emitter = userEmitters.get(notification.getReceiverId());
        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(notification.getMessage()));
                } catch (Exception e) {
                    // 예외 처리
                }
            });
        }
    }

    //만약 연결 요청이 오면 새로운 SseEmitter 객체를 만들고 (이 때, 생성자의 파라미터에는 SseEmitter의 유효시간이 들어갑니다)
    //그리고 해당 사용자에 맞는 emitter를 map에 저장하고, 연결이 끝나거나 / 만료되거나 / 에러가 발생했을 때에는 해당 맵에서 삭제합니다.
    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(userId, emitter);

        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError((e) -> userEmitters.remove(userId));

        return emitter;
    }


    // Redis에서 메세지를 받아오는 부분
    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener((message, pattern) -> {
            String body = redisTemplate.getStringSerializer().deserialize(message.getBody());
            Notification notification = null;
            try {
                notification = objectMapper.readValue(body, Notification.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            assert notification != null;

            sendRealTimeNotification(notification);
        }, Collections.singleton(new PatternTopic("notificationTopic")));

        return container;
    }
}
