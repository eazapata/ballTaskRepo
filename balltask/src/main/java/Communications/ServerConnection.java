package Communications;

import Communications.Channel;
import Communications.ClientIdentified;
import Default.BallTask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerConnection implements Runnable {

    private int port = 9999;
    private BallTask ballTask;
    private Thread serverThread;
    private Channel channel;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean running = true;

    /**
     * Constructor con paramtros
     * @param channel canal a settear para inicar la comunicación con el otro equipo
     * @param ballTask balltask que settara al iniciar la comunicacion.
     */
    public ServerConnection(Channel channel, BallTask ballTask) {

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ballTask = ballTask;
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
                    ClientIdentified clientIdentified = new ClientIdentified(this.socket, this.channel,this.ballTask);
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

