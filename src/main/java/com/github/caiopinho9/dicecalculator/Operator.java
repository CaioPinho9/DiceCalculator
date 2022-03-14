package com.github.caiopinho9.dicecalculator;

import javax.swing.*;
import java.util.Arrays;

public class Operator {

//    d20<d20 + 5
//    4d6 + 2d8 + 3
//    4d6~1
//    CD
private String expression;
    private String[] expandedExpression;
    private final double[] possibility = new double[200];
    private int bonus = 0;
    private int difficultyClass;
    private int reduceTimes = 0;
    private double[] oldSummatory;
    private boolean advantage = false;
    private boolean disadvantage = false;
    private boolean d100 = false;

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

        } else if (expression.contains("D")) {
            expandedExpression = new String[1];
            expandedExpression[0] = expression;
            readDice(0);

        } else if (expression.matches("[0-9]+")) {
            oldSummatory = new double[1];
            expression = expression.replaceAll("\\D+", "");
            oldSummatory[0] = Double.parseDouble(expression);

        } else {
            JOptionPane.showMessageDialog(null,"Error: No valid expression.");

        }
        finish();
    }

    private void finish() {
        for (int oldSummatoryIndex = 0; oldSummatoryIndex < oldSummatory.length; oldSummatoryIndex++) {
            if (oldSummatory[oldSummatoryIndex] != 0) {
                possibility[(int) oldSummatory[oldSummatoryIndex]] += 1;
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

//        2>d20 3<d20 4>d100 4d6~1

//        Advantage Ex: d20>d20
        if (expandedExpression[expressionNumber].contains(">") || expandedExpression[expressionNumber].contains("<")) {

            advantage = true;
            if (expandedExpression[expressionNumber].contains("<")) {
                disadvantage = true;
            }

            expandedExpression[expressionNumber] = expandedExpression[expressionNumber].replace(">", "");
            expandedExpression[expressionNumber] = expandedExpression[expressionNumber].replace("<", "");

//            Divide the expression in 2 parts, and check which one is just the side of a dice
            String[] division = expandedExpression[expressionNumber].split("D", 2);
            int times = Integer.parseInt(division[0]);
            int sides = Integer.parseInt(division[1]);
            oldSummatory = diceArray(times, sides);

        } else if (expandedExpression[expressionNumber].contains("~")){
            String[] reduce = expandedExpression[expressionNumber].split("~",2);
            String[] division = reduce[0].split("D", 2);

            advantage = true;

            int times = Integer.parseInt(division[0]);
            int sides = Integer.parseInt(division[1]);
            reduceTimes = Integer.parseInt(reduce[1])+1;
            oldSummatory = diceArray(times, sides);

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

        if (side == 100) {
            d100 = true;
        }

//          [1,1,1,1]
        int lastIndex = 0;
        int summatoryIndex = 0;

        for (int index = 0; index < diceArray.length;) {

//            possibility[Arrays.stream(diceArray).sum()] += 1;

//                Saves to add
            if (advantage) {
                summatory[summatoryIndex] = advantage(diceArray, times);
            } else {
                summatory[summatoryIndex] = Arrays.stream(diceArray).sum();
            }
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

    private int advantage(int[] diceArray, int times) {
        int[] sortedDiceArray = Arrays.stream(diceArray).sorted().toArray();
        int sum = 0;
        if (reduceTimes != 0) {
            times = reduceTimes;
        }
//      Advantage
        if (!disadvantage) {
            for (int i = 0; i<times-1; i++) {
                sortedDiceArray[i] = 0;
            }
//      Disadvantage
        } else {
            for (int i = 0; i<times-1; i++) {
                int o = sortedDiceArray.length-i-1;
                sortedDiceArray[o] = 0;

            }
        }

        for (int summatoryIndex = 0; summatoryIndex < sortedDiceArray.length; summatoryIndex++) {
            sum = Arrays.stream(sortedDiceArray).sum();
        }
        return sum;
    }

    public int getBonus() {
        return bonus;
    }

    public double[] getPossibility() {
        return possibility;
    }

    public void setOldSummatory(double[] oldSummatory) {
        this.oldSummatory = oldSummatory;
    }

    public int getDifficultyClass() {
        return difficultyClass;
    }

    public boolean isD100() {
        return d100;
    }
}
