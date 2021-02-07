import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ControlPanel extends JPanel implements Runnable, ActionListener {

    private JTable statisticsTable;
    private Thread threadControlPanel;
    private JToggleButton play, pause, reset;
    private Statistics statistics;
    private ArrayList<Ball> ballList;

    public ControlPanel(Statistics statistics, ArrayList balls) {
        this.statistics = statistics;
        this.ballList = balls;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.createPane(c);
        this.addTableContent(c);
        this.threadControlPanel = new Thread(this);
        this.threadControlPanel.start();
    }

    public void createPane(GridBagConstraints c) {


        this.play = new JToggleButton("Play");
        this.play.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipadx = 150;
        c.insets = new Insets(0, 10, 0, 0);
        this.add(this.play, c);

        this.pause = new JToggleButton("Pause");
        this.pause.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(this.pause, c);

        this.reset = new JToggleButton("Reset");
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
        this.statisticsTable.getTableHeader().setVisible(true);
        this.add(this.statisticsTable.getTableHeader(), c);
        c.gridy = 4;
        c.gridheight = 4;
        c.insets = new Insets(0, 10, 0, 0);
        this.add(this.statisticsTable, c);
    }

    private void pauseBalls() {
        for (int i = 0; i < this.ballList.size(); i++) {
            this.ballList.get(i).setStopped(true);
        }
    }

    private void playBalls() {
        for (int i = 0; i < this.ballList.size(); i++) {
            if (this.ballList.get(i).isOutSide()) {
                this.ballList.get(i).setStopped(false);
            }
        }
    }

    private void refreshStats() {
        this.statisticsTable.setValueAt(statistics.getTotalBalls(), 0, 1);
        this.statisticsTable.setValueAt(statistics.getTotalBalls() - statistics.getInsideBH(), 1, 1);
        this.statisticsTable.setValueAt(statistics.getInsideBH(), 2, 1);
        this.statisticsTable.setValueAt(statistics.getPausedBalls(),3,1);
    }

    public void run() {
        while (true) {
            refreshStats();
        }
    }
}
