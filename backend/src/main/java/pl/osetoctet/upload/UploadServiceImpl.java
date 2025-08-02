package pl.osetoctet.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.osetoctet.common.exception.SaveException;
import pl.osetoctet.common.exception.ValidationException;
import pl.osetoctet.upload.model.dto.UploadResponseDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
class UploadServiceImpl implements UploadService {

    @Value("${app.upload.directory}")
    private String uploadDirectory;

    @Value("${app.upload.allowed-extensions}")
    private String allowedExtensions;

    @Override
    public UploadResponseDto uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("validation.upload.file.empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new ValidationException("validation.upload.filename.required");
        }

        String extension = getFileExtension(filename).toLowerCase();
        List<String> allowed = Arrays.asList(allowedExtensions.split(","));

        if (!allowed.contains(extension)) {
            throw new ValidationException("validation.upload.file.typeNotAllowed");
        }

        try {
            Path uploadPath = Paths.get(uploadDirectory);
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID() + "." + fileExtension;

            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File uploaded successfully: {}", uniqueFilename);

            return new UploadResponseDto(uniqueFilename);
        } catch (IOException e) {
            throw new SaveException("Failed to save image file", e);
        }
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            throw new ValidationException("validation.upload.file.extensionRequired");
        }
        return filename.substring(lastDot + 1);
    }

}
