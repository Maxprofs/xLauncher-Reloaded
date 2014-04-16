package ru.xeroxp.launcher;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class xAuth implements Runnable {

    private final String login;
    private final String password;
    private final xTheme theme;
    private final Random rand = new Random();
    private static String socketip;
    private static int socketport;
    private static String[] checkformats;
    private static int symbolscount = 0;
    private static String launcherformat;
    private static String launchersize;

    public xAuth(String login, String password, xTheme theme) {
        this.theme = theme;
        this.login = login;
        this.password = strToInt(xorEncode(password));
    }
    
    public xAuth(String login, xTheme theme, String password) {
        this.theme = theme;
        this.login = login;
        this.password = password;
    }
    
    public void run() {
        this.sendAuth();
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
    
    private void setError(String text) {
        this.theme.setError(text);
    }

    void getCheckFormats() {
        try {
            InetAddress e = InetAddress.getByName(socketip);
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket(e, socketport);
            String[] suites = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(suites);
            socket.startHandshake();
            socket.setSoTimeout(10000);
            InputStream sine = socket.getInputStream();
            OutputStream soute = socket.getOutputStream();
            DataInputStream ine = new DataInputStream(sine);
            DataOutputStream oute = new DataOutputStream(soute);
            oute.writeUTF("formats");
            oute.flush();
            String formats = ine.readUTF();
            formats = xCipherUtils.decrypt(formats);
            socket.close();
            checkformats = new String[0];
            if (!formats.isEmpty()) {
            String[] formatsarr = formats.split(";");
            if (symbolscount == 0) symbolscount = formatsarr[0].length();
            for(int i = 1; i < formatsarr.length; i++) {
                addtoarraycf(formatsarr[i]);
            }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }
    }

    private static void addtoarraycf(String text) {
        checkformats = addToArray(checkformats, text);
    }

    void getServerConnect() {
        xServerConnect.loadServers();
        String[] servers = {};

        for (final xServerConnect server : xServerConnect.getConnectServers()) {
            String s = (server.getServerIp() + ";" + server.getServerPort());
            servers = addToArray(servers, s);
        }

        int randomNum = this.rand.nextInt(servers.length);
        socketip = servers[randomNum].split(";")[0];
        socketport = Integer.parseInt(servers[randomNum].split(";")[1]);
    }
    
    private void sendAuth() {
        this.theme.setAuth("�����������");
        getServerConnect();
        launcherSize();
        getCheckFormats();
        String salt;
        
        try {
            System.out.println("Connection to authorization server");
            InetAddress e = InetAddress.getByName(socketip);
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket(e, socketport);
            String[] suites = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(suites);
            socket.startHandshake();
            socket.setSoTimeout(10000);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            salt = xCipherUtils.genSalt(symbolscount);
            out.writeUTF("0:" + xCipherUtils.encrypt(salt + this.login + ":" + this.password + ":" + xMain.getVersion() + ":" + launcherformat + ":" + launchersize));
            out.flush();

            label63:
            while(true) {
                while(true) {
                    while(true) {
                        String input = in.readUTF();
                        String response = (xCipherUtils.decrypt(input)).substring(symbolscount);
                        if(!response.equals("0")) {
                            if(!response.equals("1")) {
                                    if(!response.equals("2")) {
                                        if(response.equals("abuse")) {
                                            this.setError("������ �����������");
                                        } else if(response.equals("fail")) {
                                            this.setError("������������ ����� ��� ������");
                                        } else if(response.equals("abanned")) {
                                            this.setError("��� ������� ��� ������������");
                                        } else if(response.equals("banned")) {
                                            this.setError("�� ���� �������������");
                                        } else if(response.equals("abuseSize")) {
                                            this.setError("������ �������������� ������");
                                        } else if(response.equals("abuseLauncherSize")) {
                                            this.setError("������� �� ������ ��������");
                                        } else if(response.equals("abuseTexture")) {
                                            this.setError("� ��� ��������� X-Ray");
                                        } else if(response.equals("abuseMod")) {
                                            this.setError("������ ��������� ���� � ������");
                                        } else if(response.equals("oldLauncher")) {
                                            this.setError("� ��� ������ �������");
                                            new xUpdater(this.theme, false);
                                        } else {
                                            String[] args1 = response.split(":");
                                            this.remember();
                                            xLauncher launcher1 = xLauncher.getLauncher();
                                            launcher1.drawServerSelect(this.login, args1[1]);
                                        }
                                        break label63;
                                    }
                                } else {
                                boolean args2 = this.clientCheck(out, in);
                                boolean args3 = checkTextures();
                                if(!args2) {
                                    salt = xCipherUtils.genSalt(symbolscount);
                                    out.writeUTF(xCipherUtils.encrypt(salt + "false"));
                                    break label63;
                                } else {
                                    if(!args3) {
                                        salt = xCipherUtils.genSalt(symbolscount);
                                        out.writeUTF(xCipherUtils.encrypt(salt + "3"));
                                    } else {
                                        salt = xCipherUtils.genSalt(symbolscount);
                                        out.writeUTF(xCipherUtils.encrypt(salt + "false"));
                                        this.setError("�������� �� ������ ��������");
                                        break label63;
                                    }
                                }
                                out.flush();
                            }
                        } else {
                            socket.setSoTimeout(20000);
                            String args = this.getHwid();
                            if(args != null) {
                                salt = xCipherUtils.genSalt(symbolscount);
                                out.writeUTF("1:" + xCipherUtils.encrypt(salt + this.getHwid()));
                            } else {
                                xUtils launcher = new xUtils();
                                salt = xCipherUtils.genSalt(symbolscount);
                                out.writeUTF("1:" + xCipherUtils.encrypt(salt + launcher.getPlatform()));
                            }
                            out.flush();
                        }
                    }
                }
            }
            
            socket.close();
        } catch (SocketTimeoutException var11) {
            this.setError("����� ����������� �������");
        } catch (IOException var12) {
            this.setError("������ ����������� ����������");
        }
    }
    
    private void remember() {
        xUtils utils = new xUtils();
        File dir = utils.getDirectory();
        File versionFile = new File(dir, "login");
        DataOutputStream dos;
        if(this.theme.getRemember()) {
            try {
                dos = new DataOutputStream(new FileOutputStream(versionFile));
                dos.writeUTF(this.login + ":" + this.password);
                dos.close();
            } catch (FileNotFoundException var8) {
                var8.printStackTrace();
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        } else {
            try {
                dos = new DataOutputStream(new FileOutputStream(versionFile));
                dos.writeUTF(this.login);
                dos.close();
            } catch (FileNotFoundException var6) {
                var6.printStackTrace();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }
    }
    
    public static void rememberMemory(String value) {
        xUtils utils = new xUtils();
        File dir = utils.getDirectory();
        File versionFile = new File(dir, "memory");
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(new FileOutputStream(versionFile));
            dos.writeUTF(value);
            dos.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        xMain.restart();
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

         while(true) {
            String line;
            if((line = input.readLine()) == null) {
               input.close();
               break;
            }

            result = result + line;
         }
      } catch (Exception var8) {
         xUtils utils1 = new xUtils();
         return utils1.getPlatform().toString();
      }

      if(result.length() < 30) {
         return result.trim();
      } else {
         xUtils utils2 = new xUtils();
         return utils2.getPlatform().toString();
      }
   }
    
   private static void launcherSize() {
       File runningLauncher;

      try {
         runningLauncher = new File(xUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
         if(runningLauncher.getPath().endsWith(".jar")) {
             launcherformat = "jar";
         } else if(runningLauncher.getPath().endsWith(".exe")) {
             launcherformat = "exe";
         }
         MessageDigest md5 = MessageDigest.getInstance("MD5");
         launchersize = calculateHash(md5, runningLauncher.getPath());
      } catch (Exception var5) {
          System.out.println(var5.getMessage());
      }
   }

    public static String xorEncode(String text)
   {
       String res = ""; 
       int j = 0;
       for (int i = 0; i < text.length(); i++)
       {
           res += (char) (text.charAt(i) ^ xSettings.passIdKey.charAt(j));
           j++;
           if (j == xSettings.passIdKey.length()) j = 0;
       }
       return res;
   }

    public static String strToInt(String text)
   {
       String res = "";
       for (int i = 0; i < text.length(); i++) res += (int)text.charAt(i) + "-";
       res = res.substring(0, res.length() - 1);
       return res;
   }
   
   private static String[] addToArray(String[] array, String s) {
        String[] ans = new String[array.length+1];
        System.arraycopy(array, 0, ans, 0, array.length);
        ans[ans.length - 1] = s;
        return ans;
    }

    boolean clientCheck(DataOutputStream out, DataInputStream in) {
        xUtils utils = new xUtils();
        String result;
        String salt;
        try {
            String ch = check(utils.getDirectory(), in);
            String ch2 = checkCount(utils.getDirectory());
            String strtoout = ch;
            for(int c = 0; c < checkformats.length; c++) {
                strtoout = strtoout + ":" + ch2.split(":")[c + 1];
            }
            salt = xCipherUtils.genSalt(symbolscount);
            out.writeUTF(xCipherUtils.encrypt(salt + strtoout));
            result = in.readUTF();
            if (!result.equals("noconnect")){
                result = (xCipherUtils.decrypt(result)).substring(symbolscount);
            }
            if (result.equals("nofiles")){
                this.setError("������ �� ������ ��������");
                return false;
            } else if (result.equals("noconnect")){
                this.setError("��� ����������");
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean mClientCheck() {
        xUtils utils = new xUtils();
        String result;
        String salt;
        try {
            InetAddress e = InetAddress.getByName(socketip);
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket(e, socketport);
            String[] suites = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(suites);
            socket.startHandshake();
            socket.setSoTimeout(20000);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            out.writeUTF("mcheckclient");
            String ch = check(utils.getDirectory(), in);
            String ch2 = checkCount(utils.getDirectory());
            String strtoout = ch;

            for(int c = 0; c < checkformats.length; c++) {
                strtoout = strtoout + ":" + ch2.split(":")[c + 1];
            }

            salt = xCipherUtils.genSalt(symbolscount);
            out.writeUTF(xCipherUtils.encrypt(salt + strtoout));
            out.flush();
            result = in.readUTF();
            socket.close();

            if (!result.equals("noconnect")){
                result = (xCipherUtils.decrypt(result)).substring(symbolscount);
            }

            return !result.equals("nofiles") && !result.equals("noconnect");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private static File[] addToFileArray(File[] array, File s) {
        File[] ans = new File[array.length+1];
        System.arraycopy(array, 0, ans, 0, array.length);
        ans[ans.length - 1] = s;
        return ans;
    }

    private static String check(File path, DataInputStream in) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        xUtils utils = new xUtils();
        String ch = utils.getDirectory().toString();
        String hash = "";
        String strfilearr = in.readUTF();
        strfilearr = (xCipherUtils.decrypt(strfilearr)).substring(symbolscount);
        String[] filearray = strfilearr.split(", ");
        File[] files = {};
        for (String aFilearray : filearray) {
            if (!strfilearr.isEmpty()) {
                File ff = new File(ch + File.separator + aFilearray);
                files = addToFileArray(files, ff);
            }
        }
        if (files.length == 0) {
            return "0";
        }

        for (File file : files) {
            xServer.loadServers();
            for (int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                xServer server = xServer.getServers()[s];

                if (!server.getFolder().isEmpty() && path.toString().equals(ch + File.separator + server.getFolder())) {
                    ch = ch + File.separator + server.getFolder();
                    break;
                }
            }
            if (file.isDirectory() && (!path.toString().equals(ch) || (!file.getName().endsWith("texturepacks") && !file.getName().endsWith("resourcepacks")))) {
                String sHash = check(new File(file.toString()), in);
                hash += (sHash.equals("0")) ? "" : sHash;
            } else if (file.isFile()) {
                for (String checkFormat : checkformats) {
                    if (file.getName().endsWith(checkFormat)) {
                        hash += calculateHash(md5, file.toString());
                        break;
                    }
                }
            }
        }

        return md5(hash);
    }

    private static String checkCount(File path) {
        xUtils utils = new xUtils();
        String ch = utils.getDirectory().toString();
        int[] fileCount = new int[checkformats.length];
        File[] files = path.listFiles();

        assert files != null;
        if (files.length == 0) {
            String count = "0";

            for (String ignored : checkformats) {
                count = count + ":0";
            }

            return count;
        }

        for (File file : files) {
            xServer.loadServers();
            for (int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                xServer server = xServer.getServers()[s];
                if (!server.getFolder().isEmpty()) {
                    if (path.toString().equals(ch + File.separator + server.getFolder())) {
                        ch = ch + File.separator + server.getFolder();
                        break;
                    }
                }
            }
            if (file.isDirectory()) {
                if (path.toString().equals(ch)) {
                    if (!file.getName().endsWith("texturepacks") && !file.getName().endsWith("resourcepacks")) {
                        String shash = checkCount(new File(path + File.separator + file.getName()));
                        for (int c = 0; c < checkformats.length; c++) {
                            int sFileCount = Integer.parseInt(shash.split(":")[c + 1]);
                            fileCount[c] = sFileCount + fileCount[c];
                        }
                    }
                } else {
                    String shash = checkCount(new File(path + File.separator + file.getName()));
                    for (int c = 0; c < checkformats.length; c++) {
                        int sFileCount = Integer.parseInt(shash.split(":")[c + 1]);
                        fileCount[c] = sFileCount + fileCount[c];
                    }
                }
            }
            if (file.isFile()) {
                for (int c = 0; c < checkformats.length; c++) {
                    if (file.getName().endsWith(checkformats[c])) {
                        ++fileCount[c];
                        break;
                    }
                }
            }
        }
        String result = "0";
        for(int c = 0; c < checkformats.length; c++) {
            result = result + ":" + fileCount[c];
        }
        return result;
    }

    private static String calculateHash(MessageDigest algorithm, String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);

        while (dis.read() != -1) ; //TODO: Understand this
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
    
    public static boolean checkTextures() {
        xUtils utils = new xUtils();
        String[] texturepackFolders = {"texturepacks", "resourcepacks"};
        for (String texturepackFolder : texturepackFolders) {
            File textures;
            xServer.loadServers();
            boolean one = false;
            for (int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                xServer server = xServer.getServers()[s];
                if (server.getFolder().isEmpty()) {
                    if (!one) {
                        String tFolder = utils.getDirectory() + File.separator + texturepackFolder;
                        textures = new File(tFolder);
                        if (chTextures(textures)) {
                            return true;
                        }
                        one = true;
                    }
                } else {
                    String tFolder = utils.getDirectory() + File.separator + server.getFolder() + File.separator + texturepackFolder;
                    textures = new File(tFolder);
                    if (chTextures(textures)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean chTextures(File textures) {
        if(textures.exists()) {
            File[] listOfTextures = textures.listFiles();

            assert listOfTextures != null;
            for (File listOfTexture : listOfTextures) {
                if (listOfTexture.isFile()) {
                    String texture = listOfTexture.getName();
                    if (texture.toLowerCase().endsWith(".zip")) {
                        int col = 0;
                        try {
                            ZipFile e = new ZipFile(listOfTexture.getPath());
                            Enumeration entries = e.entries();

                            while (entries.hasMoreElements()) {
                                ZipEntry entry = (ZipEntry) entries.nextElement();
                                String entryName = entry.getName();
                                if (entryName.equals("pack.png")) {
                                    ++col;
                                }
                                boolean op = false;
                                if ((entryName.contains("ctm") || entryName.contains("blocks")) && entryName.contains(".png") && (!(entryName.contains("torch") || entryName.contains("overlay") || entryName.contains("dust")))) {
                                    if (entryName.contains("ore") || entryName.contains("wool") || entryName.contains("brick") || entryName.contains("stone") || entryName.contains("sand") || entryName.contains("dirt") || entryName.contains("_block.") || entryName.contains("clay") || entryName.contains("grass_") || entryName.contains("planks") || entryName.contains("log_")) {
                                        op = true;
                                    }
                                }
                                if (op) {
                                    ++col;
                                    InputStream entryStream = e.getInputStream(entry);
                                    BufferedImage textureImage = ImageIO.read(entryStream);
                                    int alphaPixels = 0;

                                    for (int x = 0; x < textureImage.getWidth(); ++x) {
                                        for (int y = 0; y < textureImage.getHeight(); ++y) {
                                            int pixel = textureImage.getRGB(x, y);
                                            if (pixel >> 24 == 0) {
                                                ++alphaPixels;
                                            }
                                        }
                                    }

                                    if (alphaPixels > (textureImage.getWidth() * textureImage.getHeight() / textureImage.getWidth())) {
                                        return true;
                                    }
                                }
                                if (entry.getName().equals("terrain.png")) {
                                    ++col;
                                    InputStream entryStream = e.getInputStream(entry);
                                    BufferedImage textureImage = ImageIO.read(entryStream);
                                    int alphaPixels = 0;

                                    for (int x = 0; x < 50; ++x) {
                                        for (int y = 0; y < 50; ++y) {
                                            int pixel = textureImage.getRGB(x, y);
                                            if (pixel >> 24 == 0) {
                                                ++alphaPixels;
                                            }
                                        }
                                    }

                                    if (alphaPixels / 25 > 5) {
                                        return true;
                                    }
                                }
                            }
                        } catch (IOException var15) {
                            System.out.println("Failed to open texturepack");
                            System.out.println(var15.getMessage());
                        }
                        if (col < 1) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
                if (listOfTexture.isDirectory()) {
                    return true;
                }
            }
        }
        return false;
    }
}
