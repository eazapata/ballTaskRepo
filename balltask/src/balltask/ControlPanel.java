/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balltask;


import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControlPanel extends JPanel implements Runnable, ActionListener {

    private JTable statisticsTable;
    private Thread threadControlPanel;
    private JButton play, pause, reset;
    private Statistics statistics;
    private BallTask ballTask;
    private boolean running;

    /**
     * Constructor recibe un balltask para tener acceso a sus listas y poder aplicar las funciones de los botones
     * sobre estas
     * @param ballTask   objeto propietario de las listas a modificar.
     * @param statistics estadisticas con la infomación de lo que pasa en el viewer.
     */
    public ControlPanel(BallTask ballTask, Statistics statistics) {
        this.statistics = statistics;
        this.ballTask = ballTask;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.createPane(c);
        this.setBackground(new Color(98, 197, 255));
        this.threadControlPanel = new Thread(this);
        this.threadControlPanel.start();
    }

    /**
     * Método para formar el control pannel.
     *
     * @param c Constraints para asignar una posición a todo lo que hay en el panel.
     */
    public void createPane(GridBagConstraints c) {
        Font custom = new Font("Serif", Font.PLAIN, 16);
        Font customTitle = new Font("Serif", Font.BOLD, 20);
        JLabel title = new JLabel("BALLTASK");
        title.setFont(customTitle);
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1;
        c.weighty = 1.5;
        this.add(title, c);

        addTableContent(c, custom);

        this.play = new JButton("Play");
        this.play.setFont(customTitle);
        this.play.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1.5;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        this.add(this.play, c);

        this.pause = new JButton("Pause");
        this.pause.setFont(customTitle);
        this.pause.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 7;
        this.add(this.pause, c);

        this.reset = new JButton("Reset");
        this.reset.setFont(customTitle);
        this.reset.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 8;
        this.add(this.reset, c);


    }

    //LISTENER
    public void actionPerformed(ActionEvent actionEvent) {

        String event = actionEvent.getActionCommand();
        if (event.equals("Play")) {
            this.reset.setSelected(false);
            this.pause.setSelected(false);
            playBalls();
        }
        if (event.equals("Reset")) {
            this.play.setSelected(false);
            this.pause.setSelected(false);
            resetBalls();
        }
        if (event.equals("Pause")) {
            this.play.setSelected(false);
            this.reset.setSelected(false);
            pauseBalls();
        }
    }

    /**
     * Método para añadir contenido a la tabla
     *
     * @param c contraints para situarla en el control panel.
     */
    private void addTableContent(GridBagConstraints c, Font custom) {

        this.statisticsTable = new JTable(4, 2);
        this.statisticsTable.setValueAt("Total: ", 0, 0);
        this.statisticsTable.setValueAt("Balls outside blackhole: ", 1, 0);
        this.statisticsTable.setValueAt("Balls inside blackhole: ", 2, 0);
        this.statisticsTable.setValueAt("Balls waiting: ", 3, 0);
        this.statisticsTable.setVisible(true);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        c.weighty = 0;
        JTableHeader header = this.statisticsTable.getTableHeader();
        TableColumnModel colMod = header.getColumnModel();
        TableColumn tabCol = colMod.getColumn(0);
        tabCol.setMinWidth(150);
        tabCol.setHeaderValue("Descripción");
        TableColumn tabCol1 = colMod.getColumn(1);
        tabCol1.setMinWidth(50);
        tabCol1.setHeaderValue("Valor");
        this.statisticsTable.setPreferredSize(new Dimension(150, 200));
        this.statisticsTable.getTableHeader().setVisible(true);
        this.statisticsTable.getTableHeader().setFont(custom);
        this.statisticsTable.setFont(custom);
        this.add(this.statisticsTable.getTableHeader(), c);
        c.gridy = 2;
        c.gridheight = 4;
        c.insets = new Insets(0, 0, 10, 0);
        this.add(this.statisticsTable, c);
    }

    /**
     * Método para pausar las pelotas.
     */
    private void pauseBalls() {
        for (int i = 0; i < this.ballTask.getBalls().size(); i++) {
            this.ballTask.getBalls().get(i).setStopped(true);
        }
    }

    /**
     * Método para activar las pelotas
     */
    private void playBalls() {
        for (int i = 0; i < this.ballTask.getBalls().size(); i++) {
            this.ballTask.getBalls().get(i).setStopped(false);
        }
    }

    /**
     * Método para actualizar las estadisticas.
     */
    private void refreshStats() {
        this.statisticsTable.setValueAt(statistics.getTotalBalls(), 0, 1);
        this.statisticsTable.setValueAt(statistics.getTotalBalls() - statistics.getInsideBH(), 1, 1);
        this.statisticsTable.setValueAt(statistics.getInsideBH(), 2, 1);
        this.statisticsTable.setValueAt(statistics.getWaitingBalls(), 3, 1);
    }

    /**
     * Método para reinicar las pelotas que hay en el viewer.
     */
    private void resetBalls() {
        for (int i = 0; i < this.ballTask.getBalls().size(); i++) {
            this.ballTask.getBalls().get(i).setRunning(false);
        }
        this.ballTask.getBalls().clear();
        this.ballTask.getBlackHoles().clear();
        this.statistics.resetStatistics();
        this.ballTask.createBlackHoles();
        this.ballTask.createBalls();
    }

    public void run() {
        this.running = true;
        while (running) {
            refreshStats();
        }
    }
}

