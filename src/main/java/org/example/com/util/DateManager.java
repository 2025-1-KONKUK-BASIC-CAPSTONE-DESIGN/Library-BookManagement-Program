package org.example.com.util;

import java.io.*;
import java.time.LocalDate;

public class DateManager {

    private static final String DATE_FILE_PATH = System.getProperty("user.home") + "/date_data.txt";

    public static LocalDate loadDateFromFile() {
        File file = new File(DATE_FILE_PATH);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return LocalDate.parse(line);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean saveDateToFile(LocalDate date) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATE_FILE_PATH))) {
            writer.write(date.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static void saveToFile(String filePath) {
        String resolvedPath = filePath.replace("{HOME}", System.getProperty("user.home"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resolvedPath))) {
            LocalDate currentDate = loadDateFromFile();
            if (currentDate != null) {
                writer.write(currentDate.toString());
                writer.newLine();
            }
            System.out.println("✅ 날짜 파일 저장 완료: " + resolvedPath);
        } catch (IOException e) {
            System.out.println("❌ 날짜 파일 저장 중 오류 발생");
            e.printStackTrace();
        }
    }
}
