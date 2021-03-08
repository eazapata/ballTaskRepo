package Communications;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientIdentified implements Runnable {

    private Socket socket;
    private Channel channel;
    private Thread identifiedThread;
    private boolean clientIdentified = false;

    public Thread getIdentifiedThread() {
        return identifiedThread;
    }

    /**
     * Contructor que recibe el socket y el channel a settear y un balltask para indicar quien empieza
     * la conexión.
     *
     * @param socket   socket de la conexión.
     * @param channel  channel por el que se comunicarán cliente y servidor.
     */
    public ClientIdentified(Socket socket, Channel channel) {
        this.socket = socket;
        this.channel = channel;
        this.identifiedThread = new Thread(this);
    }

    public boolean isClientIdentified() {
        return clientIdentified;
    }
    
    

    @Override
    public void run() {
        while (!this.clientIdentified) {
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());
                String header = input.readUTF();
                System.out.println(header);
 //Comprueba si es un balltask y si es así setea el channel
                if (header.equals("BALLTASK")) {
                    System.out.println("Server: Setting ");
                    this.channel.setSocket(this.socket);

                    DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                    out.writeUTF("OK");

                } else {
                    System.out.println("This connection is not a balltask.");
                    

                }
                this.clientIdentified = true;
            } catch (IOException e) {
                e.printStackTrace();
                this.socket = null;
            }
        }
    }
}