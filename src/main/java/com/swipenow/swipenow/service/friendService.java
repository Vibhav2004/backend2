package com.swipenow.swipenow.service;

import com.swipenow.swipenow.entity.Friends;
import com.swipenow.swipenow.entity.User;
import com.swipenow.swipenow.repository.UserRepo;
import com.swipenow.swipenow.repository.friendRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class friendService {
    @Autowired
    private friendRepo friendRepo;
    @Autowired
    private UserRepo userRepo;


    @Transactional
    public Friends addFriends(Friends dto) {

        String username1 = dto.getFriend1(); // actually DTO usernames
        String username2 = dto.getFriend2();

        if (username1 == null || username2 == null || username1.isBlank() || username2.isBlank()) {
            throw new RuntimeException("Invalid friend data");
        }

        if (username1.equals(username2)) {
            throw new RuntimeException("You can't add yourself");
        }

        // fetch users from DB
        User user1 = userRepo.findByUsername(username1);
        User user2 = userRepo.findByUsername(username2);

        if (user1 == null || user2 == null) {
            throw new RuntimeException("User not found");
        }

        // Check if friendship already exists (bidirectional)
        boolean exists = friendRepo.existsByFriend1AndFriend2(user1.getUsername(), user2.getUsername())
                || friendRepo.existsByFriend1AndFriend2(user2.getUsername(), user1.getUsername());

        if (exists) {
            throw new RuntimeException("Friendship already exists");
        }

        // Update friend counts
        user1.setFriends(user1.getFriends() != null ? user1.getFriends() + 1 : 1);
        user2.setFriends(user2.getFriends() != null ? user2.getFriends() + 1 : 1);

        userRepo.save(user1);
        userRepo.save(user2);

        // Save friendship properly as entities
        Friends friendship = new Friends();
        friendship.setFriend1(username1);
        friendship.setFriend2(username2);
        friendship.setTime(LocalDateTime.now());

        return friendRepo.save(friendship);
    }


    public boolean areFriends(String user1, String user2) {
        return friendRepo.existsByFriend1AndFriend2(user1, user2) ||
                friendRepo.existsByFriend1AndFriend2(user2, user1);
    }

    public List<Friends> getAllFriends(String username) {
        return friendRepo.findByFriend1OrFriend2(username, username);
    }




}
