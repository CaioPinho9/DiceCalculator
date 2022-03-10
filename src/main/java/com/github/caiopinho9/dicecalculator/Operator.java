package com.github.caiopinho9.dicecalculator;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Operator {

//    d20<d20 + 5
//    4d6 + 2d8 + 3
//    4d6~1
//    CD
    String expression;
    String[] expandedExpression;
    double[] possibility;

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
                addDice();
                if (expandedExpression[expressionNumber].contains("d")){
                    readDice(expressionNumber);
                }
            }

        }

    }

    private void addDice() {
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
            dice = new Dice(side);
            possibility = new double[(side*times)+1];

//          [1,1,1,1]
            Arrays.fill(diceArray,1);
            int lastIndex = 0;
            for (int index = 0; index < diceArray.length;) {

                possibility[Arrays.stream(diceArray).sum()] += 1;

//                Checks all index
                for (int check = 0; check<=lastIndex; check++) {

//
                    if (index == lastIndex && diceArray[lastIndex] >= side) {
                        index = 0;
                        diceArray[lastIndex] = 1;
                        lastIndex++;
                        diceArray[lastIndex]++;
                        break;
                    }

//                    When the array is completed, it breaks the loop Ex: 4d6 [6,6,6,6]
                    if (Arrays.stream(diceArray).sum() == side*times) {
                        index = diceArray.length;
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
}
