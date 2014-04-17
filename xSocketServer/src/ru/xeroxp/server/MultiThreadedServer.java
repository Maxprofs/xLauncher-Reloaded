package ru.xeroxp.server;

import ru.xeroxp.server.config.Settings;
import ru.xeroxp.server.utils.Debug;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

class MultiThreadedServer implements Runnable {

    private int serverPort = Settings.PORT_WORK;
    private SSLServerSocket serverSocket = null;
    private boolean isStopped = false;

    public MultiThreadedServer() {
        this.serverPort = Settings.PORT_WORK;
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            SSLSocket clientSocket;
            try {
                clientSocket = (SSLSocket) this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    Debug.infoMessage("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

            new Thread(
                    new Worker(clientSocket)
            ).start();
        }

        Debug.infoMessage("Server Stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        Debug.infoMessage("Opening server socket...");

        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            this.serverSocket = (SSLServerSocket) ssf.createServerSocket(this.serverPort);
            String[] suites = this.serverSocket.getSupportedCipherSuites();
            this.serverSocket.setEnabledCipherSuites(suites);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

}