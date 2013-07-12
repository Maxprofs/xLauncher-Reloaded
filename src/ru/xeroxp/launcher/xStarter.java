package ru.xeroxp.launcher;

import java.util.ArrayList;

public class xStarter
{
    public static void main(String[] args) throws Exception
    {	
        try
        {
            String jarpath = xStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String memory = xTheme.readMemory();
            if (memory == null || memory.isEmpty()) memory = Integer.toString(512);
            
            ArrayList<String> params = new ArrayList<String>();
            xUtils utils = new xUtils();
            if (utils.getPlatform().toString().equals("windows")) params.add("javaw");
            else params.add("java");
            params.add("-Xms" + Integer.parseInt(memory)/2 + "m");
            params.add("-Xmx" + memory + "m");
            params.add("-Dsun.java2d.noddraw=true");
            params.add("-Dsun.java2d.d3d=false");
            params.add("-Dsun.java2d.opengl=false");
            params.add("-Dsun.java2d.pmoffscreen=false");
            params.add("-classpath");
            params.add(jarpath);
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