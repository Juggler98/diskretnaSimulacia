import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.*;

public class Histogram extends MyApplicationFrame {

    private final String title;

    final HistogramDataset dataset;

    public Histogram(String title) {
        super("Semestralka 1");
        this.title = title;
        dataset = new HistogramDataset();
        JFreeChart chart = createChart(dataset);
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        final JPanel content = new JPanel(new BorderLayout());
        final ChartPanel chartPanel = new ChartPanel(chart);
        content.add(chartPanel);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int height = bounds.height < 800 ? bounds.height - 360 - 50 - bounds.y - insets.top - insets.bottom : 500;

        chartPanel.setPreferredSize(new java.awt.Dimension(684, height));

        int x = bounds.x + bounds.width - insets.right - chartPanel.getPreferredSize().width - 16;
        int y = bounds.y + insets.top + 2 + 360;
        setLocation(x, y);

        setContentPane(content);
    }

    private JFreeChart createChart(final IntervalXYDataset dataset) {
        final JFreeChart result = ChartFactory.createHistogram(
                title,
                "Value",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        final XYPlot plot = result.getXYPlot();

        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);

        ValueAxis xaxis = plot.getDomainAxis();
        ValueAxis yaxis = plot.getRangeAxis();

        xaxis.setAutoRange(true);
        yaxis.setAutoRange(true);

        return result;
    }

    public void addPoints(LinkedList<Integer> hodnoty, int min, int max) {
        double[] values = new double[hodnoty.size()];
        int index = 0;
        for (Integer i : hodnoty) {
            values[index++] = i;
        }
        dataset.addSeries("key", values, (max - min) / 2 + 20);
    }

}  