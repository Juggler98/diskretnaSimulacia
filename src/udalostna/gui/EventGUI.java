package udalostna.gui;

import charts.Histogram;
import charts.LineChart;
import simCores.EventCore;
import udalostna.gui.ISimDelegate;
import udalostna.salon.SalonSimulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class EventGUI extends JFrame implements ISimDelegate {

    private int simulationCount = 0;
    private int iterationCount = 0;
    private int sleepTime = 1000 / 100;
    private int gap = 1;
    private int n = 10;
    private int gapPercentStart = 0;
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
    private final JButton stop;

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

        JButton start = new JButton("Start");
        stop = new JButton("Stop");
        JButton pause = new JButton("Pause");
        JTextField pocetOpakovani = new JTextField();
        JTextField medzera = new JTextField();
        JTextField medzeraNaZaciatku = new JTextField();
        JTextField n = new JTextField();

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
        JLabel resultLabelHint = new JLabel("Simulačný čas:");
        JCheckBox jCheckBoxMedzivysledky = new JCheckBox("Medzivysledky");

        jTextArea.setEditable(false);
        jTextArea.setToolTipText("Medzi vysledky");
        jTextAreaResults.setEditable(false);
        jTextAreaResults.setToolTipText("Vysledky");
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pocetOpakovani.setToolTipText("Pocet opakovani");
        medzera.setToolTipText("Zobraz kazdy (p) vysledok");
        medzeraNaZaciatku.setToolTipText("Preskoc prvych (q%) vysledkov");
        n.setToolTipText("n");
        pocetOpakovani.setText("1000");
        medzera.setText("1000");
        medzeraNaZaciatku.setText("10");
        n.setText("33");

        spinner.setToolTipText("Rychlost");

        start.addActionListener(e -> {
                this.gap = Integer.parseInt(medzera.getText());
                this.gapPercentStart = Integer.parseInt(medzeraNaZaciatku.getText());
                this.iterationCount = Integer.parseInt(pocetOpakovani.getText());
                this.n = Integer.parseInt(n.getText());
                simulationCount++;
                salonSimulation = new SalonSimulation((17 - 9) * 3600, 2, 6, 5);
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
            stop.setEnabled(false);
            start.setEnabled(true);
            pause.setEnabled(false);
            salonSimulation.setPaused(false);
            pause.setText("Pause");
            pause.setSize(buttonWidth, buttonHeight);
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

        medzera.addActionListener(e -> this.gap = Integer.parseInt(medzera.getText()));
        medzeraNaZaciatku.addActionListener(e -> this.gapPercentStart = Integer.parseInt(medzeraNaZaciatku.getText()));
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

        n.setBounds(space, space, 120, buttonHeight);
        pocetOpakovani.setBounds(space, space + buttonHeight, 120, buttonHeight);
        medzera.setBounds(space, space + buttonHeight * 2, 80, buttonHeight);
        medzeraNaZaciatku.setBounds(space + 80, space + buttonHeight * 2, 40, buttonHeight);
        spinner.setBounds(space, space + buttonHeight * 4, buttonWidth - 5, buttonHeight + 10);
        jScrollPane.setBounds((width - width / 4) - (100 / 2) - 20, space + buttonHeight * 6 + 16, 100, 140);
        resultLabelHint.setBounds((width / 2) - (buttonWidth / 2), space + buttonHeight, buttonWidth + 20, buttonHeight);
        resultLabel.setBounds((width / 2) - (buttonWidth / 2) + buttonWidth + 20, space + buttonHeight, buttonWidth * 2, buttonHeight);
        jScrollPaneResults.setBounds(space * 3, space + buttonHeight * 6 + 16, 300, 140);
        jCheckBoxMedzivysledky.setBounds((width - width / 4) - (100 / 2) + 100 - 16, space + buttonHeight * 6 + 16, 116, 140);

        panel.add(pocetOpakovani);
        panel.add(medzera);
        panel.add(medzeraNaZaciatku);
        panel.add(start);
        panel.add(stop);
        panel.add(pause);
        panel.add(spinner);
        panel.add(n);
        panel.add(jScrollPane);
        panel.add(resultLabel);
        panel.add(resultLabelHint);
        panel.add(jScrollPaneResults);
        panel.add(jCheckBoxMedzivysledky);

        setContentPane(panel);

        int x = bounds.x + bounds.width - insets.right - this.getWidth();
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);

        this.setVisible(true);
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

        if (medziVysledok > max && medziVysledok < 3 * n) {
            max = medziVysledok;
        }

        if (showMedzivysledky)
            jTextArea.append(medziVysledok + "\n");

        if (iteration >= iterationCount / 100.0 * gapPercentStart) {
            if (iteration % gap == 0) {
                lineChart.addPoint(iteration, result);
            }
        }

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

    @Override
    public void refresh(EventCore eventCore) {
        SalonSimulation salonSimulation = (SalonSimulation) eventCore;
//        System.out.println(eventCore.getSimTime());
        calendar.set(0, Calendar.JANUARY,0,9,0,0);
        calendar.add(Calendar.SECOND, (int) eventCore.getSimTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        resultLabel.setText((hour < 10 ? "" + 0 + hour : hour) + ":" + (minute < 10 ? "" + 0 + minute : minute) + ":" + (second < 10 ? "" + 0 + second : second));
        if (!salonSimulation.isRun()) {
            stop.doClick();
        }
        //System.out.println(salonSimulation.getRadRecepcia().size());
    }

    private class RunnableImpl implements Runnable {
        public void run() {
            salonSimulation.simulate(iterationCount);
        }
    }

}

