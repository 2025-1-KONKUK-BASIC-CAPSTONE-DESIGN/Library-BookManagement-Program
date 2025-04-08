package org.example.com.prompt.user;

import org.example.com.util.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DatePrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n📅 날짜 변경");

        while (true) {
            System.out.print("변경할 날짜를 입력하세요 (YYYY-MM-DD, 취소: 0): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("❗ 날짜 변경을 취소했습니다.");
                return;
            }

            if (!Validator.isValidBirth(input)) {
                System.out.println("❌ 형식이 올바르지 않거나 유효하지 않은 날짜입니다.");
                continue;
            }

            LocalDate newDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("✅ 날짜가 " + newDate + " 로 설정되었습니다. (참고용)");
            return;
        }
    }
}