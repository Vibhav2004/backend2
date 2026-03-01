package com.swipenow.swipenow.service;

import com.swipenow.swipenow.entity.OtpData;
import com.swipenow.swipenow.repository.OtpRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp(String email) {
        // Generate 4-digit OTP
        String otp = String.valueOf(1000 + new Random().nextInt(9000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        // Save or update OTP in DB
        OtpData otpEntity = otpRepository.findByEmail(email)
                .orElse(new OtpData(email, otp, expiryTime));

        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);

        otpRepository.save(otpEntity);

        return otp;
    }

    // Validate OTP
    public boolean verifyOtp(String email, String otp) {
        return otpRepository.findByEmail(email)
                .map(entity -> {
                    boolean isValid = entity.getOtp().equals(otp) &&
                            entity.getExpiryTime().isAfter(LocalDateTime.now());
                    if (isValid) {
                        otpRepository.delete(entity); // Delete after successful use
                    }
                    otpRepository.delete(entity);
                    return isValid;
                })
                .orElse(false);
    }

    // Scheduled cleanup of expired OTPs
    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 60000) // every 1 min
    public void removeExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpRepository.findAll().forEach(entity -> {
            if (entity.getExpiryTime().isBefore(now)) {
                otpRepository.delete(entity);
            }
        });
    }
}
