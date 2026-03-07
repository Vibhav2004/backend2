package com.swipenow.swipenow.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swipenow.swipenow.entity.DailyBatch;
import com.swipenow.swipenow.entity.Meme;
import com.swipenow.swipenow.entity.User;
import com.swipenow.swipenow.repository.DailyBatchRepo;
import com.swipenow.swipenow.repository.MemeRepo;
import com.swipenow.swipenow.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemeService {

    @Autowired
    private MemeRepo memeRepo;

    @Autowired
    private DailyBatchRepo dailyBatchRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepo userRepo;

//
//    // 🔥 Main method called by controller
//    public List<String> getAllMemes() {
////         User user = userRepo.findByUsername();
//        LocalDate today = LocalDate.now();
////
////        if (user.getLastBatchDate() == null || !user.getLastBatchDate().equals(today)) {
////
////            int nextBatch = user.getBatchNumber() + 1;
////
////            long totalMemes = memeRepo.count();
////            int maxBatch = (int) Math.ceil((double) totalMemes / 300);
////
////            if (nextBatch > maxBatch) nextBatch = 1;
////
////            user.setBatchNumber(nextBatch);
////            user.setLastBatchDate(today);
////            userRepo.save(user);
////        }
//
//
//        try {
//            Optional<DailyBatch> optionalBatch =
//                    dailyBatchRepository.findByBatchDate(today);
//
//            // If no batch exists, create it
//            if (optionalBatch.isEmpty()) {
//                createDailyBatch();
//                optionalBatch = dailyBatchRepository.findByBatchDate(today);
//            }
//
//            DailyBatch batch = optionalBatch
//                    .orElseThrow(() -> new RuntimeException("Batch not created"));
//
//            // Convert JSON -> List<Long>
//            List<Long> memeIds = objectMapper.readValue(
//                    batch.getMemeIdsJson(),
//                    new TypeReference<List<Long>>() {}
//            );
//
//            // Fetch memes from DB
//            List<Meme> memes = memeRepo.findAllById(memeIds);
//
//            // Shuffle order
//            Collections.shuffle(memes);
//
//            // Return only URLs
//            return memes.stream()
//                    .map(Meme::getUrl)
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching memes", e);
//        }
//    }



public List<String> getAllMemes(String username) throws Exception {
    createDailyBatch();
    User user = userRepo.findByUsername(username);
    if (user == null)
        throw new RuntimeException("User not found");

    LocalDate today = LocalDate.now();

    // FIRST LOGIN
    if (user.getBatchNumber() == 0) {
        user.setBatchNumber(1);
        user.setLastBatchDate(today);
        userRepo.save(user);
    }

    // NEW DAY → advance batch
    if (!today.equals(user.getLastBatchDate())) {

        int nextBatch = user.getBatchNumber() + 1;

        long totalBatches = dailyBatchRepository.count();

        if (nextBatch > totalBatches)
            nextBatch = 1;

        user.setBatchNumber(nextBatch);
        user.setLastBatchDate(today);
        userRepo.save(user);
    }

    int batchNo = user.getBatchNumber();

    System.out.println("Serving batch #" + batchNo + " to user: " + username);
    DailyBatch batch = dailyBatchRepository.findById((long) batchNo)
            .orElseThrow(() -> new RuntimeException("Batch not found"));

    try {

        List<Long> ids = objectMapper.readValue(
                batch.getMemeIdsJson(),
                new TypeReference<List<Long>>() {}
        );

        List<Meme> memes = memeRepo.findAllById(ids);

        Collections.shuffle(memes);

        return memes.stream()
                .map(Meme::getUrl)
                .toList();

    } catch (Exception e) {
        throw new RuntimeException("Batch parse failed", e);
    }
}



//    public List<String> getAllMemes(String username) {
//
//        User user = userRepo.findByUsername(username);
//        if (user == null) throw new RuntimeException("User not found");
//
//        LocalDate today = LocalDate.now();
//
//        // =========================
//        // DAILY BATCH PROGRESSION
//        // =========================
//        if (user.getLastBatchDate() == null || !user.getLastBatchDate().equals(today)) {
//
//            int nextBatch = user.getBatchNumber() + 1;
//
//            long totalMemes = memeRepo.count();
//            int maxBatch = (int) Math.ceil((double) totalMemes / 300);
//
//            if (nextBatch > maxBatch) nextBatch = 1;
//
//            user.setBatchNumber(nextBatch);
//            user.setLastBatchDate(today);
//            userRepo.save(user);
//        }
//
//        int batchNumber = user.getBatchNumber();
//
//        System.out.println("Serving batch #" + batchNumber + " for user " + username);
//
//        try {
//
//            // Fetch all memes sorted by id
//            List<Meme> memes = memeRepo.findAll()
//                    .stream()
//                    .sorted(Comparator.comparingLong(Meme::getMemeId))
//                    .toList();
//
//            int startIndex = (batchNumber - 1) * 300;
//            int endIndex = Math.min(startIndex + 300, memes.size());
//
//            if (startIndex >= memes.size()) {
//                startIndex = 0;
//                endIndex = Math.min(300, memes.size());
//            }
//
//            List<Meme> batchMemes = memes.subList(startIndex, endIndex);
//
//            Collections.shuffle(batchMemes);
//
//            return batchMemes.stream()
//                    .map(Meme::getUrl)
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching memes", e);
//        }
//    }
//public List<String> getAllMemes(String username) {
//
////    User user = userRepo.findByUsername(username);
////    if (user == null)
////        throw new RuntimeException("User not found");
//
//    LocalDate today = LocalDate.now();
////
////    // FIRST TIME USER
////    if (user.getLastBatchDate() == null) {
////        user.setBatchNumber(1);
////        user.setLastBatchDate(today);
////        userRepo.save(user);
////    }
//
//    // NEW DAY → increment FIRST
//    if (!today.equals(user.getLastBatchDate())) {
//
//        int nextBatch = user.getBatchNumber() + 1;
//
//        long totalBatches = dailyBatchRepository.count();
//
//        // loop back if finished all batches
//        if (nextBatch > totalBatches)
//            nextBatch = 1;
//
//        user.setBatchNumber(nextBatch);
//        user.setLastBatchDate(today);
//        userRepo.save(user);
//    }
//
//    int batchNo = user.getBatchNumber();
//
//    DailyBatch batch = dailyBatchRepository.findById((long) batchNo)
//            .orElseThrow(() -> new RuntimeException("Batch not found"));
//
//
//    try {
//
//        List<Long> ids = objectMapper.readValue(
//                batch.getMemeIdsJson(),
//                new TypeReference<List<Long>>() {}
//        );
//
//        List<Meme> memes = memeRepo.findAllById(ids);
//
//        Collections.shuffle(memes);
//
//        return memes.stream()
//                .map(Meme::getUrl)
//                .toList();
//
//    } catch (Exception e) {
//        throw new RuntimeException("Batch parse failed", e);
//    }
//}


//    private void createDailyBatch() throws Exception {
//
//        LocalDate today = LocalDate.now();
//
//        if (dailyBatchRepository.findByBatchDate(today).isPresent()) {
//            return;
//        }
//
//        // Get latest 300 memes sorted by postedTime (null safe)
//        List<Meme> memes = memeRepo.findAll()
//                .stream()
//                .sorted((a, b) -> {
//                    if (a.getPostedTime() == null) return 1;
//                    if (b.getPostedTime() == null) return -1;
//                    return b.getPostedTime().compareTo(a.getPostedTime());
//                })
//                .limit(300)
//                .toList();
//
//        List<Long> memeIds = memes.stream()
//                .map(Meme::getMemeId)
//                .collect(Collectors.toList());
//
//        String json = objectMapper.writeValueAsString(memeIds);
//
//        DailyBatch batch = new DailyBatch();
//        batch.setBatchDate(today);
//        batch.setMemeIdsJson(json);
//
//        dailyBatchRepository.save(batch);
//    }
private void createDailyBatch() throws Exception {
    LocalDate today = LocalDate.now();

    if (dailyBatchRepository.findByBatchDate(today).isPresent()) {
        return;
    }

    // Fetch all memes sorted by memeId ascending
    List<Meme> memes = memeRepo.findAll()
            .stream()
            .sorted(Comparator.comparingLong(Meme::getMemeId))
            .toList();

    // Determine which batch number we are on
    long totalBatchesCreated = dailyBatchRepository.count();
//   int startIndex = (user.getBatchNumber() - 1) * 300;
   int startIndex = (int) (totalBatchesCreated * 300);
    int endIndex = Math.min(startIndex + 300, memes.size());

    // If startIndex >= memes.size(), loop back to start
    if (startIndex >= memes.size()) {
        startIndex = startIndex % memes.size();
        endIndex = Math.min(startIndex + 300, memes.size());
    }

    List<Meme> batchMemes = memes.subList(startIndex, endIndex);

    List<Long> memeIds = batchMemes.stream()
            .map(Meme::getMemeId)
            .collect(Collectors.toList());

    String json = objectMapper.writeValueAsString(memeIds);

    DailyBatch batch = new DailyBatch();
    batch.setBatchDate(today);
    batch.setMemeIdsJson(json);

    dailyBatchRepository.save(batch);
}



public List<String> getGuestMemes() {

    List<Meme> memes = memeRepo.getRandomMemes();

    return memes.stream()
            .map(Meme::getUrl)
            .toList();
}
}