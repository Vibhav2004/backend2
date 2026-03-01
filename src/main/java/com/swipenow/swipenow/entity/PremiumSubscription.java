package com.swipenow.swipenow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PREMIUM_SUBSCRIPTIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle 12c+ identity column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the user subscribing

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "subscription_type", nullable = false, length = 50)
    private String subscriptionType; // e.g., "monthly", "yearly"

    @Column(name = "amount_paid")
    private Double amountPaid; // Can be null if free trial or gift
}
