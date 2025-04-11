package org.example.com.prompt.start;

import org.example.com.model.Date;
import org.example.com.prompt.admin.AdminPrompt;
import org.example.com.util.DateManager;
import org.example.com.util.FileManager;
import org.example.com.model.User;
import org.example.com.util.Validator;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class LoginPrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n🔐 로그인 절차를 시작합니다.");

        Date currentDate = DateManager.loadDateFromFile();
        while (true) {
            System.out.print("날짜를 입력하세요 (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();

            if (!Validator.isValidDateFormat(input) || !Validator.isValidDate(input)) {
                System.out.println("❌ 형식이 잘못되었거나 유효하지 않은 날짜입니다. 다시 입력해주세요.");
                continue;
            }
            if (currentDate != null && !Validator.isAfterOrEqual(input, currentDate.getValue())) {
                System.out.println("❌ 과거 날짜는 사용할 수 없습니다. 다시 입력해주세요.");
                continue;
            }

            DateManager.saveDateToFile(Date.parse(input));
            break;
        }

        List<User> users = FileManager.loadUsers();

        String id;
        User currentUser = null;
        while (true) {
            System.out.print("ID를 입력하세요: ");
            id = scanner.nextLine().trim();

            final String inputId = id; // effectively final
            currentUser = users.stream()
                    .filter(u -> u.getId().equals(inputId))
                    .findFirst()
                    .orElse(null);

            if (currentUser == null) {
                System.out.println("❌ 존재하지 않는 ID입니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("비밀번호를 입력하세요: ");
            String pwd = scanner.nextLine().trim();

            if (!currentUser.getPassword().equals(pwd)) {
                System.out.println("❌ 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                continue;
            }
            break;
        }

        System.out.println("✅ 로그인 성공!");

        // 관리자인지 확인 (관리자 ID는 admin으로 가정)
        if (id.equalsIgnoreCase("admin")) {
            new AdminPrompt().start();
        } else {
//            new UserPrompt(currentUser).start();
        }
    }
}