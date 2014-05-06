package ru.xeroxp.launcher.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class xMD5Utils {
    public static String hash(File file) throws IOException {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DigestInputStream dis = new DigestInputStream(bis, algorithm);

            while (true) {
                if (dis.read() == -1) {
                    break;
                }
            }

            byte[] hash = algorithm.digest();

            return byteArrayToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String hash(String string) {
        try {
            MessageDigest algorythm = MessageDigest.getInstance("MD5");
            algorythm.update(string.getBytes());
            byte[] digest = algorythm.digest();
            string = byteArrToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return string;
    }

    private static String byteArrayToHex(byte[] hash) {
        Formatter formatter = new Formatter();

        for (byte b : hash) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    private static String byteArrToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();

        for (byte aBArr : bArr) {
            int unsigned = aBArr & 255;

            if (unsigned < 16) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(unsigned));
        }

        return sb.toString();
    }
}
