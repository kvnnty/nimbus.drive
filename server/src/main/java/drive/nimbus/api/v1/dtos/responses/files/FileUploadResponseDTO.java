package drive.nimbus.api.v1.dtos.responses.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponseDTO {
  private String url;
  private String contentType;
  private long size;
}
