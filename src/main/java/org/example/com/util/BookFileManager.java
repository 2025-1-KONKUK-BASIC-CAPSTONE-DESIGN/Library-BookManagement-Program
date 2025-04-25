package org.example.com.util;

import org.example.com.model.Book;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BookFileManager {
    private static final String BOOK_FILE_PATH = System.getProperty("user.home") + "/book_data.txt";

    public static void saveBook(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE_PATH, true))) {
            writer.write(book.toDataString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❌ 도서를 저장하는 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static List<Book> loadBooks() {
        List<Book> list = new ArrayList<>();
        try {
            Path path = Paths.get(BOOK_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
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

    public static void saveAllBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE_PATH))) {
            for (Book book : books) {
                writer.write(book.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ 전체 도서를 저장하는 중 오류 발생");
            e.printStackTrace();
        }
    }
    public static void saveToFile(String filePath, List<Book> books) {
        // {HOME}을 실제 사용자 홈 디렉터리로 치환
        String resolvedPath = filePath.replace("{HOME}", System.getProperty("user.home"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resolvedPath))) {
            for (Book book : books) {
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