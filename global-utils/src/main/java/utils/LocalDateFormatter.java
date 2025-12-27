package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convert a string in the format YYYY-MM-DD to a LocalDate.
     *
     * @param dateString the date string to convert
     * @return the LocalDate representation of the date string
     * @throws DateTimeParseException if the date string is not in the valid format
     */
    public static LocalDate convertStringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format. Please use YYYY-MM-DD format.", dateString, e.getErrorIndex());
        }
    }
}
