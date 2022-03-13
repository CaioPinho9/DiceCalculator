package com.github.caiopinho9.dicecalculator;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Operator {

//    d20<d20 + 5
//    4d6 + 2d8 + 3
//    4d6~1
//    CD
private String expression;
    private String[] expandedExpression;
    private double[] possibility = new double[200];
    private int bonus = 0;
    private int difficultyClass;
    private double[] oldSummatory;
    private boolean finish = true;


    public Operator(String expression) {
        this.expression = expression;
        expand();

    }

    void expand(){

//      Ex: 4d6+2d8+3 -> 4d6/2d8/3
        boolean firstDice = true;
        expression = expression.replaceAll(" ", "");
        expression = expression.toUpperCase();

        expression = expression.replaceAll("CD", "DC");
        expression =  expression.replaceAll("DC", "DC");
        expression = expression.replaceAll("CA", "DC");
        expression =  expression.replaceAll("AC", "DC");

        if (expression.contains("DC")) {
            String[] tempExpression;
            tempExpression = expression.split("DC",2);
            expression = tempExpression[0];
            difficultyClass = Integer.parseInt(tempExpression[1]);
        }

        if (expression.contains("+")) {
            expandedExpression = expression.split("\\+", 12);
            for (int expressionNumber = 0; expressionNumber < expandedExpression.length; expressionNumber++) {

//
                if (expandedExpression[expressionNumber].contains("D") && firstDice) {
                    readDice(expressionNumber);
                    firstDice = false;

                } else if (expandedExpression[expressionNumber].contains("D") && !firstDice) {
                    String[] division = expandedExpression[expressionNumber].split("D", 2);
                    int times = Integer.parseInt(division[0]);
                    int side = Integer.parseInt(division[1]);
                    addDice(times, side, oldSummatory);

                } else {
                    bonus += Integer.parseInt(expandedExpression[expressionNumber]);
                }
            }
        } else {
            if (expression.contains("D")) {
                expandedExpression = new String[1];
                expandedExpression[0] = expression;
                readDice(0);
            } else if (expression.equals("3")) {
                JOptionPane.showMessageDialog (null, "Toma no Cu");

            } else {
                JOptionPane.showMessageDialog (null, "Error: No valid expression");

            }
        }
        finish();
    }

    private void finish() {
        if (finish) {
            for (int oldSummatoryIndex = 0; oldSummatoryIndex < oldSummatory.length; oldSummatoryIndex++) {
                if (oldSummatory[oldSummatoryIndex] != 0) {
                    possibility[(int) oldSummatory[oldSummatoryIndex]] += 1;
                }
            }
        }
    }

    private void addDice(int times, int side, double[] oldSummatory) {
        double[] newSummatory;
        int summatoryBackupIndex = 0;
        newSummatory = diceArray(times, side);
        double[] summatoryBackup = new double[oldSummatory.length*newSummatory.length+1];
        Arrays.fill(summatoryBackup, 0);
        Arrays.fill(possibility, 0);

        for (int oldSummatoryIndex = 0; oldSummatoryIndex < oldSummatory.length; oldSummatoryIndex++) {
            if (oldSummatory[oldSummatoryIndex] != 0) {

                for (int newSummatoryIndex = 0; newSummatoryIndex < newSummatory.length; newSummatoryIndex++) {
                    if (newSummatory[newSummatoryIndex] != 0) {
                        summatoryBackup[summatoryBackupIndex] = oldSummatory[oldSummatoryIndex] +
                                                                newSummatory[newSummatoryIndex];
                        summatoryBackupIndex++;
                    }
                }
            }
        }
        setOldSummatory(summatoryBackup);
    }

    private void readDice(int expressionNumber) {
        Dice dice = null;

//        Vantagem Ex: d20>d20
        if (expandedExpression[expressionNumber].contains(">")) {

//            Divide the expression in 3 parts, and check which one is just the side of a dice
            String[] division = expandedExpression[expressionNumber].split("D", 3);

            dice = new Dice(Integer.parseInt(division[2]));
            possibility = dice.doAdvantage(true);
            finish = false;

//        Desvantagem Ex: d20<d20
        } else if (expandedExpression[expressionNumber].contains("<")) {

//            Divide the expression in 3 parts, and check which one is just the side of a dice
            String[] division = expandedExpression[expressionNumber].split("D", 3);

            dice = new Dice(Integer.parseInt(division[2]));
            possibility = dice.doAdvantage(true);
            finish = false;
        } else if (expandedExpression[expressionNumber].contains("~")){

//        Soma Ex: 4d6
        } else {
            String[] division = expandedExpression[expressionNumber].split("D", 2);
            int times;
            if (!division[0].equals("")) {
                times = Integer.parseInt(division[0]);
            } else {
                times = 1;
            }

            int side = Integer.parseInt(division[1]);
            oldSummatory = diceArray(times, side);

        }
    }

    private double[] diceArray(int times, int side) {
        int[] diceArray = new int[times];
        double[] summatory = new double[(int) Math.pow(side,times)+1];
        Arrays.fill(summatory, 0);
        Arrays.fill(diceArray, 1);

//          [1,1,1,1]
        int lastIndex = 0;
        int summatoryIndex = 0;

        for (int index = 0; index < diceArray.length;) {

//            possibility[Arrays.stream(diceArray).sum()] += 1;

//                Saves to add later
            summatory[summatoryIndex] = Arrays.stream(diceArray).sum();
            summatoryIndex++;

//                Checks all index
            for (int check = 0; check<=lastIndex; check++) {

//                    When the array is completed, it breaks the loop Ex: 4d6 [6,6,6,6]
                if (Arrays.stream(diceArray).sum() == side*times) {
                    index = diceArray.length;
                    break;
                }

                if (index == lastIndex && diceArray[lastIndex] >= side) {
                    index = 0;
                    diceArray[lastIndex] = 1;
                    lastIndex++;
                    diceArray[lastIndex]++;
                    break;
                }



//                    When this array[index] is full it resets and increases the index
                if (diceArray[index] == side) {
                    diceArray[index] = 1;
                    index += 1;

//                    If it's not full the array increases
                } else {
                    diceArray[index]++;
                    index = 0;
                    break;
                }
            }
        }
        return summatory;
    }

    public int getBonus() {
        return bonus;
    }

    public double[] getPossibility() {
        return possibility;
    }

    public double[] getOldSummatory() {
        return oldSummatory;
    }

    public void setOldSummatory(double[] oldSummatory) {
        this.oldSummatory = oldSummatory;
    }

    public int getDifficultyClass() {
        return difficultyClass;
    }
}
