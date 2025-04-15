package org.example.com.prompt.user;

import org.example.com.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            System.out.print("변경할 날짜를 입력하세요 (YYYY-MM-DD, 취소: 0): ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("❗ 날짜 변경을 취소했습니다.");
                return;
            }

            if (!input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                System.out.println("!! 올바른 형식이 아닙니다. YYYY-MM-DD 형식으로 다시 입력해주세요.");
                continue;
            }

            try {
                LocalDate newDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (!newDate.isAfter(currentDate)) {
                    System.out.printf("!! 오늘 날짜는 %s입니다. 오늘 이후 날짜를 입력해주세요.\n", currentDate);
                    continue;
                }

                currentDate = newDate;
                int overdueDays = 0; // 연체 계산은 추후 구현

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                System.out.printf("날짜가 %s로 변경되었습니다.\n", newDate.format(formatter));
                System.out.printf("현재 %d권 대출하였으며, %d일 연체되었습니다.\n",
                        currentUser.getLoanCount(), overdueDays);
                return;

            } catch (DateTimeParseException e) {
                System.out.println("!! 올바른 날짜가 아닙니다. 다시 입력해주세요.");
            }
        }
    }
}
