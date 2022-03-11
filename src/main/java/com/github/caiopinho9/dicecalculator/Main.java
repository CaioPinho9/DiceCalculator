package com.github.caiopinho9.dicecalculator;

import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.commons.math3.util.Precision;

import javax.swing.*;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        String expression = "3d4";
        expression = JOptionPane.showInputDialog("Say Expression");
        Operator operator = new Operator(expression);
        final String title = "Dice Calculator";
        double[] possibility;

        possibility = operator.getPossibility();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        /*
        for (int i = 1; i < side+1; i++){
            for (int o = 1; o < side+1; o++){
                int number = i+o;
                possibility[number] += 1;
                System.out.print(i + ", ");
                System.out.print(o + ", ");
                System.out.print(number + ", ");
                System.out.println(possibility[number]);

            }
        }
         */


        for (int i = 1; i < possibility.length; i++){
            if (possibility[i] != 0) {
                double chance = possibility[i];

                double probability = Precision.round((chance/ Arrays.stream(possibility).sum())*100,2);
                dataset.addValue(probability, "Probability", String.valueOf(i + operator.getBonus()));
            }
        }


        Chart chart = new Chart(title, "", expression, dataset);
        chart.showChart(800,640);

    }
}
