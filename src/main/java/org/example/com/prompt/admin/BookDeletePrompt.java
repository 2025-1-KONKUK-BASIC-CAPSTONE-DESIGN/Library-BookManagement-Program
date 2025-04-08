package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.List;
import java.util.Scanner;

public class BookDeletePrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 삭제할 도서가 없습니다.");
            return;
        }

        System.out.println("\n📕 도서 목록:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.printf("%d. %s / %s / %s / ISBN: %s / 재고: %d\n",
                    i + 1, book.getTitle(), book.getAuthor(), book.getPublisher(),
                    book.getIsbn(), book.getTotalQuantity());
        }

        int index;
        while (true) {
            System.out.print("삭제할 도서 번호를 입력하세요 (취소: 0): ");
            String input = scanner.nextLine().trim();
            try {
                index = Integer.parseInt(input);
                if (index == 0) {
                    System.out.println("❗ 삭제를 취소했습니다.");
                    return;
                }
                if (index < 1 || index > books.size()) {
                    System.out.println("❌ 유효한 번호를 입력해주세요.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자로 입력해주세요.");
            }
        }

        Book removed = books.remove(index - 1);
        BookFileManager.saveAllBooks(books);
        System.out.println("✅ 도서 [" + removed.getTitle() + "] 가 삭제되었습니다.");
    }
}