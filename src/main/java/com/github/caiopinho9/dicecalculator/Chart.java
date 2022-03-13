package com.github.caiopinho9.dicecalculator;

import org.jfree.chart.*;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.*;

public class Chart {
    private final String chartTitle;
    private final String xAxisText;
    private final String yAxisText;
    private final DefaultCategoryDataset categoryDataset;
    private JFreeChart barChart;
    private boolean invertColors;

    public Chart(String chartTitle, String yAxisText, String xAxisText, DefaultCategoryDataset categoryDataset, boolean invertColors) {
        this.chartTitle = chartTitle;
        this.xAxisText = xAxisText;
        this.yAxisText = yAxisText;
        this.invertColors = invertColors;
        this.categoryDataset = categoryDataset;
        this.createChart();
        this.color();
    }

    public void color() {
        CategoryPlot categoryplot = barChart.getCategoryPlot();
        BarRenderer bar = new BarRenderer();
        bar.setItemMargin(0); //reduce the width between the bars.
        bar.setSeriesPaint(0,new Color(20, 152, 222)); //first bar
        if (invertColors) {
            bar.setSeriesPaint(0,new Color(222, 20, 20)); //first bar
            bar.setSeriesPaint(1,new Color(20, 152, 222)); //second bar
            bar.setItemMargin(-0.5);
        }
        categoryplot.setRenderer(bar);
        categoryplot.setBackgroundPaint(new Color(199, 199, 199, 255));

        CategoryItemRenderer renderer = ((CategoryPlot)barChart.getPlot()).getRenderer();

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.BOTTOM_CENTER);
        renderer.setDefaultPositiveItemLabelPosition(position);
        renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        final CategoryPlot plot = barChart.getCategoryPlot();

        BarRenderer plotRenderer = (BarRenderer) plot.getRenderer();
        plotRenderer.setShadowVisible(false);
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
    }

    private void createChart() {
        this.barChart = ChartFactory.createBarChart(
                this.chartTitle,
                this.xAxisText,
                this.yAxisText,
                this.categoryDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        this.barChart.setBorderPaint(black);
    }

    public void showChart(int width, int height){
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        ChartPanel chartPanel = new ChartPanel(this.barChart);
        chartPanel.setPreferredSize(new Dimension(width, height));
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);

    }
}
