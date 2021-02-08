import java.awt.*;
import java.util.Random;

public class BlackHole implements VisualObject {

    private int width;
    private int height;
    private int cordY;
    private int cordX;
    private Rectangle rect;
    private Color color;
    private Ball ball;
    private Statistics statistics;

    public BlackHole(BallTask ballTask, int cordY, int cordX, int width, int height, Statistics statistics) {
        this.width = width;
        this.height = height;
        this.cordY = cordY;
        this.cordX = cordX;
        this.rect = new Rectangle(width, height);
        this.rect.setBounds(this.cordX, this.cordY, width, height);
        this.color = Color.white;
        this.statistics = statistics;
    }

    public Rectangle getRect() {
        return rect;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.cordX, this.cordY, width, height);

    }


    //Metodo para añadir una pelota
    public synchronized void putBall(Ball ball) {
        this.statistics.setWaitingBalls();
        while (this.ball != null) {
            try {
                wait();
                ball.setStopped(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.ball = ball;
        this.statistics.setInsideBH();
        this.statistics.removeWaitingBalls();
        ball.setSleepTime(50);
        ball.setOutSide(false);
        ball.setStopped(false);
        notifyAll();
    }

    //Metodo para retirar la pelota
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