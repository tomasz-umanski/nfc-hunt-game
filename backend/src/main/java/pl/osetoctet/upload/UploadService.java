package pl.osetoctet.upload;

import org.springframework.web.multipart.MultipartFile;
import pl.osetoctet.upload.model.dto.UploadResponseDto;

public interface UploadService {

    UploadResponseDto uploadFile(MultipartFile file);

}
