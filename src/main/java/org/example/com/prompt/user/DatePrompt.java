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

            // date 확인
            String dateError = Validator.validateDateDetailed(input);
            if (!dateError.isEmpty()) {
                System.out.println(dateError);
                continue;
            }

            try {
                LocalDate newDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate current = DateManager.loadDateFromFile();

                if (current == null) {
                    System.err.println("current 데이터가 없습니다.");
                    return;
                }

                LocalDate loanDate = LocalDate.parse(current.toString());
                long daysBetween = ChronoUnit.DAYS.between(loanDate, newDate);
                int overdueDays = (int) Math.max(0, daysBetween - 13);

                DateManager.saveDateToFile(Date.parse(input));
                currentDate = newDate;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                System.out.printf("날짜가 %s로 변경되었습니다.\n", newDate.format(formatter));
                System.out.printf("현재 %d권 대출하였으며, %d일 연체되었습니다.\n",
                        currentUser.getLoanCount(), overdueDays);
                return;

            } catch (DateTimeParseException e) {
                System.out.println("❌ 존재하지 않는 날짜입니다. 다시 입력해주세요.");
            }
        }
    }
}