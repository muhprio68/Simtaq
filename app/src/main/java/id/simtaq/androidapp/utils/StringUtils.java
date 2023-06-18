package id.simtaq.androidapp.utils;

import android.util.Log;

public class StringUtils {

    public static String left(String a, int size) {
        return left(a, size, ' ');
    }

    public static String right(String a, int size) {
        return right(a, size, ' ');
    }

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String justify(String a, String b, int size) {
        return justify(a, b, size, ' ');
    }

    public static String doubleLine(int size) {
        return insertChar('=', size);
    }

    public static String singleLine(int size) {
        return insertChar('-', size);
    }

    public static String center(String s, int size, char pad) {

        if (s.length() > size)
            s = s.substring(0, size);

        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        sb.append("\n");
        Log.d("center", sb.toString());
        return sb.toString();
    }

    public static String justify(String a, String b, int size, char pad) {
        int ukuran = size - (b.length() + 1);

        if (a.length() > ukuran)
            a = a.substring(0, ukuran);
        StringBuilder sb = new StringBuilder(size);
        sb.append(a);
        for (int i = 0; i < (size - (a.length() + b.length())); i++) {
            sb.append(pad);
        }
        sb.append(b);
        sb.append("\n");
        Log.d("justify", sb.toString());
        return sb.toString();
    }

    public static String newLine(int size) {
        StringBuilder sb = new StringBuilder(size);
        sb.append(" ");
        sb.append("\n");
        return sb.toString();
    }

    public static String insertChar(char pad, int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(pad);
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String left(String a, int size, char pad) {
        StringBuilder sb = new StringBuilder(size);
        sb.append(a);
        for (int i = 0; i < (size - a.length()); i++) {
            sb.append(pad);
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String right(String a, int size, char pad) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - a.length()); i++) {
            sb.append(pad);
        }
        sb.append(a);
        sb.append("\n");
        return sb.toString();
    }

}
