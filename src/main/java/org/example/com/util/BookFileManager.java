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
}