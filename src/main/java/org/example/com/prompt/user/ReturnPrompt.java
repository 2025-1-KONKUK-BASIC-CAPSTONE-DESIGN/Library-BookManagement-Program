package org.example.com.prompt.user;

import org.example.com.model.Book;
import org.example.com.model.Loan;
import org.example.com.model.User;
import org.example.com.util.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        List<Loan> loans = LoanManager.loadNotReturnedLoans(currentUser.getId());
        List<Book> books = BookFileManager.loadBooks();

        System.out.println("\n📕 반납 가능한 도서 목록:");
        boolean found = false;

        LocalDate today = DateManager.loadDateFromFile();
        if (today == null) {
            System.err.println("today 데이터 값이 없습니다.");
            return;
        }

        for (Loan loan : loans) {
            found = true;
            LocalDate dueDate = LocalDate.parse(loan.getDueDate());
            long remainingDays = ChronoUnit.DAYS.between(today, dueDate);
            if (remainingDays < 0) {
                System.out.printf("ISBN: %s / 도서명: %s / 대출일: %s / 반납 예정일: %s / 연체 일수: %d일\n",
                        loan.getIsbn(), loan.getTitle(), loan.getLoanDate(), loan.getDueDate(), -remainingDays);
            } else {
                System.out.printf("ISBN: %s / 도서명: %s / 대출일: %s / 반납 예정일: %s / 남은 일수: %d일\n",
                        loan.getIsbn(), loan.getTitle(), loan.getLoanDate(), loan.getDueDate(), remainingDays);
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

            String isbnError = Validator.validateIsbnDetailed(isbn);
            if (!isbnError.isEmpty()) {
                System.out.println(isbnError);
                continue;
            }


            Loan selectedLoan = loans.stream()
                    .filter(l -> l.getIsbn().equals(isbn))
                    .findFirst()
                    .orElse(null);
            Book selectedBook;

            if (selectedLoan == null) {
                System.out.println("!! 목록에 존재하지 않는 도서입니다.");
                continue;
            }

            selectedLoan.setReturnDate(today.toString());   //대출일 지정
// 2차 추가 연체 정보 반영
            int overdue = LoanManager.calculateOverdueDays(selectedLoan);
            int penalty = LoanManager.calculatePenalty(overdue);
            selectedLoan.setOverdueDays(overdue);
            selectedLoan.setPenaltyLeft(penalty);

            if (penalty > 0) {
                currentUser.setTotalPenalty(currentUser.getTotalPenalty() + penalty);
            }
            selectedBook = books.stream()
                    .filter(book -> book.getBookId().equals(selectedLoan.getBookId()))
                    .findFirst()
                    .orElse(null);

            if (selectedBook == null) {
                System.out.println("!! 목록에 존재하지 않는 도서입니다.");
                continue;
            }

            selectedBook.setAvailableQuantity(selectedBook.getAvailableQuantity() + 1);
            currentUser.setLoanCount(currentUser.getLoanCount() - 1);
            BookFileManager.saveAllBooks(books);
            FileManager.updateUser(currentUser);
            LoanManager.updateLoan();

            long overdueDays = LoanManager.maxOverdueDays(today, loans);

            System.out.printf("%s이 반납되었습니다. 현재 %d권 대출하였으며, 연체일은 %d일입니다.\n",
                    selectedLoan.getTitle(), currentUser.getLoanCount(), overdueDays);

            return;
        }
    }
}
