package org.example.com.util;

import org.example.com.model.Book;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookFileManager {
    // 도서 데이터 파일 경로
    private static final String BOOK_FILE_PATH = System.getProperty("user.home") + "/book_data.txt";
    // 한 줄 최대 글자 수
    private static final int MAX_LINE_LENGTH = 50;

    /**
     * 파일에서 모든 도서 정보를 읽어 리스트로 반환
     */
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get(BOOK_FILE_PATH);
        if (!Files.exists(path)) {
            return books;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    books.add(Book.fromDataString(line));
                } catch (IllegalArgumentException ex) {
                    System.out.println("❌ 잘못된 도서 데이터 형식: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ 도서를 읽어오는 중 오류 발생");
            e.printStackTrace();
        }
        return books;
    }

    /**
     * 단일 도서를 파일 끝에 추가 저장
     */
    public static void saveBook(Book book) {
        String data = book.toDataString();
        if (data.length() > MAX_LINE_LENGTH) {
            System.out.println("❌ 도서 정보가 너무 깁니다 (" + data.length() + "자). 최대 "
                + MAX_LINE_LENGTH + "자 이내로 입력해주세요.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(
                 new FileWriter(BOOK_FILE_PATH, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❌ 도서를 저장하는 중 오류 발생");
            e.printStackTrace();
        }
    }

    /**
     * 전체 도서를 파일에 덮어쓰기 저장
     */
    public static void saveAllBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(
                 new FileWriter(BOOK_FILE_PATH))) {
            for (Book book : books) {
                String data = book.toDataString();
                if (data.length() > MAX_LINE_LENGTH) {
                    System.out.println("❌ 다음 도서 정보는 너무 깁니다 (" 
                        + data.length() + "자), 저장에서 제외됩니다: " 
                        + book.getTitle());
                    continue;
                }
                writer.write(data);
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
        try (BufferedWriter writer = new BufferedWriter(
                 new FileWriter(resolvedPath))) {
            for (Book book : books) {
                String data = book.toDataString();
                if (data.length() > MAX_LINE_LENGTH) {
                    System.out.println("❌ 다음 도서 정보는 너무 깁니다 (" 
                        + data.length() + "자), 저장에서 제외됩니다: " 
                        + book.getTitle());
                    continue;
                }
                writer.write(data);
                writer.newLine();
            }
            System.out.println("✅ 파일 저장 완료: " + resolvedPath);
        } catch (IOException e) {
            System.out.println("❌ 파일 저장 중 오류 발생");
            e.printStackTrace();
        }
    }
}
