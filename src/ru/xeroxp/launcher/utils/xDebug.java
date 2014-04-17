package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;

public class xDebug {
    public static void errorMessage(String message) {
        if (xSettings.DEBUG_MODE) {
            System.err.println("\n========= ERROR =========\n" + message + "\n=========================");
        }
    }

    public static void infoMessage(String message) {
        if (xSettings.DEBUG_MODE) {
            System.out.println("INFO: " + message);
        }
    }
}
