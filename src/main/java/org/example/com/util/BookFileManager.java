package org.example.com.util;

import org.example.com.model.Book;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BookFileManager {
    private static final String BOOK_FILE_PATH = System.getProperty("user.home") + "/book_data.txt";
    // 한 줄(도서 데이터) 최대 글자 수
    private static final int MAX_LINE_LENGTH = 50;

    /**
     * 단일 도서를 파일 끝에 추가 저장
     * @return 저장 성공 시 true, 길이 초과나 I/O 오류 시 false
     */
    public static boolean saveBook(Book book) {
        String data = book.toDataString();
        try (BufferedWriter writer = new BufferedWriter(
                 new FileWriter(BOOK_FILE_PATH, true))) {
            writer.write(data);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("❌ 도서를 저장하는 중 오류 발생");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 파일에서 모든 도서 정보를 읽어 리스트로 반환
     */
    public static List<Book> loadBooks() {
        List<Book> list = new ArrayList<>();
        try {
            Path path = Paths.get(BOOK_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            for (String line : Files.readAllLines(path)) {
                if (line.trim().isEmpty()) continue;
                Book book = Book.fromDataString(line);
                if (book != null) list.add(book);
            }
        } catch (IOException e) {
            System.out.println("❌ 도서 정보를 불러오는 중 오류 발생");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 전체 도서를 파일에 덮어쓰기 저장
     */
    public static void saveAllBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE_PATH))) {
            for (Book book : books) {
                // ▶ 길이 검사 제거: 모든 도서를 그대로 기록
                writer.write(book.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ 전체 도서를 저장하는 중 오류 발생");
            e.printStackTrace();
        }
    }

    /**
     * 지정한 파일 경로({HOME} 치환 지원)에 전체 도서를 저장
     */
    public static void saveToFile(String filePath, List<Book> books) {
        String resolvedPath = filePath.replace("{HOME}", System.getProperty("user.home"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resolvedPath))) {
            for (Book book : books) {
                // ▶ 길이 검사 제거: 모든 도서를 그대로 기록
                writer.write(book.toDataString());
                writer.newLine();
            }
            System.out.println("✅ 파일 저장 완료: " + resolvedPath);
        } catch (IOException e) {
            System.out.println("❌ 파일 저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static boolean isIsbnExists(String isbn) {
        List<Book> books = loadBooks();
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBookIdExists(String bookId) {
        List<Book> books = loadBooks();
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    public static String generateNextBookId() {
        List<Book> books = BookFileManager.loadBooks();
        // 현재 연도 2자리 구하기
        int year = java.time.LocalDate.now().getYear() % 100;
        String yearStr = String.format("%02d", year);

        int maxId = 0;
        // 현재 연도에 해당하는 최대 순번 찾기
        for (Book book : books) {
            String bookId = book.getBookId();
            if (bookId != null && bookId.startsWith("LIB" + yearStr + "-")) {
                String idPart = bookId.substring(7); // "LIByy-" 다음 5자리
                try {
                    int id = Integer.parseInt(idPart);
                    if (id > maxId) maxId = id;
                } catch (NumberFormatException e) {
                    // 무시
                }
            }
        }
        // 최대값 제한 체크
        if (maxId >= 99999) {
            throw new IllegalStateException("장서번호가 LIB" + yearStr + "-99999를 초과할 수 없습니다.");
        }
        int nextId = maxId + 1;
        return String.format("LIB%s-%05d", yearStr, nextId);
    }
}