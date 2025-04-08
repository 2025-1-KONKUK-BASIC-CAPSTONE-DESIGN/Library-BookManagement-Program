package org.example.com.prompt.admin;

import java.util.Scanner;

public class AdminPrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n🛠️ 관리자 메뉴");
            System.out.println("1. 도서 추가");
            System.out.println("2. 도서 삭제");
            System.out.println("3. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    new BookAddPrompt().start();
                    break;
                case "2":
                    new BookDeletePrompt().start();
                    break;
                case "3":
                    System.out.println("👋 관리자 메뉴를 종료합니다.");
                    return;
                default:
                    System.out.println("❌ 입력에 해당하는 명령어가 없습니다. 1~3 중 하나만 입력해 주세요.");
            }
        }
    }
}