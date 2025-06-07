package drive.nimbus.api.v1.controllers.files;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import drive.nimbus.api.v1.dtos.responses.files.FileUploadResponseDTO;
import drive.nimbus.api.v1.payload.ApiResponse;
import drive.nimbus.api.v1.services.files.FileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "Files Controller", description = "APIs for file upload, download, and management")
public class FileController {

  private final FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse<FileUploadResponseDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
    return ApiResponse.success("File uploaded successfully", fileService.storeFile(file));
  }

  @GetMapping("/download/{filename:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
    Resource resource = fileService.loadFileAsResource(filename);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
            resource.getFilename() + "\"")
        .body(resource);
  }

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
    Resource resource = fileService.loadFileAsResource(filename);

    String contentType = "application/octet-stream";
    contentType = Files.probeContentType(resource.getFile().toPath());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

}
