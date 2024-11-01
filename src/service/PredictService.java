package service;

import config.PredictConfig;
import entity.Ecosystem;



public class PredictService {


    private final PredictConfig config;
    private final EcosystemService ecosystemService;

    public PredictService(EcosystemService ecosystemService) {
        this.config = PredictConfig.getInstance();
        this.ecosystemService = ecosystemService;
    }


    public void predictPopulationChanges(Ecosystem ecosystem) {

        long totalHerbivoreAnimalPopulation = ecosystemService.getAllHerbivorePopulation(ecosystem);

        long totalCarnivorePopulation = ecosystemService.getAllCarnivorePopulation(ecosystem);

        long totalPlantQuantity = ecosystemService.getAllPlantsQuantity(ecosystem);

        double waterVolume = ecosystem.getEnvironmentSettings().getWaterVolume();
        int temperature = ecosystem.getEnvironmentSettings().getTemperature();
        int precipitationChance = ecosystem.getEnvironmentSettings().getPrecipitationChance();
        int humidity = ecosystem.getEnvironmentSettings().getHumidity();

        double totalWaterConsumption = calculateTotalWaterConsumption(totalPlantQuantity, totalHerbivoreAnimalPopulation, totalCarnivorePopulation);

        double plantGrowth = (double) 100 / predictPlantGrowth(humidity, temperature, precipitationChance, waterVolume, totalWaterConsumption);
        double herbivoreGrowth = (double) 100 / predictAnimalTypeGrowth(totalPlantQuantity, waterVolume, totalHerbivoreAnimalPopulation, totalWaterConsumption);
        double carnivoreGrowth = (double) 100 / predictAnimalTypeGrowth(totalHerbivoreAnimalPopulation, waterVolume, totalCarnivorePopulation, totalWaterConsumption);

        displayPredict(ecosystem.getName(), plantGrowth, carnivoreGrowth, herbivoreGrowth);
    }

    private double calculateTotalWaterConsumption(long totalPlantQuantity, long totalHerbivorePopulation, long totalCarnivorePopulation) {
        double plantWaterConsumption = config.getWaterConsumptionPerPlant() * totalPlantQuantity;
        double animalWaterConsumption = config.getWaterConsumptionPerAnimal() * (totalHerbivorePopulation + totalCarnivorePopulation);
        return plantWaterConsumption + animalWaterConsumption;
    }


    private int predictPlantGrowth(int humidity, int temperature, int precipitationChance, double waterVolume, double totalWaterConsumption) {
        int growthProbability = 0;
        if (15 <= temperature && temperature <= 25) {
            growthProbability += 10;
        } else if (7 < temperature && temperature < 15) {
            growthProbability += 5;
        } else {
            growthProbability -= 10;
        }
        if (60 <= humidity && humidity <= 70) {
            growthProbability += 10;
        } else {
            growthProbability -= 7;
        }
        if (precipitationChance >= 4) {
            growthProbability += 13;
        } else if (precipitationChance == 0) {
            growthProbability -= 14;
        }
        if (totalWaterConsumption * 3 <= waterVolume) {
            growthProbability += 12;
        } else {
            growthProbability -= 12;
        }
        return growthProbability;
    }


    private int predictAnimalTypeGrowth(long food, double waterVolume, long animalTypePopulation, double totalWaterConsumption) {
        int growthProbability = 0;

        if (food > 2 * animalTypePopulation) {
            growthProbability += 12;
        } else if (food < animalTypePopulation) {
            growthProbability -= 12;
        }
        if (waterVolume >= totalWaterConsumption * 3) {
            growthProbability += 9;
        } else {
            growthProbability -= 12;
        }
        return growthProbability;
    }

    private void displayPredict(String ecosystemName, double plantGrowth, double carnivoreGrowth, double herbivoreGrowth) {
        System.out.printf("In the ecosystem %s, it is predicted that %n", ecosystemName);
        System.out.printf("The herbivore population will %s by %.2f%%. %n", herbivoreGrowth >= 0 ? "increase" : "decrease", herbivoreGrowth);
        System.out.printf("The carnivore population will %s by %.2f%%. %n", carnivoreGrowth >= 0 ? "increase" : "decrease", carnivoreGrowth);
        System.out.printf("The plant population will %s by %.2f%%. %n", plantGrowth >= 0 ? "increase" : "decrease", plantGrowth);

    }
}
