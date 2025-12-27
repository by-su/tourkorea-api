package info.tourkorea.fileservice.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileUtilTest {

    FileUtil fileUtil = new FileUtil();

    @DisplayName("파일 확장자 추출 테스트")
    @Test
    void extensionTest() {
        File file = new File("classpath:/Tour Korea.png");
        String extension = fileUtil.getExtension(file.getName());
        Assertions.assertEquals("png", extension);
    }

    @DisplayName("파일 이름 없을 때 예외 발생")
    @Test
    void extensionWithoutName() {
        File file = new File("classpath:/test");
        Assertions.assertThrows(IllegalArgumentException.class, () -> fileUtil.getExtension(file.getName()));
    }

    @DisplayName("확장자와 타입이 다를 때 예외 발생")
    @Test
    void test() {
        File file = new File("classpath:/test.jpg");
        Assertions.assertFalse(() -> fileUtil.isValidMimeType(fileUtil.getExtension(file.getName()), "image/jpg"));
    }

}