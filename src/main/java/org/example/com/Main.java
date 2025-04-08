package org.example.com;

import org.example.com.prompt.start.StartPrompt;

public class Main {
    public static void main(String[] args) {
        System.out.println("📘 도서관 도서관리 프로그램을 시작합니다.");
        StartPrompt startPrompt = new StartPrompt();
        startPrompt.start();
    }
}