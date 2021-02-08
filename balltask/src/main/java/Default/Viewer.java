package Default;

import Default.BallTask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import Graphics.*;

public class Viewer extends Canvas implements Runnable {

    private BufferedImage background;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private BallTask ballTask;

    public Viewer(int width, int height, BallTask ballTask) {
        this.ballTask = ballTask;
        this.setSize(width, height);
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    public void setBlackHoles(ArrayList<BlackHole> blackHoles) {
        this.blackHoles = blackHoles;
    }

    /**
     * Carga un fondo de pantalla para el viewer
     */
    public void loadBackground() {
        try {
            File file = new File("src/main/resources/fondo.png");
            this.background = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint() {
        BufferStrategy bs;
        bs = this.getBufferStrategy();

        Graphics g = bs.getDrawGraphics();
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
        if (this.blackHoles != null) {
            for (BlackHole blackHole : this.blackHoles) {
                blackHole.paint(g);
            }
        }
        if (this.balls != null) {
            for (int i = 0; i <this.balls.size() ; i++) {
                this.balls.get(i).paint(g);
            }
          /*  for (Graphics.Ball ball : this.balls) {
                ball.paint(g);
            }
            this.balls.removeAll(this.ballTask.getToRemove());
            this.balls.addAll(this.ballTask.getToAdd());
            this.ballTask.getToRemove().clear();
            this.ballTask.getToAdd().clear();*/
        }

        bs.show();
        g.dispose();

    }

    public void run() {
        this.createBufferStrategy(2);
        do {
            paint();
        } while (true);
    }
}
