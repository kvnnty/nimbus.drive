package drive.nimbus.api.v1.services.files;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import drive.nimbus.api.v1.dtos.responses.files.FileUploadResponseDTO;

public interface FileService {
  FileUploadResponseDTO storeFile(MultipartFile file);

  Resource loadFileAsResource(String filename);
}