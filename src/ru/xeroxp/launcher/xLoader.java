package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.process.JavaProcess;
import ru.xeroxp.launcher.process.JavaProcessLauncher;
import ru.xeroxp.launcher.process.JavaProcessRunnable;
import ru.xeroxp.launcher.utils.xUtils;

import java.io.File;

public class xLoader implements JavaProcessRunnable {
    public static JavaProcess process;
    private final Object lock = new Object();
    private boolean isWorking = false;
    private final String userName;
    private String sessionId = "0";
    private final String jarFile;
    private String server = "0";
    private String port = "25565";
    private final String folder;
    private final String version;

    public xLoader(String userName, String sessionId, String server, String port, String folder, String jar, String version) {
        this.userName = userName;
        this.sessionId = sessionId;
        this.server = server;
        this.port = port;
        this.jarFile = jar;
        this.folder = folder;
        this.version = version;
        playGame();
    }

    public xLoader(String userName) {
        this.userName = userName;
        this.jarFile = xSettings.offlineClient[1];
        this.folder = xSettings.offlineClient[0];
        this.version = xSettings.offlineClient[2];
        playGame();
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

    void playGame() {
        synchronized (this.lock) {
            if (this.isWorking) {
                return;
            }
            setWorking(true);
            launchGame();
        }
    }

    void launchGame() {
        try {
            String memory = xTheme.readMemory();

            if (memory == null || memory.isEmpty()) {
                memory = Integer.toString(512);
            }

            String separator = System.getProperty("file.separator");
            File nativesPath;
            File libraryPath;
            File workDir;
            File assetsDir;
            File jarPath;

            if (this.folder.isEmpty()) {
                nativesPath = new File(xUtils.getDirectory(), "bin" + separator + "natives");
                libraryPath = new File(xUtils.getDirectory(), "libraries");
                workDir = xUtils.getDirectory();
                jarPath = new File(xUtils.getDirectory(), "bin" + separator + this.jarFile);
                assetsDir = new File(xUtils.getDirectory(), "assets");
            } else {
                nativesPath = new File(xUtils.getDirectory(), this.folder + separator + "bin" + separator + "natives");
                libraryPath = new File(xUtils.getDirectory(), this.folder + separator + "libraries");
                workDir = new File(xUtils.getDirectory(), this.folder);
                jarPath = new File(xUtils.getDirectory(), this.folder + separator + "bin" + separator + this.jarFile);
                assetsDir = new File(xUtils.getDirectory(), this.folder + separator + "assets");
            }

            String libraries = getLibraries(libraryPath);

            if (!libraries.isEmpty()) {
                libraries = libraries + ";" + jarPath.getAbsolutePath();
            } else {
                libraries = jarPath.getAbsolutePath();
            }

            JavaProcessLauncher processLauncher = new JavaProcessLauncher(xUtils.getJavaExecutable(), new String[0]);

            if (jarFile.toLowerCase().contains("forge")) {
                processLauncher.addCommands("-Dfml.ignoreInvalidMinecraftCertificates=true");
                processLauncher.addCommands("-Dfml.ignorePatchDiscrepancies=true");
            }

            processLauncher.addCommands("-Xmx" + memory + "M");
            File assetsDirectory = assetsDir;

            if (xUtils.getPlatform().toString().equals("macos")) {
                processLauncher.addCommands("-Xdock:icon=" + new File(assetsDirectory, "icons/minecraft.icns").getAbsolutePath());
                processLauncher.addCommands("-Xdock:name=" + xSettings.gameName);
            }

            processLauncher.addCommands("\"" + "-Djava.library.path=" + nativesPath.getAbsolutePath() + "\"");
            processLauncher.addCommands("-cp", "\"" + libraries + "\"");

            if (jarFile.toLowerCase().contains("forge")) {
                processLauncher.addCommands("net.minecraft.launchwrapper.Launch");
                processLauncher.addCommands("--tweakClass", "cpw.mods.fml.common.launcher.FMLTweaker");
            } else {
                processLauncher.addCommands("net.minecraft.client.main.Main");
            }

            //processLauncher.addCommands(new String[] { userName, sessionId, version });
            processLauncher.addCommands("--username", userName);
            processLauncher.addCommands("--session", sessionId);
            processLauncher.addCommands("--version", version);
            processLauncher.addCommands("--gameDir", "\"" + workDir.getAbsolutePath() + "\"");
            processLauncher.addCommands("--assetsDir", "\"" + assetsDir.getAbsolutePath() + "\"");

            if (!server.equals("0")) {
                processLauncher.addCommands("--server", server);
                processLauncher.addCommands("--port", port);
            }

            process = processLauncher.start();
            process.safeSetExitRunnable(this);
        } catch (Exception e) {
            e.printStackTrace();
            setWorking(false);
        }
    }

    @Override
    public void onJavaProcessEnded(JavaProcess process) {
        int exitCode = process.getExitCode();

        if (exitCode == 0) {
            System.out.println("Game ended with no troubles detected (exit code " + exitCode + ")");
        } else {
            System.out.println("Game ended with bad state (exit code " + exitCode + ")");

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
                System.out.println(errorText);
            }
            setWorking(false);
        }
        if (!xMain.error) {
            System.exit(1);
        }
    }

}