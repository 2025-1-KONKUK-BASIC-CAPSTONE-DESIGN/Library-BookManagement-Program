package org.example.com.prompt.user;

import org.example.com.model.Book;
import org.example.com.model.User;
import org.example.com.util.BookFileManager;
import org.example.com.util.Validator;
import org.example.com.util.FileManager;
import java.time.temporal.ChronoUnit;
import org.example.com.util.DateManager;
import org.example.com.model.Date;
import java.time.LocalDate;
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
        boolean found = false;
        for (Book book : books) {
            if (book.getAvailableQuantity() < book.getTotalQuantity()) {
                found = true;
                System.out.printf("- %s / 저자: %s / ISBN: %s / 대출 중: %d권\n",
                        book.getTitle(), book.getAuthor(), book.getIsbn(),
                        book.getTotalQuantity() - book.getAvailableQuantity());
            }
        }

        if (!found) {
            System.out.println("❗ 현재 반납 가능한 도서가 없습니다.");
            return;
        }

        while (true) {
            System.out.print("\n반납할 책의 ISBN을 입력해주세요 (취소: 0): ");
            String isbn = scanner.nextLine();

            if (isbn.equals("0")) {
                System.out.println("❗ 반납을 취소했습니다.");
                return;
            }

            if (!isbn.matches("^\\d{13}$")) {
                System.out.println("❌ ISBN 형식이 올바르지 않습니다. 공백 없는 13자리 숫자를 입력해주세요.");
                continue;
            }



            String isbnError = Validator.validateIsbnDetailed(isbn);
            if (!isbnError.isEmpty()) {
                System.out.println(isbnError);
                continue;
            }


            Book selectedBook = null;
            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    selectedBook = book;
                    break;
                }
            }

            if (selectedBook == null) {
                System.out.println("!! 목록에 존재하지 않는 도서입니다.");
                continue;
            }

            if (selectedBook.getAvailableQuantity() == selectedBook.getTotalQuantity()) {
                System.out.println("!! 고유하지 않은 ISBN입니다.");
                continue;
            }


            selectedBook.setAvailableQuantity(selectedBook.getAvailableQuantity() + 1);
            currentUser.setLoanCount(currentUser.getLoanCount() - 1);
            BookFileManager.saveAllBooks(books);
            FileManager.updateUser(currentUser);


            LocalDate today = LocalDate.parse(DateManager.loadDateFromFile().getValue());
            LocalDate loanDate = LocalDate.parse(DateManager.loadLoanDateFromFile().getValue());
            long daysBetween = ChronoUnit.DAYS.between(loanDate, today);
            int overdueDays = (int) Math.max(0, daysBetween - 13);
            System.out.printf("%s이 반납되었습니다. 현재 %d권 대출하였으며, 연체일은 %d일입니다.\n",
                    selectedBook.getTitle(), currentUser.getLoanCount(), overdueDays);


            return;
        }
    }
}
