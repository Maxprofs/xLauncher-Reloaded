package ru.xeroxp.launcher;

import ru.xeroxp.launcher.utils.xFileUtils;

import java.util.ArrayList;
import java.util.List;

class xStarter {
    public static void main(String[] args) throws Exception {
        try {
            String jarPath = xStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String memory = xLoader.getMemory();

            List<String> params = new ArrayList<String>();
            params.add(xFileUtils.getPlatform().toString().equals("windows") ? "javaw" : "java");
            params.add("-Xmx" + (memory == null || memory.isEmpty() ? "512" : memory) + "m");
            params.add("-Dsun.java2d.noddraw=true");
            params.add("-Dsun.java2d.d3d=false");
            params.add("-Dsun.java2d.opengl=false");
            params.add("-Dsun.java2d.pmoffscreen=false");
            params.add("-classpath");
            params.add(jarPath);
            params.add("ru.xeroxp.launcher.xMain");

            Process process = new ProcessBuilder(params).start();

            if (process == null) {
                throw new Exception("Launcher can't be started!");
            }

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            xMain.start();
        }
    }
}