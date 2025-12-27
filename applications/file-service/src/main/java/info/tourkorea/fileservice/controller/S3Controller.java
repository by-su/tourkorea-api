package info.tourkorea.fileservice.controller;

import com.amazonaws.services.s3.model.S3Object;
import info.tourkorea.fileservice.common.constant.FileCategory;
import info.tourkorea.fileservice.dto.FileUploadResponse;
import info.tourkorea.fileservice.service.S3Service;
import info.tourkorea.fileservice.service.UserOpenFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import response.ApiResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final UserOpenFeign userOpenFeign;
    private static final String S3_URL_PREFIX = "https://tour-korea-prod.s3.ap-northeast-2.amazonaws.com/";

    @GetMapping
    public ApiResponse<?> readFile(@RequestParam String url) {
        try {
            S3Object file = s3Service.getFile(url);
            return ApiResponse.success(HttpStatus.OK.name(), file.getObjectContent());
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.NOT_FOUND.name(), e.getMessage());
        }
    }

    @PostMapping(value = "/profiles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            String path = s3Service.uploadFile(FileCategory.PROFILE, file);
            String fileUrl = getUrl(path);
            userOpenFeign.update(fileUrl);
            return ApiResponse.success(HttpStatus.OK.name(), new FileUploadResponse(fileUrl));
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
        }
    }

    @PostMapping(value = "/articles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> uploadArticleImage(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fileName = s3Service.uploadFile(FileCategory.ARTICLE, file);
            return ApiResponse.success(HttpStatus.OK.name(), new FileUploadResponse(getUrl(fileName)));
        } catch (Exception e) {
            return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
        }
    }

    private static String getUrl(String fileName) {
        return S3_URL_PREFIX + fileName;
    }

}
