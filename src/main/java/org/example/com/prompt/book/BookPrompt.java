package org.example.com.prompt.book;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.List;
import java.util.Scanner;

public class BookPrompt {
    private final Scanner scanner = new Scanner(System.in);
    private final String caller; // AdminPrompt 또는 UserPrompt

    public BookPrompt(String caller) {
        this.caller = caller;
    }

    public void start() {
        while (true) {
            System.out.println("\n📖 BookPrompt");
            System.out.println("1. 전체 도서 목록 보기");
            System.out.println("2. 대출 가능 도서 목록 보기");
            System.out.println("3. 종료");
            System.out.print("명령어를 입력하세요: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    showAllBooks();
                    break;
                case "2":
                    showAvailableBooks();
                    break;
                case "3":
                    System.out.println("📦 도서 프롬프트를 종료합니다. 이전 메뉴로 돌아갑니다.");
                    return;
                default:
                    System.out.println("❌ 잘못된 명령입니다. 1~3 중 하나를 입력해 주세요.");
            }
        }
    }

    private void showAllBooks() {
        List<Book> books = BookFileManager.loadBooks();

        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        System.out.println("\nISBN\t도서명\t저자\t출판사\t대출 가능 수량\t전체 수량");
        for (Book book : books) {
            System.out.printf("%s\t%s\t%s\t%s\t%d\t%d\n",
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getAvailableQuantity(),
                    book.getTotalQuantity()
            );
        }
    }

    private void showAvailableBooks() {
        List<Book> books = BookFileManager.loadBooks();

        boolean found = false;
        System.out.println("\nISBN\t도서명\t저자\t출판사\t대출 가능 수량\t전체 수량");
        for (Book book : books) {
            if (book.getAvailableQuantity() > 0) {
                System.out.printf("%s\t%s\t%s\t%s\t%d\t%d\n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getAvailableQuantity(),
                        book.getTotalQuantity()
                );
                found = true;
            }
        }

        if (!found) {
            System.out.println("📭 대출 가능한 도서가 없습니다.");
        }
    }
}
