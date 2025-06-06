package org.example.com.prompt.book;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookListPrompt {
    public void listAllBook() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        // 1. ISBN 기준 집계
        Map<String, Book> bookMap = new LinkedHashMap<>();
        Map<String, Integer> availableMap = new LinkedHashMap<>(); // 가용 수량 합산용

        for (Book book : books) {
            String isbn = book.getIsbn();
            if (!bookMap.containsKey(isbn)) {
                // 임시 Book 객체 생성 (가용 수량은 나중에 set)
                Book newBook = new Book(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getIsbn(),
                        book.getTotalQuantity(),
                        null // bookId는 집계에 불필요
                );
                bookMap.put(isbn, newBook);
                availableMap.put(isbn, book.getAvailableQuantity());
            } else {
                Book agg = bookMap.get(isbn);
                agg.setTotalQuantity(agg.getTotalQuantity() + book.getTotalQuantity());
                availableMap.put(isbn, availableMap.get(isbn) + book.getAvailableQuantity());
            }
        }

        // 최종적으로 availableQuantity를 세팅
        for (String isbn : bookMap.keySet()) {
            bookMap.get(isbn).setAvailableQuantity(availableMap.get(isbn));
        }

        System.out.println("\n📚 전체 도서 목록:");
        System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20s\t%-15s\n", "ISBN", "도서명", "저자", "출판사", "대출 가능 수량", "전체 수량");
        for (Book book : bookMap.values()) {
            System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20d\t%-15d\n",
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getAvailableQuantity(),
                    book.getTotalQuantity()
            );
        }
    }


    public void listAvailableBook() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        System.out.println("\n📚 대출 가능 도서 목록:");

        // ISBN 기준 집계
        Map<String, Book> bookMap = new LinkedHashMap<>();
        Map<String, Integer> availableMap = new LinkedHashMap<>(); // 가용 수량 합산용

        for (Book book : books) {
            String isbn = book.getIsbn();
            if (!bookMap.containsKey(isbn)) {
                // 임시 Book 객체 생성 (가용 수량은 나중에 set)
                Book newBook = new Book(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getIsbn(),
                        book.getTotalQuantity(),
                        null // bookId는 집계에 불필요
                );
                bookMap.put(isbn, newBook);
                availableMap.put(isbn, book.getAvailableQuantity());
            } else {
                Book agg = bookMap.get(isbn);
                agg.setTotalQuantity(agg.getTotalQuantity() + book.getTotalQuantity());
                availableMap.put(isbn, availableMap.get(isbn) + book.getAvailableQuantity());
            }
        }

        // 최종적으로 availableQuantity를 세팅
        for (String isbn : bookMap.keySet()) {
            bookMap.get(isbn).setAvailableQuantity(availableMap.get(isbn));
        }

        System.out.println("\n📚 대출 가능 도서 목록:");
        System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20s\t%-15s\n", "ISBN", "도서명", "저자", "출판사", "대출 가능 수량", "전체 수량");
        for (Book book : bookMap.values()) {
            if (book.getAvailableQuantity() > 0) {
                System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20d\t%-15d\n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getAvailableQuantity(),
                        book.getTotalQuantity()
                );
            }
        }
    }
}
