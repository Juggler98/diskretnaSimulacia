import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GUI extends JFrame {

    private int simulationCount = 0;
    private int iterationCount = 0;
    private int sleepTime = 0;
    private int gap = 1;
    private int n = 10;
    private int strategia = 1;
    private boolean run = false;
    private boolean showMedzivysledky = false;
    private double result;
    private int hodnota = 0;
    private LineChart lineChart;

    private int realIterationCount = 1;
    private LinkedList<Integer> hodnoty = new LinkedList<>();
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    private final JTextArea jTextArea;
    private final JTextArea jTextAreaResults;
    private final JLabel resultLabel;

    private Thread t1;

    ParkingSimulation parkingSimulation;

    public GUI() {
        super("Semestralka 1");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final int width = 700;
        final int height = 360;
        final int buttonWidth = 72;
        final int buttonHeight = 24;
        final int space = 5;

        this.setSize(width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        JTextField pocetOpakovani = new JTextField();
        JTextField medzera = new JTextField();
        JTextField n = new JTextField();
        JSlider spomalenie = new JSlider(0, 100);
        JRadioButton strategia1 = new JRadioButton("Prve volne");
        JRadioButton strategia2 = new JRadioButton("2*n/3");
        JRadioButton strategia3 = new JRadioButton("n/2");
        jTextArea = new JTextArea();
        jTextAreaResults = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        JScrollPane jScrollPaneResults = new JScrollPane(jTextAreaResults);
        resultLabel = new JLabel();
        JLabel resultLabelHint = new JLabel("Vysledok:");
        JCheckBox jCheckBoxMedzivysledky = new JCheckBox("Medzivysledky");

        jTextArea.setEditable(false);
        jTextArea.setToolTipText("Medzi vysledky");
        jTextAreaResults.setEditable(false);
        jTextAreaResults.setToolTipText("Vysledky");
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pocetOpakovani.setToolTipText("Pocet opakovani");
        medzera.setToolTipText("Zobraz kazdy (p) vysledok");
        n.setToolTipText("n");
        pocetOpakovani.setText("10000000");
        medzera.setText("1000");
        n.setText("50");

        spomalenie.setMajorTickSpacing(10);
        spomalenie.setMinorTickSpacing(1);
        spomalenie.setPaintTicks(true);
        spomalenie.setPaintLabels(true);
        spomalenie.setValue(100);
        spomalenie.setToolTipText("Rychlost (%)");

        start.addActionListener(e -> {
            if (!run) {
                this.gap = Integer.parseInt(medzera.getText());
                this.iterationCount = Integer.parseInt(pocetOpakovani.getText());
                this.n = Integer.parseInt(n.getText());
                if (strategia1.isSelected()) {
                    strategia = 1;
                } else if (strategia2.isSelected()) {
                    strategia = 2;
                } else if (strategia3.isSelected()) {
                    strategia = 3;
                }
                run = true;
                simulationCount++;
                parkingSimulation = new ParkingSimulation(this.n, strategia, this);
                t1 = new Thread(new RunnableImpl());
                t1.start();
            }
        });

        stop.addActionListener(e -> {
            run = false;
            if (parkingSimulation != null)
                parkingSimulation.stop();
        });
        medzera.addActionListener(e -> this.gap = Integer.parseInt(medzera.getText()));
        pocetOpakovani.addActionListener(e -> this.iterationCount = Integer.parseInt(pocetOpakovani.getText()));

        spomalenie.addChangeListener(e -> {
            int value = spomalenie.getValue();
            if (value >= 70) {
                sleepTime = (int) (100 - (100 * (value / 100.0)));
            } else if (value == 0) {
                sleepTime = 10000;
            } else if (value <= 10) {
                sleepTime = (int) (2000 - (2000 * (value / 100.0)));
            } else {
                sleepTime = (int) (500 - (500 * (value / 100.0)));
            }
        });

        strategia1.setActionCommand("1");
        strategia2.setActionCommand("2");
        strategia3.setActionCommand("3");

        strategia1.setSelected(true);

        ActionListener actionListener = e -> {
            switch (e.getActionCommand()) {
                case "1":
                    if (!run) {
                        strategia2.setSelected(false);
                        strategia3.setSelected(false);
                    }
                    if (!strategia1.isSelected()) {
                        strategia1.setSelected(true);
                    }
                    if (run && strategia1.isSelected() && strategia != 1) {
                        strategia1.setSelected(false);
                    }
                    break;
                case "2":
                    if (!run) {
                        strategia1.setSelected(false);
                        strategia3.setSelected(false);
                    }
                    if (!strategia2.isSelected()) {
                        strategia2.setSelected(true);
                    }
                    if (run && strategia2.isSelected() && strategia != 2) {
                        strategia2.setSelected(false);
                    }
                    break;
                case "3":
                    if (!run) {
                        strategia1.setSelected(false);
                        strategia2.setSelected(false);
                    }
                    if (!strategia3.isSelected()) {
                        strategia3.setSelected(true);
                    }
                    if (run && strategia3.isSelected() && strategia != 3) {
                        strategia3.setSelected(false);
                    }
                    break;
            }
        };

        strategia1.addActionListener(actionListener);
        strategia2.addActionListener(actionListener);
        strategia3.addActionListener(actionListener);

        jCheckBoxMedzivysledky.addActionListener(e -> showMedzivysledky = jCheckBoxMedzivysledky.isSelected());

        start.setBounds(130, space, buttonWidth, buttonHeight);
        stop.setBounds(130, space + buttonHeight, buttonWidth, buttonHeight);
        n.setBounds(space, space, 120, buttonHeight);
        pocetOpakovani.setBounds(space, space + buttonHeight, 120, buttonHeight);
        medzera.setBounds(space, space + buttonHeight * 2, 120, buttonHeight);
        spomalenie.setBounds(2, space + buttonHeight * 4, 660, buttonHeight * 2);
        strategia1.setBounds(space, space + buttonHeight * 3, buttonWidth + 20, buttonHeight);
        strategia2.setBounds(space + buttonWidth + 20, space + buttonHeight * 3, buttonWidth - 10, buttonHeight);
        strategia3.setBounds(space + buttonWidth * 2 + 10, space + buttonHeight * 3, buttonWidth - 20, buttonHeight);
        jScrollPane.setBounds((width - width / 4) - (100 / 2) - 20, space + buttonHeight * 6 + 16, 100, 140);
        resultLabelHint.setBounds((width / 2) - (buttonWidth / 2), space + buttonHeight, buttonWidth, buttonHeight);
        resultLabel.setBounds((width / 2) - (buttonWidth / 2) + buttonWidth, space + buttonHeight, buttonWidth * 2, buttonHeight);
        jScrollPaneResults.setBounds(space * 3, space + buttonHeight * 6 + 16, 300, 140);
        jCheckBoxMedzivysledky.setBounds((width - width / 4) - (100 / 2) + 100 - 16, space + buttonHeight * 6 + 16, 116, 140);

        panel.add(pocetOpakovani);
        panel.add(medzera);
        panel.add(start);
        panel.add(stop);
        panel.add(spomalenie);
        panel.add(n);
        panel.add(strategia1);
        panel.add(strategia2);
        panel.add(strategia3);
        panel.add(jScrollPane);
        panel.add(resultLabel);
        panel.add(resultLabelHint);
        panel.add(jScrollPaneResults);
        panel.add(jCheckBoxMedzivysledky);

        setContentPane(panel);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int x = bounds.x + bounds.width - insets.right - this.getWidth();
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);

        this.setVisible(true);
    }

    public void prepare() {
        lineChart = new LineChart("Simulacia c. " + simulationCount);
        lineChart.pack();
        lineChart.setVisible(true);

        jTextArea.setText("");
        resultLabel.setText("");

        hodnota = 0;
        realIterationCount = 1;
        hodnoty = new LinkedList<>();
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
    }

    public void calculate(int medziVysledok, int iteration) {
        hodnota += medziVysledok;
        result = hodnota * 1.0 / iteration;

        if (hodnoty.size() <= 1000000) {
            hodnoty.add(medziVysledok);
            if (medziVysledok > max) {
                max = medziVysledok;
            }
            if (medziVysledok < min) {
                min = medziVysledok;
            }
        }

        if (showMedzivysledky)
            jTextArea.append(medziVysledok + "\n");

        if (iteration % gap == 0) {
            lineChart.addPoint(iteration, result);
            resultLabel.setText(result + "");
        }

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
        String strategiaStr = "error";
        if (strategia == 1) {
            strategiaStr = "(Prve volne)";
        } else if (strategia == 2) {
            strategiaStr = "(2*n/3)";
        } else if (strategia == 3) {
            strategiaStr = "(n/2)";
        }
        String text = String.format("---Simulacia c. %d---\nn: %d\nPocet iteracii: %d\nStrategia: %d %s\nVysledok: %f\n\n", simulationCount, n, realIterationCount, strategia, strategiaStr, result);
        jTextAreaResults.append(text);
        resultLabel.setText(result + "");
        //jTextAreaResults.setCaretPosition(jTextAreaResults.getDocument().getLength());
        run = false;
        final Histogram histogram = new Histogram("Simulacia c. " + simulationCount);
        histogram.pack();
        histogram.setVisible(true);
        histogram.addPoints(hodnoty, min, max);
    }

    private class RunnableImpl implements Runnable {
        public void run() {
            parkingSimulation.simulate(iterationCount);
        }
    }


}
