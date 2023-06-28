package com.wellsfargo.logscanner.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCharsetDetector {

    public static void main(String[] args) {
        String filePath = "C:/Balaji/Java/serverlogs/server1.log";
        try {
            String charset = detectFileCharset(filePath);
            System.out.println("Detected Charset: " + charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String detectFileCharset(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Charset charset = detectCharset(path);
        return charset.name();
    }

    public static Charset detectCharset(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return detectCharset(bytes);
    }

    public static Charset detectCharset(byte[] bytes) {
        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
        if (isCharset(bytes, StandardCharsets.UTF_8)) {
            charset = StandardCharsets.UTF_8;
        } else if (isCharset(bytes, StandardCharsets.ISO_8859_1)) {
            charset = StandardCharsets.ISO_8859_1;
        } else if (isCharset(bytes, StandardCharsets.US_ASCII)) {
            charset = StandardCharsets.US_ASCII;
        } else if (isCharset(bytes, StandardCharsets.UTF_16BE)) {
            charset = StandardCharsets.UTF_16BE;
        } else if (isCharset(bytes, StandardCharsets.UTF_16LE)) {
            charset = StandardCharsets.UTF_16LE;
        }
        // Add more charsets as needed

        return charset;
    }

    private static boolean isCharset(byte[] bytes, Charset charset) {
        String content = new String(bytes, charset);
        byte[] convertedBytes = content.getBytes(charset);
        return java.util.Arrays.equals(bytes, convertedBytes);
    }
}
