package org.example.com.prompt.user;

import org.example.com.model.Book;
import org.example.com.util.*;
import org.example.com.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LoanPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public LoanPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {

        if (currentUser.getLoanCount() >= 5) {
            System.out.println("❌ 대출 권한 초과: 최대 5권까지 대출할 수 있습니다.");
            return;
        }

        List<Book> books = BookFileManager.loadBooks();

        boolean hasAvailable = false;
        System.out.println("\n📚 대출 가능한 도서 목록:");
        for (Book book : books) {
            if (book.getAvailableQuantity() > 0) {
                hasAvailable = true;
                System.out.printf("- %s / 저자: %s / ISBN: %s / 남은 수량: %d\n",
                        book.getTitle(), book.getAuthor(), book.getIsbn(), book.getAvailableQuantity());
            }
        }

        if (!hasAvailable) {
            System.out.println("❗ 대출 가능한 도서가 없습니다.");
            return;
        }

        while (true) {
            System.out.print("\n대출할 책의 ISBN을 입력해주세요 (취소: 0): ");
            String isbn = scanner.nextLine();

            if (isbn.equals("0")) {
                System.out.println("❗ 대출을 취소했습니다.");
                return;
            }

            if (!isbn.matches("^\\d{13}$")) {
                System.out.println("❌ ISBN 형식이 올바르지 않습니다. 공백 없는 13자리 숫자를 입력해주세요.");
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

            if (selectedBook.getAvailableQuantity() <= 0) {
                System.out.println("❌ 해당 도서는 현재 대출이 불가능합니다.");
                continue;
            }

            selectedBook.setAvailableQuantity(selectedBook.getAvailableQuantity() - 1);
            currentUser.setLoanCount(currentUser.getLoanCount() + 1);
            BookFileManager.saveAllBooks(books);
            FileManager.updateUser(currentUser);

            long remainingDays = 13L; // 대출 기본 기간
            LocalDate loadedDate = DateManager.loadDateFromFile();
            if (loadedDate == null) {
                System.err.println("날짜 데이터가 없습니다.");
                return;
            }

            // 대출 기록을 rental_data.txt에 저장
            String loanRecord = String.join("\t",
                    selectedBook.getIsbn(),
                    selectedBook.getTitle(),
                    currentUser.getId(),
                    loadedDate.toString(),  //대출일
                    loadedDate.plusDays(remainingDays).toString(),
                    ""  // 아직 반납하지 않았으므로 빈 문자열
            );
            LoanManager.saveLoanRecord(loanRecord);

            System.out.printf("%s이 대출되었습니다. 현재 %d권 대출하였으며, 잔여 반납일은 %d일입니다.\n",
                    selectedBook.getTitle(), currentUser.getLoanCount(), remainingDays);
            return;
        }
    }
}