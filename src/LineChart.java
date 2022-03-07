import java.awt.*;

import javax.swing.*;

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
    private int iteration = 0;

    private double max = Integer.MIN_VALUE;
    private double min = Integer.MAX_VALUE;

    private final String title;

    public LineChart(String title) {

        super("Semestralka 1");

        this.series = new XYSeries("Semestralka 1");
        this.title = title;

        final XYSeriesCollection dataset = new XYSeriesCollection(this.series);
        chart = createChart(dataset);

        //Sets background color of chart
        chart.setBackgroundPaint(Color.LIGHT_GRAY);

        //Created JPanel to show graph on screen
        final JPanel content = new JPanel(new BorderLayout());

        //Created Chartpanel for chart area
        final ChartPanel chartPanel = new ChartPanel(chart);

        //Added chartpanel to main panel
        content.add(chartPanel);

        GraphicsConfiguration config = this.getGraphicsConfiguration();
        Rectangle bounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

        int height = bounds.height < 800 ? bounds.height - 100 - bounds.y - insets.top - insets.bottom : 720;

        //Sets the size of whole window (JPanel)
        chartPanel.setPreferredSize(new java.awt.Dimension(bounds.width - 700 - 10 - insets.left - insets.right, height));

        //Puts the whole content on a Frame
        setContentPane(content);

        int x = bounds.x + insets.left;
        int y = bounds.y + insets.top + 2;
        setLocation(x, y);
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createXYLineChart(
                title,
                "Iteration",
                "Value",
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


    public void addPoint(int iteration, double point) {
        if (this.iteration < 20000) {
            if (point > max) {
                max = point;
            }
            if (point < min) {
                min = point;
            }
            if (max > min) {
                chart.getXYPlot().getRangeAxis().setRange(min, max);
            }
        }
        this.series.add(iteration, point);
        this.iteration++;
    }

}  