package com.souravmodak.feedtest.api;

import com.souravmodak.feedtest.models.entities.Post;
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
            "Loves adventure and spontaneity, always on the lookout for new places to explore and interesting people to meet. The world is full of endless possibilities, and they're eager to experience every bit of it 🌍✨",
            "A true coffee enthusiast who finds joy in sipping a freshly brewed cup while watching the sun set. Often found curled up with a good book, creating a calm and peaceful atmosphere for a quiet evening ☕🌅📖",
            "Spreading positivity wherever they go, one smile at a time. No matter the challenges that life brings, they always embrace it with a heart full of gratitude and a mindset that keeps them moving forward 😊❤️",
            "A passionate bookworm 📚  who loves diving into fantasy worlds. Often lost in the pages of a good book, dreaming of magical realms filled with adventure, mystery, and wonder 💫",
            "Dancing through life with grace and passion, constantly seeking the rhythm that makes them feel alive. Whether it's in a ballroom or on a lively dance floor, they cherish every moment of movement and expression 💃🎶",
            "A self-proclaimed foodie, always on the lookout for the next culinary adventure. From local street food to international delicacies, they live to explore new flavors and savor every bite 🍕🍣",
            "A passionate photographer who loves capturing the beauty of nature. Their camera is always ready to preserve the awe-inspiring landscapes and moments from their travels to exotic, off-the-beaten-path destinations 📸🚀",
            "A nature lover at heart 🌿 who seeks peace in the mountains and finds tranquility in the great outdoors. There's something truly magical about feeling connected to the earth and embracing the solitude that nature offers 🏔️",
            "A firm believer in kindness and positivity, striving to make the world a better place one act of love at a time. They believe that small gestures of kindness can lead to bigger changes and bring light to even the darkest places 🌈🤗",
            "Strong, independent, and full of life, always pushing boundaries and breaking limits. With a fierce determination to succeed, they're constantly seeking to grow, learn, and rise above every obstacle that comes their way 💪🔥"
    );

    @GetMapping
    public ResponseEntity<List<Post>> getPhotos() {
        List<Post> photoDetails = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(PHOTO_DIRECTORY)) {  // No restriction on file types
            for (Path filePath : stream) {
                String fileName = filePath.getFileName().toString();
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

                String randomName = NAMES.get(new Random().nextInt(NAMES.size()));
                String randomDesc = DESCRIPTIONS.get(new Random().nextInt(DESCRIPTIONS.size()));

                Post photoData = new Post();
                photoData.setName(randomName);
                photoData.setDescription(randomDesc);
                photoData.setGender("Female");
                photoData.setPhotoName(encodedFileName);
                photoData.setMediaType(getFileExtension(fileName));

                photoDetails.add(photoData);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new ArrayList<>());
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
