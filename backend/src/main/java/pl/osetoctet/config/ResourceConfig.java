package pl.osetoctet.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${app.upload.directory}")
    private String uploadDirectory;

    @Value("${app.upload.url-pattern}")
    private String urlPattern;

    @PostConstruct
    public void initializeUploadDirectory() {
        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            } else {
                log.info("Upload directory exists: {}", uploadPath.toAbsolutePath());
            }
        } catch (Exception e) {
            log.error("Failed to create upload directory: {}", uploadDirectory, e);
            throw new RuntimeException("Cannot initialize upload directory", e);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = uploadDirectory.endsWith("/")
                ? "file:" + uploadDirectory
                : "file:" + uploadDirectory + "/";

        registry.addResourceHandler(urlPattern)
                .addResourceLocations(resourceLocation);
    }

}
