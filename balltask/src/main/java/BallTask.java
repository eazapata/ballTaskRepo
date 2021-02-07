
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class BallTask extends JFrame {
    private Viewer viewer;
    private ControlPanel controlPanel;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private ArrayList<Ball> toRemove = new ArrayList<Ball>();
    private ArrayList<Ball> toAdd = new ArrayList<Ball>();
    private Dimension dimension;
    private Statistics statistics;
    private Channel channel;
    private ServerConnection serverConnection;
    private ClientConnection clientConnection;

    //GETTERS Y SETTERS
    //------------------------------------------------------------------------

    public ArrayList<Ball> getToRemove() {
        return toRemove;
    }

    public void setToRemove(ArrayList<Ball> toRemove) {
        this.toRemove = toRemove;
    }

    public ArrayList<Ball> getToAdd() {
        return toAdd;
    }

    public void setToAdd(ArrayList<Ball> toAdd) {
        this.toAdd = toAdd;
    }

    /**
     * Consctructor con parámetros.
     */
    public BallTask(String ip) {
        this.setTitle("Original");
        this.channel = new Channel(this);

        this.serverConnection = new ServerConnection(this.channel);
        this.clientConnection = new ClientConnection(this.channel, ip);
        this.dimension = getToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        this.setVisible(true);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.statistics = new Statistics();
        this.viewer = new Viewer(dimension.width, dimension.height, this);
        createBlackHoles();
        this.balls = createBalls();
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

    public void addNewBall(Ball ball) {
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

    public void removeBall(Ball ball) {
        this.balls.remove(ball);

    }

    //MÉTODOS PRIVADOS

    private void addControlPanel(GridBagConstraints c) {
        this.controlPanel = new ControlPanel(statistics, balls);
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

    private ArrayList<Ball> createBalls() {
        ArrayList<Ball> balls = new ArrayList<Ball>();
        for (int i = 0; i < 10; i++) {
            Ball ball = new Ball(this, this.channel);
            balls.add(ball);
            this.statistics.setBall();
        }
        return balls;
    }

    private void createBlackHoles() {
        this.blackHoles = new ArrayList<BlackHole>();
        BlackHole blackHole = new BlackHole(this, this.getHeight() / 4, this.getWidth() / 4,
                400, 175);
        BlackHole blackHole1 = new BlackHole(this, this.getHeight() / 2, this.getWidth() / 2,
                400, 175);
        blackHoles.add(blackHole);
        blackHoles.add(blackHole1);
        this.viewer.setBlackHoles(this.blackHoles);
    }

}



