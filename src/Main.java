import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import repository.EcosystemRepository;
import repository.FileEcosystemRepository;
import service.EcosystemService;

import java.util.Scanner;

public class Main {
    private static EcosystemService service;
    private static Scanner scanner;

    public static void main(String[] args) {
        EcosystemRepository repository = new FileEcosystemRepository();
        service = new EcosystemService(repository);
        scanner = new Scanner(System.in);

        start(); // Запускаем основной метод
    }

    public static void start() {
        while (true) {
            System.out.println("1. Create Ecosystem");
            System.out.println("2. Save Ecosystem");
            System.out.println("3. Load Ecosystem");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createEcosystem();
                    break;
                case 2:
                    saveEcosystem();
                    break;
                case 3:
                    loadEcosystem();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createEcosystem() {
        System.out.print("Enter ecosystem name: ");
        String name = scanner.nextLine();
        Ecosystem ecosystem = new Ecosystem(name);

        while (true) {
            System.out.print("Add animal? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter animal name: ");
                String animalName = scanner.nextLine();
                System.out.print("Enter population: ");
                int population = Integer.parseInt(scanner.nextLine());
                ecosystem.addAnimal(new Animal(animalName, population));
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Add plant? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter plant name: ");
                String plantName = scanner.nextLine();
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                ecosystem.addPlant(new Plant(plantName, quantity));
            } else {
                break;
            }
        }

        System.out.println("Ecosystem created: " + ecosystem);
    }

    private static void saveEcosystem() {
        System.out.print("Enter ecosystem name: ");
        String name = scanner.nextLine();
        // Здесь предположим, что экосистема уже создана, и мы можем её сохранить
        // На практике, нужно хранить ссылку на текущую экосистему
        Ecosystem ecosystem = new Ecosystem(name); // Замените на реальную экосистему
        service.saveEcosystem(name, ecosystem);
        System.out.println("Ecosystem saved: " + name);
    }

    private static void loadEcosystem() {
        System.out.print("Enter ecosystem name: ");
        String name = scanner.nextLine();
        Ecosystem ecosystem = service.loadEcosystem(name);
        System.out.println("Loaded ecosystem: " + ecosystem);
    }
}