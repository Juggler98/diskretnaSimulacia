package charts;

import java.awt.*;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class Histogram extends MyApplicationFrame {

    private final String title;
    private final int n;

    public Histogram(String title, int[] hodnoty, int min, int max) {
        super(title);
        this.title = title;
        this.n = hodnoty.length;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i <= max; i++) {
            dataset.setValue(hodnoty[i], "", i + "");
        }
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

    private JFreeChart createChart(final DefaultCategoryDataset dataset) {
        final JFreeChart result = ChartFactory.createStackedBarChart(
                title,
                "Value",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        final CategoryPlot plot = result.getCategoryPlot();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        for (int i = 0; i < n; i++) {
            renderer.setSeriesPaint(i, Color.red);
        }

        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);

        CategoryAxis xaxis = plot.getDomainAxis();

        xaxis.setCategoryMargin(0);
        xaxis.setLowerMargin(0.02);
        xaxis.setUpperMargin(0.02);

        ValueAxis yaxis = plot.getRangeAxis();
        yaxis.setAutoRange(true);

        return result;
    }

}  