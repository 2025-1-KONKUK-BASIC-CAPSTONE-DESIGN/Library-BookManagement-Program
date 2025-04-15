package org.example.com.prompt.start;

import org.example.com.model.Date;
import org.example.com.model.User;
import org.example.com.prompt.admin.AdminPrompt;
import org.example.com.prompt.user.UserPrompt;
import org.example.com.util.DateManager;
import org.example.com.util.FileManager;
import org.example.com.util.Validator;

import java.util.List;
import java.util.Scanner;

public class StartPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User admin = new User("admin", "admin", "010-0000-0000", "admin@gmail.com", "1931-05-10");

    public void start() {
        while (true) {
            System.out.println("\n📚 StartPrompt");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    new SignUpPrompt().start();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    System.out.println("👋 프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("❌ 입력에 해당하는 명령어가 없습니다. 1, 2, 3 중 하나만 입력해 주세요.");
            }
        }
    }

    private void login() {
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
        users.add(admin);   //admin 계정은 가입하지 않아도 존재.

        User currentUser;
        while (true) {
            System.out.print("ID를 입력하세요: ");
            String id = scanner.nextLine().trim();

            currentUser = users.stream()
                    .filter(u -> u.getId().equals(id))
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

        if (currentUser.getId().equalsIgnoreCase("admin")) {
            new AdminPrompt(currentUser).start();
        } else {
            new UserPrompt(currentUser).start();
        }
    }
}
