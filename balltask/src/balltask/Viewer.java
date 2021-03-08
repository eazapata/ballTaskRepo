/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balltask;


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

    //CONSTRUCTOR
    public Viewer(int width, int height) {
        this.setSize(width, height);
    }

    // GETTERS Y SETTERS
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
            File file = new File("src/resources/fondo.png");
            this.background = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //METODO PARA PINTAR TANTO EL FONDO DE PANTALLA COMO EL CONTENIDO DE LAS LISTAS
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
