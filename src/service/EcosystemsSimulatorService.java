package service;

import config.PredictConfig;
import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import repository.SimulationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static enums.AnimalType.CARNIVORE;
import static enums.AnimalType.HERBIVORE;

public class EcosystemsSimulatorService {

    private final EcosystemService ecosystemService;
    private List<String> ecosystems;
    private final PredictConfig config;
    private final Random rand = new Random();
    private final SimulationRepository simulationRepository;
    private volatile boolean running = true;


    public EcosystemsSimulatorService(EcosystemService ecosystemService, SimulationRepository simulationRepository) {
        this.ecosystems = new ArrayList<>();
        this.ecosystemService = ecosystemService;
        this.simulationRepository = simulationRepository;
        this.config = PredictConfig.getInstance();
    }

    public void startEcosystemsSimulator() {


        while (running) {
            ecosystems = ecosystemService.getAllEcosystemNames();
            for (String ecosystemName : ecosystems) {

                Ecosystem ecosystem = ecosystemService.loadEcosystem(ecosystemName);

                simulationRepository.write(ecosystemName, "------ new Day ------");

                double waterVolume = ecosystem.getEnvironmentSettings().getWaterVolume();
                waterUsing(ecosystem, waterVolume);

                managePlants(ecosystem);
                manageHerbivoreAnimals(ecosystem);
                manageCarnivoreAnimals(ecosystem);

                int chance = ecosystem.getEnvironmentSettings().getPrecipitationChance();
                if (chance >= rand.nextInt(7)) {

                    waterVolume += config.getWaterIncrease();
                    ecosystem.getEnvironmentSettings().setWaterVolume(waterVolume);

                    simulationRepository.write(ecosystemName, "precipitation update waterVolume: " + waterVolume);
                } else simulationRepository.write(ecosystemName, "no precipitation!!!");

                ecosystemService.saveEcosystem(ecosystem);

            }
            try {
                Thread.sleep(10000);

            } catch (InterruptedException e) {
                System.err.println("Simulation thread interrupted: " + e.getMessage());
            }
        }
    }

    public void stopEcosystemsSimulator() {
        running = false;
    }

    private void waterUsing(Ecosystem ecosystem, Double waterVolume) {
        long totalHerbivorePopulation = ecosystemService.getAllHerbivorePopulation(ecosystem);
        long totalCarnivorePopulation = ecosystemService.getAllCarnivorePopulation(ecosystem);
        long totalPlantQuantity = ecosystemService.getAllPlantsQuantity(ecosystem);

        double requiredWaterVolume = ((totalHerbivorePopulation + totalCarnivorePopulation) * config.getWaterConsumptionPerAnimal() +
                totalPlantQuantity * config.getWaterConsumptionPerPlant());
        simulationRepository.write(ecosystem.getName(), "waterVolume: " + waterVolume);

        if (waterVolume >= requiredWaterVolume) {
            waterVolume -= requiredWaterVolume;
            simulationRepository.write(ecosystem.getName(), "all organisms drank !");
        } else if (waterVolume <= 0) {
            waterVolume = 0.0;
            simulationRepository.write(ecosystem.getName(), "no water reserves !!!!!!!!");
        }
        ecosystem.getEnvironmentSettings().setWaterVolume(waterVolume);
    }

    private void manageHerbivoreAnimals(Ecosystem ecosystem) {
        long herbivorePopulation = ecosystemService.getAllHerbivorePopulation(ecosystem);
        long plantsQuantity = ecosystemService.getAllPlantsQuantity(ecosystem);
        for (Animal animal : ecosystem.getAnimals()) {
            if (animal.getAnimalType() == HERBIVORE) {

                int population = animal.getPopulation();
                double growFactor = calcAnimalGrowFactor(ecosystem, plantsQuantity, herbivorePopulation);

                if (population != 0) {

                    population += (int) Math.round(animal.getPopulation() * growFactor);

                    if (population <= 0) {

                        population = 0;
                        simulationRepository.write(ecosystem.getName(), "this herbivore animal " + animal.getName() + " type is disappeare!!!!");

                    } else {
                        simulationRepository.write(ecosystem.getName(), "herbivore animal :" + animal.getName() + " population :" + population);
                    }
                    animal.setPopulation(population);
                }else{simulationRepository.write(ecosystem.getName(), "this type " + animal.getName() + " is disappear!!!! !");}
            }
        }
    }


