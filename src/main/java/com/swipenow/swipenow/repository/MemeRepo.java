package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.entity.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MemeRepo extends JpaRepository<Meme, Long> {
    @Query("SELECT m.url FROM Meme m WHERE m.postedBy = :postedBy")
    List<String> findMemeUrlsByPostedBy(@Param("postedBy") String postedBy);

    @Query("SELECT m.url FROM Meme m")
    List<String> findAllUrls();

    @Query(value = "SELECT * FROM meme ORDER BY meme_id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Meme> findBatch(int limit, int offset);

}
