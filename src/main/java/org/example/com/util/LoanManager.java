package org.example.com.util;

import org.example.com.model.Loan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoanManager {
    private static final String LOAN_FILE_PATH = System.getProperty("user.home") + "/loan_data.txt";

    public static void saveLoanRecord(String record) {
        String loanPath = System.getProperty("user.home") + "/loan_data.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(loanPath, true))) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❌ 대여 기록 저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static List<Loan> loadNotReturnedLoans(String id) {
        List<Loan> list = new ArrayList<>();
        try {
            Path path = Paths.get(LOAN_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                Loan record = Loan.fromDataString(line);

                if (record == null) continue;
                if (!record.getReturnDate().isEmpty()) continue;   //대출 완료한 경우는 패스
                if (!record.getUserId().equals(id)) continue;   //현재 사용자의 대출 정보만 가져온다.

                list.add(record);
            }
        } catch (IOException e) {
            System.out.println("❌ 대여 정보를 불러오는 중 오류 발생");
            e.printStackTrace();
        }
        return list;
    }

    public static void updateLoan(Loan updatedLoan) {
        List<Loan> loans = new ArrayList<>();
        try {
            Path path = Paths.get(LOAN_FILE_PATH);
            if (!Files.exists(path)) Files.createFile(path);

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                Loan loan = Loan.fromDataString(line);
                if (loan == null) continue;

                if (loan.getIsbn().equals(updatedLoan.getIsbn()) &&
                    loan.getUserId().equals(updatedLoan.getUserId()) &&
                    loan.getLoanDate().equals(updatedLoan.getLoanDate())) {
                    loans.add(updatedLoan); // replace old record
                } else {
                    loans.add(loan);
                }
            }

            // Rewrite the entire file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOAN_FILE_PATH))) {
                for (Loan loan : loans) {
                    writer.write(String.join("\t",
                        loan.getIsbn(),
                        loan.getTitle(),
                        loan.getUserId(),
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        loan.getReturnDate() == null ? "" : loan.getReturnDate()
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("❌ 대여 기록 수정 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static void saveToFile(String filePath) {
        String resolvedPath = filePath.replace("{HOME}", System.getProperty("user.home"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resolvedPath))) {
            Path loanPath = Paths.get(LOAN_FILE_PATH);
            if (!Files.exists(loanPath)) return;

            List<String> lines = Files.readAllLines(loanPath);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("✅ 대여 파일 저장 완료: " + resolvedPath);
        } catch (IOException e) {
            System.out.println("❌ 대여 파일 저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static Long maxOverdueDays(LocalDate today, List<Loan> loans) {
        long overdueDays = 0;
        for (Loan loan : loans) {
            LocalDate dueDate = LocalDate.parse(loan.getDueDate());
            long remainingDays = ChronoUnit.DAYS.between(today, dueDate);
            if (remainingDays < 0) {
                overdueDays = Math.max(overdueDays, -remainingDays);
            }
        }
        return overdueDays;
    }

    public static Long maxOverdueDays(LocalDate today, String userid) {
        List<Loan> loans = loadNotReturnedLoans(userid);
        return maxOverdueDays(today, loans);
    }
}
