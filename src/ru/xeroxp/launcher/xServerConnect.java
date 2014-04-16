package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class xServerConnect {

    private static final List<xServerConnect> connectServers = new ArrayList<xServerConnect>();
    private final String serverIp;
    private final int serverPort;

    public xServerConnect(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public static void loadServers() {
        connectServers.clear();
        Collections.addAll(connectServers, xSettings.connectServers);
    }

    public static xServerConnect[] getConnectServers() {
        int size = connectServers.size();
        xServerConnect[] serverList = new xServerConnect[size];
        int i = 0;

        for (Iterator var4 = connectServers.iterator(); var4.hasNext(); ++i) {
            xServerConnect server = (xServerConnect) var4.next();
            serverList[i] = server;
        }

        return serverList;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public int getServerPort() {
        return this.serverPort;
    }
}
