package ewewukek.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class FileUtils {

    public static String readFile(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("readFile("+path+")");
            e.printStackTrace();
            return "";
        }
    }

    public static String[] splitStringByChar(String s, char c) {
        int n = 0;
        for (int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == c) ++n;
        }
        ++n;
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

    public static FloatBuffer readFloatBuffer(String path) {
        String raw = readFile(path);
        if (raw.length() == 0) return null;
        String[] values = splitStringByChar(raw, ',');
        FloatBuffer fb = BufferUtils.createFloatBuffer(values.length);
        for (String v: values) {
            fb.put(Float.parseFloat(v));
        }
        return fb;
    }

    public static IntBuffer readIntBuffer(String path) {
        String raw = readFile(path);
        if (raw.length() == 0) return null;
        String[] values = splitStringByChar(raw, ',');
        IntBuffer ib = BufferUtils.createIntBuffer(values.length);
        for (String v: values) {
            ib.put(Integer.parseInt(v));
        }
        return ib;
    }
}