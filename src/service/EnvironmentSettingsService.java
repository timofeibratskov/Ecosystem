package service;

import entity.Ecosystem;
import util.InputValidator;

import java.util.Scanner;

public class EnvironmentSettingsService {
    private static EnvironmentSettingsService instance;

    public static EnvironmentSettingsService getInstance() {
        if (instance == null) {
            instance = new EnvironmentSettingsService();
        }
        return instance;
    }
    private EnvironmentSettingsService() {
    }

    public void manageEnvSettings(Ecosystem ecosystem, Scanner scanner) {
        while (true) {
            System.out.print("Manage Environment Settings (t/w/h/p/n): ");
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
        int temperature = InputValidator.getValidIntInput(scanner, "Enter temperature: ");
        ecosystem.getEnvironmentSettings().setTemperature(temperature);
    }

    private void editWaterVolume(Ecosystem ecosystem, Scanner scanner) {
        int waterVolume = InputValidator.getValidPositiveIntInput(scanner, "Enter water volume: ");
        ecosystem.getEnvironmentSettings().setWaterVolume(waterVolume);
    }

    private void editHumidity(Ecosystem ecosystem, Scanner scanner) {
        int humidity = InputValidator.getValidPositiveIntInput(scanner, "Enter humidity: ");
        ecosystem.getEnvironmentSettings().setHumidity(humidity);
    }

    private void editPrecipitationChance(Ecosystem ecosystem, Scanner scanner) {
        int precipitationChance = InputValidator.getValidPositiveIntInput(scanner, "Enter precipitation chance: ");
        ecosystem.getEnvironmentSettings().setPrecipitationChance(precipitationChance);
    }
}