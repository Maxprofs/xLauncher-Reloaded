package ru.xeroxp.launcher;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import javax.imageio.ImageIO;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class xAuth implements Runnable {
    
    private String login;
    private String password;
    private xTheme theme;
    private Random rand = new Random();
    private static String socketip;
    private static int socketport;
    private static String[] checkformats;
    private static int symbolscount = 0;
    private static String launcherformat;
    private static String launchersize;
    
    public xAuth(String login, String password, xTheme theme) {
        this.theme = theme;
        this.login = login;
        this.password = strtoint(xorencode(password.toString(), xSettings.passidkey));
    }
    
    public xAuth(String login, xTheme theme, String password) {
        this.theme = theme;
        this.login = login;
        this.password = password;
    }
    
    public void run() {
        this.sendAuth();
    }
    
    public static String md5(String string) {
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
        StringBuffer sb = new StringBuffer();
        
        for(int i = 0; i < bArr.length; ++i) {
            int unsigned = bArr[i] & 255;
            if(unsigned < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(unsigned));
        }
        return sb.toString();
    }
    
    private void setError(String text) {
        this.theme.setError(text);
    }
    
    public void getCheckFormats() {
        try {
            InetAddress e = InetAddress.getByName(this.socketip);
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sockete = (SSLSocket) sf.createSocket(e, socketport);
            String[] suites = sockete.getSupportedCipherSuites();
            sockete.setEnabledCipherSuites(suites);
            sockete.startHandshake();
            sockete.setSoTimeout(10000);
            InputStream sine = sockete.getInputStream();
            OutputStream soute = sockete.getOutputStream();
            DataInputStream ine = new DataInputStream(sine);
            DataOutputStream oute = new DataOutputStream(soute);
            oute.writeUTF("formats");
            oute.flush();
            String formats = ine.readUTF();
            formats = xCipherUtils.decrypt(formats);
            sockete.close();
            this.checkformats = new String[0];
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
    
    public static void addtoarraycf(String text) {
        checkformats = addToArray(checkformats, text);
    }
    
    public void getServerConnect() {
        xServerConnect.loadServers();
        String[] servers = {};
        for (final xServerConnect server : xServerConnect.getConnectServers())
        {
            String s = (server.getServerIp() + ";" + server.getServerPort());
            servers = addToArray(servers, s);
        }
        int randomNum = this.rand.nextInt(servers.length);
        this.socketip = servers[randomNum].split(";")[0];
        this.socketport = Integer.parseInt(servers[randomNum].split(";")[1]);
    }
    
    private void sendAuth() {
        this.theme.setAuth("Авторизация");
        getServerConnect();
        launcherSize();
        getCheckFormats();
        String salt;
        
        try {
            System.out.println("Connection to authorization server");
            InetAddress e = InetAddress.getByName(this.socketip);
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
            salt = xCipherUtils.gensalt(symbolscount);
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
                                            this.setError("Ошибка авторизации");
                                        } else if(response.equals("fail")) {
                                            this.setError("Неправильный логин или пароль");
                                        } else if(response.equals("abanned")) {
                                            this.setError("Ваш аккаунт был заблокирован");
                                        } else if(response.equals("banned")) {
                                            this.setError("Вы были заблокированы");
                                        } else if(response.equals("abuseSize")) {
                                            this.setError("Нельзя модифицировать клиент");
                                        } else if(response.equals("abuseLauncherSize")) {
                                            this.setError("Лаунчер не прошел проверку");
                                        } else if(response.equals("abuseTexture")) {
                                            this.setError("У вас обнаружен X-Ray");
                                        } else if(response.equals("abuseMod")) {
                                            this.setError("Нельзя добавлять моды в клиент");
                                        } else if(response.equals("oldLauncher")) {
                                            this.setError("У Вас старый лаунчер");
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
                                boolean args2 = this.clientcheck(out, in);
                                boolean args3 = checkTextures();
                                if(!args2) {
                                    salt = xCipherUtils.gensalt(symbolscount);
                                    out.writeUTF(xCipherUtils.encrypt(salt + "false"));
                                    break label63;
                                } else {
                                    if(!args3) {
                                        salt = xCipherUtils.gensalt(symbolscount);
                                        out.writeUTF(xCipherUtils.encrypt(salt + "3"));
                                    } else {
                                        salt = xCipherUtils.gensalt(symbolscount);
                                        out.writeUTF(xCipherUtils.encrypt(salt + "false"));
                                        this.setError("Текстуры не прошли проверку");
                                        break label63;
                                    }
                                }
                                out.flush();
                            }
                        } else {
                            socket.setSoTimeout(20000);
                            String args = this.getHwid();
                            if(args != null) {
                                salt = xCipherUtils.gensalt(symbolscount);
                                out.writeUTF("1:" + xCipherUtils.encrypt(salt + this.getHwid()));
                            } else {
                                xUtils launcher = new xUtils();
                                salt = xCipherUtils.gensalt(symbolscount);
                                out.writeUTF("1:" + xCipherUtils.encrypt(salt + launcher.getPlatform()));
                            }
                            out.flush();
                        }
                    }
                }
            }
            
            socket.close();
        } catch (SocketTimeoutException var11) {
            this.setError("Время подключения истекло");
        } catch (IOException var12) {
            this.setError("Сервер авторизации недоступен");
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
      File runningLauncher = null;
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
    
   public static String xorencode(String text, String key)
   {
       String res = ""; 
       int j = 0;
       for (int i = 0; i < text.length(); i++)
       {
           res += (char)(text.charAt(i) ^ key.charAt(j));
           j++; if(j==key.length()) j = 0;
       }
       return res;
   }
   
   public static String strtoint(String text)
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
    
    public boolean clientcheck(DataOutputStream out, DataInputStream in) {
        xUtils utils = new xUtils();
        String result = "noconnect";
        String salt;
        try {
            String ch = check(utils.getDirectory(), in);
            String ch2 = checkcount(utils.getDirectory());
            String strtoout = ch;
            for(int c = 0; c < checkformats.length; c++) {
                strtoout = strtoout + ":" + ch2.split(":")[c + 1];
            }
            salt = xCipherUtils.gensalt(symbolscount);
            out.writeUTF(xCipherUtils.encrypt(salt + strtoout));
            result = in.readUTF();
            if (!result.equals("noconnect")){
                result = (xCipherUtils.decrypt(result)).substring(symbolscount);
            }
            if (result.equals("nofiles")){
                this.setError("Клиент не прошел проверку");
                return false;
            } else if (result.equals("noconnect")){
                this.setError("Нет соединения");
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static boolean mclientcheck() {
        xUtils utils = new xUtils();
        String result = "noconnect";
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
            String ch2 = checkcount(utils.getDirectory());
            String strtoout = ch;
            for(int c = 0; c < checkformats.length; c++) {
                strtoout = strtoout + ":" + ch2.split(":")[c + 1];
            }
            salt = xCipherUtils.gensalt(symbolscount);
            out.writeUTF(xCipherUtils.encrypt(salt + strtoout));
            out.flush();
            result = in.readUTF();
            socket.close();
            if (!result.equals("noconnect")){
                result = (xCipherUtils.decrypt(result)).substring(symbolscount);
            }
            if (result.equals("nofiles")){
                return false;
            } else if (result.equals("noconnect")){
                return false;
            } else {
                return true;
            }
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
    
    public static String check(File path, DataInputStream in) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        xUtils utils = new xUtils();
        String ch = utils.getDirectory().toString();
        String hash = "";
        String strfilearr = in.readUTF();
        strfilearr = (xCipherUtils.decrypt(strfilearr)).substring(symbolscount);
        String[] filearray = strfilearr.split(", ");
        File[] files = {};
        for (int f = 0; f < filearray.length; f++){
            if (!strfilearr.isEmpty()){
            File ff = new File(ch + File.separator + filearray[f]);
            files = addToFileArray(files, ff);
            }
        }
        if (files.length == 0) {
            return "0";
        }
        for(int i = 0; i < files.length; i++) {
            if(files[i].exists()){
                xServer.loadServers();
                for(int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                    xServer server = xServer.getServers()[s];
                    if (!server.getFolder().isEmpty()){
                        if (path.toString().equals(ch + File.separator + server.getFolder())){
                            ch = ch + File.separator + server.getFolder();
                            break;
                        }
                    }
                }
                if(files[i].isDirectory()) {
                    if(path.toString().equals(ch)){
                        if(files[i].getName().endsWith("texturepacks") || files[i].getName().endsWith("resourcepacks")){
                        } else {
                            String shash = check(new File(files[i].toString()), in);
                            String sshash = "";
                            if(shash.equals("0")){
                                sshash = "";
                            } else {
                                sshash = shash;
                            }
                            hash = hash + sshash;
                        }
                    } else {
                        String shash = check(new File(files[i].toString()), in);
                        if(shash.equals("0")){
                            shash = "";
                        }
                        String sshash = shash;
                        hash = hash + sshash;
                    }
                }
                if(files[i].isFile()) {
                    for(int c = 0; c < checkformats.length; c++) {
                        if(files[i].getName().endsWith(checkformats[c])) {
                            hash = hash + calculateHash(md5, files[i].toString());
                            break;
                        }
                    }
                }
            }
        }
        String result = md5(hash);
        return result;
    }
    
    public static String checkcount(File path) throws Exception {
        xUtils utils = new xUtils();
        String ch = utils.getDirectory().toString();
        int[] filecount = new int[checkformats.length];
        File[] files = path.listFiles();
        if (files.length == 0) {
            String count = "0";
            for(int c = 0; c < checkformats.length; c++) {
                count = count + ":0";
            }
            return count;
        }
        for(int i = 0; i < files.length; i++) {
            xServer.loadServers();
            for(int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                xServer server = xServer.getServers()[s];
                if (!server.getFolder().isEmpty()){
                    if (path.toString().equals(ch + File.separator + server.getFolder())){
                        ch = ch + File.separator + server.getFolder();
                        break;
                    }
                }
            }
            if(files[i].isDirectory()) {
                if(path.toString().equals(ch)){
                    if(files[i].getName().endsWith("texturepacks") || files[i].getName().endsWith("resourcepacks")){
                    } else {
                        String shash = checkcount(new File(path+File.separator+files[i].getName()));
                        for(int c = 0; c < checkformats.length; c++) {
                            int sfilecount = Integer.parseInt(shash.split(":")[c + 1]);
                            filecount[c] = sfilecount + filecount[c];
                        }
                    }
                } else {
                    String shash = checkcount(new File(path+File.separator+files[i].getName()));
                    for(int c = 0; c < checkformats.length; c++) {
                        int sfilecount = Integer.parseInt(shash.split(":")[c + 1]);
                        filecount[c] = sfilecount + filecount[c];
                    }
                }
            }
            if(files[i].isFile()) {
                for(int c = 0; c < checkformats.length; c++) {
                    if(files[i].getName().endsWith(checkformats[c])) {
                        ++filecount[c];
                        break;
                    }
                }
            }
        }
        String result = "0";
        for(int c = 0; c < checkformats.length; c++) {
            result = result + ":" + filecount[c];
        }
        return result;
    }
    
    public static String calculateHash(MessageDigest algorithm, String fileName) throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);
        
        while (dis.read() != -1);
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
        String[] texturepacksFolders = {"texturepacks", "resourcepacks"};
        for(int p = 0; p < texturepacksFolders.length; ++p) {
            File textures;
            xServer.loadServers();
            boolean one = false;
            for(int s = 0; s < xSettingsOfTheme.Servers.length; ++s) {
                xServer server = xServer.getServers()[s];
                if (server.getFolder().isEmpty()){
                    if (!one){
                        textures = new File(utils.getDirectory() + File.separator + texturepacksFolders[p]);
                        if(chTextures(textures)) return true;
                        one = true;
                    }
                } else {
                    textures = new File(utils.getDirectory() + File.separator + server.getFolder() + File.separator + texturepacksFolders[p]);
                    if(chTextures(textures)) return true;
                }
            }
        }
        return false;
    }
    
    public static boolean chTextures(File textures) {
        if(textures.exists()) {
            File[] listofTextures = textures.listFiles();
            
            for(int i = 0; i < listofTextures.length; ++i) {
                if(listofTextures[i].isFile()) {
                    String texture = listofTextures[i].getName();
                    if(texture.toLowerCase().endsWith(".zip")) {
                        int col = 0;
                        try {
                            ZipFile e = new ZipFile(listofTextures[i].getPath());
                            Enumeration entries = e.entries();
                            
                            while(entries.hasMoreElements()) {
                                ZipEntry entry = (ZipEntry)entries.nextElement();
                                if(entry.getName().equals("pack.png")) {
                                    ++col;
                                }
                                if(entry.getName().equals("terrain.png")) {
                                    ++col;
                                    InputStream entryStream = e.getInputStream(entry);
                                    BufferedImage textureImage = ImageIO.read(entryStream);
                                    int alphaPixels = 0;
                                    
                                    for(int y = 0; y < 50; ++y) {
                                        for(int z = 0; z < 50; ++z) {
                                            int pixel = textureImage.getRGB(y, z);
                                            if(pixel >> 24 == 0) {
                                                ++alphaPixels;
                                            }
                                        }
                                    }
                                    
                                    if(alphaPixels / 25 > 5) {
                                        return true;
                                    }
                                    break;
                                }
                            }
                        } catch (IOException var15) {
                            System.out.println("Failed to open texturepack");
                            System.out.println(var15.getMessage());
                        }
                        if (col < 1){
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
                if(listofTextures[i].isDirectory()) {
                    String texture = listofTextures[i].getName();
                    File listofdirTextures = new File(textures + File.separator + texture);
                    File[] dirtexture = listofdirTextures.listFiles();
                    int col = 0;
                    for(int l = 0; l < dirtexture.length; ++l) {
                        String texturedir = dirtexture[l].getName();
                        try {
                            if(texturedir.equals("pack.png")) {
                                ++col;
                            }
                            if(texturedir.equals("terrain.png")) {
                                ++col;
                                BufferedImage textureImage = ImageIO.read(new File(listofdirTextures + File.separator + texturedir));
                                int alphaPixels = 0;
                                
                                for(int y = 0; y < 50; ++y) {
                                    for(int z = 0; z < 50; ++z) {
                                        int pixel = textureImage.getRGB(y, z);
                                        if(pixel >> 24 == 0) {
                                            ++alphaPixels;
                                        }
                                    }
                                }
                                
                                if(alphaPixels / 25 > 5) {
                                    return true;
                                }
                                break;
                            }
                        } catch (IOException var15) {
                            System.out.println("Failed to open texturepack");
                            System.out.println(var15.getMessage());
                        }
                    }
                    if (col < 1){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
