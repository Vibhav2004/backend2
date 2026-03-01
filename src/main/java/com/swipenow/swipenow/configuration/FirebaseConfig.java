//package com.swipenow.swipenow.configuration;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.InputStream;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void init() {
//        try {
//            InputStream serviceAccount =
//                    getClass()
//                            .getClassLoader()
//                            .getResourceAsStream("firebase-service-account.json");
//
//            if (serviceAccount == null) {
//                throw new RuntimeException(
//                        "❌ firebase-service-account.json not found in resources folder");
//            }
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    // optional but recommended
//                    // .setProjectId("swipe-now")
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                System.out.println("✅ Firebase initialized successfully");
//            }
//
//        } catch (Exception e) {
//            System.err.println("🔥 Firebase initialization failed");
//            throw new RuntimeException(e); // FAIL FAST
//        }
//    }
//}
