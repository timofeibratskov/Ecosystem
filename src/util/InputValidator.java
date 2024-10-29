package util;

import java.util.Scanner;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

public class InputValidator {

    public static int getValidIntInput(Scanner scanner, String prompt, IntPredicate validator) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());

                // Проверка на превышение максимального значения для int
                if (value > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Number too large for int: " + value);
                }

                if (validator.test(value)) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static double getValidDoubleInput(Scanner scanner, String prompt, DoublePredicate validator) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());

                // Проверка на превышение максимального значения для double
                if (value > Double.MAX_VALUE) {
                    throw new IllegalArgumentException("Number too large for double: " + value);
                }

                if (validator.test(value)) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
