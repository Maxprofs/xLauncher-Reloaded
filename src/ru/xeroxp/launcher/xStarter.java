package ru.xeroxp.launcher;

import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.utils.xUtils;

import java.util.ArrayList;
import java.util.List;

class xStarter {
    public static void main(String[] args) throws Exception {
        try {
            String jarPath = xStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String memory = xTheme.readMemory();
            if (memory == null || memory.isEmpty()) memory = Integer.toString(512);

            List<String> params = new ArrayList<String>();
            if (xUtils.getPlatform().toString().equals("windows")) params.add("javaw");
            else params.add("java");
            params.add("-Xmx" + memory + "m");
            params.add("-Dsun.java2d.noddraw=true");
            params.add("-Dsun.java2d.d3d=false");
            params.add("-Dsun.java2d.opengl=false");
            params.add("-Dsun.java2d.pmoffscreen=false");
            params.add("-classpath");
            params.add(jarPath);
            params.add("ru.xeroxp.launcher.xMain");

            ProcessBuilder pb = new ProcessBuilder(params);
            Process process = pb.start();
            if (process == null) throw new Exception("Launcher can't be started!");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            xMain.start();
        }
    }
}