package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.DTO.UserStatsDTO;
import com.swipenow.swipenow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepo  extends JpaRepository<User, Long> {

    User getByEmail(String email);


    User findByUsername(String username);
    
    @Query(value = """
            SELECT username, streak, score, swipes
            FROM users
            """, nativeQuery = true)
    List<UserStatsDTO> getLeaderboard();

    User findByEmail(String email);

    List<User> findByFcmTokenIsNotNull();

    List<User> findByLastSwipeAtBefore(LocalDateTime time);

    List<User> findByLastSwipeAtBeforeAndFcmTokenIsNotNull(LocalDateTime limit);
}
