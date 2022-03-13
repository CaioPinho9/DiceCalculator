package com.github.caiopinho9.dicecalculator;

import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.commons.math3.util.Precision;

import javax.swing.*;
import java.util.Arrays;


public class Main {


    public static void main(String[] args) {
        String expression;
        expression = JOptionPane.showInputDialog("Say Expression");
        Operator operator = new Operator(expression);
        final String title = "Dice Calculator";
        double[] possibility;
        double[] probability = new double[500];
        double successDifficultyClassChance = 0;
        double failDifficultyClassChance = 100;
        boolean invertColor = false;


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

        for (int i = 1; i < possibility.length; i++) {
            double chance = possibility[i];
            probability[i] = Precision.round((chance / Arrays.stream(possibility).sum()) * 100, 2);
            if ((i + operator.getBonus())>=operator.getDifficultyClass()){
                successDifficultyClassChance += probability[i];
                failDifficultyClassChance -= probability[i];
            }
        }

        successDifficultyClassChance = Precision.round(successDifficultyClassChance,2);
        failDifficultyClassChance = Precision.round(failDifficultyClassChance,2);

        for (int i = 1; i < possibility.length; i++){
            if (possibility[i] != 0) {
                if (operator.getDifficultyClass() == 0) {
                    dataset.addValue(probability[i], "100%", String.valueOf(i + operator.getBonus()));

                } else if (i + operator.getBonus() < operator.getDifficultyClass()){
                    dataset.addValue(probability[i], failDifficultyClassChance +"%", String.valueOf(i + operator.getBonus()));
                    invertColor = true;

                } else {
                    dataset.addValue(probability[i], successDifficultyClassChance +"%", String.valueOf(i + operator.getBonus()));
                }


            }
        }

        Chart chart = new Chart(title, "", expression, dataset, invertColor);

        chart.showChart(1000,700);

    }
}
