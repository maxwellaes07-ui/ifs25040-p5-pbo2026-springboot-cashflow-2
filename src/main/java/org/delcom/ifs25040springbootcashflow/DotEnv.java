package org.delcom.ifs25040springbootcashflow;

import java.nio.file.Files;
import java.nio.file.Path;

public final class DotEnv {
    private DotEnv() {}
    public static void load() {
        Path file = Path.of(".env");
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file)) {
                String s = line.trim();
                if (s.isEmpty() || s.startsWith("#") || !s.contains("=")) continue;
                String[] pair = s.split("=", 2);
                System.setProperty(pair[0].trim(), pair[1].trim());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Gagal membaca .env", e);
        }
    }
}
