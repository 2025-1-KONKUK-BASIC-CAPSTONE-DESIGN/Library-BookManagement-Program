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
        if (data.length() > MAX_LINE_LENGTH) {
            System.out.println("❌ 도서 정보가 너무 깁니다 (" + data.length() + "자). 최대 "
                + MAX_LINE_LENGTH + "자 이내로 입력해주세요.");
            return false;
        }
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
}
