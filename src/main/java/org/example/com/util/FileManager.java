package org.example.com.util;

import org.example.com.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    private static final String USER_FILE_PATH = System.getProperty("user.home") + "/user_data.txt";

    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            bw.write(user.toDataString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보를 저장하는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public static boolean isIdExists(String id) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getId().equals(id)) return true;
        }
        return false;
    }

    public static boolean isEmailExists(String email) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getEmail().equals(email)) return true;
        }
        return false;
    }

    public static List<User> loadUsers() {
        List<User> list = new ArrayList<>();

        try {
            Path path = Paths.get(USER_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\t");
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

    // ✅ 사용자 전체 덮어쓰기 저장
    public static void saveAllUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
            for (User user : users) {
                bw.write(user.toDataString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보를 저장하는 중 오류 발생");
            e.printStackTrace();
        }
    }

    // ✅ 특정 사용자 1명만 갱신 저장
    public static void updateUser(User updatedUser) {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                break;
            }
        }
        saveAllUsers(users);
    }
}