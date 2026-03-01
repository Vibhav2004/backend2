package com.swipenow.swipenow.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FRIENDS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who initiated the friendship
    @Column( nullable = false)
    private String friend1;

    // User who is the friend

    @Column( nullable = false)
    private String friend2;

    // Time of friendship creation
    @Column(nullable = false)
    private LocalDateTime time;
}