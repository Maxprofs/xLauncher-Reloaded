package ru.xeroxp.launcher.process;

import ru.xeroxp.launcher.misc.xDebug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

class ProcessMonitorThread extends Thread {

    private final JavaProcess process;

    public ProcessMonitorThread(JavaProcess process) {
        this.process = process;
    }

    @Override
    public void run() {
        InputStreamReader reader = new InputStreamReader(this.process.getRawProcess().getInputStream());
        BufferedReader buf = new BufferedReader(reader);
        String line;

        while (this.process.isRunning()) {
            try {
                while ((line = buf.readLine()) != null) {
                    xDebug.infoMessage("Client> " + line);
                    this.process.getSysOutLines().add(line);
                }
            } catch (IOException e) {
                Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    buf.close();
                } catch (IOException e) {
                    Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        JavaProcessRunnable onExit = this.process.getExitRunnable();
        if (onExit != null) {
            onExit.onJavaProcessEnded(this.process);
        }
    }
}
