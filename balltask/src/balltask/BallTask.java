/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balltask;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import Graphics.Ball;
import Graphics.BlackHole;
import Communications.*;
import java.util.HashSet;

public class BallTask extends JFrame {
    private Viewer viewer;
    private ControlPanel controlPanel;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private Dimension dimension;
    private Statistics statistics;
    private Channel channel;
    private ServerConnection serverConnection;
    private ClientConnection clientConnection;
    private String direction;


    /**
     * Consctructor con parámetros.
     */
    public BallTask(String ip,String direction) {
        this.direction = direction;
        this.setTitle("Ball task");
        this.channel = new Channel(this);
        this.serverConnection = new ServerConnection(this.channel,this);
        this.clientConnection = new ClientConnection(this.channel, ip, this);

        this.dimension = getToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.statistics = new Statistics();
        this.viewer = new Viewer(dimension.width, dimension.height);
        createBlackHoles();
        createBalls();
        this.viewer.setBalls(this.balls);

        this.addControlPanel(c);
        this.addViewer(c);
        this.pack();
    }

    //GETTERS Y SETTERS
    //------------------------------------------------------------------------
    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<BlackHole> getBlackHoles() {
        return blackHoles;
    }

    public String getDirection() {
        return direction;
    }

    public Viewer getViewer() {
        return viewer;
    }
    
    

    public static void main(String[] args) {
        String direction = "derecha";
        String remoteIp = null;
        while (remoteIp == null) {
            remoteIp = JOptionPane.showInputDialog("Introduce remote ip: ");
             Object[] options = {"Izquierda","Derecha"};
            int n = JOptionPane.showOptionDialog(null,//parent container of JOptionPane
            "Indica el lado por el que quieres enviar y recibir pelotas",
            "Indica dirección.",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,//do not use a custom Icon
            options,//the titles of buttons
            options[1]);//default button title
            if(n == 0){
                 direction = "izquierda";
            }else if(n == 1){
                direction = "derecha";
            }
        }
         BallTask ballTask = new BallTask(remoteIp,direction);
         
    }

    //MÉTODOS PÚBLICOS

    /**
     * Método que recibe una pelota y la añade a la lista, tras esto actualiza las estadisticas.
     *
     * @param ball objeto pelota que añadiremos a la lista
     */
    public void addNewBall(Ball ball) {
        this.statistics.setBall();
        this.balls.add(ball);

    }

    /**
     * Método que crea las pelotas y las añade a la lista.
     */
    public void createBalls() {
        balls = new ArrayList<Ball>();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(this, this.channel);
            balls.add(ball);
            this.statistics.setBall();
        }
        this.viewer.setBalls(balls);
    }

    /**
     * Método que creas los blackholes y los añade a la lista.
     */
    public void createBlackHoles() {
        int blackHoleHeight = this.getHeight() / 5;
        int blackHoleWidth = this.getWidth() / 5;
        this.blackHoles = new ArrayList<BlackHole>();

        //Estas operaciones son para que los blackholes aparezcan en zonas especificas de la pantalla sin importar el
        //tamaño de la ventana

        BlackHole blackHole = new BlackHole(this.getHeight() / 4,
                this.getWidth() / 4 - (blackHoleWidth / 2),
                blackHoleWidth, blackHoleHeight, this.statistics);
        BlackHole blackHole1 = new BlackHole(this.getHeight() / 2,
                this.getWidth() / 2,
                blackHoleWidth, blackHoleHeight, this.statistics);
        blackHoles.add(blackHole);
        blackHoles.add(blackHole1);
        this.viewer.setBlackHoles(this.blackHoles);
    }

    /**
     * Método que comprueba la posición de la pelota y le indica que tiene que hacer, en caso de no indicarle nada
     * seguirá con el mismo recorrido.
     *
     * @param ball pelota que queremos comprobar.
     */

    public void checkMove(Ball ball) {

        if (ball.getCordX() - ball.getVelX() <= 0) {

            ball.moveBall("left");

        } else if (ball.getCordX() + ball.getVelX() >= this.viewer.getWidth() - ball.getSize()) {

            ball.moveBall("right");

        } else if (ball.getCordY() - ball.getVelY() <= 0) {

            ball.moveBall("up");

        } else if (ball.getCordY() + ball.getVelY() >= this.viewer.getHeight() - ball.getSize()) {
            ball.moveBall("down");
        } else {
            ball.moveBall("");
        }
        checkBlackHole(ball);


    }

    /**
     * Métdo que recibe dos enteros y calcula un valor aleatorio entre estos
     *
     * @param min valor mínimo asignable
     * @param max valor máximo asignable
     * @return el valor aleatorio calculado
     */
    public int generateRandomInt(int min, int max) {

        int randomValue = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return randomValue;
    }

    /**
     * Método que usamos para eliminar una pelota de la lista y actualizar las estadisdisticas
     *
     * @param ball pelota que queremos eliminar de la lista
     */
    public void removeBall(Ball ball) {
        this.statistics.removeBall();
        this.balls.remove(ball);

    }
    
    public void setClientIp(String ip){
        this.clientConnection.setIp(ip);
        
    }

    //MÉTODOS PRIVADOS

    /**
     * Añade el controlPannel al frame de balltask
     *
     * @param c contraints de balltask para situarlo en la pantalla.
     */
    private void addControlPanel(GridBagConstraints c) {
        this.controlPanel = new ControlPanel(this, statistics);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.weighty = 1.0;
        this.add(controlPanel, c);
    }

    /**
     * Añade el viewer al frame de balltask
     *
     * @param c contraints de balltask para situarlo en la pantalla.
     */
    private void addViewer(GridBagConstraints c) {

        this.viewer.loadBackground();
        c.gridx = 1;
        c.weightx = 0.9;
        this.add(this.viewer, c);
        Thread viewerThread = new Thread(this.viewer);
        viewerThread.start();

    }

    /**
     * Comprueba si hay alguna pelota en el blackhole para indicar que acción tiene que realizar.
     *
     * @param ball pelota que queremos comprobar
     */
    private void checkBlackHole(Ball ball) {

        for (BlackHole blackHole : this.blackHoles) {
            if (blackHole.getRect().intersects(ball.getRect()) && ball.isOutSide()) {
                blackHole.putBall(ball);
            }
            if (!blackHole.getRect().intersects(ball.getRect()) && !ball.isOutSide()) {
                blackHole.removeBall(ball);
            }
        }
    }
}