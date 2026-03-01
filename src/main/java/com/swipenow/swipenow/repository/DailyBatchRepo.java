package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.entity.DailyBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyBatchRepo extends JpaRepository<DailyBatch, Long> {

    Optional<DailyBatch> findByBatchDate(LocalDate batchDate);
}