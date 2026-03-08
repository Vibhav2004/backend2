package com.swipenow.swipenow.service;

import java.time.LocalDate;
import java.util.List;

import com.swipenow.swipenow.DTO.UserStatsDTO;
import com.swipenow.swipenow.entity.DeletedUsers;
import com.swipenow.swipenow.entity.Friends;
import com.swipenow.swipenow.entity.User;
import com.swipenow.swipenow.repository.DeleteRepo;
import com.swipenow.swipenow.repository.UserRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  UserRepo userRepo;
  @Autowired
    DeleteRepo deleteRepo;
    public User registerUser(User user) {
        user.setMemes(0);
        user.setBatchNumber(0);
//        user.setTermsAndCondition(true);
       return userRepo.save(user);
    }

    public User loginUser(User user) {
        User existingUser = userRepo.getByEmail(user.getEmail());

        if (existingUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password"); // you can make a custom exception
        }

        return existingUser;
    }


    public User getUser(String username) {
        User usersExist=userRepo.findByUsername(username);
        if(usersExist==null){
            throw new RuntimeException("Username Does Not Exist");
        }
        return usersExist;
    }

//    public User updateUser(User incoming) {
//
//        User existing = userRepo.findByUsername(incoming.getUsername());
//
//        if (existing == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        // =========================
//        // NULL SAFETY
//        // =========================
//        int existingSwipes = existing.getSwipes() == null ? 0 : existing.getSwipes();
//        int existingScore  = existing.getScore()  == null ? 0 : existing.getScore();
//        Integer existingStreakObj = existing.getStreak();
//
//        int incomingSwipes = incoming.getSwipes() == null ? 0 : incoming.getSwipes();
//        int incomingStreak = incoming.getStreak() == null ? 0 : incoming.getStreak();
//        int incomingScore  = incoming.getScore()  == null ? 0 : incoming.getScore();
//
//        // =========================
//        // SWIPES
//        // =========================
//        existing.setSwipes(existingSwipes + incomingSwipes);
//
//        // =========================
//        // STREAK (SERVER-TRUSTED)
//        // =========================
//        LocalDate today = LocalDate.now();
//        LocalDate lastIncrement = existing.getLastStreakDate();
//
//        boolean alreadyIncrementedToday =
//                lastIncrement != null && lastIncrement.isEqual(today);
//
//        if (incomingStreak > 0 && !alreadyIncrementedToday) {
//
//            if (existingStreakObj == null || existingStreakObj == 0) {
//                existing.setStreak(1);
//            } else {
//                existing.setStreak(existingStreakObj + 1); // ALWAYS +1 (ignore frontend value)
//            }
//
//            existing.setLastStreakDate(today);
//        }
//        // else: do nothing (frontend cannot force increment)
//
//        // =========================
//        // SCORE
//        // =========================
//        existing.setScore(existingScore + incomingScore);
//
//        return userRepo.save(existing);
//    }
//
//
     public List<UserStatsDTO> getLeaderboard() {
        return userRepo.getLeaderboard();
    }


