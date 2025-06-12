package org.example.com.prompt.user;

import org.example.com.model.Loan;
import org.example.com.model.User;
import org.example.com.util.DateManager;
import org.example.com.util.LoanManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class ExtendPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final User currentUser;

    public ExtendPrompt(User user) {
        this.currentUser = user;
    }

    public void start() {
        LoanManager.getUserLoanRecord(currentUser.getId());
        while(true) {
            if(currentUser.isOverdueDays() == true) {
                System.out.println("!!연체 상태입니다. 도서를 연장할 수 없습니다.");
                return;
            }

            if(currentUser.getTotalPenalty() > 0) {
                System.out.println("!!패널티 상태입니다. 도서를 연장할 수 없습니다.");
                return;
            }

            System.out.print("연장할 책의 ISBN을 입력해주세요::");
            String isbn = scanner.nextLine();

            if (!LoanManager.isIsbnExists(isbn)) {
                System.out.println("!! 현재 대출 중인 도서가 아닙니다.");
                return; //돌아가기
            }

            if(!canExtendLoan(isbn)) {
                System.out.println("!! 해당 도서는 이미 연장되었습니다.");
                return;
            }

            extendLoan(isbn);
            return;
        }
    }

    private boolean canExtendLoan(String isbn) {
        return extendableLoan(isbn).isPresent();
    }

    private Optional<Loan> extendableLoan(String isbn) {
        return LoanManager.getUserNowLoanRecord().stream()
                .filter(loan -> loan.getIsbn().equals(isbn))
                .sorted(Comparator.comparing(loan -> LocalDate.parse(loan.getLoanDate())))
                .filter(loan -> !loan.isExtended())
                .findFirst();
    }

    private void extendLoan(String isbn) {
        Optional<Loan> optionalLoan = extendableLoan(isbn);
        if(optionalLoan.isEmpty()) {
            System.out.println("처리 중 오류가 생겼습니다.");
            return;
        }
        Loan loan = optionalLoan.get();
        loan.setIsExtended(true);
        LocalDate dueDate = LocalDate.parse(loan.getDueDate());
        LocalDate newDueDate = dueDate.plusDays(7);
        loan.setDueDate(newDueDate.toString());
        LoanManager.updateLoan();
        LocalDate today = DateManager.loadDateFromFile();
//         오늘(today)부터 newReturnDate까지 남은 일수 계산
        assert today != null;
        long daysLeft = ChronoUnit.DAYS.between(today, newDueDate);
        System.out.println(loan.getTitle() + "도서의 반납 예정일이 " + newDueDate.getYear() + "년 " + newDueDate.getMonthValue() + "월 " + newDueDate.getDayOfMonth()+"로 연장되었습니다.");
        System.out.println("현재 "+ currentUser.getLoanCount() + "권 대출 중이며, 연장된 반납일까지 " + daysLeft + "일 남았습니다.");
    }
}
