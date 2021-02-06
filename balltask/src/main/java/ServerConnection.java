

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable {

    private int port = 9999;
    private BallTask ballTask;
    private Thread serverThread;
    private Channel channel;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean running = true;

    public ServerConnection(Channel channel) {

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
        this.serverThread = new Thread(this);
        this.serverThread.start();
    }

    /**
     * Método encargado de establecer conexión con el cliente
     */
    private void startConnection() {
        try {
          if (!this.channel.isOk()) {
                this.socket = serverSocket.accept();
                    String clientAddress = this.socket.getInetAddress().getHostAddress();
                    System.out.println("New connection from: " + clientAddress);
                    ClientIdentified clientIdentified = new ClientIdentified(this.socket, this.channel);
                    clientIdentified.getIdentifiedThread().start();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (this.running) {
            try {
                startConnection();
            } catch (Exception e) {
                System.out.println("Error initial connection" + e);
            }
        }


    }
}

