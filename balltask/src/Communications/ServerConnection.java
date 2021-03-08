package Communications;

import balltask.BallTask;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerConnection implements Runnable {

    private int port = 9999;
    private Thread serverThread;
    private Channel channel;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean running = true;
    private BallTask balltask;

    /**
     * Constructor con parametros
     * @param channel canal a settear para iniciar la comunicación con el otro equipo
     */
    public ServerConnection(Channel channel, BallTask balltask) {

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.balltask = balltask;
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
                    this.balltask.setClientIp(clientAddress);
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