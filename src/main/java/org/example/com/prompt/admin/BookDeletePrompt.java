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
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                System.out.println("제목: " + book.getTitle());
                System.out.println("대출 가능 수량: " + book.getAvailableQuantity());
                System.out.println("장서관리번호");
                // 같은 ISBN의 모든 장서관리번호 출력
                for (Book b : books) {
                    if (b.getIsbn().equals(isbn)) {
                        System.out.println(b.getBookId());
                    }
                }
                break;
            }
        }
        return isbn;
    }

    public boolean deleteCheckBookId(String isbn) {
        List<Book> books = BookFileManager.loadBooks();

        // ISBN → 장서관리번호 Map 직접 만들기
        Map<String, List<String>> isbnToBookIds = new HashMap<>();
        for (Book book : books) {
            String bookId = book.getBookId();
            isbnToBookIds.computeIfAbsent(book.getIsbn(), k -> new ArrayList<>()).add(bookId);
        }

        // 이미 ISBN의 존재는 검증되었으니, 여기서는 바로 장서관리번호만 받으면 됨
        System.out.print("삭제할 장서관리번호를 입력하세요: ");
        String bookId = scanner.nextLine().trim();

        List<String> bookIds = isbnToBookIds.get(isbn);

        if (bookIds == null || !bookIds.contains(bookId)) {
            System.out.println("입력한 장서관리번호는 해당 ISBN의 도서 목록에 없습니다.");
            return false;
        }

        // 실제 삭제
        boolean removed = books.removeIf(book ->
                book.getIsbn().equals(isbn) && book.getBookId().equals(bookId));
        if (removed) {
            BookFileManager.saveAllBooks(books);
            return true;
        } else {
            System.out.println("삭제 과정에서 오류가 발생했습니다.");
            return false;
        }
    }
}
