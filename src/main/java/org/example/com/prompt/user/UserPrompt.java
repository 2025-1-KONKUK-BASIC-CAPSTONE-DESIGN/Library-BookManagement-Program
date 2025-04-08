package org.example.com.prompt.user;

import org.example.com.prompt.book.BookPrompt;
import org.example.com.model.User;

import java.util.Scanner;

public class UserPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public UserPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        while (true) {
            System.out.println("\n📘 사용자 메뉴 (로그인 ID: " + currentUser.getId() + ")");
            System.out.println("1. 도서 목록 조회");
            System.out.println("2. 도서 대출");
            System.out.println("3. 도서 반납");
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
                    new DatePrompt().start();
                    break;
                case "5":
                    System.out.println("👋 사용자 메뉴를 종료합니다.");
                    return;
                default:
                    System.out.println("❌ 잘못된 입력입니다. 1~5 사이의 번호를 입력하세요.");
            }
        }
    }
}