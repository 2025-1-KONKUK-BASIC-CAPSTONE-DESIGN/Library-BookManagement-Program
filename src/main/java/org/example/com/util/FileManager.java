package org.example.com.util;

import org.example.com.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    private static final String USER_FILE_PATH = System.getProperty("user.home") + "/user_data.txt";

    // 파일에 사용자 정보 저장
    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            bw.write(user.toDataString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보를 저장하는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // ID 중복 확인
    public static boolean isIdExists(String id) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getId().equals(id)) return true;
        }
        return false;
    }

    // 이메일 중복 확인
    public static boolean isEmailExists(String email) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getEmail().equals(email)) return true;
        }
        return false;
    }

    // 전체 사용자 목록 로드
    public static List<User> loadUsers() {
        List<User> list = new ArrayList<>();

        try {
            Path path = Paths.get(USER_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\t");
                if (parts.length != 6) continue;

                User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                user.setLoanCount(Integer.parseInt(parts[5]));
                list.add(user);
            }
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보를 불러오는 중 오류 발생");
            e.printStackTrace();
        }

        return list;
    }
}