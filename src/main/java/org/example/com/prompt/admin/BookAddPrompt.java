package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.List;
import java.util.Scanner;

public class BookAddPrompt {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n📗 도서 추가를 시작합니다.");

        // ISBN 입력
        String isbn;
        while (true) {
            System.out.print("ISBN (13자리 숫자)을 입력하세요: ");
            isbn = scanner.nextLine().trim();
            if (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
                System.out.println("❌ ISBN은 공백 없는 13자리 숫자만 가능합니다.");
                continue;
            }
            break;
        }

        // 중복 ISBN 확인 및 수량 증가
        List<Book> books = BookFileManager.loadBooks();
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                book.setTotalQuantity(book.getTotalQuantity() + 1);
                BookFileManager.saveAllBooks(books);
                System.out.println("⚠️ 중복된 도서가 있습니다. 권수를 하나 추가 완료하였습니다.");
                return;
            }
        }

        // 도서명 입력 (50자 이내로 제한)
        String title;
        while (true) {
            System.out.print("도서명을 입력하세요 (최대 50자): ");
            title = scanner.nextLine().trim();
            int length = (int)title.codePoints().count();
            if (length == 0) {
                System.out.println("❌ 도서명은 최소 1자 이상이어야 합니다.");
                continue;
            }
            if (length > 50) {
                System.out.println("❌ 도서명은 50자 이내로 입력해주세요. (현재 " + length + "자)");
                continue;
            }
            if (title.contains("\t") || title.contains("\r") || title.contains("\n")) {
                System.out.println("❌ 도서명에 탭/개행 문자를 넣을 수 없습니다.");
                continue;
            }
            break;
        }

        // 저자 입력
        String author;
        while (true) {
            System.out.print("저자를 입력하세요: ");
            author = scanner.nextLine().trim();
            if (author.isEmpty() || author.contains("\n") || author.contains("\t")) {
                System.out.println("❌ 저자는 1자 이상이며, 탭이나 개행 문자를 포함할 수 없습니다.");
                continue;
            }
            break;
        }

        // 출판사 입력
        String publisher;
        while (true) {
            System.out.print("출판사를 입력하세요: ");
            publisher = scanner.nextLine().trim();
            if (publisher.isEmpty() || publisher.contains("\n") || publisher.contains("\t")) {
                System.out.println("❌ 출판사는 1자 이상이며, 탭이나 개행 문자를 포함할 수 없습니다.");
                continue;
            }
            break;
        }

        // 새 도서 생성 및 저장 시도
        Book newBook = new Book(title, author, publisher, isbn, 1);
        boolean saved = BookFileManager.saveBook(newBook);

        if (saved) {
            System.out.println("✅ 도서 추가가 완료되었습니다.");
        } else {
            System.out.println("❌ 도서 추가에 실패했습니다. (50자 초과 또는 저장 오류)");
        }
    }
}
