package udalostna.gui;

import charts.LineChart;
import simCores.EventCore;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EventGUI extends JFrame implements ISimDelegate {

    private int iterationCount = 0;
    private int sleepTime = 1000 / 100;

    private final JLabel resultLabel;
    private final JLabel replicationLabel;
    private final JLabel intervalSpolahlivostiLabel;
    private final JButton start;
    private final JButton pause;
    private final JButton stop;

    private boolean isRunning = false;

    private int pocetZakaznikov = 0;

    private final JTable[] tables = new JTable[5];
    private final JTextField[] zamestnanciField = new JTextField[3];
    private final JTextField pocetOpakovani;

    private static final Calendar calendar = Calendar.getInstance();

    private SalonSimulation salonSimulation;

    public EventGUI() {
        super("Salón krásy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        final int width = (int) (bounds.width * 0.6);
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
        JButton test = new JButton("Test");
        JButton graf = new JButton("Graf");
        pocetOpakovani = new JTextField();
        for (int i = 0; i < 3; i++) {
            zamestnanciField[i] = new JTextField();
        }

        int[] spinnerValues = {1000 * 1000, 500 * 1000, 250 * 1000, 100 * 1000, 50 * 1000, 25 * 1000, 10 * 1000, 5 * 1000, 2 * 1000, 1000, 1000 / 2, 1000 / 5, 1000 / 10, 1000 / 25, 1000 / 50, 1000 / 100, 1000 / 250, 1000 / 500, 1, 0};
        String[] spinnerData = {"x1/1000", "x1/500", "x1/250", "x1/100", "x1/50", "x1/25", "x1/10", "x1/5", "x1/2", "x1", "x2", "x5", "x10", "x25", "x50", "x100", "x250", "x500", "x1000", "Virtual"};
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerData);
        JSpinner spinner = new JSpinner(spinnerListModel);
        spinner.setValue("x100");

        resultLabel = new JLabel();
        replicationLabel = new JLabel();
        intervalSpolahlivostiLabel = new JLabel();
        JLabel resultLabelHint = new JLabel("Simulačný čas:");
        JLabel replicationLabelHint = new JLabel("Replikácia č.:");
        JLabel intervalSpolahlivostiLabelHint = new JLabel("90% interval spoľahlivosti času v salóne:");

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
        tables[0].setModel(new DefaultTableModel(new Object[][]{{"Recepcia", 0, 0.0, 0.0}, {"Ucesy", 0, 0.0, 0.0}, {"Licenie", 0, 0.0, 0.0}}, new String[]{"Pracovisko", "Aktualny rad", "Priemerna dlzka", "Celkova priemerna dlzka"}));

        for (int i = 0; i < 4; i++) {
            TableColumn column = tables[0].getColumnModel().getColumn(i);
            if (i != 3) {
                column.setPreferredWidth(buttonWidth);
            } else {
                column.setPreferredWidth(buttonWidth + 30);
            }
        }

        for (JTable table : tables) {
            table.setDefaultRenderer(String.class, centerRenderer);
            table.setDefaultRenderer(Integer.class, centerRenderer);
            table.setDefaultRenderer(Double.class, centerRenderer);
        }


        start.addActionListener(e -> {
            if (!isRunning) {
                isRunning = true;
                start.setEnabled(false);
                this.iterationCount = Integer.parseInt(pocetOpakovani.getText());
                int recepcne = Integer.parseInt(zamestnanciField[0].getText());
                int kadernicky = Integer.parseInt(zamestnanciField[1].getText());
                int kozmeticky = Integer.parseInt(zamestnanciField[2].getText());

                Object[][] tableData = new Object[recepcne + kadernicky + kozmeticky][6];
                for (int i = 0; i < recepcne + kadernicky + kozmeticky; i++) {
                    for (int j = 0; j < 6; j++) {
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
                tables[1].setModel(new DefaultTableModel(tableData, new String[]{"Zamestnanec", "Obsluhuje", "Zákaznika č.", "Odpracovaný čas", "Využitie"}));
                tables[2].setModel(new DefaultTableModel(null, new String[]{"Zákaznik", "Stav", "Prichod", "Objednávka", "Účes", "Hlbkové čistenie", "Líčenie", "Platba", "Odchod", "Celkový čas"}));
                tables[2].getColumnModel().getColumn(0).setPreferredWidth(buttonWidth + 40);
                tables[2].getColumnModel().getColumn(1).setPreferredWidth(buttonWidth + 60);
                tables[2].getColumnModel().getColumn(3).setPreferredWidth(buttonWidth + 40);
                tables[2].getColumnModel().getColumn(5).setPreferredWidth(buttonWidth + 60);
                tables[2].getColumnModel().getColumn(9).setPreferredWidth(buttonWidth + 60);

                pocetZakaznikov = 0;

                for (int i = 0; i < 4; i++) {
                    TableColumn column = tables[1].getColumnModel().getColumn(i);
                    if (i == 0) {
                        column.setPreferredWidth(buttonWidth + 40);
                    } else if (i == 3) {
                        column.setPreferredWidth(buttonWidth + 60);
                    } else {
                        column.setPreferredWidth(buttonWidth);
                    }
                }

                salonSimulation = new SalonSimulation((17 - 9) * 3600, recepcne, kadernicky, kozmeticky);
                Object[][] tableData2 = new Object[salonSimulation.getStatsNames().length][2];
                for (int i = 0; i < salonSimulation.getStatsNames().length; i++) {
                    tableData2[i][0] = salonSimulation.getStatsNames()[i];
                }
                tables[3].setModel(new DefaultTableModel(tableData2, new String[]{"Názov", "Hodnota", "Priemerná hodnota"}));
                tables[3].getColumnModel().getColumn(0).setPreferredWidth(buttonWidth * 2);
                tables[3].getColumnModel().getColumn(2).setPreferredWidth(buttonWidth * 2 - 20);

                salonSimulation.registerDelegate(this);
                salonSimulation.setSleepTime(sleepTime);

                SimulationThread simulationThread = new SimulationThread();
                simulationThread.start();
                stop.setEnabled(true);
                pause.setEnabled(true);
            }
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

        test.addActionListener(e -> {
            TestThread thread = new TestThread();
            thread.start();
        });

        graf.addActionListener(e -> {
            GrafThread thread = new GrafThread();
            thread.start();
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

        start.setBounds(130, space, buttonWidth, buttonHeight);
        stop.setBounds(130, space + buttonHeight, buttonWidth, buttonHeight);
        pause.setBounds(130, space + buttonHeight * 2, buttonWidth, buttonHeight);
        test.setBounds(width - space - 80, space, 60, buttonHeight);
        graf.setBounds(width - space - 80, space + buttonHeight, 60, buttonHeight);

        for (int i = 0; i < 3; i++) {
            zamestnanciField[i].setBounds(space + 40 * i, space, 40, buttonHeight);
        }
        pocetOpakovani.setBounds(space, space + buttonHeight, 120, buttonHeight);
        spinner.setBounds(space, 2 * space + buttonHeight * 2, buttonWidth - 5, buttonHeight + 10);
        resultLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + buttonHeight + 1, buttonWidth + 20, buttonHeight);
        resultLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + buttonHeight + 1, buttonWidth * 2, buttonHeight);
        replicationLabelHint.setBounds((width / 4) - (buttonWidth / 2), space + 1, buttonWidth + 20, buttonHeight);
        replicationLabel.setBounds((width / 4) - (buttonWidth / 2) + buttonWidth + 20, space + 1, buttonWidth * 2, buttonHeight);
        intervalSpolahlivostiLabelHint.setBounds(space, height - buttonHeight - 60, buttonWidth + 160, buttonHeight);
        intervalSpolahlivostiLabel.setBounds(space + buttonWidth + 160, height - buttonHeight - 60, buttonWidth * 2, buttonHeight);
        tablesScrollPane[0].setBounds(space, space + buttonHeight * 5, 480, 72);
        tablesScrollPane[1].setBounds(space, space * 4 + buttonHeight * 5 + 72, 480, 232);
        tablesScrollPane[2].setBounds(space, space * 8 + buttonHeight * 5 + 72 + 232, 840, 300);
        tablesScrollPane[3].setBounds(space * 4 + 480, space + buttonHeight * 5, 360, 248);

        panel.add(pocetOpakovani);
        panel.add(start);
        panel.add(stop);
        panel.add(pause);
        panel.add(test);
        panel.add(graf);
        panel.add(spinner);
        for (int i = 0; i < 3; i++) {
            panel.add(zamestnanciField[i]);
        }
        panel.add(resultLabel);
        panel.add(replicationLabel);
        panel.add(intervalSpolahlivostiLabel);
        panel.add(resultLabelHint);
        panel.add(replicationLabelHint);
        panel.add(intervalSpolahlivostiLabelHint);

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
        isRunning = false;
    }

    private void graf() {
        int kadernicky = Integer.parseInt(zamestnanciField[1].getText());
        int kozmeticky = Integer.parseInt(zamestnanciField[2].getText());
        LineChart lineChart = new LineChart(String.format("%d Kaderníčok, %d Kozmetičiek", kadernicky, kozmeticky), "Recepčné", "Priemerná dĺžka radu");
        lineChart.setSize(740, 620);
        lineChart.pack();
        lineChart.setVisible(true);
        for (int recepcne = 1; recepcne <= 10; recepcne++) {
            SalonSimulation salonSimulation = new SalonSimulation((17 - 9) * 3600, recepcne, kadernicky, kozmeticky);
            salonSimulation.simulate(Integer.parseInt(pocetOpakovani.getText()));
            lineChart.addPoint(recepcne, salonSimulation.getCelkoveDlzkyRadov()[0] / salonSimulation.getPocetReplikacii());
        }
    }

    private void test() throws IOException {
        int min = Integer.MAX_VALUE;
        String solution = "";
        BufferedWriter writer = new BufferedWriter(new FileWriter("resultData.txt"));
        writer.write("Recepcne,Kadernicky,Kozmeticky,Cas na objednavku (min),Cas v salone (hod)\n");
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                for (int k = 1; k <= 10; k++) {
                    SalonSimulation salonSimulation = new SalonSimulation((17 - 9) * 3600, i, j, k);
                    salonSimulation.simulate(Integer.parseInt(pocetOpakovani.getText()));
//                    System.out.println(i + "," + j + "," + k);
//                    System.out.println(salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.getPocetReplikacii());
//                    System.out.println(salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.getPocetReplikacii());
//                    System.out.println();
                    writer.write(i + "," + j + "," + k + "," + salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.getPocetReplikacii() + "," + salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.getPocetReplikacii() +"\n");
                    if (salonSimulation.getCelkoveCasy()[0] / 3600 / salonSimulation.getPocetReplikacii() <= 3 && salonSimulation.getCelkoveCasy()[1] / 60 / salonSimulation.getPocetReplikacii() <= 4) {
                        if (i + j + k < min) {
                            min = i + j + k;
                            solution = i + " " + j + " " + k;
                        } else if (i + j + k == min) {
                            solution += "\n" + i + " " + j + " " + k;
                        }
                    }
                }
            }
        }
        writer.close();
        JOptionPane.showMessageDialog(null, "Best solutions: " + solution);
    }

    @Override
    public synchronized void refresh(EventCore eventCore) {
        SalonSimulation salonSimulation = (SalonSimulation) eventCore;
        if (!salonSimulation.isRun()) {
            this.stop();
        } else {
            replicationLabel.setText(salonSimulation.getPocetReplikacii() + 1 + "");
        }

        if (salonSimulation.getSleepTime() != 0 || !salonSimulation.isRun()) {
            resultLabel.setText(getTime((int) eventCore.getSimTime(), 9));
            if (salonSimulation.getPocetReplikacii() > 30) {
                double s = Math.sqrt((1.0 / (salonSimulation.getPocetReplikacii() - 1) * salonSimulation.getxI()[0]) - (1.0 / (salonSimulation.getPocetReplikacii() - 1) * salonSimulation.getxI()[1]));
                double start = salonSimulation.getxI()[1] / salonSimulation.getPocetReplikacii() - s * 1.645 / Math.sqrt(salonSimulation.getPocetReplikacii());
                double end = salonSimulation.getxI()[1] / salonSimulation.getPocetReplikacii() + s * 1.645 / Math.sqrt(salonSimulation.getPocetReplikacii());
                intervalSpolahlivostiLabel.setText(String.format("<%s, %s>", getTime((int) start, 0), getTime((int) end, 0)));
            }


            tables[0].getModel().setValueAt(salonSimulation.getRadRecepcia().size(), 0, 1);
            tables[0].getModel().setValueAt(salonSimulation.getRadUces().size(), 1, 1);
            tables[0].getModel().setValueAt(salonSimulation.getRadLicenie().size(), 2, 1);

            for (int i = 0; i < 3; i++) {
                Pracovisko pracovisko;
                if (i == 0)
                    pracovisko = salonSimulation.getPracoviskoRecepcia();
                else if (i == 1)
                    pracovisko = salonSimulation.getPracoviskoUcesy();
                else
                    pracovisko = salonSimulation.getPracoviskoLicenie();
                if (pracovisko.getLastRadChange() != 0)
                    tables[0].getModel().setValueAt(salonSimulation.getDlzkyRadov()[i] / pracovisko.getLastRadChange(), i, 2);
                else
                    tables[0].getModel().setValueAt(0.0, i, 2);
                tables[0].getModel().setValueAt(salonSimulation.getCelkoveDlzkyRadov()[i] / salonSimulation.getPocetReplikacii(), i, 3);
            }

            for (int i = 0; i < salonSimulation.getZamestnanci().size(); i++) {
                Zamestnanec zamestnanec = salonSimulation.getZamestnanci().get(i);
                tables[1].getModel().setValueAt(zamestnanec.isObsluhuje() ? "X" : "", i, 1);
                tables[1].getModel().setValueAt(zamestnanec.isObsluhuje() ? zamestnanec.getObsluhujeZakaznika() : "", i, 2);
                tables[1].getModel().setValueAt(getTime((int) zamestnanec.getOdpracovanyCas(), 0), i, 3);
                tables[1].getModel().setValueAt(Math.round(zamestnanec.getVyuzitie() * 100 * 100) / 100.0, i, 4);
            }

            if (salonSimulation.getZakaznici().size() <= 1 && pocetZakaznikov > 1) {
                tables[2].setModel(new DefaultTableModel(null, new String[]{"Zákaznik", "Stav", "Prichod", "Objednávka", "Účes", "Hlbkové čistenie", "Líčenie", "Platba", "Odchod", "Celkový čas"}));
                pocetZakaznikov = 0;
            }

            for (int i = 0; i < salonSimulation.getZakaznici().size(); i++) {
                ZakaznikSalonu zakaznikSalonu = salonSimulation.getZakaznici().get(i);
                if (pocetZakaznikov < salonSimulation.getZakaznici().size()) {
                    for (int j = pocetZakaznikov; j < salonSimulation.getZakaznici().size(); j++) {
                        DefaultTableModel model = (DefaultTableModel) tables[2].getModel();
                        model.addRow(new Object[]{"Zákazník č. " + ++pocetZakaznikov});
                    }

                }
                tables[2].getModel().setValueAt(zakaznikSalonu.getStavZakaznika(), i, 1);

                if (zakaznikSalonu.getCasPrichodu() > 0) {
                    tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasPrichodu(), 9), i, 2);
                }

                if (zakaznikSalonu.getCasOdchodu() > 0) {
                    tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasOdchodu(), 9), i, 8);
                    tables[2].getModel().setValueAt(getTime((int) (zakaznikSalonu.getCasOdchodu() - zakaznikSalonu.getCasPrichodu()), 0), i, 9);
                }

                tables[2].getModel().setValueAt(zakaznikSalonu.getStavZakaznika(), i, 1);

                for (int j = 0; j < 5; j++) {
                    if (zakaznikSalonu.getCasZaciatkuObsluhy(j) > 0) {
                        tables[2].getModel().setValueAt(getTime((int) zakaznikSalonu.getCasZaciatkuObsluhy(j), 9), i, 3 + j);
                    }
                }
            }

            for (int i = 0; i < salonSimulation.getStatsNames().length - 4; i++) {
                tables[3].getModel().setValueAt(salonSimulation.getStatsVykonov()[i], i, 1);
                tables[3].getModel().setValueAt(salonSimulation.getStatsAllVykonov()[i] / salonSimulation.getPocetReplikacii(), i, 2);
            }
            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCasy()[1] / salonSimulation.getStatsVykonov()[9]), 0), 10, 1);
            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[1] / salonSimulation.getPocetReplikacii()), 0), 10, 2);

            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCasy()[0] / salonSimulation.getStatsVykonov()[9]), 0), 11, 1);
            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[0] / salonSimulation.getPocetReplikacii()), 0), 11, 2);

            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCasy()[2] / (salonSimulation.getStatsVykonov()[0] + salonSimulation.getStatsVykonov()[4])), 0), 12, 1);
            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[2] / salonSimulation.getPocetReplikacii()), 0), 12, 2);

            tables[3].getModel().setValueAt(getTime((int) (salonSimulation.getCelkoveCasy()[3] / salonSimulation.getPocetReplikacii()), 0), 13, 2);

        }
    }

    private static String getTime(int seconds, int startHour) {
        calendar.set(0, Calendar.JANUARY, 0, startHour, 0, 0);
        calendar.add(Calendar.SECOND, seconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return (hour < 10 ? "" + 0 + hour : hour) + ":" + (minute < 10 ? "" + 0 + minute : minute) + ":" + (second < 10 ? "" + 0 + second : second);
    }

    private class SimulationThread extends Thread {
        @Override
        public void run() {
            salonSimulation.simulate(iterationCount);
        }
    }

    private class TestThread extends Thread {
        @Override
        public void run() {
            try {
                test();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class GrafThread extends Thread {
        @Override
        public void run() {
            graf();
        }
    }

}

