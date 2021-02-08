package Communications;

import Communications.Channel;

import java.io.IOException;
import java.net.Socket;

public class HealthChannel implements Runnable {

    private Channel channel;
    private boolean health;
    private Socket socket;

    /**
     * Contructor con parametros, recibe un channel para poder interactuar con sus atributos.
     * @param channel objeto el cual se quiere comprobar si está ok para enviar y recibir información
     */
    public HealthChannel(Channel channel) {
        this.channel = channel;
        Thread healthThread = new Thread(this);
        healthThread.start();
    }

    //GETTER
    public void setHealth(boolean health) {
        this.health = health;
    }

    @Override
    public void run() {
        while (this.channel.isOk()) {
            int i = 0;
            this.health = false;
            this.channel.sendACK("channel ok?");
            while (i < 10 && !this.health) {
                i++;
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.health) {
                this.channel.setOk(false);
                System.out.println("Communications.Channel closed");
                try {
                    this.channel.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
