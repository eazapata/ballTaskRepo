package Communications;

import Communications.Channel;
import Default.BallTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientIdentified implements Runnable {

    private Socket socket;
    private Channel channel;
    private Thread identifiedThread;
    private boolean clientIdentified;
    private BallTask ballTask;

    public Thread getIdentifiedThread() {
        return identifiedThread;
    }

    /**
     * Contructor que recibe el socket y el channel a settear y un balltask para indicar quien empieza
     * la conexión.
     * @param socket socket de la conexión.
     * @param channel channel por el que se comunicarán cliente y servidor.
     * @param ballTask balltask que recibirá un string para saber que equipo inicia la conexión
     */
    public ClientIdentified(Socket socket, Channel channel,BallTask ballTask) {
        this.socket = socket;
        this.channel = channel;
        this.ballTask = ballTask;
        identifiedThread = new Thread(this);
    }


    @Override
    public void run() {
        this.clientIdentified = false;
        while (!this.clientIdentified) {
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());
                String header = input.readUTF();
                System.out.println(header);

                if (header.equals("BALLTASK")) {
                    System.out.println("Server: Setting ");
                    this.channel.setSocket(this.socket,"Server");

                    DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                    out.writeUTF("OK");
                    this.clientIdentified = true;

                } else {
                    System.out.println("This connection is not a balltask.");
                    this.clientIdentified = true;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
