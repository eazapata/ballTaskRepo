import java.awt.*;
import java.io.Serializable;


public class Ball implements Runnable, VisualObject, Serializable {

    private int velX;
    private int velY;
    private int cordX;
    private int cordY;
    private Thread ballThread;
    private boolean outSide;
    private boolean stopped;
    private BallTask ballTask;
    private Color color;
    private int sleepTime;
    private Rectangle rect;
    private int size;
    private boolean running;
    private Channel channel;

    /**
     * Constructor sin parametros para crear una pelota en el channel con la información recibida
     */
    public Ball() {
    }

    /**
     * Constructor con parametros que crea una pelota. Recibe un channel para saber como enviarse y un balltask para
     * acceder a los métodos de comprobar posición.
     * @param ballTask clase que crea el objeto ball y que contiene el método de comprobar posición.
     * @param channel clase que tiene el método para enviar la pelota.
     */
    public Ball(BallTask ballTask, Channel channel) {
        this.ballTask = ballTask;
        this.size = this.ballTask.generateRandomInt(30, 150);
        this.outSide = true;
        this.color = new Color(this.ballTask.generateRandomInt(10, 255),
                this.ballTask.generateRandomInt(10, 255),
                this.ballTask.generateRandomInt(10, 255));
        this.cordY = this.ballTask.generateRandomInt(0, this.ballTask.getHeight() - (this.size * 3));
        this.cordX = this.ballTask.generateRandomInt(0, this.ballTask.getWidth() - (this.size * 3));
        this.velY = 1;
        this.velX = 1;
        this.sleepTime = 10;
        this.stopped = false;
        this.channel = channel;
        this.rect = new Rectangle(this.size, this.size);
        ballThread = new Thread(this);
        ballThread.start();
    }
 // GETTERS Y SETTERS
    public Thread getBallThread() {
        return ballThread;
    }

    public void setBallThread(Thread ballThread) {
        this.ballThread = ballThread;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setBallTask(BallTask ballTask) {
        this.ballTask = ballTask;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getCordX() {
        return cordX;
    }

    public void setCordX(int cordX) {
        this.cordX = cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public void setCordY(int cordY) {
        this.cordY = cordY;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isOutSide() {
        return outSide;
    }

    public void setOutSide(boolean outSide) {
        this.outSide = outSide;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    public boolean isStopped() {
        return stopped;
    }

    /**
     * Método para mover la bola, recibe la acción que le dice que hacer a la pelota, está accion la recibe del balltask
     */
    public void moveBall(String action) {
        int absX = Math.abs(this.getVelX());
        int absY = Math.abs(this.getVelY());

        if (!this.stopped) {
            if (action.equals("right") && (this.channel.isOk())){// && this.ballTask.getWindow().equals("Server")) {
                this.running = false;
                System.out.println("enviando pelota " + this.channel.isOk());
                this.channel.send(this);
            } /*else if (action.equals("left") && (this.channel.isOk()) && this.ballTask.getWindow().equals("client")) {
                this.running = false;
                System.out.println("enviando pelota " + this.channel.isOk());
                this.channel.send(this);
            }*/ else if (action.equals("left")) {
                this.setVelX(absX);
            } else if (action.equals("right")) {
                this.setVelX(-absX);
            } else if (action.equals("up")) {
                this.setVelY(absY);
            } else if (action.equals("down")) {
                this.setVelY(-absY);
            }
            this.rect.setBounds(this.cordX, this.cordY, this.size, this.size);
            cordX = cordX + velX;
            cordY = cordY + velY;
        }
    }

    /**
     * Método para pintar la bola. Recibe el graphics del viewer
     */
    public void paint(Graphics g) {
        if (running) {

            g.setColor(this.color);
            g.fillOval(this.cordX, this.cordY, this.size, this.size);
        }
    }

    /**
     * Run de la pelota que que comprueba si puede moverse y en que dirección.
     */
    public void run() {
        this.running = true;
        while (running) {
            try {
                Thread.sleep(this.sleepTime);
                ballTask.checkMove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}