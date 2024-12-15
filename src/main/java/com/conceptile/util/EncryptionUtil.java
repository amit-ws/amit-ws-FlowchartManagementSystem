package com.conceptile.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EncryptionUtil {
    static final String ALGORITHM = "AES";
    static final String TRANSFORMATION = "AES";

    private static String SECRET_KEY = "12345678901234567890123456789012";

    static SecretKey secretKey;

    static {
        try {
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new RuntimeException("ENCRYPTION_KEY environment variable is not set!");
            }
            secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing encryption key", e);
        }
    }

    public static String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }
}
