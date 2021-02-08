import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {


    private String ip;
    private int port = 9999;
    private Channel channel;
    private boolean running = true;
    private BallTask ballTask;

    /**
     * Constructor que recibe el channel por el cual se comunicará con el otro equipo la ip a la que tiene que
     * conectarse y el balltask para indicar quien inicia la conexión
     * @param channel objeto a través del cual enviará la información
     * @param ip ip del equipo remoto a la que se conectará
     * @param ballTask objeto al que setteara un string para indicar quien incia la conexión.
     */

    public ClientConnection(Channel channel,String ip, BallTask ballTask) {
        this.channel = channel;
        this.ip = ip;
        this.ballTask = ballTask;
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
                    this.ballTask.setWindow("client");
                    System.out.println("Conexion establecida");
                }
          }
        } catch (Exception e) {
            System.out.println("Setting socket");
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
