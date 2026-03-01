package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.entity.OtpData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository  extends JpaRepository<OtpData, Long> {

    Optional<OtpData> findByEmail(String email);
}
