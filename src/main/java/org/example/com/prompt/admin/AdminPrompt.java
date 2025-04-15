package org.example.com.prompt.admin;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.example.com.prompt.book.BookPrompt;
import org.example.com.prompt.user.DatePrompt;
import org.example.com.util.BookFileManager;
// import org.example.com.util.DateFileManager;
// import org.example.com.util.RentalFileManager;

public class AdminPrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n🛠️ AdminPrompt");
            System.out.println("1. 도서 추가");
            System.out.println("2. 도서 삭제");
            System.out.println("3. 도서 프롬프트");
            System.out.println("4. 데이터 파일 저장 (도서, 날짜, 대여 중 하나를 인자로 입력하세요)");
            System.out.println("5. 날짜변경");
            System.out.println("6. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            if (input.equals("4")) {
                System.out.println("❌ 명령어 4번은 인자가 필요합니다. 도서/날짜/대여 중 하나를 입력해주세요.");
                continue;
            }

            if (input.startsWith("4 ")) {
                String[] parts = input.split("\\s+");
                if (parts.length == 2) {
                    String type = parts[1];
                    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String filename;
                    switch (type) {
                        case "도서":
                            filename = today + "-도서-데이터-파일.txt";
                            BookFileManager.saveToFile(new File(filename).getAbsolutePath(), BookFileManager.loadBooks());
                            System.out.println("✅ 도서 데이터 저장 완료 (" + filename + ")");
                            break;
                        // case "날짜":
                        //     filename = today + "-날짜-데이터-파일.txt";
                        //     DateFileManager.saveToFile(filename);
                        //     System.out.println("✅ 날짜 데이터 저장 완료 (" + filename + ")");
                        //     break;
                        // case "대여":
                        //     filename = today + "-대여-데이터-파일.txt";
                        //     RentalFileManager.saveToFile(filename);
                        //     System.out.println("✅ 대여 데이터 저장 완료 (" + filename + ")");
                        //     break;
                        default:
                            System.out.println("❌ 잘못된 항목입니다. '도서', '날짜', '대여' 중 하나를 입력하세요.");
                    }
                    continue;
                } else {
                    System.out.println("❌ 올바른 형식은 '4 도서' 와 같이 입력해야 합니다.");
                    continue;
                }
            }

            switch (input) {
                case "1":
                    new BookAddPrompt().start();
                    break;
                case "2":
                    new BookDeletePrompt().start();
                    break;
                case "3":
                    new BookPrompt("admin").start();
                    break;
                case "5":
                    new DatePrompt().start();
                    break;
                case "6":
                    System.out.println("👋 관리자 메뉴를 종료합니다.");
                    return;
                default:
                    System.out.println("❌ 입력에 해당하는 명령어가 없습니다. 1~6 중 하나만 입력해 주세요.");
            }
        }
    }
}