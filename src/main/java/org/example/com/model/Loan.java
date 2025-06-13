package org.example.com.model;

public class Loan {
    private final String isbn;
    private final String bookId;
    private final String title;
    private final String userId;
    private final String loanDate;
    private String dueDate;
    private String returnDate;
    private int overdueDays;
    private int penaltyLeft;
    private boolean isExtended;

    public Loan(String isbn, String bookId, String title, String userId, String loanDate, String dueDate, String returnDate) {
        this.isbn = isbn;
        this.bookId = bookId;
        this.title = title;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        // 2차 추가
        this.overdueDays = 0;
        this.penaltyLeft = 0;
        this.isExtended = false;
    }

    public static Loan fromDataString(String line) {
        String[] parts = line.split("\t");
        if (parts.length < 6) {
            return null;
        }
        String isbn = parts[0];
        String bookId = parts[1];
        String title = parts[2];
        String userId = parts[3];
        String loanDate = parts[4];
        String dueDate = parts[5];
        String returnDate = (parts.length > 6) ? parts[6] : "";
        return new Loan(isbn, bookId, title, userId, loanDate, dueDate, returnDate);
    }
    // 2차 추가 전체 데이터를 문자열로 반환하는 메서드
    public String toFullDataString() {
        return String.join("\t",
                isbn,
                bookId,
                title,
                userId,
                loanDate,
                dueDate,
                returnDate == null ? "" : returnDate,
                String.valueOf(overdueDays),
                String.valueOf(penaltyLeft),
                isExtended ? "Y" : "N"
        );
    }
    // 2차 추가 연체, 패널티, 연장 여부를 포함한 전체 데이터 파서
    public static Loan fromFullDataString(String line) {
        String[] parts = line.split("\t");
        if (parts.length < 6) return null;  // 최소한 대출정보는 있어야 함

        String isbn = parts[0];
        String bookId = parts[1];
        String title = parts[2];
        String userId = parts[3];
        String loanDate = parts[4];
        String dueDate = parts[5];
        String returnDate = parts.length > 6 ? parts[6] : "";

        int overdueDays = (parts.length > 7) ? Integer.parseInt(parts[7]) : 0;
        int penaltyLeft = (parts.length > 8) ? Integer.parseInt(parts[8]) : 0;
        boolean isExtended = (parts.length > 9) && parts[9].equalsIgnoreCase("Y");

        Loan loan = new Loan(isbn, bookId, title, userId, loanDate, dueDate, returnDate);
        loan.setOverdueDays(overdueDays);
        loan.setPenaltyLeft(penaltyLeft);
        loan.setIsExtended(isExtended);
        return loan;
    }


    public String getIsbn() {
        return isbn;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
// 2차 추가
    public int getOverdueDays() {
        return overdueDays;
    }

    public int getPenaltyLeft() {
        return penaltyLeft;
    }

    public boolean getIsExtended() {
        return isExtended;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public void setPenaltyLeft(int penaltyLeft) {
        this.penaltyLeft = penaltyLeft;
    }

    public void setIsExtended(boolean isExtended) {
        this.isExtended = isExtended;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate.trim();
    }

}
