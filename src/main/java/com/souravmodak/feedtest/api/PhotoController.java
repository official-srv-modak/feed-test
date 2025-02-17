package com.souravmodak.feedtest.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import java.io.IOException;
import java.nio.file.*;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*") // Allows all origins (can be adjusted as needed)
public class PhotoController {

    private static final String DIRECTORY_PATH = "photos"; // Store files in server folder
    private static final Path PHOTO_DIRECTORY = Paths.get(DIRECTORY_PATH);

    static {
        try {
            if (!Files.exists(PHOTO_DIRECTORY)) {
                Files.createDirectories(PHOTO_DIRECTORY);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create photos directory", e);
        }
    }

    private static final List<String> NAMES = Arrays.asList(
            "Emma", "Olivia", "Ava", "Sophia", "Isabella",
            "Mia", "Charlotte", "Amelia", "Harper", "Evelyn"
    );

    private static final List<String> DESCRIPTIONS = Arrays.asList(
            "Loves adventure and spontaneity, exploring new places and meeting interesting people 🌍✨",
            "A coffee enthusiast who enjoys sunsets, reading books, and a quiet evening ☕🌅📖",
            "Spreading positivity with every step, one smile at a time, embracing life's ups and downs 😊❤️",
            "Bookworm 📚 who loves fantasy stories and daydreams about magical worlds 💫",
            "Dancing through life with grace and passion, making memories on every dance floor 💃🎶",
            "A foodie who lives for discovering new flavors, always seeking the next culinary adventure 🍕🍣",
            "Passionate about photography, capturing the beauty of nature, and traveling to exotic locations 📸🚀",
            "Nature lover 🌿 who finds peace and solitude in the mountains, feeling connected to the earth 🏔️",
            "Believer in kindness and positivity, striving to make the world a better place with each act of love 🌈🤗",
            "Strong, independent, and full of life, constantly pushing boundaries and breaking limits 💪🔥"
    );

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getPhotos() {
        List<Map<String, String>> photoDetails = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(PHOTO_DIRECTORY)) {  // No restriction on file types
            for (Path filePath : stream) {
                String fileName = filePath.getFileName().toString();
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

                String randomName = NAMES.get(new Random().nextInt(NAMES.size()));
                String randomDesc = DESCRIPTIONS.get(new Random().nextInt(DESCRIPTIONS.size()));

                Map<String, String> photoData = new HashMap<>();
                photoData.put("fileName", fileName);
                photoData.put("name", randomName);
                photoData.put("description", randomDesc);  // Longer description now
                photoData.put("gender", "Female");
                photoData.put("photoName", encodedFileName);

                photoDetails.add(photoData);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Collections.singletonList(
                    Map.of("error", "Unable to read files from directory: " + PHOTO_DIRECTORY)
            ));
        }

        return ResponseEntity.ok(photoDetails);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            String decodedFileName = URLDecoder.decode(filename, StandardCharsets.UTF_8);
            Path filePath = PHOTO_DIRECTORY.resolve(decodedFileName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Determine file extension and media type
            String fileExtension = getFileExtension(decodedFileName);
            MediaType mediaType = getMediaType(fileExtension);

            // For video files (e.g., mp4), handle video streaming
            if ("mp4".equals(fileExtension)) {
                // If it's a video, we may want to include more specific headers for streaming
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + decodedFileName + "\"");
                headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                headers.add(HttpHeaders.PRAGMA, "no-cache");

                Resource resource = new InputStreamResource(Files.newInputStream(filePath));

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(mediaType)
                        .body(resource);
            } else {
                // For non-video files (image, etc.)
                Resource resource = new InputStreamResource(Files.newInputStream(filePath));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + decodedFileName + "\"")
                        .contentType(mediaType)
                        .body(resource);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Helper method to get the file extension
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }

    // Helper method to get the media type based on file extension
    private MediaType getMediaType(String fileExtension) {
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
            case "png":
                return MediaType.IMAGE_JPEG;  // Default to JPEG for all image files
            case "mp4":
                return MediaType.valueOf("video/mp4");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;  // Default binary stream for unsupported types
        }
    }
}
