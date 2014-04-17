package ru.xeroxp.server;

import ru.xeroxp.server.config.Settings;
import ru.xeroxp.server.utils.CipherUtils;
import ru.xeroxp.server.utils.Debug;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

class Worker implements Runnable {
    private SSLSocket clientSocket = null;

    public Worker(SSLSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            clientSocket.startHandshake();
            InputStream sin = clientSocket.getInputStream();
            OutputStream sout = clientSocket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            long time = System.currentTimeMillis();
            String[] args1;
            String getm;

            getm = in.readUTF();
            args1 = getm.split(":");

            if (args1[0].equals("0")) {
                String salt;
                args1 = ("0:" + ((CipherUtils.decrypt(args1[1], "3"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                String userN = args1[1];
                URL localURL;
                BufferedReader localBufferedReader;
                String result;

                if (!(!args1[4].equals("jar") && !args1[5].equals(Main.launcherSizeExe) || !args1[5].equals(Main.launcherSizeJar))) {
                    localURL = new URL(Settings.MAIN_FILE + "?action=auth&user=" + URLEncoder.encode(userN, "UTF-8") + "&password=" + URLEncoder.encode(xorEncode(intToStr(args1[2]), Settings.PASS_ID_KEY), "UTF-8") + "&version=" + URLEncoder.encode(args1[3], "UTF-8"));
                    localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                    result = localBufferedReader.readLine();
                } else {
                    result = "abuseLauncherSize";
                }
                salt = CipherUtils.genSalt();
                out.writeUTF(CipherUtils.encrypt(salt + result, "3"));

                if (result.equals("0")) {
                    args1 = in.readUTF().split(":");
                    args1 = ("1:" + ((CipherUtils.decrypt(args1[1], "3"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                    String hwid = args1[1];
                    String[] platform = {"windows", "macos", "solaris", "linux"};
                    boolean check = true;
                    for (String aPlatform : platform) {
                        if (hwid.contains(aPlatform)) {
                            check = false;
                            break;
                        } else {
                            check = true;
                        }
                    }
                    if (check) {
                        localURL = new URL(Settings.MAIN_FILE + "?action=authcheck&user=" + URLEncoder.encode(userN, "UTF-8") + "&s=" + URLEncoder.encode(hwid, "UTF-8"));
                        localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                        result = localBufferedReader.readLine();
                    } else {
                        result = "1";
                    }
                    salt = CipherUtils.genSalt();
                    out.writeUTF(CipherUtils.encrypt(salt + result, "3"));

                    if (result.equals("1")) {
                        String ch = Main.hash;
                        for (int r = 0; r < Main.files.length; r++) {
                            salt = CipherUtils.genSalt();
                            out.writeUTF(CipherUtils.encrypt(salt + (Main.files[r]), "3"));
                        }

                        String args = in.readUTF();
                        args = (CipherUtils.decrypt(args, "3")).substring(Settings.SYMBOLS_COUNT);
                        boolean d = true;
                        for (int p = 0; p < Settings.CHECK_FORMATS.length + 1; p++) {
                            String arg = args.split(":")[p];
                            if (!arg.equals(ch.split(":")[p])) {
                                salt = CipherUtils.genSalt();
                                out.writeUTF(CipherUtils.encrypt(salt + "nofiles", "3"));
                                d = false;
                                break;
                            }
                        }
                        if (d) {
                            result = "2";
                            salt = CipherUtils.genSalt();
                            out.writeUTF(CipherUtils.encrypt(salt + result, "3"));
                        }

                        if (result.equals("2")) {
                            String args11 = in.readUTF();
                            args1 = ((CipherUtils.decrypt(args11, "3")).substring(Settings.SYMBOLS_COUNT)).split(":");

                            if (args1[0].equals("3")) {
                                localURL = new URL(Settings.MAIN_FILE + "?action=getsession&user=" + URLEncoder.encode(userN, "UTF-8"));
                                localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                                result = localBufferedReader.readLine();
                                String res = result.split(":")[1];
                                String answer = "4:" + CipherUtils.encrypt((xorEncode(intToStr(res), Settings.SESSION_ID_KEY)), "1");
                                salt = CipherUtils.genSalt();
                                out.writeUTF(CipherUtils.encrypt(salt + answer, "3"));
                            }
                        }
                    }
                }
            } else if (args1[0].equals("s")) {
                URL localURL;
                BufferedReader localBufferedReader;
                String result;
                args1 = ("s:" + ((CipherUtils.decrypt((args1[1]), "2"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                String ses = CipherUtils.decrypt((URLDecoder.decode(args1[2], "UTF-8")), "1");
                localURL = new URL(Settings.JOIN_SERVER + "?user=" + URLEncoder.encode(args1[1], "UTF-8") + "&sessionId=" + URLEncoder.encode(ses, "UTF-8") + "&serverId=" + URLEncoder.encode(args1[3], "UTF-8"));
                localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                result = localBufferedReader.readLine();
                out.writeUTF(result);
            } else if (args1[0].equals("mcheckclient")) {
                String salt;
                String ch = Main.hash;

                for (int r = 0; r < Main.files.length; r++) {
                    salt = CipherUtils.genSalt();
                    out.writeUTF(CipherUtils.encrypt(salt + (Main.files[r]), "3"));
                }

                String args = in.readUTF();
                args = (CipherUtils.decrypt(args, "3")).substring(Settings.SYMBOLS_COUNT);
                boolean d = true;

                for (int p = 0; p < Settings.CHECK_FORMATS.length + 1; p++) {
                    String arg = args.split(":")[p];
                    if (!arg.equals(ch.split(":")[p])) {
                        salt = CipherUtils.genSalt();
                        out.writeUTF(CipherUtils.encrypt(salt + "nofiles", "3"));
                        d = false;
                        break;
                    }
                }

                if (d) {
                    salt = CipherUtils.genSalt();
                    out.writeUTF(CipherUtils.encrypt(salt + "2", "3"));
                }
            } else if (args1[0].equals("formats")) {
                String salt = CipherUtils.genSalt();
                out.writeUTF(CipherUtils.encrypt(salt + ";" + Main.formats, "3"));
            }

            sout.close();
            sout.close();
            System.out.println("Request processed: " + time);
        } catch (Exception e) {
            System.out.println("Client is disconnected: " + e.getMessage());
        }
    }

    private static String calculateHash(MessageDigest algorithm, String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);

        while (dis.read() != -1) ;
        byte[] hash = algorithm.digest();

        return byteArray2Hex(hash);
    }

    private static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private static String md5(String string) {
        try {
            MessageDigest e1 = MessageDigest.getInstance("MD5");
            e1.update(string.getBytes());
            byte[] digest = e1.digest();
            string = byteArrToHexString(digest);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }
        return string;
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

    private static String[] addToArray(String[] array, String s) {
        String[] ans = new String[array.length + 1];
        System.arraycopy(array, 0, ans, 0, array.length);
        ans[ans.length - 1] = s;
        return ans;
    }

    private static String xorEncode(String text, String key) {
        String res = "";
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            res += (char) (text.charAt(i) ^ key.charAt(j));
            j++;
            if (j == key.length()) j = 0;
        }
        return res;
    }

    private static String intToStr(String text) {
        String res = "";
        for (int i = 0; i < text.split("-").length; i++) res += (char) Integer.parseInt(text.split("-")[i]);
        return res;
    }

    private static void addToArray(String text) {
        Main.files = addToArray(Main.files, text);
    }

    public static void launcherSize(String path) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Main.launcherSizeJar = calculateHash(md5, path + File.separator + "launcher" + File.separator + Settings.LAUNCHER_FILE_NAME + ".jar");
            Main.launcherSizeExe = calculateHash(md5, path + File.separator + "launcher" + File.separator + Settings.LAUNCHER_FILE_NAME + ".exe");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String check(File path) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String dir = new File("").getAbsolutePath() + File.separator + "check";
        String hash = "";
        int[] fileCount = new int[Settings.CHECK_FORMATS.length];

        if (!new File(dir).exists()) {
            Debug.errorMessage("Maybe the server is running in the wrong place!\nFolder 'check' does not exist!\n" + dir);
        }

        File[] files = path.listFiles();

        String filesArray = "";
        assert files != null;
        for (int f = 0; f < files.length; f++) {
            filesArray = ((f == 0) ? "" : filesArray) + files[f].toString().substring(dir.length());
        }

        addToArray(filesArray);

        if (files.length == 0) {
            String count = "0";

            for (String ignored : Settings.CHECK_FORMATS) {
                count += ":0";
            }

            return count;
        }

        for (File file : files) {
            if (file.isDirectory() && !file.getName().endsWith("texturepacks") && !file.getName().endsWith("resourcepacks")) {
                String sHash = check(new File(path + File.separator + file.getName()));

                for (int c = 0; c < Settings.CHECK_FORMATS.length; c++) {
                    int sFileCount = Integer.parseInt(sHash.split(":")[c + 1]);
                    fileCount[c] += sFileCount;
                }

                hash += (sHash.split(":")[0].equals("0")) ? "" : sHash.split(":")[0];
            }

            if (file.isFile()) {
                for (int c = 0; c < Settings.CHECK_FORMATS.length; c++) {
                    if (file.getName().endsWith(Settings.CHECK_FORMATS[c])) {
                        hash += calculateHash(md5, path + File.separator + file.getName());
                        ++fileCount[c];
                        break;
                    }
                }
            }
        }

        String result = md5(hash);
        for (int c = 0; c < Settings.CHECK_FORMATS.length; c++) {
            result += ":" + fileCount[c];
        }

        return result;
    }
}