package com.github.caiopinho9.dicecalculator;

public class Dice {
    int side;
    double[] possibility;

    public Dice(int side) {
        this.side = side;
        possibility = new double[side*side];

    }

    public double[] doAdvantage(boolean advantage) {
        //This part makes the first part of the combination, like: [1,x],[2,x],[3,x]
        //When x = side, the first number changes to the next
        for (int firstNumber = 1; firstNumber <= side; firstNumber++) {

            //This part makes the second part of the combination, [y,1],[y,2],[y,3]
            for (int secondNumber = 1; secondNumber <= side; secondNumber++) {

                // Advantage
                if (firstNumber >= secondNumber && advantage) {
                    possibility[firstNumber] += 1;

                } else if (advantage){
                    possibility[secondNumber] += 1;

                // Disadvantage
                } else if (firstNumber <= secondNumber) {
                    possibility[firstNumber] += 1;

                } else {
                    possibility[secondNumber] += 1;
                }
                /*
                System.out.print(firstNumber + ", ");
                System.out.print(secondNumber + ", ");
                System.out.print(number + ", ");
                System.out.println(possibility[number]);
                */
            }
        }
        return possibility;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}
