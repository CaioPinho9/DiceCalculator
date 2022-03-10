package com.github.caiopinho9.dicecalculator;

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
    private double[] summatory = new double[5000];
    private int bonus = 0;


    public Operator(String expression) {
        this.expression = expression;
        expand();

    }

    void expand(){

//      Ex: 4d6 + 2d8 + 3 -> 4d6/+/2d8/+/3
        expandedExpression = expression.split(" ", 12);
        for (int expressionNumber = 0; expressionNumber<expandedExpression.length; expressionNumber++){

//
            if (expandedExpression[expressionNumber].contains("d")){
                readDice(expressionNumber);
            }

            if (expandedExpression[expressionNumber].contains("+")){
                if (expandedExpression[expressionNumber+1].contains("d")) {
                    expressionNumber++;
                    String[] division = expandedExpression[expressionNumber].split("d", 2);
                    int times = Integer.parseInt(division[0]);
                    int side = Integer.parseInt(division[1]);
                    addDice(times, side);

                } else {
                    bonus += Integer.parseInt(expandedExpression[expressionNumber+1]);
                }
            }
        }
    }

    private void addDice(int times, int side) {
        double[] summatoryBackup = new double[5000];
        Arrays.fill(possibility, 0);

        for (int summatoryIndex = 0; summatoryIndex < summatory.length; summatoryIndex++) {
            if (summatory[summatoryIndex] != 0) {
                int[] diceArray = new int[times];


//          [1,1,1,1]
                Arrays.fill(diceArray, 1);
                int lastIndex = 0;

                for (int index = 0; index < diceArray.length; ) {
                    double sum = Arrays.stream(diceArray).sum() + summatory[summatoryIndex];
                    System.out.println(summatory[summatoryIndex]);
                    System.out.println(sum);
                    possibility[(int) sum] += 1;

//                Saves to add later
                    summatoryBackup[summatoryIndex] = sum;


//                Checks all index
                    for (int check = 0; check <= lastIndex; check++) {

//                    When the array is completed, it breaks the loop Ex: 4d6 [6,6,6,6]
                        if (Arrays.stream(diceArray).sum() == side * times) {
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
            }
            summatoryIndex++;
        }
        summatory = summatoryBackup;
    }

    private void readDice(int expressionNumber) {
        Dice dice = null;

//        Vantagem Ex: d20>d20
        if (expandedExpression[expressionNumber].contains(">")) {

//            Divide the expression in 3 parts, and check which one is just the side of a dice
            String[] division = expandedExpression[expressionNumber].split("d", 3);

            dice = new Dice(Integer.parseInt(division[2]));
            possibility = dice.doAdvantage(true);

//        Desvantagem Ex: d20<d20
        } else if (expandedExpression[expressionNumber].contains("<")) {

//            Divide the expression in 3 parts, and check which one is just the side of a dice
            String[] division = expandedExpression[expressionNumber].split("d", 3);

            dice = new Dice(Integer.parseInt(division[2]));
            possibility = dice.doAdvantage(true);
        } else if (expandedExpression[expressionNumber].contains("~")){

//        Soma Ex: 4d6
        } else {
            String[] division = expandedExpression[expressionNumber].split("d", 2);

            int times = Integer.parseInt(division[0]);
            int side = Integer.parseInt(division[1]);
            int[] diceArray = new int[times];
            Arrays.fill(summatory, 0);

//          [1,1,1,1]
            int lastIndex = 0;
            int summatoryIndex = 0;

            for (int index = 0; index < diceArray.length;) {

                possibility[Arrays.stream(diceArray).sum()] += 1;

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
        }
    }

    public int getBonus() {
        return bonus;
    }

    public double[] getPossibility() {
        return possibility;
    }
}
