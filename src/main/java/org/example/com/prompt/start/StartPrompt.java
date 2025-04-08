package org.example.com.prompt.start;

import java.util.Scanner;

public class StartPrompt {

    private final Scanner scanner = new Scanner(System.in);

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
                    new LoginPrompt().start();
                    break;
                case "3":
                    System.out.println("👋 프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("❌ 입력에 해당하는 명령어가 없습니다. 1, 2, 3 중 하나만 입력해 주세요.");
            }
        }
    }
}