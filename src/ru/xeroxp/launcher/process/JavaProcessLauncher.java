package ru.xeroxp.launcher.process;

import ru.xeroxp.launcher.utils.xFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaProcessLauncher {

    private final String jvmPath;
    private final List<String> commands;
    private File directory;


    public JavaProcessLauncher(String jvmPath, String[] commands) {
        if (jvmPath == null) {
            jvmPath = xFileUtils.getJavaExecutable();
        }

        this.jvmPath = jvmPath;
        this.commands = new ArrayList<String>(commands.length);
        this.addCommands(commands);
    }

    public JavaProcess start() throws IOException {
        List<String> full = this.getFullCommands();
        return new JavaProcess(full, (new ProcessBuilder(full)).directory(this.directory).redirectErrorStream(true).start());
    }

    List<String> getFullCommands() {
        List<String> result = new ArrayList<String>(this.commands);
        result.add(0, this.getJavaPath());
        return result;
    }

    public void addCommands(String... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public JavaProcessLauncher directory(File directory) {
        this.directory = directory;
        return this;
    }

    String getJavaPath() {
        return this.jvmPath;
    }

    public String toString() {
        return "JavaProcessLauncher[commands=" + this.commands + ", java=" + this.jvmPath + "]";
    }
}
