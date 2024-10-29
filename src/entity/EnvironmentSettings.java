package entity;

public class EnvironmentSettings {
    private int temperature; //градусы
    private int humidity; //
    private double waterVolume;
    private int precipitationChance;

    public EnvironmentSettings(int temperature, double waterVolume, int precipitationProbability) {
        this.temperature = temperature;
        this.waterVolume = waterVolume;
        this.precipitationChance = precipitationProbability;
        this.humidity =calculateHumidity();
    }
    private int calculateHumidity() {
        final double  MAXHUMIDITY = 100; // Максимальная относительная влажность в процентах
        double tempFactor = 1.0 - (temperature / 100.0); // Коэффициент температуры
        int calculatedHumidity = (int) (MAXHUMIDITY * tempFactor * ( waterVolume / (waterVolume + 10)));
        return Math.min(100, Math.max(0, calculatedHumidity)); // Ограничение от 0 до 100
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public double getWaterVolume() {
        return waterVolume;
    }
    public void setWaterVolume(double waterVolume) {
        this.waterVolume = waterVolume;
        this.humidity = calculateHumidity();
    }
    public int getPrecipitationChance(){
        return precipitationChance;
    }
    public void setPrecipitationChance(int precipitationProbability) {
        this.precipitationChance = precipitationProbability;
    }
    @Override
    public String toString() {
        return "temperature=" + temperature + ", waterVolume="
               + waterVolume + ",\n humidity=" + humidity +
               ", precipitationChance=" +precipitationChance;
    }
}
