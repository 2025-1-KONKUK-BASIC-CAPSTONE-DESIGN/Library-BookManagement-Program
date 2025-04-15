package org.example.com.prompt.user;

import org.example.com.model.User;
import java.util.Scanner;
import org.example.com.prompt.book.BookPrompt;

public class UserPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public UserPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        while (true) {
            System.out.printf("\n[%s님 접속 중]\n", currentUser.getId());
            System.out.println("1. 도서 프롬포트");
            System.out.println("2. 대출");
            System.out.println("3. 반납");
            System.out.println("4. 날짜 변경");
            System.out.println("5. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    new BookPrompt().start();
                    break;
                case "2":
                    new LoanPrompt(currentUser).start();
                    break;
                case "3":
                    new ReturnPrompt(currentUser).start();
                    break;
                case "4":
                    new DatePrompt(currentUser).start();
                    break;
                case "5":
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println(" 입력에 해당하는 명령어가 없습니다. 1~5 사이 숫자를 입력해주세요.");
            }
        }
    }
}