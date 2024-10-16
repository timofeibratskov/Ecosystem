package service;

import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import repository.EcosystemRepository;

import java.util.Scanner;

public class EcosystemService {
    private final EcosystemRepository  REPOSITORY;

    public EcosystemService(EcosystemRepository repository) {
        this.REPOSITORY = repository;
    }

    // Метод для создания экосистемы
    public Ecosystem createEcosystem(Scanner scanner) {
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
        return ecosystem;
    }

    // Метод для сохранения экосистемы
    public void saveEcosystem(String name, Ecosystem ecosystem) {
        REPOSITORY.save(name, ecosystem);
        System.out.println("Ecosystem saved: " + name);
    }

    // Метод для загрузки экосистемы
    public Ecosystem loadEcosystem(String name) {
        Ecosystem ecosystem = REPOSITORY.load(name);
        System.out.println("Loaded ecosystem: " + ecosystem);
        return ecosystem;
    }
}