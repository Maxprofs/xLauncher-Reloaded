package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;

public class xDebug {
    public static void errorMessage(String message) {
        if (xSettings.debug) {
            System.err.println("\n========= ERROR =========\n" + message + "\n=========================");
        }
    }

    public static void infoMessage(String message) {
        if (xSettings.debug) {
            System.out.println("INFO: " + message);
        }
    }
}
