package ru.xeroxp.launcher.process;

import java.util.List;

public class JavaProcess {
    private final List<String> commands;
    private final Process process;
    private final LimitedCapacityList sysOutLines = new LimitedCapacityList(String.class, 5);
    private JavaProcessRunnable onExit;

    public JavaProcess(List<String> commands, Process process) {
        this.commands = commands;
        this.process = process;
        ProcessMonitorThread monitor = new ProcessMonitorThread(this);
        monitor.start();
    }

    public Process getRawProcess() {
        return this.process;
    }

    public LimitedCapacityList getSysOutLines() {
        return this.sysOutLines;
    }

    public boolean isRunning() {
        try {
            this.process.exitValue();
            return false;
        } catch (IllegalThreadStateException var2) {
            return true;
        }
    }

    public void safeSetExitRunnable(JavaProcessRunnable runnable) {
        this.setExitRunnable(runnable);
        if (!this.isRunning() && runnable != null) {
            runnable.onJavaProcessEnded(this);
        }

    }

    public JavaProcessRunnable getExitRunnable() {
        return this.onExit;
    }

    void setExitRunnable(JavaProcessRunnable runnable) {
        this.onExit = runnable;
    }

    public int getExitCode() {
        try {
            return this.process.exitValue();
        } catch (IllegalThreadStateException var2) {
            var2.fillInStackTrace();
            throw var2;
        }
    }

    public String toString() {
        return "JavaProcess[commands=" + this.commands + ", isRunning=" + this.isRunning() + "]";
    }

    public void stop() {
        this.process.destroy();
    }
}