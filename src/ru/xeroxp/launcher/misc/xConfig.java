package ru.xeroxp.launcher.misc;

import ru.xeroxp.launcher.utils.xFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class xConfig {
    public static final String LAUNCHER = "launcher";
    public static final String VERSIONS = "versions";

    private final File configFile;
    private final Properties props = new Properties();

    public xConfig(String fileName) throws IOException {
        this.configFile = new File(xFileUtils.getRootDirectory(), fileName + ".properties");

        if (this.configFile.createNewFile()) {
            this.props.load(new FileInputStream(this.configFile));
            this.setDefautls(fileName);
        } else {
            this.props.load(new FileInputStream(this.configFile));
        }
    }

    public String get(String key) {
        return this.props.getProperty(key);
    }

    public void set(String key, String value) throws IOException {
        this.props.setProperty(key, value);
        this.props.store(new FileOutputStream(configFile), null);
    }

    private void setDefautls(String fileName) throws IOException {
        if (fileName.equals(LAUNCHER)) {
            this.set("memory", "512");
        }
    }
}
