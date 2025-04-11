package org.example.com.util;

import org.example.com.model.Date;
import java.io.*;
import java.time.LocalDate;

public class DateManager {

    private static final String DATE_FILE_PATH = System.getProperty("user.home") + "/date_data.txt";

    public static Date loadDateFromFile() {
        File file = new File(DATE_FILE_PATH);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return Date.parse(line);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean saveDateToFile(Date date) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATE_FILE_PATH))) {
            writer.write(date.getValue());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
