package charts;

import java.awt.*;

import javax.swing.*;

import monteCarloParking.MyApplicationFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChart extends MyApplicationFrame {

    private final XYSeries series;
    private final JFreeChart chart;
    private final ChartPanel chartPanel;
    private final String title;

    private double max = Integer.MIN_VALUE;
    private double min = Integer.MAX_VALUE;

    private int iteration = 0;

    public LineChart(String title, String osX, String osY) {

        super(title);

        this.series = new XYSeries(title);
        this.title = title;

        final XYSeriesCollection dataset = new XYSeriesCollection(this.series);
        chart = createChart(dataset, osX, osY);
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        final JPanel content = new JPanel(new BorderLayout());
        chartPanel = new ChartPanel(chart);
        content.add(chartPanel);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int height = bounds.height < 800 ? bounds.height - 100 - bounds.y - insets.top - insets.bottom : 720;
        chartPanel.setPreferredSize(new java.awt.Dimension(bounds.width - 700 - 10 - insets.left - insets.right, height));

        int x = bounds.x + insets.left;
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);

        setContentPane(content);
    }

    private JFreeChart createChart(final XYDataset dataset, String osX, String osY) {
        final JFreeChart result = ChartFactory.createXYLineChart(
                title,
                osX,
                osY,
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

    public void addPoint(int x, double y) {
        if (this.iteration < 100000) {
            if (y > max) {
                max = y;
            }
            if (y < min) {
                min = y;
            }
            if (max > min) {
                chart.getXYPlot().getRangeAxis().setRange(min, max);
            }
        }
        this.series.add(x, y);
        this.iteration++;
    }

    public void setSize(int width, int height) {
        chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
    }



}  