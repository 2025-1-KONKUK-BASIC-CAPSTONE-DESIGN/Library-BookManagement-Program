package org.example.com.prompt.user;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;
import org.example.com.model.User;

import java.util.List;
import java.util.Scanner;

public class LoanPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public LoanPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 대출할 수 있는 도서가 없습니다.");
            return;
        }

        System.out.println("\n📚 대출 가능한 도서 목록:");
        int availableCount = 0;
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getAvailableQuantity() > 0) {
                System.out.printf("%d. %s / 저자: %s / ISBN: %s / 남은 수량: %d\n",
                        i + 1, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getAvailableQuantity());
                availableCount++;
            }
        }

        if (availableCount == 0) {
            System.out.println("❗ 모든 도서가 대출 중입니다.");
            return;
        }

        int index;
        while (true) {
            System.out.print("대출할 도서 번호를 입력하세요 (취소: 0): ");
            String input = scanner.nextLine().trim();
            try {
                index = Integer.parseInt(input);
                if (index == 0) {
                    System.out.println("❗ 대출을 취소했습니다.");
                    return;
                }
                if (index < 1 || index > books.size()) {
                    System.out.println("❌ 유효한 번호를 입력해주세요.");
                    continue;
                }
                if (books.get(index - 1).getAvailableQuantity() <= 0) {
                    System.out.println("❌ 해당 도서는 대출이 불가능합니다.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자로 입력해주세요.");
            }
        }

        Book selected = books.get(index - 1);
        selected.setAvailableQuantity(selected.getAvailableQuantity() - 1);
        currentUser.setLoanCount(currentUser.getLoanCount() + 1);
        BookFileManager.saveAllBooks(books);

        System.out.println("✅ 도서 [" + selected.getTitle() + "] 대출이 완료되었습니다.");
    }
}