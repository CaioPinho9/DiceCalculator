package com.github.caiopinho9.dicecalculator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class Chart {
    private final String chartTitle;
    private final String xAxisText;
    private final String yAxisText;
    private final DefaultCategoryDataset categoryDataset;
    private JFreeChart barChart;

    public Chart(String chartTitle, String yAxisText, String xAxisText, DefaultCategoryDataset categoryDataset) {
        this.chartTitle = chartTitle;
        this.xAxisText = xAxisText;
        this.yAxisText = yAxisText;
        this.categoryDataset = categoryDataset;
        this.createChart();
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
    }

    public void showChart(int width, int height){
        JFrame frame = new JFrame(this.chartTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        ChartPanel chartPanel = new ChartPanel(this.barChart);
        chartPanel.setPreferredSize(new Dimension(width, height));
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);

    }

}
