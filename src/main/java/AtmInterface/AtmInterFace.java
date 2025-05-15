package AtmInterface;

import java.util.InputMismatchException;

public class AtmInterFace {
    public static void main(String[] args) {
        ATM atm = new ATM();
        int choice;
        do {
            try {
                atm.menuOption();
                choice = atm.scanner.nextInt();
                switch (choice) {
                    case 1:
                        atm.registerAccount();
                        break;
                    case 2:
                        atm.depositMoney();
                        break;
                    case 3:
                        atm.withDrawMoney();
                        break;
                    case 4:
                        atm.checkBalance();
                        break;
                    case 5:
                        atm.deleteAccount();
                        break;
                    case 6:
                        System.out.println("Exiting. Thank you for using ATM.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number between 1-6.");
                atm.scanner.next(); // clear invalid input
                choice = 0;
            }
        } while (choice != 6);
    }
}