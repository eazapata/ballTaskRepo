import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {


    private String ip = "192.168.0.11";
    private int port = 9999;
    private Channel channel;
    private boolean running = true;

    //CONSTRUCTOR

    public ClientConnection(Channel channel) {
        this.channel = channel;
        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    /**
     * Método encargado de iniciar la conexión con el server
     */
    private void startConnection() {
        Socket socket = null;
        try {
         if (!this.channel.isOk()) {
                socket = new Socket(ip, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String greeting = "BALLTASK";
                out.writeUTF(greeting);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String response = in.readUTF();
                if (!this.channel.isOk() && response.equals("OK")) {
                    this.channel.setSocket(socket);
                    System.out.println("Conexion establecida");
                }
          }
        } catch (Exception e) {
            System.out.println("Setting socket");
            //log.error("failed to connect to server", e);
        }
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                startConnection();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
