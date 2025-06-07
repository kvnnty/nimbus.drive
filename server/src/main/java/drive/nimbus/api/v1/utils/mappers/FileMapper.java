package drive.nimbus.api.v1.utils.mappers;

import drive.nimbus.api.v1.dtos.responses.files.FileUploadResponseDTO;
import drive.nimbus.api.v1.entities.files.StoredFile;

public class FileMapper {
  public static FileUploadResponseDTO toDto(StoredFile file) {
    return FileUploadResponseDTO.builder()
        .url(file.getUrl())
        .contentType(file.getContentType())
        .size(file.getSize())
        .build();
  }
}
