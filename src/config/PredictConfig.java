package config;
public class PredictConfig {
    private static final PredictConfig instance = new PredictConfig();

    private final double waterConsumptionPerPlant = 0.2;
    private final double waterConsumptionPerAnimal = 0.1;
    private final double diseaseProbability = 0.1;
    private final double herbivoreGrowthRate = 0.35;
    private final double maxPredatorProbability = 0.2;
    private final double carnivoreGrowthRate = 0.445;

    private PredictConfig() {}

    public static PredictConfig getInstance() {
        return instance;
    }

    public double getWaterConsumptionPerPlant() {
        return waterConsumptionPerPlant;
    }

    public double getWaterConsumptionPerAnimal() {
        return waterConsumptionPerAnimal;
    }

    public double getDiseaseProbability() {
        return diseaseProbability;
    }

    public double getHerbivoreGrowthRate() {
        return herbivoreGrowthRate;
    }

    public double getMaxPredatorProbability() {
        return maxPredatorProbability;
    }

    public double getCarnivoreGrowthRate() {
        return carnivoreGrowthRate;
    }
}
