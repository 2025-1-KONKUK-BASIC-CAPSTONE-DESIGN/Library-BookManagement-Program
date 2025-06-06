package org.example.com.model;

public class Loan {
    private final String isbn;
    private final String bookId;
    private final String title;
    private final String userId;
    private final String loanDate;
    private final String dueDate;
    private String returnDate;

    public Loan(String isbn, String bookId, String title, String userId, String loanDate, String dueDate, String returnDate) {
        this.isbn = isbn;
        this.bookId = bookId;
        this.title = title;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
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
        return new Loan(isbn, title, userId, loanDate, dueDate, returnDate, bookId);
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

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate.trim();
    }

}
