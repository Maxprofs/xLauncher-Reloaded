package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.utils.base.Base64;
import ru.xeroxp.launcher.xAuth;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class xCipherUtils {
    private static final byte[] key = xSettings.KEY;

    public static String xorEncode(String text) {
        String res = "";
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            res += (char) (text.charAt(i) ^ xSettings.PASS_ID_KEY.charAt(j));
            j++;
            if (j == xSettings.PASS_ID_KEY.length()) j = 0;
        }

        return res;
    }

    public static String encrypt(String strToEncrypt) {
        try {
            strToEncrypt = xCipherUtils.genSalt() + strToEncrypt;
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            xDebug.errorMessage("Error while encrypting: " + e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
        } catch (Exception e) {
            xDebug.errorMessage("Error while decrypting: " + e);

        }
        return null;
    }

    private static String genSalt() {
        Random rnd = new Random();
        char[] text = new char[xAuth.symbolsCount];
        for (int i = 0; i < xAuth.symbolsCount; i++) {
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            text[i] = characters.charAt(rnd.nextInt(characters.length()));
        }
        return new String(text);
    }
}