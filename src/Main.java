import repository.EcosystemRepository;
import repository.FileEcosystemRepository;
import repository.LogFileSimulationRepository;
import repository.SimulationRepository;
import service.*;
import entity.Ecosystem;

import java.util.List;
import java.util.Scanner;

import static util.InputValidator.getValidIntInput;

public class Main {

    private static EcosystemService service;
    private static Scanner scanner;
    private static Thread simulatorThread;
    private static boolean isRunning = true;

    public static void main(String[] args) {

        start();
    }

    private static void printMenu() {
        System.out.println("1. Create Ecosystem");
        System.out.println("2. Display Ecosystem");
        System.out.println("3. Edit Ecosystem");
        System.out.println("4. Delete Ecosystem");
        System.out.println("5. Display All Ecosystem Names");
        System.out.println("6. Exit");
    }

    public static void start() {
        EcosystemRepository repository = FileEcosystemRepository.getInstance();
        AnimalService animalService = new AnimalService();
        PlantService plantService = new PlantService();
        EnvironmentSettingsService environmentSettingsService = new EnvironmentSettingsService();
        PredictService predictService = new PredictService(service);
        service = new EcosystemService(repository, animalService, plantService, environmentSettingsService, predictService);
        scanner = new Scanner(System.in);
        SimulationRepository simulationRepository = LogFileSimulationRepository.getInstance();
        EcosystemsSimulatorService simulatorService = new EcosystemsSimulatorService(service, simulationRepository);
        simulatorThread = new Thread(simulatorService::startEcosystemsSimulator);
        simulatorThread.start();
        while (isRunning) {
            printMenu();
            int choice = getValidIntInput(scanner, "Choose an option: ", value -> 1 <= value && value <= 6);

            switch (choice) {
                case 1 -> createEcosystem();
                case 2 -> displayEcosystem();
                case 3 -> editEcosystem();
                case 4 -> deleteEcosystem();
                case 5 -> listAllEcosystems();
                case 6 -> {
                    System.out.println("Exiting...");
                    isRunning = false;
                    simulatorService.stopEcosystemsSimulator(); // Остановка симуляции
                    try {
                        simulatorThread.join(); // Ждем завершения потока симуляции
                    } catch (InterruptedException e) {
                        System.err.println("Error while waiting for simulation thread to finish: " + e.getMessage());
                    }
                }
            }
        }
    }

    private static void createEcosystem() {
        Ecosystem ecosystem = service.createEcosystem(scanner);
        service.saveEcosystem(ecosystem);
        System.out.println("Ecosystem created and saved: " + ecosystem.getName());
    }

    private static void displayEcosystem() {
        listAllEcosystems();
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
        listAllEcosystems();
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
        listAllEcosystems();
        System.out.print("Enter ecosystem name to delete: ");
        String name = scanner.nextLine();
        service.deleteEcosystem(name);
        System.out.println("Ecosystem deleted: " + name);
    }

    private static void listAllEcosystems() {
        List<String> ecosystemNames = service.getAllEcosystemNames();

        System.out.println("List of all ecosystems:");
        ecosystemNames.forEach(name -> System.out.println("- " + name));
    }
}