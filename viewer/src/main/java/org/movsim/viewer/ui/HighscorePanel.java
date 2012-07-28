package org.movsim.viewer.ui;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import org.movsim.input.ProjectMetaData;
import org.movsim.simulator.SimulationRun;
import org.movsim.simulator.SimulationRunnable;
import org.movsim.simulator.Simulator;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.utilities.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighscorePanel implements SimulationRun.CompletionCallback, SimulationRunnable.UpdateStatusCallback {

    final static Logger logger = LoggerFactory.getLogger(HighscorePanel.class);
    private static final long serialVersionUID = 1L;
    private Simulator simulator;

    public HighscorePanel(ResourceBundle resourceBundle, Simulator simulator) {
        this.simulator = simulator;

        simulator.getSimulationRunnable().setCompletionCallback(this);
        simulator.getSimulationRunnable().addUpdateStatusCallback(this);
    }

    /**
     * @param simulationTime
     * @param totalVehicleFuelUsedLiters
     */
    private void highscoreForGames(final double simulationTime, final double totalVehicleFuelUsedLiters) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                String highscoreFilename = ProjectMetaData.getInstance().getProjectName() + "_highscore.txt";
                Vector<String> highscores = getHighscores(highscoreFilename);
                int rank = 1;
                String username = null;
                if (highscores.size() > 0) {
                    String scoreString = highscores.elementAt(rank - 1);
                    double score;
                    while ((score = Double.parseDouble(scoreString.substring(0, scoreString.indexOf(";")))) <= simulationTime) {
                        if (++rank > highscores.size())
                            break;
                        scoreString = highscores.elementAt(rank - 1);
                    }
                }
                if (rank <= 10) {
                    final String[] rankMarker = { "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th" };
                    // TODO limit input to reasonable number of characters

                    username = JOptionPane.showInputDialog(null, "Please enter your name:", rank + rankMarker[rank - 1]
                            + " place - Congratulations!", JOptionPane.INFORMATION_MESSAGE);

                }
                highscores.insertElementAt((int) simulationTime + ";" + totalVehicleFuelUsedLiters + ";" + username,
                        rank - 1);

                PrintWriter hswriter = FileUtils.getWriter(highscoreFilename);
                for (int i = 0; i < highscores.size();) {
                    hswriter.println(highscores.elementAt(i++));
                }
                hswriter.flush();
                hswriter.close();
                
                displayHighscores(highscoreFilename);
            }
        });
    }

    /**
     * Reads and validates the high score table
     * 
     * @return the high score table
     */
    private Vector<String> getHighscores(String filename) {
        Vector<String> highscores = new Vector<String>();
        try {
            BufferedReader hsreader = FileUtils.getReader(filename);
            String entry;
            int bettertime = 0;
            while ((entry = hsreader.readLine()) != null) {
                String[] entries = entry.split(";", 3);
                int worsetime = Integer.parseInt(entries[0]);
                double fuel = Double.parseDouble(entries[1]);
                if ((worsetime < bettertime) || (fuel < 0)) {
                    logger.error("high score file {} contains corrupt data", filename);
                    throw new Exception();
                }
                highscores.addElement(entry);
                bettertime = worsetime;
            }
        } catch (final Exception e) {
            logger.error("error reading file {} - starting new high score", filename);
            return new Vector<String>();
        }
        return highscores;
    }

    /**
     * Displays the high score table
     */
    public void displayHighscores(String filename) {
        Vector<String> highscores = getHighscores(filename);
        String[] columnNames = { "Rank", "Name", "Time (seconds)", "Fuel (liters)" };
        String[][] rowData = new String[10][4];
        for (int i = 0; (i < highscores.size()) && (i < 10); i++) {
            String[] entries = highscores.elementAt(i).split(";", 3);
            rowData[i][0] = Integer.toString(i + 1);
            rowData[i][1] = entries[2];
            rowData[i][2] = String.format("%d", Integer.parseInt(entries[0]));
            rowData[i][3] = String.format("%.2f", Double.parseDouble(entries[1]));
        }
        JTable highscoreTable = new JTable(rowData, columnNames);
        highscoreTable.setEnabled(false);
        
        // TODO hold Frame or add HighscorePanel to appFrame or make this a JFrame
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.add(new JScrollPane(highscoreTable));
        f.pack();
        // f.setResizable(false);
        f.setVisible(true);
        
//        this.removeAll();
//        final JScrollPane comp = new JScrollPane(highscoreTable);
//        this.add(comp, BorderLayout.NORTH);
//        SwingHelper.setComponentSize(this, 400, 240);
//        setVisible(true);
//        appFrame.resize(1200, 800);
    }

    @Override
    public void updateStatus(double simulationTime) {
        if (simulator.isFinished()) {
            // hack to simulationComplete
            simulator.getSimulationRunnable().setDuration(simulationTime);
        }
    }
    
    @Override
    public void simulationComplete(double simulationTime) {
        RoadNetwork roadNetwork = simulator.getRoadNetwork();
        final double totalVehicleFuelUsedLiters = roadNetwork.totalVehicleFuelUsedLiters();
        highscoreForGames(simulationTime, totalVehicleFuelUsedLiters);
    }

}
