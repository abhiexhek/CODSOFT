package StudentGradeCalculator;
import java.util.Scanner;

public class StudentGradeCalculator {
    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\tWelcome To The Student Grade Calculator System\t");
        int subjectCount;
        while (true) {
            System.out.print("Enter the number of subjects: ");
            subjectCount = scanner.nextInt();
            if (subjectCount > 0) {
                break;
            } else {
                System.out.println("Error: Number of subjects must be at least 1. Try again.");
            }
        }

        double totalMarks = 0;
        for (int i = 1; i <= subjectCount; i++) {
            while (true) {
                System.out.print("Enter marks obtained in subject " + i + " (out of 100): ");
                int markObtained = scanner.nextInt();
                if (markObtained >= 0 && markObtained <= 100) {
                    totalMarks += markObtained;
                    break;
                } else {
                    System.out.println("Error: Marks must be between 0 and 100. Try again.");
                }
            }
        }

        System.out.println("\nResults:");
        System.out.println("Total Marks Obtained: " + totalMarks);

        double percentage = percentageCount(totalMarks, subjectCount);
        System.out.println("Average Percentage: " + percentage + "%");

        double grade = gradeCount(percentage);
        System.out.println("Grade (GPA): " + grade);

        System.out.println("\nThank you for using the Student Grade Calculator!");
        scanner.close();
    }

    static public double percentageCount(double totalMarks, double subjectCount) {
        return Math.round((totalMarks / subjectCount) * 100.0) / 100.0;
    }

    static public double gradeCount(double percentage) {
        return Math.round((percentage / 25) * 100.0) / 100.0;
    }
}

    /*
    Input: Take marks obtained (out of 100) in each subject.
    Calculate Total Marks: Sum up the marks obtained in all subjects.
    Calculate Average Percentage: Divide the total marks by the total number of subjects to get the
    average percentage.
    Grade Calculation: Assign grades based on the average percentage achieved.
    Display Results: Show the total marks, average percentage, and the corresponding grade to the user
     */
