// src/main/java/org/example/com/prompt/admin/BookAddPrompt.java
package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;
import org.example.com.util.Validator;
import java.util.Scanner;

public class BookAddPrompt {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n 도서 추가를 시작합니다.");

        System.out.print("도서명을 입력하세요: ");
        String title = scanner.nextLine().trim();

        System.out.print("저자를 입력하세요: ");
        String author = scanner.nextLine().trim();

        System.out.print("출판사를 입력하세요: ");
        String publisher = scanner.nextLine().trim();

        String isbn;
        while (true) {
            System.out.print("ISBN (13자리 숫자)을 입력하세요: ");
            isbn = scanner.nextLine().trim();
            if (!Validator.isValidISBN(isbn)) {
                System.out.println("❌ ISBN은 공백 없는 13자리 숫자만 가능합니다.");
                continue;
            }
            break;
        }

        int total;
        while (true) {
            System.out.print("전체 수량을 입력하세요: ");
            String input = scanner.nextLine().trim();
            try {
                total = Integer.parseInt(input);
                if (total <= 0 || total > 9999) {
                    System.out.println("❌ 수량은 1~9999 사이의 숫자여야 합니다.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자로 입력해주세요.");
            }
        }

        Book newBook = new Book(title, author, publisher, isbn, total);
        boolean saved = BookFileManager.saveBook(newBook);

        if (saved) {
            System.out.println("✅ 도서가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("❌ 도서 추가에 실패했습니다. 다시 시도해주세요.");
        }
    }
}
