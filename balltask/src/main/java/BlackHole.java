import java.awt.*;
import java.util.Random;

public class BlackHole implements VisualObject {

    private int width;
    private int height;
    private int cordY;
    private int cordX;
    private Random random;
    private BallTask ballTask;
    private Rectangle rect;
    private Color color;
    private Ball ball;

    public BlackHole(BallTask ballTask) {
        this.ballTask = ballTask;
        this.random = new Random();
        this.width = 500;
        this.height = 200;
        this.cordY = this.random.nextInt(this.ballTask.getHeight() - (this.height * 2));
        this.cordX = this.random.nextInt(this.ballTask.getWidth() - (this.width * 2));
        this.rect = new Rectangle(width, height);
        this.rect.setBounds(this.cordX, this.cordY, width, height);
        this.color = Color.white;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.cordX, this.cordY, width, height);

    }



    //Metodo para añadir una pelota
    public synchronized void putBall(Ball ball) {

        while (this.ball != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.ball = ball;
        ball.setBorderColor(new Color(255,0,0));
        ball.setSleepTime(50);
        ball.setOutSide(false);
        notifyAll();
    }

    //Metodo para retirar la pelota
    public synchronized void removeBall(Ball ball) {
        if (this.ball != null) {
            if (ball.equals(this.ball)) {
                this.ball = null;
                ball.setBorderColor(ball.getColor());
                ball.setOutSide(true);
                ball.setSleepTime(1);
                notifyAll();
            }
        }
    }
}