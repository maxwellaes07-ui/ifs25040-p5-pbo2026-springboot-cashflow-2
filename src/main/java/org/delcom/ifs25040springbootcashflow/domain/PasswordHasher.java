package org.delcom.ifs25040springbootcashflow.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class PasswordHasher {
    private PasswordHasher() {}
    public static String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
    public static boolean matches(String raw, String hash) { return MessageDigest.isEqual(hash(raw).getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8)); }
}
