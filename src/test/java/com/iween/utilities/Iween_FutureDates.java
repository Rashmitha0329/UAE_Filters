package com.iween.utilities;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Iween_FutureDates {

    public static class DateResult {
        public String day;
        public String month;
        public String year;

        public DateResult(String day, String month, String year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        @Override
        public String toString() {
            return day + "-" + month + "-" + year;
        }
    }

    public Map<String, DateResult> furtherDate() {
        LocalDate currentDate = LocalDate.now();  // ✅ Get current date from system

        // Add future days
        LocalDate datePlus1 = currentDate.plusDays(1);
        LocalDate datePlus2 = currentDate.plusDays(2);
        LocalDate datePlus3 = currentDate.plusDays(3);
        LocalDate datePlus4 = currentDate.plusDays(4);
        LocalDate datePlus5 = currentDate.plusDays(5);
        LocalDate datePlus8 = currentDate.plusDays(8);
        LocalDate datePlus10 = currentDate.plusDays(10);
        LocalDate datePlus12 = currentDate.plusDays(12);
        LocalDate datePlus15 = currentDate.plusDays(15);

        // Helper method to create DateResult
        Map<String, DateResult> resultMap = new HashMap<>();
        resultMap.put("datePlus1", toDateResult(datePlus1));
        resultMap.put("datePlus2", toDateResult(datePlus2));
        resultMap.put("datePlus3", toDateResult(datePlus3));
        resultMap.put("datePlus4", toDateResult(datePlus4));
        resultMap.put("datePlus5", toDateResult(datePlus5));
        resultMap.put("datePlus8", toDateResult(datePlus8));
        resultMap.put("datePlus10", toDateResult(datePlus10));
        resultMap.put("datePlus12", toDateResult(datePlus12));
        resultMap.put("datePlus15", toDateResult(datePlus15));

        return resultMap;
    }

    private DateResult toDateResult(LocalDate date) {
        return new DateResult(
            String.valueOf(date.getDayOfMonth()),
            date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            String.valueOf(date.getYear())
        );
    }
}
