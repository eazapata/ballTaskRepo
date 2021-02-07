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

    public BlackHole(BallTask ballTask,int cordY,int cordX,int width,int height) {
        this.width =width;
        this.height = height;
        this.cordY = cordY;
        this.cordX = cordX;
        this.rect = new Rectangle(width, height);
        this.rect.setBounds(this.cordX, this.cordY, width, height);
        this.color = Color.white;
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getCordY() {
        return cordY;
    }

    public void setCordY(int cordY) {
        this.cordY = cordY;
    }

    public int getCordX() {
        return cordX;
    }

    public void setCordX(int cordX) {
        this.cordX = cordX;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
        ball.setSleepTime(50);
        ball.setOutSide(false);
        notifyAll();
    }

    //Metodo para retirar la pelota
    public synchronized void removeBall(Ball ball) {
        if (this.ball != null) {
            if (ball.equals(this.ball)) {
                this.ball = null;
                ball.setOutSide(true);
                ball.setSleepTime(10);
                notifyAll();
            }
        }
    }
}