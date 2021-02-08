import java.awt.*;

public class BlackHole implements VisualObject {

    private int width;
    private int height;
    private int cordY;
    private int cordX;
    private Rectangle rect;
    private Color color;
    private Ball ball;
    private Statistics statistics;

    /**
     * Constructor con parametros del blackhole, recibe las coordenadas en las que se situará, el alto y ancho de este
     * y las estadisticas donde introducirá información cuando reciba una pelota
     * @param cordY coordenada de Y
     * @param cordX coordenada de X
     * @param width ancho del blackhole
     * @param height alto del blackhole
     * @param statistics objeto statistics
     */
    public BlackHole(int cordY, int cordX, int width, int height, Statistics statistics) {
        this.width = width;
        this.height = height;
        this.cordY = cordY;
        this.cordX = cordX;
        this.rect = new Rectangle(width, height);
        this.rect.setBounds(this.cordX, this.cordY, width, height);
        this.color = Color.white;
        this.statistics = statistics;
    }
   //GETTERS Y SETTERS
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Método paint que recibe el graphics del viewer
     * @param g
     */
    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.cordX, this.cordY, width, height);

    }


    /**
     * Método synchronized que añade una pelota al blackhole y las demás las pone en espera
     * @param ball pelota que introducimos en el blackhole.
     */
    public synchronized void putBall(Ball ball) {
        this.statistics.setWaitingBalls();
        while (this.ball != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.ball = ball;
        this.statistics.setInsideBH();
        this.statistics.removeWaitingBalls();
        ball.setSleepTime(50);
        ball.setOutSide(false);
        notifyAll();
    }

    /**
     * Méto que usamos para retirar una pelota del blackhole
     * @param ball pelota que retiramos
     */
    public synchronized void removeBall(Ball ball) {
        if (this.ball != null) {
            if (ball.equals(this.ball)) {
                this.ball = null;
                ball.setOutSide(true);
                ball.setSleepTime(10);
                this.statistics.removeFromBlackHole();
                notifyAll();
            }
        }
    }
}