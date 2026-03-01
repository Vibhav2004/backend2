package com.swipenow.swipenow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_batches")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private LocalDate batchDate;

    @Column(columnDefinition = "TEXT")
    private String memeIdsJson; // JSON list of memeIds
}