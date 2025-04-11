package org.example.com.prompt.user;

import org.example.com.model.Date;
import org.example.com.model.User;
import org.example.com.util.DateManager;
import org.example.com.util.Validator;
import java.time.format.DateTimeParseException;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DatePrompt {
    private final Scanner scanner = new Scanner(System.in);
    private static LocalDate currentDate = LocalDate.now();
    private final User currentUser;

    public DatePrompt(User user) {
        this.currentUser = user;
    }

    public static LocalDate getCurrentDate() {
        return currentDate;
    }

    public void start() {
        System.out.println("\n📅 날짜 변경");

        while (true) {
            System.out.print("변경할 날짜를 입력하세요 (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();

            if (!Validator.isValidDateFormat(input) || !Validator.isValidDate(input)) {
                System.out.println("❌ 올바르지 않은 날짜입니다.");
                continue;
            }

            Date current = DateManager.loadDateFromFile();

            if (!Validator.isAfterOrEqual(input, current.getValue())) {
                System.out.println("❌ 현재 날짜보다 이전일 수 없습니다.");
                continue;
            }

            DateManager.saveDateToFile(Date.parse(input));
            LocalDate newDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            currentDate = newDate;
            int overdueDays = 0; // 날짜 데이터 생성 후 수정할것

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            System.out.printf("날짜가 %s로 변경되었습니다.\n", newDate.format(formatter));
            System.out.printf("현재 %d권 대출하였으며, %d일 연체되었습니다.\n",
                    currentUser.getLoanCount(), overdueDays);
            return;
        }
    }
}