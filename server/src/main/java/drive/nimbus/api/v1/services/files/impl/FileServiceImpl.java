package drive.nimbus.api.v1.services.files.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import drive.nimbus.api.v1.dtos.responses.files.FileUploadResponseDTO;
import drive.nimbus.api.v1.entities.files.StoredFile;
import drive.nimbus.api.v1.exceptions.FileException;
import drive.nimbus.api.v1.exceptions.InternalServerException;
import drive.nimbus.api.v1.repositories.files.FileRepository;
import drive.nimbus.api.v1.services.files.FileService;
import drive.nimbus.api.v1.utils.helpers.FileUtil;
import drive.nimbus.api.v1.utils.mappers.FileMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  @Value("${file.upload-dir:uploads}")
  private String uploadDir;

  @Value("${file.public-base-url}")
  private String publicBaseUrl;

  private Path fileStorageLocation;

  @PostConstruct
  public void init() {
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
      log.info("File storage directory initialized at: {}", this.fileStorageLocation);
    } catch (IOException ex) {
      log.error("Failed to initialize file storage directory", ex);
      throw new InternalServerException("Failed to initialize file storage location");
    }
  }

  @Override
  public FileUploadResponseDTO storeFile(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();

    if (!StringUtils.hasText(originalFilename) || originalFilename == null) {
      throw new FileException("Uploaded file must have a name.");
    }

    String sanitizedFilename = StringUtils.cleanPath(originalFilename);
    if (sanitizedFilename.contains("..")) {
      throw new FileException("Invalid path sequence in filename: " + sanitizedFilename);
    }

    String uniqueFileName = FileUtil.generateUniqueFileName(sanitizedFilename);

    try {
      Path targetPath = this.fileStorageLocation.resolve(uniqueFileName);
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      String fileUrl = buildPublicUrl(uniqueFileName);

      StoredFile fileEntity = StoredFile.builder()
          .fileName(uniqueFileName)
          .contentType(file.getContentType())
          .size(file.getSize())
          .url(fileUrl)
          .build();

      fileRepository.save(fileEntity);
      return FileMapper.toDto(fileEntity);

    } catch (IOException ex) {
      log.error("Failed to store file: {}", uniqueFileName, ex);
      throw new FileException("Failed to store file: " + sanitizedFilename, ex);
    }
  }

  @Override
  public Resource loadFileAsResource(String filename) {
    try {
      Path filePath = this.fileStorageLocation.resolve(filename).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists() || !resource.isReadable()) {
        throw new FileException("File not found or not readable: " + filename);
      }

      return resource;
    } catch (Exception ex) {
      throw new FileException("Failed to load file: " + filename, ex);
    }
  }

  private String buildPublicUrl(String fileName) {
    return publicBaseUrl.endsWith("/") ? publicBaseUrl + fileName : publicBaseUrl + "/" + fileName;
  }
}
