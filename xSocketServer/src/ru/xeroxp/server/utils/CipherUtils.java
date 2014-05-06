package ru.xeroxp.server.utils;

import ru.xeroxp.server.config.Settings;
import ru.xeroxp.server.utils.base.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class CipherUtils {
    private static final byte[] key1 = Settings.SERVER_KEY;
    private static final byte[] key2 = Settings.CLIENT_KEY;
    private static final byte[] key3 = Settings.LAUNCHER_KEY;
    private static final int symbolsCount = Settings.SYMBOLS_COUNT;
    private static byte[] key;

    public static String encrypt(String strToEncrypt, String num) {
        if (num.equals("2")) {
            key = key2;
        } else if (num.equals("3")) {
            key = key3;
        } else {
            key = key1;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String num) {
        if (num.equals("2")) {
            key = key2;
        } else if (num.equals("3")) {
            key = key3;
        } else {
            key = key1;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);

        }
        return null;
    }

    public static String genSalt() {
        Random rnd = new Random();
        char[] text = new char[symbolsCount];
        for (int i = 0; i < symbolsCount; i++) {
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            text[i] = characters.charAt(rnd.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String xorEncode(String text, String key) {
        String res = "";
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            res += (char) (text.charAt(i) ^ key.charAt(j));
            j++;
            if (j == key.length()) j = 0;
        }
        return res;
    }
}