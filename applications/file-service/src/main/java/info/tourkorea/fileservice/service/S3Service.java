package info.tourkorea.fileservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import info.tourkorea.exception.FileUploadException;
import info.tourkorea.fileservice.common.constant.FileCategory;
import info.tourkorea.fileservice.common.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import response.ErrorCode;

import java.io.IOException;
import java.util.Objects;

import static info.tourkorea.fileservice.common.utils.UUIDUtil.generateFileUUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;
    private final FileUtil fileUtil;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String uploadFile(FileCategory category, MultipartFile file) {
        checkIfFileIsSafe(file);
        checkIfFileIsValidSize(file);

        String fileName = generateFileUUID(Objects.requireNonNull(file.getOriginalFilename()));

        PutObjectRequest putObjectRequest = null;

        try {
            String path = getKeyName(category, fileName);
            putObjectRequest = new PutObjectRequest(bucket, path, file.getInputStream(), null);
            s3Client.putObject(putObjectRequest);
            return path;
        } catch (IOException e) {
            throw new IllegalArgumentException("File upload failed: " + e.getMessage());
        }
    }

    private void checkIfFileIsValidSize(MultipartFile file) {
        // 10MB 이상 파일 업로드 불가
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new FileUploadException(ErrorCode.FILE_SIZE_OVER);
        }
    }

    private void checkIfFileIsSafe(MultipartFile file) {
        String extension = fileUtil.getExtension(file.getOriginalFilename());

        if (!isValidExtension(extension)) {
            throw new IllegalArgumentException("Invalid file extension");
        }

        if (!isValidMIMEType(file)) {
            throw new IllegalArgumentException("Invalid MIME type");
        }
    }

    private boolean isValidMIMEType(MultipartFile file) {
        String contentType = file.getContentType();
        return fileUtil.isValidMimeType(
                fileUtil.getExtension(Objects.requireNonNull(file.getOriginalFilename())),
                contentType
        );
    }

    private boolean isValidExtension(String extension) {
        return fileUtil.isValidExtension(extension.toLowerCase());
    }

    public S3Object getFile(String keyName) {
        return s3Client.getObject(bucket, keyName);
    }

    private String getKeyName(FileCategory fileCategory, String keyName) {
        return fileCategory.getCategory() + "/" + keyName;
    }
}
