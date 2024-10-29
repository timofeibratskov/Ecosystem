package service;

import config.PredictConfig;
import entity.Ecosystem;
import enums.AnimalType;

import java.util.Random;

public class PredictService {

    private static PredictService instance;
    private final PredictConfig config;
    private final Random random = new Random();

    private PredictService() {
        this.config =PredictConfig.getInstance() ;
    }

    public static PredictService getInstance() {
        if (instance == null) {
            instance = new PredictService();
        }
        return instance;
    }

    public void predictPopulationChanges(Ecosystem ecosystem) {
        long totalHerbivoreAnimalPopulation = ecosystem.getAnimals().stream()
                .filter(animal -> animal.getAnimalType() == AnimalType.HERBIVORE)
                .count();
        long totalCarnivorePopulation = ecosystem.getAnimals().stream()
                .filter(animal -> animal.getAnimalType() == AnimalType.CARNIVORE)
                .count();
        long totalPlantQuantity = ecosystem.getPlants().size();

        double waterVolume = ecosystem.getEnvironmentSettings().getWaterVolume();
        int temperature = ecosystem.getEnvironmentSettings().getTemperature();
        int precipitationChance = ecosystem.getEnvironmentSettings().getPrecipitationChance();
        int humidity = ecosystem.getEnvironmentSettings().getHumidity();

        double totalWaterConsumption = calculateTotalWaterConsumption(totalPlantQuantity, totalHerbivoreAnimalPopulation, totalCarnivorePopulation);

        double plantGrowth = predictPlantGrowth(humidity, temperature, precipitationChance, waterVolume, totalPlantQuantity);
        double herbivoreGrowth = predictHerbivoreGrowth(totalPlantQuantity, waterVolume, totalHerbivoreAnimalPopulation, totalWaterConsumption);
        double carnivoreGrowth = predictCarnivoreGrowth(totalHerbivoreAnimalPopulation, waterVolume, totalCarnivorePopulation, totalWaterConsumption);

        int newPlantQuantity = (int) (totalPlantQuantity * (1 + plantGrowth));
        int newHerbivorePopulation = (int) (totalHerbivoreAnimalPopulation * (1 + herbivoreGrowth));
        int newCarnivorePopulation = (int) (totalCarnivorePopulation * (1 + carnivoreGrowth));

        logPopulationChanges(
                calculateChangePercentage(newHerbivorePopulation, totalHerbivoreAnimalPopulation),
                calculateChangePercentage(newCarnivorePopulation, totalCarnivorePopulation),
                calculateChangePercentage(newPlantQuantity, totalPlantQuantity)
        );
    }

    private double calculateTotalWaterConsumption(long totalPlantQuantity, long totalHerbivorePopulation, long totalCarnivorePopulation) {
        double plantWaterConsumption = config.getWaterConsumptionPerPlant() * totalPlantQuantity;
        double animalWaterConsumption = config.getWaterConsumptionPerAnimal() * (totalHerbivorePopulation + totalCarnivorePopulation);
        return plantWaterConsumption + animalWaterConsumption;
    }

    private double predictPlantGrowth(int humidity, int temperature, int precipitationChance, double waterVolume, long totalPlantQuantity) {
        double growthProbability = 0.0;

        growthProbability += (humidity >= 40 && humidity <= 70) ? 0.1 : (humidity < 40 ? -0.1 : -0.05);
        growthProbability += (temperature >= 15 && temperature <= 25) ? 0.1 : (temperature > 25 ? -0.1 : 0);
        growthProbability += (precipitationChance >= 4) ? 0.1 : (precipitationChance < 3 ? -0.05 : 0);
        growthProbability += (config.getWaterConsumptionPerPlant() * totalPlantQuantity <= waterVolume) ? 0.2 : 0.05;

        return growthProbability;
    }

    private double predictHerbivoreGrowth(long totalPlantQuantity, double waterVolume, long totalHerbivorePopulation, double totalWaterConsumption) {
        double growthProbability = 0.0;
        growthProbability += (totalWaterConsumption <= waterVolume) ? config.getHerbivoreGrowthRate() : -0.2;
        growthProbability += random.nextDouble() < config.getDiseaseProbability() ? -0.1 : 0;
        growthProbability += random.nextDouble() < config.getMaxPredatorProbability() ? -0.1 : 0;
        growthProbability -= totalPlantQuantity < totalHerbivorePopulation * 0.4 ? 0.05 : 0;

        return growthProbability;
    }

    private double predictCarnivoreGrowth(long totalHerbivorePopulation, double waterVolume, long totalCarnivorePopulation, double totalWaterConsumption) {
        double growthProbability = 0.0;
        growthProbability += (totalWaterConsumption <= waterVolume) ? config.getCarnivoreGrowthRate() : -0.1;
        growthProbability += random.nextDouble() < config.getDiseaseProbability() ? -0.1 : 0;
        growthProbability += totalHerbivorePopulation >= totalCarnivorePopulation / 2 ? 0.1 : -0.05;

        return growthProbability;
    }

    private double calculateChangePercentage(int newPopulation, long originalPopulation) {
        return (double) (newPopulation - originalPopulation) / originalPopulation;
    }

    private void logPopulationChanges(double hAnimalChange, double cAnimalChange, double plantChange) {
        System.out.printf("Популяция травоядных %s на %.2f%%.%n", hAnimalChange >= 0 ? "вырастет" : "упадет", Math.abs(hAnimalChange) * 100);
        System.out.printf("Популяция плотоядных %s на %.2f%%.%n", cAnimalChange >= 0 ? "вырастет" : "упадет", Math.abs(cAnimalChange) * 100);
        System.out.printf("Популяция растений %s на %.2f%%.%n", plantChange >= 0 ? "вырастет" : "упадет", Math.abs(plantChange) * 100);
    }
}
