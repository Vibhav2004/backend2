package com.swipenow.swipenow.controller;
import org.springframework.util.DigestUtils;

import com.swipenow.swipenow.DTO.UserStatsDTO;
import com.swipenow.swipenow.entity.Friends;
import com.swipenow.swipenow.entity.Meme;
import com.swipenow.swipenow.entity.User;
import com.swipenow.swipenow.repository.MemeRepo;
import com.swipenow.swipenow.repository.UserRepo;
import com.swipenow.swipenow.service.*;
//import com.swipenow.swipenow.service.friendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@RestController
public class endpoints {
    @Autowired
    private UserService userService;
    @Autowired
    private friendService friendService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;

    private final Map<String, Integer> counter = new ConcurrentHashMap<>();
    private LocalDate today = LocalDate.now();

    @Autowired
    private MemeService memeService;



    private final UserRepo repo;

    public endpoints(UserRepo repo, SupaBaseService supabaseService) {
        this.repo = repo;
        this.supabaseService = supabaseService;
    }
    


     @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        User user= userService.getUser(username);
        return ResponseEntity.ok(user);
    }


   @PostMapping("/update")
   public ResponseEntity<User> updateUser( @RequestBody User user){
       User users= userService.updateUser(user);
        return ResponseEntity.ok(users);
   }

   @PostMapping("/Register-User")
    public User registerUser(@RequestBody User user) {

        return userService.registerUser(user);
   }



   @PostMapping("/Login-User")
    public User loginUser(@RequestBody User user) {

        return userService.loginUser(user);
   }

   @GetMapping("/All-User")
     public List<UserStatsDTO> getLeaderboard() {
        return userService.getLeaderboard();
    }

    @PostMapping("/add-friends")
    public ResponseEntity<Friends> addFriends(@RequestBody Friends dto) {
        Friends saved = friendService.addFriends(dto);
        return ResponseEntity.ok(saved);
    }

