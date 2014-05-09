package ru.xeroxp.server;

import ru.xeroxp.server.config.Settings;
import ru.xeroxp.server.utils.CipherUtils;
import ru.xeroxp.server.utils.Debug;
import ru.xeroxp.server.utils.MD5Utils;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

class Worker implements Runnable {
    private SSLSocket clientSocket = null;

    public Worker(SSLSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private static String intToStr(String text) {
        String res = "";
        for (int i = 0; i < text.split("-").length; i++) res += (char) Integer.parseInt(text.split("-")[i]);
        return res;
    }

    public static void launcherSize(String path) {
        try {
            Main.launcherSizeJar = MD5Utils.fileHash(path + File.separator + "launcher" + File.separator + Settings.LAUNCHER_FILE_NAME + ".jar");
            Main.launcherSizeExe = MD5Utils.fileHash(path + File.separator + "launcher" + File.separator + Settings.LAUNCHER_FILE_NAME + ".exe");
            Debug.infoMessage(Main.launcherSizeJar + " : " + Main.launcherSizeExe);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String check(File path) throws Exception {
        String dir = new File("").getAbsolutePath() + File.separator + "check";
        String hash = "";
        int[] fileCount = new int[Settings.CHECK_FORMATS.length];

        if (!new File(dir).exists()) {
            Debug.errorMessage("Maybe the server is running in the wrong place!\nFolder 'check' does not exist!\n" + dir);
        }

        File[] files = path.listFiles();

        String filesArray = "";
        assert files != null;
        for (int i = 0; i < files.length; i++) {
            String file = files[i].toString().substring(dir.length());
            filesArray = (i == 0) ? file : filesArray + ", " + file;
        }

        Main.files.add(filesArray);

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
                        hash += MD5Utils.fileHash(path + File.separator + file.getName());
                        ++fileCount[c];
                        break;
                    }
                }
            }
        }

        String result = MD5Utils.stringHash(hash);
        for (int c = 0; c < Settings.CHECK_FORMATS.length; c++) {
            result += ":" + fileCount[c];
        }

        return result;
    }

    @Override
    public void run() {
        try {
            clientSocket.startHandshake();
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            long time = System.currentTimeMillis();
            String[] inputArgs = inputStream.readUTF().split(":");

            if (inputArgs[0].equals("formats")) {
                outputStream.writeUTF(CipherUtils.encrypt(CipherUtils.genSalt() + ";" + Main.formats, "3"));
            } else if (inputArgs[0].equals("auth")) {
                inputArgs = ("auth:" + ((CipherUtils.decrypt(inputArgs[1], "3"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                String userN = inputArgs[1];
                URL localURL;
                BufferedReader localBufferedReader;
                String result;

                if (inputArgs[4].equals("jar") && inputArgs[5].equals(Main.launcherSizeJar) || inputArgs[5].equals(Main.launcherSizeExe)) {
                    localURL = new URL(Settings.MAIN_FILE + "?action=auth&user=" + URLEncoder.encode(userN, "UTF-8") + "&password=" + URLEncoder.encode(CipherUtils.xorEncode(intToStr(inputArgs[2]), Settings.PASS_ID_KEY), "UTF-8") + "&version=" + URLEncoder.encode(inputArgs[3], "UTF-8"));
                    localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                    result = localBufferedReader.readLine();
                } else {
                    result = "abuseLauncher";
                }

                String salt = CipherUtils.genSalt();
                outputStream.writeUTF(CipherUtils.encrypt(salt + result, "3"));

                if (result.equals("0")) {
                    inputArgs = inputStream.readUTF().split(":");
                    inputArgs = ("1:" + ((CipherUtils.decrypt(inputArgs[1], "3"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                    String hwid = inputArgs[1];
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
                    outputStream.writeUTF(CipherUtils.encrypt(salt + result, "3"));

                    if (result.equals("1")) {
                        for (String file : Main.files) {
                            salt = CipherUtils.genSalt();
                            outputStream.writeUTF(CipherUtils.encrypt(salt + file, "3"));
                        }

                        String args = (CipherUtils.decrypt(inputStream.readUTF(), "3")).substring(Settings.SYMBOLS_COUNT);

                        boolean d = true;
                        for (int i = 0; i < Settings.CHECK_FORMATS.length + 1; i++) {
                            String arg = args.split(":")[i];

                            if (!arg.equals(Main.hash.split(":")[i])) {
                                salt = CipherUtils.genSalt();
                                outputStream.writeUTF(CipherUtils.encrypt(salt + "nofiles", "3"));
                                d = false;
                                break;
                            }
                        }

                        if (d) {
                            result = "2";
                            salt = CipherUtils.genSalt();
                            outputStream.writeUTF(CipherUtils.encrypt(salt + result, "3"));
                        }

                        if (result.equals("2")) {
                            String args11 = inputStream.readUTF();
                            inputArgs = ((CipherUtils.decrypt(args11, "3")).substring(Settings.SYMBOLS_COUNT)).split(":");

                            if (inputArgs[0].equals("3")) {
                                localURL = new URL(Settings.MAIN_FILE + "?action=getsession&user=" + URLEncoder.encode(userN, "UTF-8"));
                                localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                                result = localBufferedReader.readLine();
                                String res = result.split(":")[1];
                                String answer = "4:" + CipherUtils.encrypt((CipherUtils.xorEncode(intToStr(res), Settings.SESSION_ID_KEY)), "1");
                                salt = CipherUtils.genSalt();
                                outputStream.writeUTF(CipherUtils.encrypt(salt + answer, "3"));
                            }
                        }
                    }
                }
            } else if (inputArgs[0].equals("s")) {
                URL localURL;
                BufferedReader localBufferedReader;
                String result;
                inputArgs = ("s:" + ((CipherUtils.decrypt((inputArgs[1]), "2"))).substring(Settings.SYMBOLS_COUNT)).split(":");
                String ses = CipherUtils.decrypt((URLDecoder.decode(inputArgs[2], "UTF-8")), "1");
                localURL = new URL(Settings.JOIN_SERVER + "?user=" + URLEncoder.encode(inputArgs[1], "UTF-8") + "&sessionId=" + URLEncoder.encode(ses, "UTF-8") + "&serverId=" + URLEncoder.encode(inputArgs[3], "UTF-8"));
                localBufferedReader = new BufferedReader(new InputStreamReader(localURL.openStream()));
                result = localBufferedReader.readLine();
                outputStream.writeUTF(result);
            } else if (inputArgs[0].equals("mcheckclient")) {
                String salt;
                for (String file : Main.files) {
                    salt = CipherUtils.genSalt();
                    outputStream.writeUTF(CipherUtils.encrypt(salt + file, "3"));
                }

                String args = inputStream.readUTF();
                args = (CipherUtils.decrypt(args, "3")).substring(Settings.SYMBOLS_COUNT);
                boolean valid = true;

                for (int i = 0; i < Settings.CHECK_FORMATS.length + 1; i++) {
                    String arg = args.split(":")[i];

                    if (!arg.equals(Main.hash.split(":")[i])) {
                        salt = CipherUtils.genSalt();
                        outputStream.writeUTF(CipherUtils.encrypt(salt + "nofiles", "3"));
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    salt = CipherUtils.genSalt();
                    outputStream.writeUTF(CipherUtils.encrypt(salt + "2", "3"));
                }
            }

            clientSocket.getInputStream().close();
            Debug.infoMessage("Request processed: " + time);
        } catch (Exception e) {
            Debug.errorMessage("Client is disconnected: " + e.getMessage());
            e.printStackTrace();
        }
    }
}