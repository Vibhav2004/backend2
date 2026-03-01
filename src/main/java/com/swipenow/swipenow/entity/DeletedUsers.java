package com.swipenow.swipenow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeletedUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String country;

    // 0 or 1, optional
    private Integer premiumUser;

    // numeric fields, optional

    private Integer swipes = 0;


    private Integer streak = 0;

    private Integer score = 0;


    // profile picture URL/path, optional
    private String pfp;

    private Integer rank;
    private Integer friends;


    private Integer memes = 0;
    private LocalDateTime lastSwipeAt;
}
