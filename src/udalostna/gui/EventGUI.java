package udalostna.gui;

import charts.Histogram;
import charts.LineChart;
import simCores.EventCore;
import udalostna.gui.ISimDelegate;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class EventGUI extends JFrame implements ISimDelegate {

    private int simulationCount = 0;
    private int iterationCount = 0;
    private int sleepTime = 1000 / 100;
    private boolean showMedzivysledky = false;
    private double result;
    private long hodnota = 0;
    private LineChart lineChart;
    private String strategiaStr;

    private int realIterationCount = 1;
    private int[] histogramHodnoty;
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    private final JTextArea jTextArea;
    private final JTextArea jTextAreaResults;
    private final JLabel resultLabel;
    private final JLabel replicationLabel;
    private final JButton start;
    private final JButton pause;
    private final JButton stop;

    private JTable[] tables = new JTable[5];

    private final Calendar calendar = Calendar.getInstance();

    private Thread t1;

    SalonSimulation salonSimulation;

    public EventGUI() {
        super("Salón krásy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        final int width = (int) (bounds.width * 0.8);
        final int height = (int) (bounds.height * 0.8);
        final int buttonWidth = 72;
        final int buttonHeight = 24;
        final int space = 5;

        this.setSize(width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        start = new JButton("Start");
        stop = new JButton("Stop");
        pause = new JButton("Pause");
        JTextField pocetOpakovani = new JTextField();
        JTextField[] zamestnanciField = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            zamestnanciField[i] = new JTextField();
        }

        int[] spinnerValues = {1000 * 1000, 500 * 1000, 250 * 1000, 100 * 1000, 50 * 1000, 25 * 1000, 10 * 1000, 5 * 1000, 2 * 1000, 1000, 1000 / 2, 1000 / 5, 1000 / 10, 1000 / 25, 1000 / 50, 1000 / 100, 1000 / 250, 1000 / 500, 1, 0};
        String[] spinnerData = {"x1/1000", "x1/500", "x1/250", "x1/100", "x1/50", "x1/25", "x1/10", "x1/5", "x1/2", "x1", "x2", "x5", "x10", "x25", "x50", "x100", "x250", "x500", "x1000", "Virtual"};
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerData);
        JSpinner spinner = new JSpinner(spinnerListModel);
        spinner.setValue("x100");

        jTextArea = new JTextArea();
        jTextAreaResults = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        JScrollPane jScrollPaneResults = new JScrollPane(jTextAreaResults);
        resultLabel = new JLabel();
        replicationLabel = new JLabel();
        JLabel resultLabelHint = new JLabel("Simulačný čas:");
        JLabel replicationLabelHint = new JLabel("Replikácia č. :");
        JCheckBox jCheckBoxMedzivysledky = new JCheckBox("Medzivysledky");

        jTextArea.setEditable(false);
        jTextArea.setToolTipText("Medzi vysledky");
        jTextAreaResults.setEditable(false);
        jTextAreaResults.setToolTipText("Vysledky");
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pocetOpakovani.setToolTipText("Pocet opakovani");
        zamestnanciField[0].setToolTipText("Recepcne");
        zamestnanciField[1].setToolTipText("Kadernicky");
        zamestnanciField[2].setToolTipText("Kozmeticky");
        pocetOpakovani.setText("1000");
        zamestnanciField[0].setText("2");
        zamestnanciField[1].setText("6");
        zamestnanciField[2].setText("5");


        spinner.setToolTipText("Rychlost");
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane[] tablesScrollPane = new JScrollPane[tables.length];


        for (int i = 0; i < tables.length; i++) {
            tables[i] = new JTable();
            tables[i].setDefaultRenderer(String.class, centerRenderer);
            tables[i].setDefaultRenderer(Integer.class, centerRenderer);
            tables[i].setDefaultRenderer(Double.class, centerRenderer);
            tablesScrollPane[i] = new JScrollPane(tables[i]);
            panel.add(tablesScrollPane[i]);
        }
        tables[0].setModel(new MyTableModel(new String[]{"Pracovisko", "Aktualny rad", "Priemerna dlzka", "Celkova priemerna dlzka"}, new Object[][]{{"Recepcia", 0, 0.0, 0.0}, {"Ucesy", 0, 0.0, 0.0}, {"Licenie", 0, 0.0, 0.0}}));


        for (int i = 0; i < 4; i++) {
            TableColumn column = tables[0].getColumnModel().getColumn(i);
            if (i != 3) {
                column.setPreferredWidth(buttonWidth);
            } else {
                column.setPreferredWidth(buttonWidth + 30);
            }
        }


        start.addActionListener(e -> {
            this.iterationCount = Integer.parseInt(pocetOpakovani.getText());
            int recepcne = Integer.parseInt(zamestnanciField[0].getText());
            int kadernicky = Integer.parseInt(zamestnanciField[1].getText());
            int kozmeticky = Integer.parseInt(zamestnanciField[2].getText());

            Object[][] tableData = new Object[recepcne + kadernicky + kozmeticky][6];
            for (int i = 0; i < recepcne + kadernicky + kozmeticky; i++) {
                for (int j = 0; j < 6; j++) {
                    tableData[i][j] = 0.0;
                    if (j == 1)
                        tableData[i][j] = " ";
                    if (i < recepcne && j == 0) {
                        tableData[i][j] = "Recepčná č. " + (i + 1);
                    } else if (i < recepcne + kadernicky && j == 0) {
                        tableData[i][j] = "Kaderníčka č. " + (i + 1 - recepcne);
                    } else if (j == 0) {
                        tableData[i][j] = "Kozmetička č. " + (i + 1 - recepcne - kadernicky);
                    }
                }
            }
            tables[1].setModel(new MyTableModel(new String[]{"Zamestnanec", "Obsluhuje", "Odpracovaný čas", "Využitie", "Priemerné využitie", "Celkové priemerné využitie"}, tableData));

            for (int i = 0; i < 6; i++) {
                TableColumn column = tables[1].getColumnModel().getColumn(i);
                if (i == 0) {
                    column.setPreferredWidth(buttonWidth + 40);
                } else if (i == 5) {
                    column.setPreferredWidth(buttonWidth + 100);
                } else if (i == 4 || i == 2) {
                    column.setPreferredWidth(buttonWidth + 60);
                } else {
                    column.setPreferredWidth(buttonWidth);
                }
            }

            simulationCount++;
            salonSimulation = new SalonSimulation((17 - 9) * 3600, recepcne, kadernicky, kozmeticky);
            salonSimulation.registerDelegate(this);
            salonSimulation.setSleepTime(sleepTime);
            t1 = new Thread(new RunnableImpl());
            t1.start();
            stop.setEnabled(true);
            start.setEnabled(false);
            pause.setEnabled(true);
        });

        stop.setEnabled(false);
        pause.setEnabled(false);

        stop.addActionListener(e -> {
            if (salonSimulation != null) {
                salonSimulation.stopEvents();
                salonSimulation.stop();
            }
            this.stop();
        });

        pause.addActionListener(e -> {
            if (salonSimulation != null) {
                if (salonSimulation.isPaused()) {
                    salonSimulation.setPaused(false);
                    pause.setText("Pause");
                    pause.setSize(buttonWidth, buttonHeight);
                } else {
                    salonSimulation.setPaused(true);
                    pause.setText("Continue");
                    pause.setSize(buttonWidth + 12, buttonHeight);
                }
            }
        });

        pocetOpakovani.addActionListener(e -> this.iterationCount = Integer.parseInt(pocetOpakovani.getText()));

        spinner.addChangeListener(e -> {
            String valueStr = (String) spinner.getValue();
            int value = 100;
            for (int i = 0; i < spinnerData.length; i++) {
                if (spinnerData[i].equals(valueStr)) {
                    value = spinnerValues[i];
                    break;
                }
            }
            sleepTime = value;
            if (salonSimulation != null) {
                salonSimulation.setSleepTime(sleepTime);
            }
        });

        jCheckBoxMedzivysledky.addActionListener(e -> showMedzivysledky = jCheckBoxMedzivysledky.isSelected());

        start.setBounds(130, space, buttonWidth, buttonHeight);
        stop.setBounds(130, space + buttonHeight, buttonWidth, buttonHeight);
        pause.setBounds(130, space + buttonHeight * 2, buttonWidth, buttonHeight);

        for (int i = 0; i < 3; i++) {
            zamestnanciField[i].setBounds(space + 40 * i, space, 40, buttonHeight);
        }
        pocetOpakovani.setBounds(space, space + buttonHeight, 120, buttonHeight);
        spinner.setBounds(space, 2 * space + buttonHeight * 2, buttonWidth - 5, buttonHeight + 10);
        jScrollPane.setBounds((width - width / 4) - (100 / 2) - 20, space + buttonHeight * 6 + 16, 100, 140);
        resultLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + buttonHeight + 1, buttonWidth + 20, buttonHeight);
        resultLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + buttonHeight + 1, buttonWidth * 2, buttonHeight);
        replicationLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + 1, buttonWidth + 20, buttonHeight);
        replicationLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + 1, buttonWidth * 2, buttonHeight);
        jScrollPaneResults.setBounds(space * 3, space + buttonHeight * 6 + 16, 300, 140);
        jCheckBoxMedzivysledky.setBounds((width - width / 4) - (100 / 2) + 100 - 16, space + buttonHeight * 6 + 16, 116, 140);
        tablesScrollPane[0].setBounds(600, 600, 480, 72);
        tablesScrollPane[1].setBounds(0, 400, 640, 220);

        panel.add(pocetOpakovani);
        panel.add(start);
        panel.add(stop);
        panel.add(pause);
        panel.add(spinner);
        for (int i = 0; i < 3; i++) {
            panel.add(zamestnanciField[i]);
        }
        panel.add(jScrollPane);
        panel.add(resultLabel);
        panel.add(resultLabelHint);
        panel.add(replicationLabelHint);
        panel.add(replicationLabel);
        panel.add(jScrollPaneResults);
        panel.add(jCheckBoxMedzivysledky);

        setContentPane(panel);

        int x = bounds.x + bounds.width - insets.right - this.getWidth();
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);

        this.setVisible(true);
    }

    private void stop() {
        if (salonSimulation.isPaused()) {
            salonSimulation.setPaused(false);
            pause.setText("Pause");
            pause.setSize(72, 24);
        }
        if (!salonSimulation.isRun()) {
            start.setEnabled(true);
            pause.setEnabled(false);
            stop.setEnabled(false);
        }
    }

    public void prepare(int n) {
        strategiaStr = "error";
        lineChart = new LineChart("Simulacia c. " + simulationCount + " - Strategia " + "\"" + strategiaStr + "\"");
        lineChart.pack();
        lineChart.setVisible(true);

        jTextArea.setText("");
        resultLabel.setText("");

        hodnota = 0;
        realIterationCount = 1;
        histogramHodnoty = new int[n + 1];
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
    }

    public void calculate(int medziVysledok, int iteration) {
        hodnota += medziVysledok;
        result = hodnota * 1.0 / iteration;

        if (medziVysledok > histogramHodnoty.length) {
            histogramHodnoty[0]++;
        } else {
            histogramHodnoty[medziVysledok]++;
        }

//        if (medziVysledok > max && medziVysledok < 3 * n) {
//            max = medziVysledok;
//        }

        if (showMedzivysledky)
            jTextArea.append(medziVysledok + "\n");

//        if (iteration >= iterationCount / 100.0 * gapPercentStart) {
//            if (iteration % gap == 0) {
//                lineChart.addPoint(iteration, result);
//            }
//        }

        resultLabel.setText(result + "");

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        realIterationCount = iteration;
    }

    public void showResults() {
        //String text = String.format("---Simulacia c. %d---\nn: %d\nPocet iteracii: %d\nStrategia: %d (%s)\nVysledok: %f\n\n", simulationCount, n, realIterationCount, strategia, strategiaStr, result);
        //jTextAreaResults.append(text);
        resultLabel.setText(result + "");
        //jTextAreaResults.setCaretPosition(jTextAreaResults.getDocument().getLength());
        final Histogram histogram = new Histogram("Simulacia c. " + simulationCount + " - Strategia " + "\"" + strategiaStr + "\"", histogramHodnoty, min, max);
        histogram.pack();
        histogram.setVisible(true);
    }

    class MyTableModel extends AbstractTableModel {
        String[] columnNames;
        Object[][] data;

        public MyTableModel(String[] columnNames, Object[][] data) {
            this.columnNames = columnNames;
            this.data = data;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    @Override
    public void refresh(EventCore eventCore) {
        SalonSimulation salonSimulation = (SalonSimulation) eventCore;
//        System.out.println(eventCore.getSimTime());
        if (!salonSimulation.isRun()) {
            this.stop();
        }
        calendar.set(0, Calendar.JANUARY, 0, 9, 0, 0);
        calendar.add(Calendar.SECOND, (int) eventCore.getSimTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        resultLabel.setText((hour < 10 ? "" + 0 + hour : hour) + ":" + (minute < 10 ? "" + 0 + minute : minute) + ":" + (second < 10 ? "" + 0 + second : second));
        replicationLabel.setText(salonSimulation.getPocetReplikacii() + "");

        if (salonSimulation.getSleepTime() != 0 || !salonSimulation.isRun()) {
            tables[0].getModel().setValueAt(salonSimulation.getRadRecepcia().size(), 0, 1);
            tables[0].getModel().setValueAt(salonSimulation.getRadUces().size(), 1, 1);
            tables[0].getModel().setValueAt(salonSimulation.getRadLicenie().size(), 2, 1);

            for (int i = 0; i < salonSimulation.getZamestnanci().size(); i++) {
                Zamestnanec zamestnanec = salonSimulation.getZamestnanci().get(i);
                calendar.set(0, Calendar.JANUARY, 0, 0, 0, 0);
                calendar.add(Calendar.SECOND, (int) zamestnanec.getOdpracovanyCas());
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);
                tables[1].getModel().setValueAt((hour < 10 ? "" + 0 + hour : hour) + ":" + (minute < 10 ? "" + 0 + minute : minute) + ":" + (second < 10 ? "" + 0 + second : second), i, 2);

                tables[1].getModel().setValueAt(zamestnanec.isObsluhuje() ? "X" : "", i, 1);

                tables[1].getModel().setValueAt(Math.round(zamestnanec.getVyuzitie() * 100 * 100) / 100.0, i, 3);

            }
        }

        //System.out.println(salonSimulation.getRadRecepcia().size());
    }

    private class RunnableImpl implements Runnable {
        public void run() {
            salonSimulation.simulate(iterationCount);
        }
    }

}

