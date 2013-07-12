package ru.xeroxp.launcher;

import java.io.File;
import java.util.ArrayList;

public class xLoader {
    public static String userName;
    public static String sessionId = "0";
    public static String jarfile;
    public static String server = "0";
    public static String port = "25565";
    public static String folder;
    public static String version;
    public static Process process;
    
    public void init(String userName, String sessionId, String server, String port, String folder, String jar, String version) {
        this.userName = userName;
        this.sessionId = sessionId;
        this.server = server;
        this.port = port;
        this.jarfile = jar;
        this.folder = folder;
        this.version = version;
    }
    
    public void init(String userName) {
        this.userName = userName;
        this.jarfile = xSettings.offlineClient[1];
        this.folder = xSettings.offlineClient[0];
        this.version = xSettings.offlineClient[2];
    }
    
    public void StartMinecraft() {
        try {
            String memory = xTheme.readMemory();
            if (memory == null || memory.isEmpty()) memory = Integer.toString(512);
            ArrayList<String> params = new ArrayList<String>();
            xUtils utils = new xUtils();
            String nativespath;
            File librarypath;
            String workdir;
            String assetsdir;
            String jarpath;
            if (this.folder.isEmpty()) {
                nativespath = utils.getDirectory() + File.separator + "bin" + File.separator + "natives";
                librarypath = new File(utils.getDirectory() + File.separator + "libraries");
                workdir = utils.getDirectory().toString();
                jarpath = utils.getDirectory() + File.separator + "bin" + File.separator + this.jarfile;
                assetsdir = utils.getDirectory() + File.separator + "assets";
            } else {
                nativespath = utils.getDirectory() + File.separator + this.folder + File.separator + "bin" + File.separator + "natives";
                librarypath = new File(utils.getDirectory() + File.separator + this.folder + File.separator + "libraries");
                workdir = utils.getDirectory() + File.separator + this.folder;
                jarpath = utils.getDirectory() + File.separator + this.folder + File.separator + "bin" + File.separator + this.jarfile;
                assetsdir = utils.getDirectory() + File.separator + this.folder + File.separator + "assets";
            }
            if (utils.getPlatform().toString().equals("windows")) params.add("javaw");
            else params.add("java");
            params.add("-Xms" + Integer.parseInt(memory)/2 + "m");
            params.add("-Xmx" + memory + "m");
            File assetsDirectory = new File(assetsdir);
            if (utils.getPlatform().toString().equals("macos")) {
                params.add("-Xdock:icon=" + new File(assetsDirectory, "icons/minecraft.icns").getAbsolutePath());
                params.add("-Xdock:name=" + xSettings.gameName);
            }
            String img = new File(xLoader.class.getResource("/images/favicon.png").toString()).getAbsolutePath();
            System.out.println(img);
            params.add("-Djava.library.path=" + nativespath);
            params.add("-classpath");
            String libraries = getLibraries(librarypath);
            if (!libraries.isEmpty()) {
                libraries = libraries + ";" + jarpath;
            } else {
                libraries = jarpath;
            }
            params.add("\"" + libraries + "\"");
            params.add("net.minecraft.client.main.Main");
            params.add("--username");
            params.add(this.userName);
            params.add("--session");
            params.add(this.sessionId);
            params.add("--version");
            params.add(this.version);
            params.add("--gameDir");
            params.add(workdir);
            params.add("--assetsDir");
            params.add(assetsdir);
            if (!this.server.equals("0")) {
                params.add("--server");
                params.add(this.server);
                params.add("--port");
                params.add(this.port);
            }
            
            ProcessBuilder pb = new ProcessBuilder(params);
            process = pb.start();
            if (process == null) throw new Exception("Client can't be started!");
            exit();
        } catch (Exception var6) {
            System.out.println(var6.getMessage());
        }
    }
    
    private static String getLibraries(File path) throws Exception {
        String libraries = "";        
        File[] files = path.listFiles();
        if (files.length == 0) {
            return "";
        }
        for(int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()) {
                String slibraries = getLibraries(new File(path+File.separator+files[i].getName()));
                if (libraries.isEmpty()) {
                    libraries = slibraries;
                } else {
                    libraries = libraries + ";" + slibraries;
                }
            }
            if(files[i].isFile()) {
                if (libraries.isEmpty()) {
                    libraries = files[i].getAbsolutePath();
                } else {
                    libraries = libraries + ";" + files[i].getAbsolutePath();
                }                
            }
        }
        return libraries;
    }
    
    private static void exit() { 
        new Thread (new Runnable() {
            public void run() {
                try {
                    process.waitFor();
                    if (!xMain.error) {
                        System.exit(1);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }).start();
    }
}