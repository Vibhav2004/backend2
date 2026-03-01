package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.entity.Friends;
import com.swipenow.swipenow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface friendRepo extends JpaRepository<Friends, Long> {
    boolean existsByFriend1AndFriend2(String user1, String user2);

    List<Friends> findByFriend1(String username);

    List<Friends> findByFriend1OrFriend2(String friend1, String friend2);

}
