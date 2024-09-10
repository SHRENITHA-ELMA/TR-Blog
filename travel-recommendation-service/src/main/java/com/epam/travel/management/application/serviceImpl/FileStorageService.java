package com.epam.travel.management.application.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileStorageService {

    // GitHub API details and credentials from application.properties or environment variables
    @Value("${github.api.url}")
    private String GITHUB_API_URL;

    @Value("${github.access.token}")
    private String GITHUB_ACCESS_TOKEN;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Uploads an image file to GitHub and returns the URL.
     *
     * @param imageFile the MultipartFile to upload
     * @return the URL of the uploaded image
     */
    public String uploadImageToGithub(MultipartFile imageFile) {
        try {
            // Convert image to Base64
            byte[] imageBytes = imageFile.getBytes();
            String encodedImage = Base64.encodeBase64String(imageBytes);

            // Extract the file extension
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // Generate a unique filename using UUID
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            String uploadUrl = GITHUB_API_URL + uniqueFilename;

            // Prepare headers and body for GitHub API request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(GITHUB_ACCESS_TOKEN);

            Map<String, String> body = new HashMap<>();
            body.put("message", "Upload blog image " + uniqueFilename);
            body.put("content", encodedImage);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            // Send PUT request to GitHub API
            ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Return the URL of the uploaded image
                return "https://github.com/ksaitejaepam/Pbe_Images/raw/main/blog_images/" + uniqueFilename;
            } else {
                throw new RuntimeException("Failed to upload image to GitHub");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while uploading image to GitHub", e);
        }
    }

    /**
     * Deletes an image from GitHub by its file name.
     *
     * @param fileName the name of the file to delete
     */
    public void deleteImageFromGithub(String fileName) {
        try {
            // GitHub API URL to get file details (including SHA)
            String getFileUrl = GITHUB_API_URL + fileName;

            // Prepare headers for GET request to fetch file SHA
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(GITHUB_ACCESS_TOKEN);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // Fetch the file details, which includes the SHA
            ResponseEntity<Map> fileResponse = restTemplate.exchange(getFileUrl, HttpMethod.GET, entity, Map.class);
            if (fileResponse.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to fetch file details from GitHub");
            }

            // Extract the file's SHA
            String sha = (String) fileResponse.getBody().get("sha");

            // GitHub API URL to delete the file
            String deleteFileUrl = GITHUB_API_URL + fileName;

            // Prepare request body for deleting the file
            Map<String, String> body = new HashMap<>();
            body.put("message", "Delete blog image " + fileName);
            body.put("sha", sha);

            HttpEntity<Map<String, String>> deleteEntity = new HttpEntity<>(body, headers);

            // Send DELETE request to GitHub API
            ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteFileUrl, HttpMethod.DELETE, deleteEntity, String.class);

            if (deleteResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("Image deleted successfully");
            } else {
                throw new RuntimeException("Failed to delete image from GitHub");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error while deleting image from GitHub: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting image from GitHub", e);
        }
    }
}

