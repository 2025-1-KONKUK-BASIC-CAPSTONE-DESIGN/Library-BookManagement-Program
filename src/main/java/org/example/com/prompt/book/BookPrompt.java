package org.example.com.prompt.book;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.nio.charset.Charset;
import java.util.List;

public class BookPrompt {

    public void start() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        System.out.println("\n📚 전체 도서 목록:");
        for (Book book : books) {
            System.out.printf("- %s\n  저자: %s\n  출판사: %s\n  ISBN: %s\n  대출 가능: %d / 전체 수량: %d\n\n",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getIsbn(),
                    book.getAvailableQuantity(),
                    book.getTotalQuantity()
            );
        }
    }
}