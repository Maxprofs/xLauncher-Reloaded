package ru.xeroxp.server.utils;

import ru.xeroxp.server.config.Settings;

public class Debug {
    public static void errorMessage(String message) {
        if (Settings.DEBUG_MODE) {
            System.err.println("\n========= ERROR =========\n" + message + "\n=========================");
        }
    }

    public static void infoMessage(String message) {
        if (Settings.DEBUG_MODE) {
            System.out.println("INFO: " + message);
        }
    }
}
