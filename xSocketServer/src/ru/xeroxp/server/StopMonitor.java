package ru.xeroxp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class StopMonitor extends Thread {

    private ServerSocket serverSocket;

    public StopMonitor() {
        setDaemon(true);
        setName("StopMonitor");
        try {
            serverSocket = new ServerSocket(Settings.PORT_STOP, 1, InetAddress.getByName(Settings.STOP_IP));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("stop monitor thread listening on: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
        Socket socket;
        try {
            socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader.readLine();
            System.out.println("stop signal received, stopping server");
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}