//    @PostMapping("/remove-friends")
//    public ResponseEntity<Friends> removeFriends(@RequestBody Friends dto) {
//        Friends saved = friendService.removeFriends(dto);
//        return ResponseEntity.ok(saved);
//    }

    @GetMapping("/friends/check")
    public ResponseEntity<Boolean> checkFriendship(
            @RequestParam String username1,
            @RequestParam String username2) {
        boolean exists = friendService.areFriends(username1, username2);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/view_friends/{username}")
    public ResponseEntity<List<Friends>> getAllFriends(@PathVariable String username) {
        return ResponseEntity.ok(friendService.getAllFriends(username));
    }


    @PostMapping("/profile-pic")
    public User setProfilePic(@RequestBody User user){
        return userService.setProfilePic(user);
    }

    @GetMapping("/Get-profile-pic")
    public String getProfilePic(@RequestParam User user){
        return userService.getProfilePic(user);
    }


    // @PostMapping("/send")
    // public String sendOtp(@RequestParam String email) {
    //     String otp = otpService.generateOtp(email);

    //     emailService.sendOtpEmail(email, otp);
    //     return "OTP sent successfully to " + email;
    // }

  // @PostMapping("/verify")
    // public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
    //     boolean isValid = otpService.verifyOtp(email, otp);
    //     if (!LocalDate.now().equals(today)) {
    //         today = LocalDate.now();
    //         counter.clear();
    //     }
    //     String key = email;
    //     counter.merge(key, 1, Integer::sum);

    //     if (counter.get(key) > 2) {
    //         throw new ResponseStatusException(
    //                 HttpStatus.TOO_MANY_REQUESTS,
    //                 "Daily limit reached"
    //         );
    //     }
    //     if (isValid) {
    //         return "OTP verified successfully!";
    //     } else {
    //         return "Invalid or expired OTP!";
    //     }
    // }

  


//    @PostMapping("/update-email")
//    public ResponseEntity<User> updateEmail(@RequestParam User user) {
//        User users= userService.updateEmail(user);
//        return ResponseEntity.ok(users);
//    }

//    @PostMapping("/update-username")
//    public ResponseEntity<User> updateUsername(@RequestBody User user) {
//        User updatedUser = userService.updateUsername(user);
//        return ResponseEntity.ok(updatedUser);
//    }


//     @PostMapping("/update-password")
//     public ResponseEntity<User> updatePassword(@RequestBody User user) {
//         User updatedUser = userService.updatePassword(user);
//         if (!LocalDate.now().equals(today)) {
//             today = LocalDate.now();
//             counter.clear();
//         }
//         String key = user.getUsername();
//         counter.merge(key, 1, Integer::sum);

//         if (counter.get(key) > 2) {
//             throw new ResponseStatusException(
//                     HttpStatus.TOO_MANY_REQUESTS,
//                     "Daily limit reached"
//             );
//         }


//         return ResponseEntity.ok(updatedUser);
//     }
// @PostMapping("/verify")
// public ResponseEntity<String> verifyCode(@RequestBody String code) {

//     String result = userService.verifyCode(code);

//     return ResponseEntity.ok(result);
// }

// @PostMapping("/verify")
// public ResponseEntity<String> verifyCode(@RequestBody String code) {

//     boolean valid = userService.verifyCode(code);

//     if(valid){
//         return ResponseEntity.ok("OK");
//     }

//     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Code");
// }

@PostMapping("/verify")
public ResponseEntity<String> verifyCode(@RequestParam String code) {

    boolean valid = userService.verifyCode(code);

    if(valid){
        return ResponseEntity.ok("OK");
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Code");
}


//    @PostMapping("/fcm-token")
//    public ResponseEntity<String> saveToken(@RequestBody Map<String, String> body) {
//
//        String token = body.get("token");
//
//        if (token == null || token.isBlank()) {
//            return ResponseEntity.badRequest().body("FCM token missing");
//        }
//
//        Long userId = 1L; // TODO: replace with authenticated user
//        User user = repo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setFcmToken(token);
//        repo.save(user);
//
//        return ResponseEntity.ok("FCM token saved");
//    }
//
//    // ===============================
//    // RECORD SWIPE
//    // ===============================
//    @PostMapping("/swipe")
//    public ResponseEntity<String> swipe() {
//
//        Long userId = 1L; // TODO: replace with authenticated user
//        User user = repo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setLastSwipeAt(LocalDateTime.now());
//
//        Integer swipes = user.getSwipes();
//        user.setSwipes(swipes == null ? 1 : swipes + 1);
//
//        repo.save(user);
//
//        return ResponseEntity.ok("Swipe recorded");
//    }



    private final SupaBaseService supabaseService;



    @PostMapping("/upload")
    public ResponseEntity<?> uploadMeme(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("postedBy") String postedBy
    ) {
        try {
            Meme savedMeme = supabaseService.uploadMeme(file, title, postedBy);
            return ResponseEntity.ok(savedMeme); // return JSON of the meme
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Upload failed: " + e.getMessage() + "\"}");
        }
    }




    @GetMapping("/urls/{postedBy}")
    public ResponseEntity<List<String>> getMemeUrls(@PathVariable String postedBy) {
        List<String> urls = supabaseService.getMemeUrls(postedBy);
        return ResponseEntity.ok(urls);
    }


    @GetMapping("/daily-memes")
    public ResponseEntity<List<String>> getMemes(@RequestParam String username) throws Exception {
        // fetch data
        List<String> memes = memeService.getAllMemes(username);

        // generate ETag hash
        String content = memes.toString();
        String etag = DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .eTag(etag)
                .cacheControl(
                        CacheControl.maxAge(1, TimeUnit.HOURS) // change if needed
                                .cachePublic()
                )
                .body(memes);
    }
//    @GetMapping("/daily-memes")
//    public ResponseEntity<List<String>> getAllMemes() {
//        List<String> urls = memeService.getAllMemes();
//        return ResponseEntity.ok(urls);
//    }
//@GetMapping("/daily-memes")
//public ResponseEntity<List<String>> getAllMemes(@RequestParam String username) {
//    return ResponseEntity.ok(memeService.getAllMemes(username));
//}



    // @DeleteMapping("/delete-account")
    // public ResponseEntity<String> deleteAccount(@RequestParam String email) {
    //     return ResponseEntity.ok(userService.deleteAccount(email));
    // }
  @DeleteMapping("/delete-account/{email}")
public ResponseEntity<String> deleteAccount(@PathVariable String email) {

    return ResponseEntity.ok(userService.deleteAccount(email));
}

    @GetMapping("/guest-memes")
    public ResponseEntity<List<String>> getGuestMemes() {
        List<String> urls = memeService.getGuestMemes();
        return ResponseEntity.ok(urls);
    }

}
