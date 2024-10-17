

import repository.EcosystemRepository;
import repository.FileEcosystemRepository;
import service.EcosystemService;
import entity.Ecosystem;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static EcosystemService service;
    private static Scanner scanner;

    public static void main(String[] args) {
        EcosystemRepository repository = FileEcosystemRepository.getInstance(); // Получаем единственный экземпляр
        service = EcosystemService.getInstance(repository); // Передаем его в EcosystemService
        scanner = new Scanner(System.in);

        start();
    }

    private static void printMenu() {
        System.out.println("1. Create Ecosystem");
        System.out.println("2. Display Ecosystem");
        System.out.println("3. Edit Ecosystem");
        System.out.println("4. Delete Ecosystem");
        System.out.println("5. Display All Ecosystems");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    public static void start() {
        while (true) {
            printMenu();
            int choice = getValidIntInput(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> createEcosystem();
                case 2 -> displayEcosystem();
                case 3 -> editEcosystem();
                case 4 -> deleteEcosystem();
                case 5 -> listAllEcosystems();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createEcosystem() {
        Ecosystem ecosystem = service.createEcosystem(scanner);
        service.saveEcosystem(ecosystem);
        System.out.println("Ecosystem created and saved: " + ecosystem.getName());
    }

    private static void displayEcosystem() {
        System.out.print("Enter ecosystem name to display: ");
        String name = scanner.nextLine();
        Ecosystem ecosystem = service.loadEcosystem(name);

        if (ecosystem != null) {
            System.out.println("Ecosystem found: ");
            System.out.println(ecosystem);
        } else {
            System.out.println("Ecosystem not found.");
        }
    }

    private static void editEcosystem() {
        System.out.print("Enter ecosystem name to edit: ");
        String name = scanner.nextLine();
        Ecosystem ecosystem = service.loadEcosystem(name);

        if (ecosystem != null) {
            System.out.println("Current ecosystem: " + ecosystem);
            service.editEcosystem(ecosystem, scanner);
            service.saveEcosystem(ecosystem);
            System.out.println("Ecosystem updated: " + ecosystem.getName());
        } else {
            System.out.println("Ecosystem not found.");
        }
    }

    private static void deleteEcosystem() {
        System.out.print("Enter ecosystem name to delete: ");
        String name = scanner.nextLine();
        service.deleteEcosystem(name);


    }

    private static void listAllEcosystems() {
        List<String> ecosystemNames = service.getAllEcosystemNames();

        if (ecosystemNames.isEmpty()) {
            System.out.println("No ecosystems found.");
        } else {
            System.out.println("List of all ecosystems:");
            for (String name : ecosystemNames) {
                System.out.println("- " + name);
            }
        }
    }

    private static int getValidIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}