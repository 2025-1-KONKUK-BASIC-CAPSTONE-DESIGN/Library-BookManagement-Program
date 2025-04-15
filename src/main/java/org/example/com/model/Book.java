package org.example.com.model;

public class Book {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int totalQuantity;
    private int availableQuantity;

    public Book(String title, String author, String publisher, String isbn, int totalQuantity) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity; // 처음에는 전체 수량과 같음
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }


    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }


    public String toDataString() {
        return String.join("\t",
                title, author, publisher, isbn,
                String.valueOf(availableQuantity),
                String.valueOf(totalQuantity)
        );
    }

    public static Book fromDataString(String line) {
        String[] parts = line.split("\\t");
        if (parts.length != 6) return null;

        Book book = new Book(
                parts[0], parts[1], parts[2],
                parts[3], Integer.parseInt(parts[5])
        );
        book.setAvailableQuantity(Integer.parseInt(parts[4]));
        return book;
    }
}