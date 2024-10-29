package service;

import entity.Ecosystem;
import entity.Plant;
import util.InputValidator;

import java.util.Scanner;

public class PlantService {
    private static PlantService instance;

    private PlantService() {
    }

    public static PlantService getInstance() {
        if (instance == null) {
            instance = new PlantService();
        }
        return instance;
    }

    public void managePlants(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("manage plants  (add/edit/remove)? (a/e/r/n): ");
            String action = scanner.nextLine().toLowerCase();
            switch (action) {
                case "a":
                    addPlant(ecosystem, scanner);
                    break;
                case "e":
                    editPlant(ecosystem, scanner);
                    break;
                case "r":
                    removePlant(ecosystem, scanner);
                    break;
                case "n":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addPlant(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter the name of the plant: ");
        String plantName = scanner.nextLine();
        int quantity = InputValidator.getValidIntInput(scanner, "Enter the quantity: ", value -> value >= 0);
        Plant plant = new Plant(plantName, quantity);
        ecosystem.addPlant(plant);
    }

    private void editPlant(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter the name of the plant to edit: ");
        String plantName = scanner.nextLine();
        Plant plant = ecosystem.getPlant(plantName);
        if (plant != null) {
            System.out.print("Enter a new name (leave blank to keep the same): ");
            String newPlantName = scanner.nextLine();
            if (!newPlantName.isEmpty()) {
                plant.setName(newPlantName);
            }
            int newQuantity = InputValidator.getValidIntInput(scanner, "Enter the new quantity: ", value -> value >= 0);
            plant.setQuantity(newQuantity);
            System.out.println("Plant updated: " + plant);
        } else {
            System.out.println("Plant not found.");
        }
    }

    private void removePlant(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Enter the name of the plant to remove: ");
        String plantName = scanner.nextLine();
        ecosystem.deletePlant(plantName);
        System.out.println("Plant removed: " + plantName);
    }
}
