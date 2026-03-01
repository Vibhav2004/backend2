//package com.swipenow.swipenow.service;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class FcmService {
//
//    public boolean sendNotification(String token, String title, String body) {
//
//        if (token == null || token.isBlank()) {
//            log.warn("FCM token missing, notification skipped");
//            return false;
//        }
//
//        try {
//            Message message = Message.builder()
//                    .setToken(token)
//                    .setNotification(
//                            Notification.builder()
//                                    .setTitle(title)
//                                    .setBody(body)
//                                    .build()
//                    )
//                    .build();
//
//            String response = FirebaseMessaging.getInstance().send(message);
//
//            log.info("FCM sent successfully: {}", response);
//            return true;
//
//        } catch (Exception e) {
//            log.error("FCM send failed for token {}", token, e);
//            return false;
//        }
//    }
//}
