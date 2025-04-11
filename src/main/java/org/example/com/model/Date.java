package org.example.com.model;

import java.time.LocalDate;

public class Date {
    private final String dateStr;

    public Date(String dateStr) {
        this.dateStr = dateStr;
    }

    public static Date parse(String str) {
        return new Date(str.trim());
    }

    public String getValue() {
        return dateStr;
    }

    @Override
    public String toString() {
        return dateStr;
    }
}