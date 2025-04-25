package org.example.com.prompt.book;

import org.example.com.model.User;
import java.util.Scanner;

public class BookPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public BookPrompt(User currentUser) {
        this.currentUser = currentUser;
    }


    public void start() {
        while (true) {
            System.out.println("\nBook Prompt");
            System.out.println("\t1. 전체 도서 목록 보기");
            System.out.println("\t2. 대출 가능 도서 목록 보기");
            System.out.println("\t3. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    new BookListPrompt().listAllBook();
                    break;
                case "2":
                    new BookListPrompt().listAvailableBook();
                    break;
                case "3":
                    if (currentUser.getId().equalsIgnoreCase("admin")) {
                        System.out.println("관리자 메뉴로 이동합니다.");
                    }
                    else {
                        System.out.println("사용자 메뉴로 이동합니다.");
                    }
                        return;
                default:
                    System.out.println(" 입력에 해당하는 명령어가 없습니다. 1~3 사이 숫자를 입력해주세요.");
            }
        }
    }
}