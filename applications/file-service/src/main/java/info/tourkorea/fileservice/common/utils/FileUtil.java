package info.tourkorea.fileservice.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FileUtil {

    private static final Map<String, String> ALLOWED_EXTENSIONS_MIME_MAP = new HashMap<>();

    static {
        ALLOWED_EXTENSIONS_MIME_MAP.put("jpg", "image/jpeg");
        ALLOWED_EXTENSIONS_MIME_MAP.put("jpeg", "image/jpeg");
        ALLOWED_EXTENSIONS_MIME_MAP.put("png", "image/png");
        ALLOWED_EXTENSIONS_MIME_MAP.put("svg", "image/svg");
        ALLOWED_EXTENSIONS_MIME_MAP.put("gif", "image/gif");
    }

    public  String getExtension(String fileName) {
        assert fileName != null;

        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            throw new IllegalArgumentException("올바르지 않은 확장자입니다.");
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public boolean MultipartFile(MultipartFile file) {
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String mimeType = file.getContentType();
        return isValidMimeType(extension, mimeType);
    }

    public boolean isValidMimeType(String extension, String mimeType) {
        String expectedMimeType = ALLOWED_EXTENSIONS_MIME_MAP.get(extension);
        return expectedMimeType != null && expectedMimeType.equalsIgnoreCase(mimeType);
    }

    public boolean isValidExtension(String extension) {
        return ALLOWED_EXTENSIONS_MIME_MAP.containsKey(extension.toLowerCase());
    }
}
