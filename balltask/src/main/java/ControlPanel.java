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

    public ControlPanel(BallTask ballTask, Statistics statistics) {
        this.statistics = statistics;
        this.ballTask = ballTask;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.createPane(c);
        this.addTableContent(c);
        this.threadControlPanel = new Thread(this);
        this.threadControlPanel.start();
    }

    public void createPane(GridBagConstraints c) {


        this.play = new JButton("Play");
        this.play.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipadx = 150;
        c.insets = new Insets(0, 10, 0, 0);
        this.add(this.play, c);

        this.pause = new JButton("Pause");
        this.pause.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(this.pause, c);

        this.reset = new JButton("Reset");
        this.reset.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        this.add(this.reset, c);

        addTableContent(c);

    }

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

    private void addTableContent(GridBagConstraints c) {

        this.statisticsTable = new JTable(4, 2);
        this.statisticsTable.setValueAt("Total: ", 0, 0);
        this.statisticsTable.setValueAt("Balls outside blackhole: ", 1, 0);
        this.statisticsTable.setValueAt("Balls inside blackhole: ", 2, 0);
        this.statisticsTable.setValueAt("Balls waiting: ", 3, 0);
        this.statisticsTable.setVisible(true);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 0, 0);
        JTableHeader header = this.statisticsTable.getTableHeader();
        TableColumnModel colMod = header.getColumnModel();
        TableColumn tabCol = colMod.getColumn(0);
        tabCol.setHeaderValue("Descripción");
        TableColumn tabCol1 = colMod.getColumn(1);
        tabCol1.setHeaderValue("Valor");
        this.statisticsTable.getTableHeader().setVisible(true);
        this.add(this.statisticsTable.getTableHeader(), c);
        c.gridy = 4;
        c.gridheight = 4;
        c.insets = new Insets(0, 10, 0, 0);
        this.add(this.statisticsTable, c);
    }

    private void pauseBalls() {
        for (int i = 0; i < this.ballTask.getBalls().size(); i++) {
            this.ballTask.getBalls().get(i).setStopped(true);
        }
    }

    private void playBalls() {
        for (int i = 0; i < this.ballTask.getBalls().size(); i++) {
            this.ballTask.getBalls().get(i).setStopped(false);
        }
    }

    private void refreshStats() {
        this.statisticsTable.setValueAt(statistics.getTotalBalls(), 0, 1);
        this.statisticsTable.setValueAt(statistics.getTotalBalls() - statistics.getInsideBH(), 1, 1);
        this.statisticsTable.setValueAt(statistics.getInsideBH(), 2, 1);
        this.statisticsTable.setValueAt(statistics.getWaitingBalls(), 3, 1);
    }


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
        while (true) {
            refreshStats();
        }
    }
}