//    public User updateUser(User incoming) {
//
//        User existing = userRepo.findByUsername(incoming.getUsername());
//
//        if (existing == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        // =========================
//        // NULL SAFETY
//        // =========================
//        int existingSwipes = existing.getSwipes() == null ? 0 : existing.getSwipes();
//        int existingScore  = existing.getScore()  == null ? 0 : existing.getScore();
//        Integer existingStreakObj = existing.getStreak();
//
//        int incomingSwipes = incoming.getSwipes() == null ? 0 : incoming.getSwipes();
//        int incomingStreak = incoming.getStreak() == null ? 0 : incoming.getStreak();
//        int incomingScore  = incoming.getScore()  == null ? 0 : incoming.getScore();
//
//        // =========================
//        // SWIPES
//        // =========================
//        existing.setSwipes(existingSwipes + incomingSwipes);
//
//        // =========================
//        // STREAK RESET CHECK (🔥 NEW)
//        // =========================
//        LocalDate today = LocalDate.now();
//        LocalDate lastIncrement = existing.getLastStreakDate();
//
//        if (lastIncrement != null && lastIncrement.isBefore(today.minusDays(1))) {
//            // Missed at least one full day → reset streak
//            existing.setStreak(0);
//            existingStreakObj = 0; // keep local variable in sync
//        }
//
//        // =========================
//        // STREAK (SERVER-TRUSTED)
//        // =========================
//        boolean alreadyIncrementedToday =
//                lastIncrement != null && lastIncrement.isEqual(today);
//
//        if (incomingStreak > 0 && !alreadyIncrementedToday) {
//
//            if (existingStreakObj == null || existingStreakObj == 0) {
//                existing.setStreak(1);
//            } else {
//                existing.setStreak(existingStreakObj + 1); // ALWAYS +1
//            }
//
//            existing.setLastStreakDate(today);
//        }
//        // else: do nothing
//
//        // =========================
//        // SCORE
//        // =========================
//        existing.setScore(existingScore + incomingScore);
//
//        return userRepo.save(existing);
//    }
public User updateUser(User incoming) {

    User existing = userRepo.findByUsername(incoming.getUsername());

    if (existing == null) {
        throw new RuntimeException("User not found");
    }

    // =========================
    // NULL SAFETY
    // =========================
    int existingSwipes = existing.getSwipes() == null ? 0 : existing.getSwipes();
    int existingScore  = existing.getScore()  == null ? 0 : existing.getScore();
    Integer existingStreakObj = existing.getStreak();

    int incomingSwipes = incoming.getSwipes() == null ? 0 : incoming.getSwipes();
    int incomingStreak = incoming.getStreak() == null ? 0 : incoming.getStreak();

    // =========================
    // SWIPES
    // =========================
    int newTotalSwipes = existingSwipes + incomingSwipes;
    existing.setSwipes(newTotalSwipes);

    // =========================
    // STREAK RESET CHECK
    // =========================
    LocalDate today = LocalDate.now();
    LocalDate lastIncrement = existing.getLastStreakDate();

    if (lastIncrement != null && lastIncrement.isBefore(today.minusDays(1))) {
        existing.setStreak(0);
        existingStreakObj = 0;
    }

    // =========================
    // STREAK UPDATE
    // =========================
    boolean alreadyIncrementedToday =
            lastIncrement != null && lastIncrement.isEqual(today);

    boolean streakUpdatedToday = false;

    if (incomingStreak > 0 && !alreadyIncrementedToday) {

        if (existingStreakObj == null || existingStreakObj == 0) {
            existing.setStreak(1);
        } else {
            existing.setStreak(existingStreakObj + 1);
        }

        existing.setLastStreakDate(today);
        streakUpdatedToday = true;
    }

    int finalStreak = existing.getStreak() == null ? 0 : existing.getStreak();

    // =========================
    // SCORE CALCULATION (UPDATED LOGIC)
    // =========================
    int scoreFromSwipes = incomingSwipes * 3;

    int scoreFromStreak = 0;
    if (streakUpdatedToday) {
        scoreFromStreak = 5;
    }

    int finalScore = existingScore + scoreFromSwipes + scoreFromStreak;
    existing.setScore(finalScore);

    return userRepo.save(existing);
}



//    public User setProfilepic(User user) {
//        User user1 = userRepo.findByUsername(user.getUsername());
//        if(user1 == null) {
//            throw new RuntimeException("Username Does Not Exist");
//        }
//        user1.setPfp(user.getPfp());
//        return userRepo.save(user1);
//    }


    public String getProfilePic(User user) {
        User user1=userRepo.findByUsername(user.getUsername());
        if(user1==null) {
            throw new RuntimeException("Username Does Not Exist");
        }
        return user1.getPfp();
    }

    public User setProfilePic(User user) {
        User user1 = userRepo.findByUsername(user.getUsername());
        if(user1 == null) {
            throw new RuntimeException("Username Does Not Exist");
        }
        user1.setPfp(user.getPfp());
        return userRepo.save(user1);
    }

    public User updateEmail(User user) {
        User userss=userRepo.findByUsername(user.getUsername());
        if (userss == null) {
            throw new RuntimeException("Username Does Not Exist");
        }
        if(!userss.getEmail().equals(user.getEmail())) {
            throw new RuntimeException("Email Already Exits");
        }
        user.setEmail(userss.getEmail());
        return userRepo.save(user);
    }

    public User updateUsername(User user) {

        // find existing user by email (stable identifier)
        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser == null) {
            throw new RuntimeException("User does not exist");
        }

        // OPTIONAL: check if new username already taken by someone else
        User usernameOwner = userRepo.findByUsername(user.getUsername());
        if (usernameOwner != null && !usernameOwner.getId().equals(existingUser.getId())) {
            throw new RuntimeException("Username already exists");
        }

        // set NEW username
        existingUser.setUsername(user.getUsername());

        return userRepo.save(existingUser);
    }


    public User updatePassword(User user) {
        User existingUser = userRepo.findByUsername(user.getUsername());

        if (existingUser == null) {
            throw new RuntimeException("Username does not exist");
        }

        // set NEW password
        existingUser.setPassword(user.getPassword());

        return userRepo.save(existingUser);
    }


    public @Nullable String deleteAccount(String email) {
        User user=userRepo.findByEmail(email);
        if(user==null) {
            throw new RuntimeException("Username Does Not Exist");
        }
        DeletedUsers deletedUsers=new DeletedUsers();
//        deletedUsers.setId(user.getId());
        deletedUsers.setEmail(user.getEmail());
        deletedUsers.setUsername(user.getUsername());
        deletedUsers.setSwipes(user.getSwipes());
        deletedUsers.setStreak(user.getStreak());
        deletedUsers.setScore(user.getScore());
        deletedUsers.setPremiumUser(user.getPremiumUser());
        deletedUsers.setPassword(user.getPassword());
        deletedUsers.setPfp(user.getPfp());
        deletedUsers.setCountry(user.getCountry());
        deletedUsers.setLastSwipeAt(user.getLastSwipeAt());
        deleteRepo.save(deletedUsers);
          userRepo.delete(user);
          return "Account Deleted Successfully";

    }
}

