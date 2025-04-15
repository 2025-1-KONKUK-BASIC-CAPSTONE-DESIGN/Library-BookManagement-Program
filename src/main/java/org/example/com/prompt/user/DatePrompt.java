package org.example.com.prompt.user;

import org.example.com.model.Date;
import org.example.com.model.User;
import org.example.com.util.DateManager;
import org.example.com.util.Validator;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;



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
            String input = scanner.nextLine();

            if (!input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                System.out.println("❌ 날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 공백 없이 입력해주세요.");
                continue;
            }

            Date current = DateManager.loadDateFromFile();

            if (!Validator.isAfterOrEqual(input, current.getValue())) {
                System.out.println("❌ 현재 날짜보다 이전일 수 없습니다.");
                continue;
            }

            LocalDate newDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate loanDate = LocalDate.parse(DateManager.loadDateFromFile().getValue());

            long daysBetween = ChronoUnit.DAYS.between(loanDate, newDate);
            int overdueDays = (int) Math.max(0, daysBetween - 13);

            DateManager.saveDateToFile(Date.parse(input));  // 날짜 변경은 마지막에 저장
            currentDate = newDate;





            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            System.out.printf("날짜가 %s로 변경되었습니다.\n", newDate.format(formatter));
            System.out.printf("현재 %d권 대출하였으며, %d일 연체되었습니다.\n",
                    currentUser.getLoanCount(), overdueDays);
            return;
        }
    }
}