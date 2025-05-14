package NumberGame;

import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class NumberGame {
    static Scanner scanner = new Scanner(System.in);
    static int totalRounds = 0;
    static int totalWins = 0;

    public static void main(String[] args) {
        System.out.println("\tWelcome to the Number Game!\t");
        gameMechanism();

        boolean playAgain = true;
        while (playAgain) {
            totalRounds++;
            playRound();

            System.out.println("Want to play another game? If yes, type 1.");
            try {
                int anotherGame = scanner.nextInt();
                if (anotherGame != 1) {
                    playAgain = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Exiting game.");
                playAgain = false;
            }
        }

        System.out.println("\nGame Summary:");
        System.out.println("Total rounds played: " + totalRounds);
        System.out.println("Total wins: " + totalWins);
        System.out.println("Thanks for playing!");
    }

    public static void playRound() {
        Random random = new Random();
        final int randomNumber = random.nextInt(100) + 1;
        System.out.println("\nA new random number between 1-100 has been generated.");

        int limit = setLimit();
        int guessCount = 0;
        boolean guessedCorrectly = false;

        while (limit > 0 && !guessedCorrectly) {
            System.out.println("\nYou have " + limit + " attempts remaining.");
            System.out.println("Guess the random number:");

            try {
                int guess = scanner.nextInt();
                guessCount++;
                limit--;

                if (guess < randomNumber) {
                    System.out.println("Your guess is too low.");
                } else if (guess > randomNumber) {
                    System.out.println("Your guess is too high.");
                } else {
                    System.out.println("Congratulations! Your guess is correct.");
                    System.out.println("You took " + guessCount + " attempts to guess the number.");
                    totalWins++;
                    guessedCorrectly = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                scanner.next();
            }

            if (limit == 0 && !guessedCorrectly) {
                System.out.println("\nSorry, you've used all your attempts.");
                System.out.println("The correct number was: " + randomNumber);
            }
        }
    }

    public static void gameMechanism() {
        System.out.println("\nHow to play:");
        System.out.println("1. A random number is generated between 1-100.");
        System.out.println("2. You have to guess the number in limited attempts.");
        System.out.println("3. If your guess is higher than the number, you'll be told it's too high.");
        System.out.println("4. If your guess is lower than the number, you'll be told it's too low.");
        System.out.println("5. Try to guess the number before you run out of attempts!\n");
    }

    public static int setLimit() {
        while (true) {
            System.out.println("Please choose your attempt limit:");
            try {
                int limit = scanner.nextInt();
                if (limit <= 0) {
                    System.out.println("Please enter a positive number");
                    continue;
                }
                return limit;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.next();
            }
        }
    }
}