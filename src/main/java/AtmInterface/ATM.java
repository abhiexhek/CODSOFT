package AtmInterface;

import java.io.*;
import java.util.*;

public class ATM {
    static final String FILE_NAME = "database.txt";
    Scanner scanner = new Scanner(System.in);
    private static final int BASE_DELAY_MS = 1000;
    private static final int DELAY_PER_1000 = 500;

    public void menuOption() {
        System.out.println("\n==== ATM Menu ====");
        System.out.println("After Registration We Think Atm Card Is Entered.\n");
        System.out.println("1. Register Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Delete Account");
        System.out.println("6. Exit");
    }

    public void depositMoney() {
        while (true) {
            try {
                String pin = getVerifiedPin();
                if (pin == null) return;

                int balance = getAccountBalance(pin);
                System.out.println("Current Balance: " + balance);
                System.out.print("Enter amount to deposit (or 0 to cancel): ");
                int amount = getPositiveIntegerInput();

                if (amount == 0) {
                    System.out.println("Deposit cancelled.");
                    return;
                }

                int delay = calculateTransactionDelay(amount);
                System.out.printf("Processing deposit (will take ~%d seconds)...\n", delay/1000);
                Thread.sleep(delay);

                updateBalance(pin, amount, true);
                return;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Try again? (yes/no): ");
                if (!scanner.next().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }


    public void withDrawMoney() {
        while (true) {
            try {
                String pin = getVerifiedPin();
                if (pin == null) return;

                int balance = getAccountBalance(pin);
                System.out.println("Available Balance: " + balance);
                System.out.print("Enter amount to withdraw (or 0 to cancel): ");
                int amount = getPositiveIntegerInput();

                if (amount == 0) {
                    System.out.println("Withdrawal cancelled.");
                    return;
                }

                if (amount > balance) {
                    System.out.println("Error: Insufficient funds.");
                    continue;
                }

                int delay = calculateTransactionDelay(amount);
                System.out.printf("Processing withdrawal (will take ~%d seconds)...\n", delay/1000);
                Thread.sleep(delay);

                updateBalance(pin, amount, false);
                return;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Try again? (yes/no): ");
                if (!scanner.next().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private int calculateTransactionDelay(int amount) {
        return BASE_DELAY_MS + (amount / 1000) * DELAY_PER_1000;
    }

    public void checkBalance() {
        while (true) {
            try {
                String pin = getVerifiedPin();
                if (pin == null) return;

                int balance = getAccountBalance(pin);
                System.out.println("Current Balance: " + balance);
                return;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Try again? (yes/no): ");
                if (!scanner.next().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    public void deleteAccount() {
        while (true) {
            try {
                String pin = getVerifiedPin();
                if (pin == null) return;

                int balance = getAccountBalance(pin);
                if (balance > 0) {
                    System.out.println("Current Balance: " + balance);
                    System.out.println("You must withdraw your remaining balance before deleting your account.");
                    System.out.print("Would you like to withdraw it now? (yes/no): ");
                    if (scanner.next().equalsIgnoreCase("yes")) {
                        System.out.printf("Processing withdrawal of %d...\n", balance);
                        Thread.sleep(calculateTransactionDelay(balance));
                        updateBalance(pin, balance, false);
                    } else {
                        System.out.println("Account deletion cancelled.");
                        return;
                    }
                }


                String fullName = getAccountName(pin);
                System.out.print("Enter your full name to confirm account deletion: ");
                scanner.nextLine();
                String inputName = scanner.nextLine().trim();

                if (!inputName.equalsIgnoreCase(fullName)) {
                    System.out.println("Error: Name does not match account records.");
                    continue;
                }

                if (confirmAction("Are you sure you want to delete your account?")) {
                    if (removeAccountFromDatabase(pin)) {
                        System.out.println("Account deleted successfully.");
                        return;
                    } else {
                        System.out.println("Error: Failed to delete account.");
                        continue;
                    }
                } else {
                    System.out.println("Account deletion cancelled.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Try again? (yes/no): ");
                if (!scanner.next().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private boolean removeAccountFromDatabase(String pin) {
        File file = new File(FILE_NAME);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(pin)) {
                    found = true;
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to access account database.");
            return false;
        }

        if (!found) {
            System.out.println("No account found with the given PIN.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error: Unable to update account database.");
            return false;
        }
    }

    private int getAccountBalance(String pin) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(pin)) {
                    return Integer.parseInt(parts[2]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error: Unable to check account balance.");
        }
        return 0;
    }

    private String getAccountName(String pin) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(pin)) {
                    return parts[0];
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to access account information.");
        }
        return "";
    }

    private void updateBalance(String pin, int amount, boolean isDeposit) {
        File file = new File(FILE_NAME);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(pin)) {
                    found = true;
                    String username = parts[0];
                    int balance = Integer.parseInt(parts[2]);

                    if (!isDeposit && amount > balance) {
                        System.out.println("Insufficient balance.");
                        updatedLines.add(line);
                    } else {
                        int updatedBalance = 0;
                        if(isDeposit){
                            updatedBalance = balance+amount;
                        }else {
                            updatedBalance = balance-amount;
                        }
                        updatedLines.add(username + "," + pin + "," + updatedBalance);
                        System.out.println((isDeposit ? "Deposited" : "Withdrawn") + " " + amount + ". New Balance: " + updatedBalance);
                    }
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to access account database.");
            return;
        }

        if (!found) {
            System.out.println("No account found with the given PIN.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to update account balance.");
        }
    }

    public void registerAccount() {
        while (true) {
            try {
                System.out.print("Enter your full name (first name and surname, or 0 to cancel): ");
                scanner.nextLine();
                String userName = scanner.nextLine().trim();

                if (userName.equals("0")) {
                    System.out.println("Registration cancelled.");
                    return;
                }

                if (!userName.contains(" ") || userName.split(" ").length < 2) {
                    System.out.println("Error: Please enter both first name and surname.");
                    continue;
                }

                System.out.print("Create a 4-digit PIN (or 0 to cancel): ");
                String pinCode = scanner.next();

                if (pinCode.equals("0")) {
                    System.out.println("Registration cancelled.");
                    return;
                }

                if (!isValidPin(pinCode)) {
                    System.out.println("Error: PIN must be exactly 4 digits.");
                    continue;
                }

                if (isUserExists(userName, pinCode)) {
                    System.out.println("Error: Username and PIN combination already exists.");
                    continue;
                }

                int balance = 0;
                addUser(userName, pinCode, balance);
                return;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Try again? (yes/no): ");
                if (!scanner.next().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private boolean isValidPin(String pin) {
        return pin.matches("\\d{4}");
    }

    public boolean isUserExists(String username, String pinCode) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equalsIgnoreCase(username) && parts[1].equals(pinCode)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to check existing accounts.");
        }
        return false;
    }

    public void addUser(String username, String pinCode, int balance) {
        if (isUserExists(username, pinCode)) {
            System.out.println("Error: Username and PIN combination already exists.");
            return;
        }

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(username + "," + pinCode + "," + balance + "\n");
            System.out.println("User registered successfully.");
        } catch (IOException e) {
            System.out.println("Error: Unable to register user.");
        }
    }

    private String getVerifiedPin() {
        int attempts = 0;
        while (attempts < 5) {
            System.out.print("Enter your 4-digit PIN: ");
            String pin = scanner.next();

            if (isValidPin(pin) && isPinExists(pin)) {
                return pin;
            } else {
                attempts++;
                System.out.println("Incorrect PIN or invalid format. Attempts left: " + (5 - attempts));
            }
        }

        System.out.println("Too many failed attempts. Please wait 60 seconds.");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            System.out.println("Error: Unable to enforce wait time.");
        }
        return null;
    }

    private boolean isPinExists(String pin) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(pin)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to verify PIN.");
        }
        return false;
    }

    private boolean confirmAction(String message) {
        System.out.print(message + " (yes/no): ");
        String confirmation = scanner.next().toLowerCase();
        return confirmation.equals("yes");
    }

    private int getPositiveIntegerInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                } else {
                    System.out.print("Please enter a positive number (or 0 to cancel): ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number (or 0 to cancel): ");
                scanner.next();
            }
        }
    }
}