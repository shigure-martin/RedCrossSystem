package com.redCross.security;

import org.springframework.security.crypto.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.Key;

public class JPACryptoConverter implements AttributeConverter<String, String> {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY = "bc307567971a1c93";

    @Override
    public String convertToDatabaseColumn(String sensitive) {
        if (sensitive == null) {
            return "";
        }
        Key key = new SecretKeySpec(KEY.getBytes(), "AES");
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final String encrypted = new String (Base64.encode(cipher.doFinal(sensitive.getBytes())), "UTF-8");
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String sensitive) {
        if (sensitive == null) {
            return "";
        }
        Key key = new SecretKeySpec(KEY.getBytes(), "AES");
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            final String decrypted = new String(cipher.doFinal(Base64.decode(sensitive.getBytes("UTF-8"))));
            return decrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
