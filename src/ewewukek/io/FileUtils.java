package ewewukek.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static ewewukek.common.Utils.*;

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

    public static FloatBuffer readFloatBuffer(String path) {
        String raw = readFile(path);
        if (raw.length() == 0) return null;

        String[] values = splitStringByChar(raw, ',');
        if (values == null) return null;

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
        if (values == null) return null;

        IntBuffer ib = BufferUtils.createIntBuffer(values.length);
        for (String v: values) {
            ib.put(Integer.parseInt(v));
        }
        return ib;
    }
}