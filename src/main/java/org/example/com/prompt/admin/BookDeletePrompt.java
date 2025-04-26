package org.example.com.prompt.admin;

import org.example.com.model.Book;
import org.example.com.util.BookFileManager;

import java.util.*;
import java.util.regex.Pattern;

public class BookDeletePrompt {
    private final Scanner scanner = new Scanner(System.in);
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern QUANTITY_PATTERN = Pattern.compile("^(?!\\s)\\d{1,3}(?!\\s)$");

    public void start() {
        List<Book> books = BookFileManager.loadBooks();
        if (books.isEmpty()) {
            System.out.println("📭 등록된 도서가 없습니다.");
            return;
        }

        Book targetBook = null;

        while (true) {
            System.out.print("deleteBookISBN: ");
            String isbn = scanner.nextLine();

            if (!ISBN_PATTERN.matcher(isbn).matches() || !isbn.equals(isbn.trim())) {
                System.out.println("!! ISBN을 입력해주세요. ISBN은 공백없는 13자리 숫자로 구성돼있습니다.");
                continue;
            }

            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    targetBook = book;
                    break;
                }
            }

            if (targetBook == null) {
                System.out.println("❌ 해당 ISBN에 해당하는 도서가 없습니다.");
                return;
            }

            System.out.println("제목: " + targetBook.getTitle());
            System.out.println("대출 가능 수량: " + targetBook.getAvailableQuantity());
            break;
        }

        while (true) {
            System.out.print("삭제 권수를 입력하세요: ");
            String input = scanner.nextLine();

            if (!QUANTITY_PATTERN.matcher(input).matches() || !input.equals(input.trim())) {
                System.out.println("■ 숫자만 와야합니다.\n■ 이 때 숫자 앞, 뒤로 공백이 오면 안됩니다.\n■ 최대 3자리수까지 가능합니다.\n■ 숫자 앞 0은 입력 가능합니다.\n즉, 0 ~ 999 까지 입력 가능합니다. 0, 00, 000, 01, 001 모두 입력 가능한 숫자입니다.");
                continue;
            }

            int deleteQty = Integer.parseInt(input);
            if (deleteQty > targetBook.getAvailableQuantity()) {
                System.out.println("!! 대출 가능 수량보다 더 큰 값을 입력할 수 없습니다.");
                continue;
            }

            targetBook.setAvailableQuantity(targetBook.getAvailableQuantity() - deleteQty);
            targetBook.setTotalQuantity(targetBook.getTotalQuantity() - deleteQty);

            BookFileManager.saveAllBooks(books);
            System.out.println("‘도서 삭제가 완료되었습니다’");
            return;
        }
    }
}