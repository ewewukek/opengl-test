package ewewukek.common;

public class Utils {

    public static String[] splitStringByChar(String s, char c) {
        int n = 0;
        for (int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == c) ++n;
        }
        ++n;
        if (n == 0) return null;

        String parts[] = new String[n];
        int pi = 0;
        int si = 0;
        for (int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == c) {
                parts[pi++] = s.substring(si, i);
                si = i + 1;
            }
        }
        parts[pi] = s.substring(si);
        return parts;
    }
}