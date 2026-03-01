package com.swipenow.swipenow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "memes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Meme {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long memeId;

        @Column(nullable = false)
        private String title;

        @Column(name = "posted_by", nullable = false)
        private String postedBy;

        @Column(nullable = false)
        private String url;

        private LocalDate postedTime;


}
