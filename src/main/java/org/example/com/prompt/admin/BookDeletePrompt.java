package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.*;
import java.util.regex.Pattern;

public class BookDeletePrompt {
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern BOOK_ID_PATTERN = Pattern.compile("^LIB\\d{2}-\\d{5}$");

    // 도서 삭제 전체 흐름
    public void start() {
        // ISBN으로 도서 삭제
        String isbn = deleteCheckIsbn();
        if (isbn == null) {
            return;
        }

        // 장서관리번호로 도서 삭제
        boolean bookIdDeleteResult = deleteCheckBookId(isbn);
        if (bookIdDeleteResult) {
            System.out.println("도서 삭제가 완료되었습니다.");
        }
    }

    public String deleteCheckIsbn() {
        List<Book> books = BookFileManager.loadBooks();
        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return null;
        }

        System.out.print("삭제할 도서의 ISBN을 입력하세요: ");
        String isbn = scanner.nextLine().trim();

        if (!ISBN_PATTERN.matcher(isbn).matches()) {
            System.out.println("!! ISBN은 공백없는 13자리 숫자로 구성돼있습니다.");
            return null;
        }

        boolean isbnExists = BookFileManager.isIsbnExists(isbn);
        if (!isbnExists) {
            System.out.println("❌ 해당 ISBN에 해당하는 도서가 없습니다.");
            return null;
        }

        // 도서 정보 출력
        System.out.println("해당 ISBN의 도서 정보:");

        // 대출 가능 수량 합산
        int totalAvailableQuantity = 0;
        String title = null;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                totalAvailableQuantity += book.getAvailableQuantity();
                if (title == null) {
                    title = book.getTitle();
                }
            }
        }

        if (totalAvailableQuantity == 0) {
            System.out.println("❌ 해당 ISBN의 모든 도서가 대출 중이거나 이용 불가 상태입니다. 삭제할 수 없습니다.");
            return null;
        }

        if (title != null) {
            System.out.println("제목: " + title);
            System.out.println("대출 가능 수량: " + totalAvailableQuantity);
            System.out.println("장서관리번호");
            for (Book b : books) {
                if (b.getIsbn().equals(isbn) && b.getAvailableQuantity()!=0) {
                    System.out.println(b.getBookId());
                }
            }
        }
        return isbn;
    }

    public boolean deleteCheckBookId(String isbn) {
        List<Book> books = BookFileManager.loadBooks();

        Map<String, List<String>> isbnToBookIds = new HashMap<>();
        for (Book book : books) {
            String bookId = book.getBookId();
            isbnToBookIds.computeIfAbsent(book.getIsbn(), k -> new ArrayList<>()).add(bookId);
        }

        String bookId;
        while (true) {
            System.out.print("삭제할 장서관리번호를 입력하세요: ");
            String input = scanner.nextLine().trim();

            if (!BOOK_ID_PATTERN.matcher(input).matches()) {
                System.out.println("!! 올바른 형식이 아닙니다. LIBOO-OOOOO 형식으로 다시 입력해주세요.");
                continue;
            }
            bookId = input;
            List<String> bookIds = isbnToBookIds.get(isbn);

            if (bookIds == null || !bookIds.contains(bookId)) {
                System.out.println("입력한 장서관리번호는 해당 ISBN의 도서 목록에 없습니다.");
                return false;
            }
            break;
        }


        String finalBookId = bookId;
        boolean removed = books.removeIf(book ->
                book.getIsbn().equals(isbn) && book.getBookId().equals(finalBookId));
        if (removed) {
            BookFileManager.saveAllBooks(books);
            return true;
        } else {
            System.out.println("삭제 과정에서 오류가 발생했습니다.");
            return false;
        }
    }
}
