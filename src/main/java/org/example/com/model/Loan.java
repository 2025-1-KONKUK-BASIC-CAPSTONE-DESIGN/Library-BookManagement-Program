package org.example.com.model;

public class Loan {
    private final String isbn;
    private final String title;
    private final String userId;
    private final String loanDate;
    private final String dueDate;
    private String returnDate;

    public Loan(String isbn, String title, String userId, String loanDate, String dueDate, String returnDate) {
        this.isbn = isbn;
        this.title = title;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public static Loan fromDataString(String line) {
        String[] parts = line.split("\t");
        if (parts.length < 5) {
            return null;
        }
        String isbn = parts[0];
        String title = parts[1];
        String userId = parts[2];
        String loanDate = parts[3];
        String dueDate = parts[4];
        String returnDate = (parts.length > 5) ? parts[5] : "";
        return new Loan(isbn, title, userId, loanDate, dueDate, returnDate);
    }

    public String getIsbn() {
        return isbn;
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
