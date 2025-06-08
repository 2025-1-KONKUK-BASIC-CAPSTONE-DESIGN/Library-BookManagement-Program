package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.*;
import java.util.regex.Pattern;

public class BookDeletePrompt {
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern BOOK_ID_PATTERN = Pattern.compile("^LIB\\d{2}-\\d{5}$");

    public void start() {
        List<Book> books = BookFileManager.loadBooks();
        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        Book targetBook = null;

        while (true) {
            System.out.print("deleteBookISBN: ");
            String isbn = scanner.nextLine().trim();

            if (!ISBN_PATTERN.matcher(isbn).matches()) {
                System.out.println("!! ISBN을 입력해주세요. ISBN은 공백없는 13자리 숫자로 구성돼있습니다.");
                continue;
            }

            if (!BookFileManager.isIsbnExists(isbn)) {
                System.out.println("❌ 해당 ISBN에 해당하는 도서가 없습니다.");
                return;
            }

            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    targetBook = book;
                    break;
                }
            }

            if (targetBook == null) {
                System.out.println("❌ 시스템 오류: 해당 ISBN 도서 객체를 찾을 수 없습니다.");
                return;
            }

            System.out.println("제목: " + targetBook.getTitle());
            System.out.println("대출 가능 수량: " + targetBook.getAvailableQuantity());
            System.out.println("장서관리번호");
            for (Book book : books) {
                if (book.getIsbn().equals(targetBook.getIsbn()) && book.getAvailableQuantity() > 0) {
                    System.out.println(book.getBookId());
                }
            }
            break;
        }

        // 장서관리번호 입력 및 삭제
        while (true) {
            System.out.print("삭제할 장서관리번호를 입력하세요: ");
            String bookIdInput = scanner.nextLine().trim();

            // 문법 형식 체크
            if (!BOOK_ID_PATTERN.matcher(bookIdInput).matches()) {
                System.out.println("!! 올바른 형식이 아닙니다. LIBOO-OOOOO 형식으로 다시 입력해주세요.");
                continue;
            }

            // 해당 도서의 장서관리번호에 존재하는지 체크
            if (!BookFileManager.isBookIdExists(bookIdInput)) {
                System.out.println("!! 해당 도서에는 입력한 장서관리번호가 없습니다. 다시 입력해주세요.");
                // 도서 정보 재출력
                System.out.println("제목: " + targetBook.getTitle());
                System.out.println("대출 가능 수량: " + targetBook.getAvailableQuantity());
                System.out.println("장서관리번호");
                for (Book book : books) {
                    if (book.getIsbn().equals(targetBook.getIsbn()) && book.getAvailableQuantity() > 0) {
                        System.out.println(book.getBookId());
                    }
                }
                continue;
            }

            Book bookToRemove = null;
            for (Book book : books) {
                if (book.getBookId().equals(bookIdInput)) {
                    bookToRemove = book;
                    break;
                }
            }

            if (bookToRemove != null) {
                if (bookToRemove.getAvailableQuantity() == 0) {
                    System.out.println("!! 현재 수량이 0인 도서는 삭제할 수 없습니다.");
                    continue;
                }

                books.remove(bookToRemove);
                BookFileManager.saveAllBooks(books);
                System.out.println("‘도서 삭제가 완료되었습니다.’");
                return;
            } else {
                System.out.println("!! 해당 장서관리번호를 가진 도서가 없습니다.");
            }
        }
    }
}