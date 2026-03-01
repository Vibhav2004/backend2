package com.swipenow.swipenow.service;

import com.swipenow.swipenow.entity.Meme;
import com.swipenow.swipenow.entity.User;
import com.swipenow.swipenow.repository.MemeRepo;
import com.swipenow.swipenow.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SupaBaseService {


    @Autowired
    private UserRepo    userRepo;

    private final MemeRepo memeRepository;
    private final WebClient webClient;

    // Use service_role key for uploads
    private final String SUPABASE_URL = "https://csuoqebombiuoslzwwys.supabase.co";
    private final String SUPABASE_SERVICE_ROLE_KEY = "sb_secret_c4NiNEg9p2eEQnz-fSreMg_b8__Tp4Q";
    private final String BUCKET = "MEME";

    public SupaBaseService(MemeRepo memeRepository) {
        this.memeRepository = memeRepository;
        this.webClient = WebClient.builder()
                .baseUrl(SUPABASE_URL)
                .defaultHeader("apikey", SUPABASE_SERVICE_ROLE_KEY)
                .defaultHeader("Authorization", "Bearer " + SUPABASE_SERVICE_ROLE_KEY)
                .build();
    }

    public Meme uploadMeme(MultipartFile file, String title, String postedBy) throws Exception {

        User user = userRepo.findByUsername(postedBy);
        if (user == null) {
            throw new Exception("User not found");
        }


        // Extract file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1); // gets 'jpg', 'png', etc.
        }

// Unique filename
        String filename = UUID.randomUUID() + "." + extension;


        // Set the path inside your bucket
        String folderPath = "USER_UPLOADED_MEME/memes"; // <- use underscores instead of spaces
        String fullPath = folderPath + "/" + filename;   // e.g., "USER_UPLOADED_MEME/memes/uuid.jpg"


        // Multipart upload
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return fullPath; // This ensures the file is saved under your folder path
            }
        });

        webClient.post()
                .uri("/storage/v1/object/" + BUCKET + "/" + fullPath + "?upsert=true") // full path in URI
                .header("Authorization", "Bearer " + SUPABASE_SERVICE_ROLE_KEY)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        // Public URL
        String imageUrl = SUPABASE_URL + "/storage/v1/object/public/" + BUCKET + "/" + fullPath;





        user.setMemes(user.getMemes() + 1);

        userRepo.save(user);

        // Save metadata in database
        Meme meme = new Meme();
        meme.setTitle(title);
        meme.setPostedBy(postedBy);
        meme.setUrl(imageUrl);
        meme.setPostedTime(LocalDate.now());

        return memeRepository.save(meme);
    }



    public List<String> getMemeUrls(String postedBy) {
        List<String> urls = memeRepository.findMemeUrlsByPostedBy(postedBy);
        return urls;
    }

    public List<String> getAllMemes() {
        return memeRepository.findAllUrls();
    }

}
