package org.example.com.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public static boolean isValidId(String id) {
        return id.matches("^[a-zA-Z0-9]{6,}$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s:]).{8,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^010\\s\\d{4}\\s\\d{4}$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]+@gmail\\.com$");
    }

    public static boolean isValidBirth(String birth) {
        try {
            LocalDate date = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int year = date.getYear();
            return year >= 1900 && year <= 2025 && date.isBefore(LocalDate.now().plusDays(1));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidDateFormat(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isAfterOrEqual(String newDate, String oldDate) {
        LocalDate newD = LocalDate.parse(newDate, FORMATTER);
        LocalDate oldD = LocalDate.parse(oldDate, FORMATTER);
        return !newD.isBefore(oldD);
    }

    public static String validateIsbnDetailed(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return "!! ISBN을 입력해주세요.";
        }
        if (isbn.contains(" ") || isbn.contains("-")) {
            return "!! ISBN에는 하이픈(-), 공백, 기타 특수문자는 포함되어서는 안 됩니다. 공백 없는 13자리 숫자를 입력해주세요.";
        }
        if (!isbn.matches("^\\d+$")) {
            return "!! ISBN에는 숫자만 포함되어 있습니다. 공백 없는 13자리 숫자를 입력해주세요.";
        }
        if (isbn.length() != 13) {
            return "!! ISBN은 정확히 13자리여야 합니다.";
        }
        return ""; // 유효함
    }

    public static boolean isValidISBN(String isbn) {
        return validateIsbnDetailed(isbn).isEmpty();
    }
}
