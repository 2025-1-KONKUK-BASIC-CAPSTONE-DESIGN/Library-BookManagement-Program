package org.example.com.prompt.book;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.List;

public class BookListPrompt {
    public void listAllBook() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        System.out.println("\n📚 전체 도서 목록:");
        System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20s\t%-15s\t%11s\n", "ISBN", "도서명", "저자", "출판사", "대출 가능 수량", "전체 수량", "장서관리번호");
        for (Book book : books) {
            System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20d\t%-15d\t%11s\n",
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getAvailableQuantity(),
                book.getTotalQuantity(),
                book.getBookId()
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

        System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20s\t%-15s\t%11s\n", "ISBN", "도서명", "저자", "출판사", "대출 가능 수량", "전체 수량", "장서관리번호");
        for (Book book : books) {
            if (book.getAvailableQuantity()>0) {
                System.out.printf("%-15s\t%-30s\t%-15s\t%-15s\t%-20d\t%-15d\t%11s\n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getAvailableQuantity(),
                        book.getTotalQuantity(),
                        book.getBookId()
                );
            }
        }
    }
}
