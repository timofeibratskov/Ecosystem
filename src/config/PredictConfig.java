package config;

public class PredictConfig {
    private static final PredictConfig instance = new PredictConfig();

    private final double WATERCONSUMPTIONPERPLANT = 5;
    private final double WATERCONSUMPTIONPERANIMAL = 4;
    private final double DISEASEPROBABILITY = 0.07;   //вероятность болезни
    private final int WTERINCREASE = 999; // фиксированное добавление воды после осадков


    public static PredictConfig getInstance() {
        return instance;
    }

    public int getWaterIncrease() {
        return WTERINCREASE;
    }

    public double getWaterConsumptionPerPlant() {
        return WATERCONSUMPTIONPERPLANT;
    }

    public double getWaterConsumptionPerAnimal() {
        return WATERCONSUMPTIONPERANIMAL;
    }

    public double getDiseaseProbability() {
        return DISEASEPROBABILITY;
    }
}
