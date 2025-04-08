package org.example.com.prompt.user;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;
import org.example.com.model.User;

import java.util.List;
import java.util.Scanner;

public class ReturnPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public ReturnPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        if (currentUser.getLoanCount() == 0) {
            System.out.println("📭 현재 반납할 도서가 없습니다.");
            return;
        }

        List<Book> books = BookFileManager.loadBooks();

        System.out.println("\n📕 반납 가능한 도서 목록:");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            if (b.getAvailableQuantity() < b.getTotalQuantity()) {
                System.out.printf("%d. %s / 저자: %s / 대출 중: %d권\n",
                        i + 1, b.getTitle(), b.getAuthor(),
                        b.getTotalQuantity() - b.getAvailableQuantity());
            }
        }

        int index;
        while (true) {
            System.out.print("반납할 도서 번호를 입력하세요 (취소: 0): ");
            String input = scanner.nextLine().trim();
            try {
                index = Integer.parseInt(input);
                if (index == 0) {
                    System.out.println("❗ 반납을 취소했습니다.");
                    return;
                }
                if (index < 1 || index > books.size()) {
                    System.out.println("❌ 유효한 번호를 입력해주세요.");
                    continue;
                }

                Book selected = books.get(index - 1);
                if (selected.getAvailableQuantity() == selected.getTotalQuantity()) {
                    System.out.println("❌ 해당 도서는 이미 모두 반납되었습니다.");
                    continue;
                }

                selected.setAvailableQuantity(selected.getAvailableQuantity() + 1);
                currentUser.setLoanCount(currentUser.getLoanCount() - 1);
                BookFileManager.saveAllBooks(books);

                System.out.println("✅ 도서 [" + selected.getTitle() + "] 반납이 완료되었습니다.");
                return;

            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자로 입력해주세요.");
            }
        }
    }
}