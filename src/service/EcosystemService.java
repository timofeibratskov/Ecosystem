package service;

import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import repository.EcosystemRepository;

import java.util.List;
import java.util.Scanner;

public class EcosystemService {
    private final EcosystemRepository repository;
    private static EcosystemService instance;


    private EcosystemService(EcosystemRepository repository) {
        this.repository = repository;
    }


    public static EcosystemService getInstance(EcosystemRepository repository) {
        if (instance == null) {
            synchronized (EcosystemService.class) {
                if (instance == null) {
                    instance = new EcosystemService(repository);
                }
            }
        }
        return instance;
    }

    public Ecosystem createEcosystem(Scanner scanner) {
        System.out.print("Enter ecosystem name: ");
        String name = scanner.nextLine();
        Ecosystem ecosystem = new Ecosystem(name);
        addAnimals(scanner, ecosystem);
        addPlants(scanner, ecosystem);
        return ecosystem;
    }

    public void editEcosystem(Ecosystem ecosystem, Scanner scanner) {
        System.out.println("Editing ecosystem: " + ecosystem.getName());


        System.out.print("Do you want to change the ecosystem name? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter new ecosystem name: ");
            String newName = scanner.nextLine();
            ecosystem.setName(newName);
            System.out.println("Ecosystem name changed to: " + newName);
        }


        editAnimals(ecosystem, scanner);

        editPlants(ecosystem, scanner);
    }

    private void editAnimals(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Do you want to edit an animal? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter animal name to edit: ");
                String animalName = scanner.nextLine();

                Animal animal = ecosystem.getAnimalByName(animalName);
                if (animal != null) {
                    System.out.print("Enter new name for " + animal.getName() + " (leave blank to keep the same): ");
                    String newAnimalName = scanner.nextLine();
                    if (!newAnimalName.isEmpty()) {
                        animal.setName(newAnimalName);
                    }

                    int newPopulation = getValidIntInput(scanner, "Enter new population for " + animal.getName() + ": ");
                    animal.setPopulation(newPopulation);
                    System.out.println("Animal updated: " + animal);
                } else {
                    System.out.println("Animal not found.");
                }
            } else {
                break;
            }
        }


        removeAnimals(ecosystem, scanner);
    }

    private void editPlants(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Do you want to edit a plant? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter plant name to edit: ");
                String plantName = scanner.nextLine();

                Plant plant = ecosystem.getPlantByName(plantName);
                if (plant != null) {
                    System.out.print("Enter new name for " + plant.getName() + " (leave blank to keep the same): ");
                    String newPlantName = scanner.nextLine();
                    if (!newPlantName.isEmpty()) {
                        plant.setName(newPlantName);
                    }

                    int newQuantity = getValidIntInput(scanner, "Enter new quantity for " + plant.getName() + ": ");
                    plant.setQuantity(newQuantity);
                    System.out.println("Plant updated: " + plant);
                } else {
                    System.out.println("Plant not found.");
                }
            } else {
                break;
            }
        }

        removePlants(ecosystem, scanner);
    }

    private void removeAnimals(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Do you want to remove an animal? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter animal name to remove: ");
                String animalName = scanner.nextLine();
                boolean removed = ecosystem.removeAnimal(animalName);
                if (removed) {
                    System.out.println("Animal removed: " + animalName);
                } else {
                    System.out.println("Animal not found.");
                }
            } else {
                break;
            }
        }
    }

    private void removePlants(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Do you want to remove a plant? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter plant name to remove: ");
                String plantName = scanner.nextLine();
                boolean removed = ecosystem.removePlant(plantName);
                if (removed) {
                    System.out.println("Plant removed: " + plantName);
                } else {
                    System.out.println("Plant not found.");
                }
            } else {
                break;
            }
        }
    }

    private void addAnimals(Scanner scanner, Ecosystem ecosystem) {
        while (true) {
            System.out.print("Add animal? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter animal name: ");
                String animalName = scanner.nextLine();
                int population = getValidIntInput(scanner, "Enter population: ");
                ecosystem.addAnimal(new Animal(animalName, population));
            } else {
                break;
            }
        }
    }

    private void addPlants(Scanner scanner, Ecosystem ecosystem) {
        while (true) {
            System.out.print("Add plant? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.print("Enter plant name: ");
                String plantName = scanner.nextLine();
                int quantity = getValidIntInput(scanner, "Enter quantity: ");
                ecosystem.addPlant(new Plant(plantName, quantity));
            } else {
                break;
            }
        }
    }

    public void saveEcosystem(Ecosystem ecosystem) {
        repository.save(ecosystem);
    }

    public Ecosystem loadEcosystem(String name) {
        return repository.load(name);
    }

    public void deleteEcosystem(String name) {
        repository.delete(name);
    }

    public List<String> getAllEcosystemNames() {
        return repository.getAllEcosystems();
    }

    private int getValidIntInput(Scanner scanner, String prompt) {
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