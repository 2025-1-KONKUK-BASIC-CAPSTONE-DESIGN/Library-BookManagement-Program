package org.example.com.util;

import org.example.com.model.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

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

    public static String validateDateDetailed(String inputDate) {
        if (inputDate == null || inputDate.isBlank()) {
            return "!! 날짜를 입력해주세요.";
        }

        if (!inputDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return "!! 날짜는 YYYY-MM-DD 형식이어야 합니다.";
        }

        String[] parts = inputDate.split("-");
        int year, month, day;

        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "!! 날짜 형식이 잘못되었습니다. 숫자만 입력해주세요.";
        }

        if (year < 1900 || year > 2025) {
            return "!! 연도는 1900년부터 2025년까지 허용됩니다.";
        }

        if (month < 1 || month > 12) {
            return "!! 월은 01부터 12 사이여야 합니다.";
        }

        if (day < 1 || day > 31) {
            return "!! 일은 01부터 31 사이여야 합니다.";
        }

        // 실제 존재하는 날짜인지 검사
        if (!isRealDate(year, month, day)) {
            return "!! 존재하지 않는 날짜입니다. 다시 확인해주세요.";
        }

        // 현재 날짜와 비교
        LocalDate current = DateManager.loadDateFromFile();

        if (current != null && inputDate.compareTo(current.toString()) < 0) {
            return "!! 오늘(" + current + ")보다 이전 날짜는 입력할 수 없습니다.";
        }

        return ""; // 유효함
    }

    private static boolean isRealDate(int year, int month, int day) {
        int[] daysInMonth = { 31, isLeap(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        return day <= daysInMonth[month - 1]; //
    }

    private static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0); // 윤년 계산
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
