package service;

import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import repository.EcosystemRepository;
import util.InputValidator;
import java.util.List;
import java.util.Scanner;

import static enums.AnimalType.CARNIVORE;
import static enums.AnimalType.HERBIVORE;

public class EcosystemService {

    private final EcosystemRepository ecosystemRepository;
    private final AnimalService animalService;
    private final PlantService plantService;
    private final EnvironmentSettingsService environmentSettingsService;
    private final PredictService predictService;

    public EcosystemService(EcosystemRepository ecosystemRepository,
                            AnimalService animalService,
                            PlantService plantService,
                            EnvironmentSettingsService environmentSettingsService,
                            PredictService predictService) {
        this.ecosystemRepository = ecosystemRepository;
        this.animalService = animalService;
        this.plantService = plantService;
        this.environmentSettingsService = environmentSettingsService;
        this.predictService = predictService;
    }



    public Ecosystem createEcosystem(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter the name of the ecosystem: ");
            name = scanner.nextLine();
            List<String> ecosystemNames = getAllEcosystemNames();
            if (ecosystemNames.contains(name)) {
                System.out.println("Ecosystem already exists!");
            } else {
                break;
            }
        }
        Ecosystem ecosystem = new Ecosystem(name);

        animalService.manageAnimals(ecosystem, scanner);
        plantService.managePlants(ecosystem, scanner);

        return ecosystem;
    }

    public void editEcosystem(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.println("Editing ecosystem: " + ecosystem.getName());
            System.out.println("""
                    1. Change ecosystem name
                    2. Manage animals
                    3. Manage plants
                    4. Manage environmentSettings
                    5. Check predicts
                    6. Exit""");

            int choice = InputValidator.getValidIntInput(scanner, "Choose an option: ", value -> 1 <= value && value <= 6);

            switch (choice) {
                case 1:
                    editEcosystemName(ecosystem, scanner);
                    break;
                case 2:
                    animalService.manageAnimals(ecosystem, scanner);
                    break;
                case 3:
                    plantService.managePlants(ecosystem, scanner);
                    break;
                case 4:
                    environmentSettingsService.manageEnvSettings(ecosystem, scanner);
                    break;
                case 5:
                    predictService.predictPopulationChanges(ecosystem);
                    return;
                case 6:
                    saveEcosystem(ecosystem);
                    return;
            }
        }
    }

    private void editEcosystemName(Ecosystem ecosystem, Scanner scanner) {
        System.out.print("Do you want to change the ecosystem name? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            String newName;
            while (true) {
                System.out.print("Enter the new ecosystem name: ");
                newName = scanner.nextLine();
                List<String> ecosystemNames = getAllEcosystemNames();
                if (ecosystemNames.contains(newName)) {
                    System.out.println("Ecosystem already exists!");
                } else {
                    break;
                }
            }
            deleteEcosystem(ecosystem.getName());
            ecosystem.setName(newName);
            System.out.println("Ecosystem name changed to: " + newName);
        } else {
            System.out.println("Ecosystem name not changed.");
        }
    }

    public void saveEcosystem(Ecosystem ecosystem) {
        ecosystemRepository.save(ecosystem);
    }

    public Ecosystem loadEcosystem(String name) {

        return ecosystemRepository.load(name);
    }

    public void deleteEcosystem(String name) {
        ecosystemRepository.delete(name);

    }

    public List<String> getAllEcosystemNames() {
        return ecosystemRepository.getAllEcosystems();
    }
    public long getAllHerbivorePopulation(Ecosystem ecosystem){
        return ecosystem.getAnimals().stream()
                .filter(animal -> animal.getAnimalType() == HERBIVORE)
                .mapToInt(Animal::getPopulation)
                .sum();
    }
    public long getAllCarnivorePopulation(Ecosystem ecosystem){
        return ecosystem.getAnimals().stream()
                .filter(animal -> animal.getAnimalType() == CARNIVORE)
                .mapToInt(Animal::getPopulation)
                .sum();
    }
    public long getAllPlantsQuantity(Ecosystem ecosystem) {
        return ecosystem.getPlants().stream()
                .mapToInt(Plant::getQuantity).sum();
    }
}
