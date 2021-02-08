package Default;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import Graphics.Ball;
import Graphics.BlackHole;
import Communications.*;
import Default.*;


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
    private String window;

    //GETTERS Y SETTERS
    //------------------------------------------------------------------------


    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<BlackHole> getBlackHoles() {
        return blackHoles;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    /**
     * Consctructor con parámetros.
     */
    public BallTask(String ip) {
        this.setTitle("Original");
        this.channel = new Channel(this);
        this.serverConnection = new ServerConnection(this.channel, this);
        this.clientConnection = new ClientConnection(this.channel, ip, this);

        this.dimension = getToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.statistics = new Statistics();
        this.viewer = new Viewer(dimension.width, dimension.height, this);
        createBlackHoles();
        createBalls();
        this.viewer.setBalls(this.balls);

        this.addControlPanel(c);
        this.addViewer(c);
        this.pack();
    }

    public static void main(String[] args) {

        String remoteIp = null;
        while (remoteIp == null) {
            remoteIp = JOptionPane.showInputDialog("Introduce remote ip: ");
        }
        BallTask ballTask = new BallTask(remoteIp);
    }

    //MÉTODOS PÚBLICOS

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
     * Método que recibe una pelota y la añade a la lista, tras esto actualiza las estadisticas.
     *
     * @param ball objeto pelota que añadiremos a la lista
     */
    public void addNewBall(Ball ball) {
        this.statistics.setBall();
        this.balls.add(ball);

    }

    /**
     * Métdo que recibe dos enteros y cácula un valor aleatorio entre estos
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
     * @param ball
     */
    public void removeBall(Ball ball) {
        this.statistics.removeBall();
        this.balls.remove(ball);

    }

    //MÉTODOS PRIVADOS

    private void addControlPanel(GridBagConstraints c) {
        this.controlPanel = new ControlPanel(this, statistics);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0.5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        this.add(controlPanel, c);
    }

    private void addViewer(GridBagConstraints c) {

        this.viewer.loadBackground();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1.9;
        this.add(this.viewer, c);
        Thread viewerThread = new Thread(this.viewer);
        viewerThread.start();

    }

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

    public void createBalls() {
        balls = new ArrayList<Ball>();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(this, this.channel);
            balls.add(ball);
            this.statistics.setBall();
        }
        this.viewer.setBalls(balls);
    }

    public void createBlackHoles() {
        this.blackHoles = new ArrayList<BlackHole>();
        BlackHole blackHole = new BlackHole(this.getHeight() / 4 - 175, this.getWidth() / 4,
                400, 175, this.statistics);
        BlackHole blackHole1 = new BlackHole(this.getHeight() / 2, this.getWidth() / 2 - 200,
                400, 175, this.statistics);
        blackHoles.add(blackHole);
        blackHoles.add(blackHole1);
        this.viewer.setBlackHoles(this.blackHoles);
    }

}








