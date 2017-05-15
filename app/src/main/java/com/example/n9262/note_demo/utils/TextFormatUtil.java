package com.example.n9262.note_demo.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static  Date parseText(String text) throws ParseException {
        return dateFormat.parse(text);
    }

    public static String getNoteSummary(String content) {
        if (content.length() > 10) {
            StringBuilder sb = new StringBuilder(content.substring(0, 10));
            sb.append("...");
            return sb.toString();
        }
        return content;
    }

}
