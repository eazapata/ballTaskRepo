package Communications;

import Default.BallTask;

import java.awt.*;
import java.io.*;
import java.net.Socket;

import Graphics.Ball;
import Default.BallTask;

public class Channel implements Runnable {

    private Socket socket;
    private boolean ok;
    private Thread channelThread;
    private BallTask ballTask;
    private HealthChannel healthChannel;

    /**
     * Constructor que recibe un balltask para poder pasarselo a las pelotas que se crean en esta clase y
     * para poder añadir y eliminar pelotas del arrayList
     *
     * @param ballTask balltask que tiene los métodos de añadir y retirar pelotas, además de los de comprobar la
     *                 posición de la pelota
     */
    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
    }

    // GETTERS Y SETTERS
    // ----------------------------------------------------------
    public synchronized boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Thread getChannelThread() {
        return channelThread;
    }

    public void setChannelThread(Thread channelThread) {
        this.channelThread = channelThread;
    }

    public boolean isStatus() {
        return ok;
    }

    public void setStatus(boolean status) {
        this.ok = status;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Método para asignar un socket al channel, si esté ya está setteado no le asigna ningún valor nuevo.
     *
     * @param socket
     */
    public synchronized void setSocket(Socket socket) {
        if (!this.ok) {
            this.ok = true;
            this.socket = socket;
            this.channelThread = new Thread(this);
            this.channelThread.start();
            this.healthChannel = new HealthChannel(this);
        }
    }

    /**
     * Método para crear una pelota nueva a partir de la info de un string.
     *
     * @param ballInfo información relevante de una pelota.
     * @return la nueva pelota.
     */
    private Ball createBall(String ballInfo) {
        String[] info = ballInfo.split(",");
        Ball ball = new Ball();
        ball.setBallTask(this.ballTask);
        ball.setSize(Integer.parseInt(info[1]));
        ball.setOutSide(true);
        ball.setCordY(Integer.parseInt(info[4]));
        ball.setCordX(0);
        ball.setVelX(Integer.parseInt(info[2]));
        ball.setVelY(Integer.parseInt(info[3]));
        ball.setSleepTime(20);
        ball.setChannel(this);
        ball.setColor(new Color(Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8])));
        ball.setRect(new Rectangle(ball.getSize(), ball.getSize()));
        ball.getRect().setBounds(ball.getCordX(), ball.getCordY(), ball.getSize(), ball.getSize());
        ball.setBallThread(new Thread(ball));
        ball.getBallThread().start();
        return ball;
    }

    /**
     * Método que envia una comprobación de estado para el healthChannel
     *
     * @param message mensaje que se enviará.
     */
    public void sendACK(String message) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(this.socket.getOutputStream());
            out.writeUTF(message);

        } catch (IOException e) {
            this.ok = false;
            e.printStackTrace();
        }
    }

    /**
     * Método para enviar la información relevante de una pelota a través del canal. Envia un string separado por comas
     * con los valores que pueden variar sobre la pelota.
     *
     * @param ball objeto que queremos enviar a través del channel.
     */
    public void send(Ball ball) {
        try {
            DataOutputStream writer = new DataOutputStream((this.socket.getOutputStream()));
            String ballInfo = "BALL" + "," +
                    ball.getSize() + "," +
                    ball.getVelX() + "," +
                    ball.getVelY() + "," +
                    ball.getCordY() + "," +
                    ball.getCordX() + "," +
                    ball.getColor().getRed() + "," +
                    ball.getColor().getGreen() + "," +
                    ball.getColor().getBlue();
            writer.writeUTF(ballInfo);
            this.ballTask.removeBall(ball);
        } catch (IOException e) {
            System.out.println("Connection reset");
            this.ok = false;
        }
    }

    /**
     * Método que usamos para recibir la información de la pelota, crear una nueva y añadirla a la lista del balltask.
     */
    public void receiveInfo() {
        DataInputStream reader = null;
        try {
            reader = new DataInputStream(this.socket.getInputStream());
            String received = null;
            received = reader.readUTF();

            if (received.equals("channel ok?")) {
                DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                out.writeUTF("ok");
            } else if (received.equals("ok")) {
                this.healthChannel.setHealth(true);
            } else if (received == null) {
                System.out.println("Content is null");
                this.ok = false;
            } else if (received.split(",")[0].equals("BALL")) {
                Ball ball = createBall(received);
                this.ballTask.addNewBall(ball);
            }

        } catch (IOException e) {
            System.out.println("Connection reset");
            this.ok = false;
        }
    }

    public void run() {
        while (this.ok) {
            try {
                receiveInfo();
            } catch (Exception e) {
                System.out.println("Recibiendo info" + e);
            }
        }
    }
}




