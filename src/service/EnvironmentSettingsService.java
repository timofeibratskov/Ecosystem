package service;

import entity.Ecosystem;
import util.InputValidator;
import java.util.Scanner;

public class EnvironmentSettingsService {


    public EnvironmentSettingsService() {
    }

    public void manageEnvSettings(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Manage Environment Settings: \ntemperature, waterVolume, humidity, precipitationChance\n(t/w/h/p/n):");
            String option = scanner.nextLine().toLowerCase();
            switch (option) {
                case "t":
                    editTemperature(ecosystem, scanner);
                    break;
                case "w":
                    editWaterVolume(ecosystem, scanner);
                    break;
                case "h":
                    editHumidity(ecosystem, scanner);
                    break;
                case "p":
                    editPrecipitationChance(ecosystem, scanner);
                    break;
                case "n":
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void editTemperature(Ecosystem ecosystem, Scanner scanner) {
        int temperature = InputValidator.getValidIntInput(scanner, "Enter temperature: ", value -> -40 <= value && value <= 40);
        ecosystem.getEnvironmentSettings().setTemperature(temperature);
    }

    private void editWaterVolume(Ecosystem ecosystem, Scanner scanner) {
        double waterVolume = InputValidator.getValidDoubleInput(scanner, "Enter water volume: ", value -> value >= 0);
        ecosystem.getEnvironmentSettings().setWaterVolume(waterVolume);
    }

    private void editHumidity(Ecosystem ecosystem, Scanner scanner) {
        int humidity = InputValidator.getValidIntInput(scanner, "Enter humidity (0-100): ", value -> value >= 0 && value <= 100);
        ecosystem.getEnvironmentSettings().setHumidity(humidity);
    }

    private void editPrecipitationChance(Ecosystem ecosystem, Scanner scanner) {
        int precipitationChance = InputValidator.getValidIntInput(scanner, "Enter precipitation chance [0;6]: ", value -> value >= 0 && value <= 6);
        ecosystem.getEnvironmentSettings().setPrecipitationChance(precipitationChance);
    }
}