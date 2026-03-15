package com.swipenow.swipenow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;
    
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String country;

    // 0 or 1, optional
    private Integer premiumUser;

    // numeric fields, optional

    private Integer swipes = 0;


    private Integer streak = 0;

    private Integer score = 0;

    private String code;
    // profile picture URL/path, optional
    private String pfp;

    private Integer rank;
    private Integer friends;


    private Integer memes = 0;



    private LocalDate lastStreakDate;


    private Boolean termsAndCondition;
    private LocalDateTime lastSwipeAt;   // for 20-hour inactivity
    private String fcmToken;
    private LocalDate lastQuotaReset;// for push notifications
    @Column(nullable = false)
    private Integer BatchNumber;
    private LocalDate lastBatchDate;
   
}
