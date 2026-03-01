//package com.swipenow.swipenow.scheduler;
//
//import com.swipenow.swipenow.entity.User;
//import com.swipenow.swipenow.repository.UserRepo;
//import com.swipenow.swipenow.service.FcmService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class NotificationScheduler {
//
//    private final UserRepo repo;
//    private final FcmService fcm;
//
//    // 🔔 1️⃣ Not swiped for 1 minute (testing)
//    @Scheduled(cron = "0 * * * * *") // every minute
//    public void inactiveUsers() {
//
//        LocalDateTime limit = LocalDateTime.now().minusMinutes(1);
//
//        List<User> users = repo.findByLastSwipeAtBeforeAndFcmTokenIsNotNull(limit);
//
//        users.forEach(u ->
//                fcm.sendNotification(
//                        u.getFcmToken(),
//                        "We miss you 👀",
//                        "You haven’t swiped in a while!"
//                )
//        );
//    }
//
//    // 🔔 2️⃣ Midnight quota reset
//    @Scheduled(cron = "0 0 0 * * *")
//    public void dailyReset() {
//
//        List<User> users = repo.findByFcmTokenIsNotNull();
//
//        users.forEach(u -> {
//            u.setSwipes(0);
//            u.setLastQuotaReset(LocalDate.now());
//            repo.save(u);
//
//            fcm.sendNotification(
//                    u.getFcmToken(),
//                    "Fresh memes are live 🔥",
//                    "Your daily quota has been reset!"
//            );
//        });
//    }
//}
