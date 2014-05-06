package ru.xeroxp.launcher;

import ru.xeroxp.launcher.gui.elements.xServer;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.misc.xConfig;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.utils.xCipherUtils;
import ru.xeroxp.launcher.utils.xFileUtils;
import ru.xeroxp.launcher.utils.xMD5Utils;
import ru.xeroxp.launcher.utils.xTextureUtils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class xAuth implements Runnable {
    public static int symbolsCount = 0;
    private static String socketIp;
    private static int socketPort;
    private static List<String> formats;
    private static String launcherFormat;
    private static String launcherHash;
    private final String login;
    private final String password;
    private final xTheme theme;

    public xAuth(String login, String password, xTheme theme) {
        this(login, theme, strToInt(xCipherUtils.xorEncode(password)));
    }

    public xAuth(String login, xTheme theme, String password) {
        this.theme = theme;
        this.login = login;
        this.password = password;
    }

    private static void calcLauncherHash() {
        try {
            File runningLauncher = new File(xUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            launcherFormat = runningLauncher.getPath().endsWith(".exe") ? "exe" : "jar";
            launcherHash = xMD5Utils.hash(runningLauncher);

            /** Отладка **/
//            launcherFormat = "jar";
//            launcherHash = xMD5Utils.hash(new File("E:/Osip/Developing/xLauncher/launcher/MagicWars.jar"));
        } catch (Exception e) {
            xDebug.errorMessage(e.getMessage());
        }
    }

    private static String strToInt(String text) {
        String res = "";

        for (int i = 0; i < text.length(); i++) {
            res += (int) text.charAt(i) + "-";
        }

        return res.substring(0, res.length() - 1);
    }

    private static String check(File path, DataInputStream in) throws IOException {
        String checkPath = xFileUtils.getRootDirectory().toString();
        String hash = "";
        String strFileArr = (xCipherUtils.decrypt(in.readUTF())).substring(symbolsCount);

        String[] fileArray = strFileArr.split(", ");
        List<File> files = new ArrayList<File>();

        for (String file : fileArray) {
            if (!strFileArr.isEmpty()) {
                files.add(new File(xFileUtils.getRootDirectory().toString() + File.separator + file));
            }
        }

        if (files.isEmpty()) {
            return "0";
        }

        for (File file : files) {
            checkPath = getCheckPath(checkPath);

            if (!file.exists()) {
                throw new IOException("File not exists: " + file.getPath());
            }

            if (file.isDirectory() && !(path.toString().equals(checkPath) && file.getName().matches(".*?(texturepacks|resourcepacks)$"))) {
                String sHash = check(new File(file.toString()), in);
                hash += sHash.equals("0") ? "" : sHash;
            } else if (file.isFile()) {
                for (String checkFormat : formats) {
                    if (file.getName().endsWith(checkFormat)) {
                        hash += xMD5Utils.hash(file);
                        break;
                    }
                }
            }
        }

        return xMD5Utils.hash(hash);
    }

    private static String getCheckPath(String checkPath) {
        xServer.loadServers();

        for (xServer server : xServer.getServers()) {
            if (!server.getFolder().isEmpty() && checkPath.equals(xFileUtils.getRootDirectory().toString() + File.separator + server.getFolder())) {
                checkPath += File.separator + server.getFolder();
                break;
            }
        }

        return checkPath;
    }

    private static String countFiles(File path) {
        int[] fileCount = new int[formats.size()];
        File[] files = path.listFiles();

        assert files != null;
        if (files.length == 0) {
            String count = "0";

            for (String ignored : formats) {
                count += ":0";
            }

            return count;
        }

        for (File file : files) {
            if (file.isDirectory() && !file.getName().matches(".*?(texturepacks|resourcepacks)$")) {
                String sHash = countFiles(new File(path + File.separator + file.getName()));

                for (int i = 0; i < formats.size(); i++) {
                    fileCount[i] += Integer.parseInt(sHash.split(":")[i + 1]);
                }
            }

            if (file.isFile()) {
                for (int i = 0; i < formats.size(); i++) {
                    if (file.getName().endsWith(formats.get(i))) {
                        ++fileCount[i];
                        break;
                    }
                }
            }
        }

        String result = "0";

        for (int c = 0; c < formats.size(); c++) {
            result += ":" + fileCount[c];
        }

        return result;
    }

    public static boolean mCheckClient() {
        try {
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(InetAddress.getByName(socketIp), socketPort);
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
            socket.startHandshake();
            socket.setSoTimeout(20000);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("mcheckclient");

            String countFiles = countFiles(xFileUtils.getRootDirectory());
            String result = check(xFileUtils.getRootDirectory(), inputStream);

            for (int c = 0; c < formats.size(); c++) {
                result += ":" + countFiles.split(":")[c + 1];
            }

            outputStream.writeUTF(xCipherUtils.encrypt(result));
            outputStream.flush();
            String response = inputStream.readUTF();
            socket.close();

            if (!response.equals("noconnect")) {
                response = (xCipherUtils.decrypt(response)).substring(symbolsCount);
            }

            return !response.equals("nofiles") && !response.equals("noconnect");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void run() {
        this.sendAuth();
    }

    private void getFormats() {
        try {
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(InetAddress.getByName(socketIp), socketPort);
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
            socket.startHandshake();
            socket.setSoTimeout(10000);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("formats");
            outputStream.flush();
            String formats = inputStream.readUTF();
            formats = xCipherUtils.decrypt(formats);
            socket.close();
            xAuth.formats = new ArrayList<String>();

            if (!formats.isEmpty()) {
                String[] formatsArr = formats.split(";");

                if (symbolsCount == 0) {
                    symbolsCount = formatsArr[0].length();
                }

                xAuth.formats.addAll(Arrays.asList(formatsArr).subList(1, formatsArr.length));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServerConnect() {
        xServerConnect.loadServers();
        List<String> servers = new ArrayList<String>();

        for (final xServerConnect server : xServerConnect.getConnectServers()) {
            String s = (server.getServerIp() + ";" + server.getServerPort());
            servers.add(s);
        }

        int num = new Random().nextInt(servers.size());
        socketIp = servers.get(num).split(";")[0];
        socketPort = Integer.parseInt(servers.get(num).split(";")[1]);
    }

    private void sendAuth() {
        this.theme.setAuth("Авторизация");
        this.getServerConnect();
        calcLauncherHash();
        this.getFormats();

        try {
            xDebug.infoMessage("Connection to authorization server");
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(InetAddress.getByName(socketIp), socketPort);
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
            socket.startHandshake();
            socket.setSoTimeout(10000);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("auth:" + xCipherUtils.encrypt(this.login + ":" + this.password + ":" + xMain.getVersion() + ":" + launcherFormat + ":" + launcherHash));
            outputStream.flush();

            while (true) {
                String response = (xCipherUtils.decrypt(inputStream.readUTF())).substring(symbolsCount);

                if (response.equals("0")) {
                    socket.setSoTimeout(20000);
                    String args = this.getHwid();

                    outputStream.writeUTF("1:" + xCipherUtils.encrypt((String) ((args != null) ? this.getHwid() : xFileUtils.getPlatform())));
                    outputStream.flush();
                } else if (response.equals("1")) {
                    if (!this.checkClient(outputStream, inputStream)) {
                        outputStream.writeUTF(xCipherUtils.encrypt("false"));
                        break;
                    }

                    if (!xTextureUtils.check()) {
                        outputStream.writeUTF(xCipherUtils.encrypt("false"));
                        this.theme.setError("Текстуры не прошли проверку");
                        break;
                    }

                    outputStream.writeUTF(xCipherUtils.encrypt("3"));
                    outputStream.flush();
                } else if (response.equals("abuse")) {
                    this.theme.setError("Ошибка авторизаци");
                } else if (response.equals("fail")) {
                    this.theme.setError("Неправильный логин или пароль");
                } else if (response.equals("abanned")) {
                    this.theme.setError("Ваш аккаунт заблокирован");
                } else if (response.equals("banned")) {
                    this.theme.setError("Вы заблокированы");
                } else if (response.equals("abuseSize")) {
                    this.theme.setError("Нельзя модифицировать клиент");
                } else if (response.equals("abuseLauncher")) {
                    this.theme.setError("Лаунчер не прошел проверку");
                } else if (response.equals("abuseTexture")) {
                    this.theme.setError("У вас обнаружен X-Ray");
                } else if (response.equals("abuseMod")) {
                    this.theme.setError("Нельзя добавлять моды в клиент");
                } else if (response.equals("oldLauncher")) {
                    this.theme.setError("Лаунчер устарел");
                    new xUpdater(this.theme);
                } else if (!response.equals("2")) {
                    try {
                        xConfig config = new xConfig(xConfig.LAUNCHER);
                        config.set("auth", this.login + (this.theme.getRemember() ? ":" + this.password : ""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    xLauncher.getIntsanse().drawServerSelect(this.login, response.split(":")[1]);
                    break;
                } else {
                    break;
                }
            }

            socket.close();
        } catch (SocketTimeoutException e) {
            this.theme.setError("Время подключения истекло");
        } catch (IOException e) {
            this.theme.setError("Сервер авторизации недоступен");
            e.printStackTrace();
        }
    }

    private String getHwid() {
        String result = "";

        try {
            File utils = File.createTempFile("hwid", ".vbs");
            utils.deleteOnExit();
            FileWriter utils3 = new FileWriter(utils);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\nSet colDrives = objFSO.Drives\nSet objDrive = colDrives.item(\"C\")\nWscript.Echo objDrive.SerialNumber";
            utils3.write(vbs);
            utils3.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + utils.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {
                String line;
                if ((line = input.readLine()) == null) {
                    input.close();
                    break;
                }

                result += line;
            }
        } catch (Exception e) {
            return xFileUtils.getPlatform().toString();
        }

        return (result.length() < 30) ? result.trim() : xFileUtils.getPlatform().toString();
    }

    private boolean checkClient(DataOutputStream outputStream, DataInputStream inputStream) {
        try {
            String count = countFiles(xFileUtils.getRootDirectory());
            String sendData = check(xFileUtils.getRootDirectory(), inputStream);

            for (int i = 0; i < formats.size(); i++) {
                sendData += ":" + count.split(":")[i + 1];
            }

            outputStream.writeUTF(xCipherUtils.encrypt(sendData));
            String response = inputStream.readUTF();

            if (response.equals("noconnect")) {
                this.theme.setError("Нет соединения");
                return false;
            }

            response = (xCipherUtils.decrypt(response)).substring(symbolsCount);

            if (response.equals("nofiles")) {
                this.theme.setError("Клиент не прошел проверку");
                return false;
            }

            return true;
        } catch (IOException e) {
            this.theme.setError("Ошибка при проверке клиента");
            e.printStackTrace();
        }

        return false;
    }
}
