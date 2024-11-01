package service;

import entity.Animal;
import entity.Ecosystem;
import enums.AnimalType;
import util.InputValidator;

import java.util.Scanner;



public class AnimalService {

   public AnimalService() {}

    public void manageAnimals(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Manage animals (add/edit/remove)? (a/e/r/n): ");
            String action = scanner.nextLine().toLowerCase();
            switch (action) {
                case "a":
                    addAnimal(ecosystem, scanner);
                    break;
                case "e":
                    editAnimal(ecosystem, scanner);
                    break;
                case "r":
                    removeAnimal(ecosystem, scanner);
                    break;
                case "n":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addAnimal(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter animal name: ");
        String animalName = scanner.nextLine();
        int population = InputValidator.getValidIntInput(scanner, "Enter quantity: ",value-> value>=0);
        AnimalType type = getAnimalType(scanner);
        Animal animal = new Animal(animalName, population, type);
        ecosystem.addAnimal(animal);
    }

    private void editAnimal(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter animal name to edit: ");
        String animalName = scanner.nextLine();
        Animal animal = ecosystem.getAnimal(animalName);
        if (animal != null) {
            System.out.print("Enter new name (leave empty to not change): ");
            String newAnimalName = scanner.nextLine();
            if (!newAnimalName.isEmpty()) {
                animal.setName(newAnimalName);
            }
            int newPopulation = InputValidator.getValidIntInput(scanner, "Enter new quantity: ",value-> value>=0);
            animal.setPopulation(newPopulation);
            System.out.println("Animal updated: " + animal);
        } else {
            System.out.println("Animal not found.");
        }
    }

    private void removeAnimal(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter animal name to remove: ");
        String animalName = scanner.nextLine();
        ecosystem.deleteAnimal(animalName);
        System.out.println("Animal removed: " + animalName);
    }

    private AnimalType getAnimalType(Scanner scanner) {
        while (true) {
            System.out.print("Is the animal herbivorous or carnivorous? (h/c): ");
            String type = scanner.nextLine().toLowerCase();
            if (type.equals("h")) return AnimalType.HERBIVORE;
            if (type.equals("c")) return AnimalType.CARNIVORE;
            System.out.println("Invalid input. Please enter 'h' or 'c'.");
        }
    }
}
