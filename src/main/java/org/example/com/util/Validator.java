package org.example.com.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    // ID: 6자 이상, 영문 대소문자 + 숫자만 허용
    public static boolean isValidId(String id) {
        return id.matches("^[a-zA-Z0-9]{6,}$");
    }

    // Password: 8자 이상, 영문 대/소문자, 숫자, 특수문자 각각 최소 1개 포함
    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s:]).{8,}$");
    }

    // 전화번호: "010 XXXX XXXX" 형식
    public static boolean isValidPhone(String phone) {
        return phone.matches("^010\\s\\d{4}\\s\\d{4}$");
    }

    // 이메일: 아이디는 특수문자 없이, 도메인은 @gmail.com 고정
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]+@gmail\\.com$");
    }

    // 생년월일: YYYY-MM-DD 형식 + 1900~2025년 사이 + 존재하는 날짜
    public static boolean isValidBirth(String birth) {
        try {
            LocalDate date = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int year = date.getYear();
            return year >= 1900 && year <= 2025 && date.isBefore(LocalDate.now().plusDays(1));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidISBN(String isbn) {
        // ISBN은 13자리 숫자만 허용 (예: 9781234567890)
        return isbn != null && isbn.matches("^\\d{13}$");
    }
}