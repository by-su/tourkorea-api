package info.tourkorea.fileservice.common.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String generateFileUUID(String originalFileName) {
        int lastIndexOfDot = originalFileName.lastIndexOf('.');

        if (lastIndexOfDot == -1 || lastIndexOfDot == 0) throw new IllegalArgumentException("Invalid file name");
        var ext = originalFileName.substring(lastIndexOfDot + 1);
        return UUID.randomUUID() + "." + ext;
    }
}
