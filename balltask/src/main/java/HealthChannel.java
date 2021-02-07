
import java.io.IOException;
import java.net.Socket;

public class HealthChannel implements Runnable {

    private Channel channel;
    private boolean health;
    private Socket socket;

    public HealthChannel(Channel channel, Socket socket) {
        this.channel = channel;
        this.socket = socket;
        Thread healthThread = new Thread(this);
        healthThread.start();
    }

    public boolean isHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }

    @Override
    public void run() {
        while (this.channel.isOk()) {
            int i = 0;
            this.health = false;
            this.channel.sendACK("channel ok?");
            while (i < 5 && !this.health) {
                i++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.health) {
                this.channel.setOk(false);
                System.out.println("Channel cerrado");
                try {
                    this.channel.getSocket().close();
                    //this.channel.setSocket(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
