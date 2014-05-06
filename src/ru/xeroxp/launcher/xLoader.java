package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.misc.xConfig;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.process.JavaProcess;
import ru.xeroxp.launcher.process.JavaProcessLauncher;
import ru.xeroxp.launcher.process.JavaProcessRunnable;
import ru.xeroxp.launcher.utils.xFileUtils;

import java.io.File;
import java.io.IOException;

public class xLoader implements JavaProcessRunnable {
    public static JavaProcess process;
    private final String userName;
    private final String jarFile;
    private final String folder;
    private final String version;
    private boolean isWorking = false;
    private String sessionId = "0";
    private String server = "0";
    private String port = "25565";

    public xLoader(String userName, String sessionId, String server, String port, String folder, String jar, String version) {
        this.userName = userName;
        this.sessionId = sessionId;
        this.server = server;
        this.port = port;
        this.jarFile = jar;
        this.folder = folder;
        this.version = version;
        this.playGame();
    }

    public xLoader(String userName) {
        this.userName = userName;
        this.jarFile = xSettings.OFFLINE_CLIENT[1];
        this.folder = xSettings.OFFLINE_CLIENT[0];
        this.version = xSettings.OFFLINE_CLIENT[2];
        this.playGame();
    }

    public static String getMemory() {
        try {
            xConfig config = new xConfig(xConfig.LAUNCHER);
            return config.get("memory");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getLibraries(File path) {
        String libraries = "";
        File[] files = path.listFiles();

        if (files == null || files.length == 0) {
            return "";
        }

        for (File file : files) {
            if (file.isDirectory()) {
                libraries = ((libraries.isEmpty()) ? "" : libraries + ";") + getLibraries(new File(path + File.separator + file.getName()));
            } else if (file.isFile()) {
                libraries = ((libraries.isEmpty()) ? "" : libraries + ";") + file.getAbsolutePath();
            }
        }

        return libraries;
    }

    private void setWorking(boolean working) {
        this.isWorking = working;
    }

    private synchronized void playGame() {
        if (this.isWorking) {
            return;
        }

        this.setWorking(true);
        this.launchGame();
    }

    private void launchGame() {
        try {
            String memory = getMemory();

            if (memory == null || memory.isEmpty()) {
                memory = Integer.toString(512);
            }

            File nativesPath;
            File libraryPath;
            File workDir;
            File assetsDir;
            File jarPath;

            if (this.folder.isEmpty()) {
                nativesPath = new File(xFileUtils.getRootDirectory(), "bin" + File.separator + "natives");
                libraryPath = new File(xFileUtils.getRootDirectory(), "libraries");
                workDir = xFileUtils.getRootDirectory();
                jarPath = new File(xFileUtils.getRootDirectory(), "bin" + File.separator + this.jarFile);
                assetsDir = new File(xFileUtils.getRootDirectory(), "assets");
            } else {
                nativesPath = new File(xFileUtils.getRootDirectory(), this.folder + File.separator + "bin" + File.separator + "natives");
                libraryPath = new File(xFileUtils.getRootDirectory(), this.folder + File.separator + "libraries");
                workDir = new File(xFileUtils.getRootDirectory(), this.folder);
                jarPath = new File(xFileUtils.getRootDirectory(), this.folder + File.separator + "bin" + File.separator + this.jarFile);
                assetsDir = new File(xFileUtils.getRootDirectory(), this.folder + File.separator + "assets");
            }

            String libraries = getLibraries(libraryPath);
            if (!libraries.isEmpty()) {
                libraries += ";" + jarPath.getAbsolutePath();
            } else {
                libraries = jarPath.getAbsolutePath();
            }

            JavaProcessLauncher processLauncher = new JavaProcessLauncher(xFileUtils.getJavaExecutable(), new String[0]);

            if (jarFile.toLowerCase().contains("forge")) {
                processLauncher.addCommands("-Dfml.ignoreInvalidMinecraftCertificates=true");
                processLauncher.addCommands("-Dfml.ignorePatchDiscrepancies=true");
            }

            processLauncher.addCommands("-Xmx" + memory + "M");
            File assetsDirectory = assetsDir;

            if (xFileUtils.getPlatform().toString().equals("macos")) {
                processLauncher.addCommands("-Xdock:icon=" + new File(assetsDirectory, "icons/minecraft.icns").getAbsolutePath());
                processLauncher.addCommands("-Xdock:name=" + xSettings.GAME_NAME);
            }

            processLauncher.addCommands("\"" + "-Djava.library.path=" + nativesPath.getAbsolutePath() + "\"");
            processLauncher.addCommands("-cp", "\"" + libraries + "\"");

            if (jarFile.toLowerCase().contains("forge")) {
                processLauncher.addCommands("net.minecraft.launchwrapper.Launch");
                processLauncher.addCommands("--tweakClass", "cpw.mods.fml.common.launcher.FMLTweaker");
            } else {
                processLauncher.addCommands("net.minecraft.client.main.Main");
            }

            processLauncher.addCommands("--username", userName);
            processLauncher.addCommands("--session", sessionId);
            processLauncher.addCommands("--version", version);
            processLauncher.addCommands("--gameDir", "\"" + workDir.getAbsolutePath() + "\"");
            processLauncher.addCommands("--assetsDir", "\"" + assetsDir.getAbsolutePath() + "\"");

            xDebug.infoMessage("Launching client with this parameters: user - " + userName + ", session - " + sessionId + ", ver -" + version);

            if (!server.equals("0")) {
                processLauncher.addCommands("--server", server);
                processLauncher.addCommands("--port", port);
            }

            process = processLauncher.start();
            process.safeSetExitRunnable(this);
        } catch (Exception e) {
            e.printStackTrace();
            this.setWorking(false);
        }
    }

    @Override
    public void onJavaProcessEnded(JavaProcess process) {
        int exitCode = process.getExitCode();

        if (exitCode == 0) {
            xDebug.infoMessage("Game ended with no troubles detected (exit code " + exitCode + ")");
        } else {
            xDebug.errorMessage("Game ended with bad state (exit code " + exitCode + ")");

            String errorText = null;
            String[] sysOut = (String[]) process.getSysOutLines().getItems();

            for (int i = sysOut.length - 1; i >= 0; i--) {
                String line = sysOut[i];
                String crashIdentifier = "#@!@#";
                int pos = line.lastIndexOf(crashIdentifier);

                if ((pos >= 0) && (pos < line.length() - crashIdentifier.length() - 1)) {
                    errorText = line.substring(pos + crashIdentifier.length()).trim();
                    break;
                }
            }

            if (errorText != null) {
                xDebug.errorMessage(errorText);
            }

            this.setWorking(false);
        }

        if (!xMain.error) {
            System.exit(1);
        }
    }
}