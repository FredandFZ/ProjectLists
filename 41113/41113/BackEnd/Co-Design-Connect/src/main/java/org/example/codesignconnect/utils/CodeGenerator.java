package org.example.codesignconnect.utils;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateInviteCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public static String generateVerificationCode(int length) {
        Random random = new Random();
        int code = (int)Math.pow(10, length - 1) + random.nextInt((int)Math.pow(10, length - 1) * 9);
        return String.valueOf(code);
    }
}