    private void managePlants(Ecosystem ecosystem) {

        double diseaseChance = config.getDiseaseProbability();
        int temperature = ecosystem.getEnvironmentSettings().getTemperature();
        int humidity = ecosystem.getEnvironmentSettings().getHumidity();
        int precipitationChance = ecosystem.getEnvironmentSettings().getPrecipitationChance();
        double water = ecosystem.getEnvironmentSettings().getWaterVolume();

        for (Plant plant : ecosystem.getPlants()) {

            int quantity = plant.getQuantity();
            double growthFactor = calcPlantsGrowthFactor(temperature, humidity, precipitationChance, diseaseChance, quantity, water);

            if (quantity != 0) {
                quantity += (int) Math.round(quantity * growthFactor);
                if (quantity <= 0) {
                    quantity = 0;
                    simulationRepository.write(ecosystem.getName(), "this plants: " + plant.getName() + " is disappear!!!! ");
                } else {
                    simulationRepository.write(ecosystem.getName(), "this plant type: " + plant.getName() + " quantity: " + quantity);
                }

                plant.setQuantity(quantity);
            }else{simulationRepository.write(ecosystem.getName(), "this type: " +plant.getName() + " is disappear!!!! !");}

        }
    }

    private double calcPlantsGrowthFactor(int temperature, int humidity, int precipitationChance,
                                          double diseaseChance, int quantity, double water) {

        int value = 0;
        if (15 <= temperature && temperature <= 25) {
            value += 10;
        } else {
            value -= 10;
        }
        if (60 <= humidity && humidity <= 70) {
            value += 5;
        }
        if (4 <= precipitationChance) {
            value += 10;
        }
        if (water != 0) {
            value += 20;
        } else {
            value -= 30;
        }


        return (1 - (quantity * diseaseChance)) / value;
    }

    private void manageCarnivoreAnimals(Ecosystem ecosystem) {

        long carnivorePopulation = ecosystemService.getAllCarnivorePopulation(ecosystem);

        long herbivorePopulation = ecosystemService.getAllHerbivorePopulation(ecosystem);

        for (Animal animal : ecosystem.getAnimals()) {
            if (animal.getAnimalType() == CARNIVORE) {

                int population = animal.getPopulation();

                double growFactor = calcAnimalGrowFactor(ecosystem, herbivorePopulation, carnivorePopulation);
                if (population > 0) {
                    population += (int) Math.round(animal.getPopulation() * growFactor);
                    if (population <= 0) {
                        population = 0;
                        simulationRepository.write(ecosystem.getName(), "this carnivore animal: " + animal.getName() + "type is disappear !!!!  ");
                    } else {
                        simulationRepository.write(ecosystem.getName(), "carnivore animal:" + animal.getName() + " population :" + population);
                    }
                    animal.setPopulation(population);
                }else{simulationRepository.write(ecosystem.getName(), "this type: "+animal.getName()+" is disappear!!!! !");}

            }
        }
    }

    private double calcAnimalGrowFactor(Ecosystem ecosystem, long foodPopulation, long animalPopulation) {
        int value = 0;
        if (foodPopulation < 0.8 * animalPopulation || foodPopulation == 0) {
            value -= 30;
        } else {
            value += 10;
        }
        if (ecosystem.getEnvironmentSettings().getWaterVolume() > 0) {
            value += 10;
        } else {
            value -= 20;
        }

        return (1 - (config.getDiseaseProbability() * animalPopulation)) / value;

    }
}
