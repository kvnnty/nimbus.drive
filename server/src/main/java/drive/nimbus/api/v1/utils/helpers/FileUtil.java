package drive.nimbus.api.v1.utils.helpers;

import java.util.UUID;

import org.springframework.util.StringUtils;

public class FileUtil {
  public static String generateUniqueFileName(String originalFileName) {
    String extension = StringUtils.getFilenameExtension(originalFileName);
    return UUID.randomUUID() + (extension != null ? "." + extension : "");
  }
}
