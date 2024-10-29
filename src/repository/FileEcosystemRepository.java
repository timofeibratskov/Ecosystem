package repository;

import entity.Animal;
import entity.Ecosystem;
import entity.Plant;
import enums.AnimalType;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileEcosystemRepository implements EcosystemRepository {
    private static FileEcosystemRepository instance;
    private final String FOLDERPATH = "ecosystems/";

    private FileEcosystemRepository() {
        try {
            Files.createDirectories(Paths.get(FOLDERPATH));
        } catch (IOException e) {
            System.err.println("Could not create ecosystems directory: " + e.getMessage());
        }
    }

    public static FileEcosystemRepository getInstance() {
        if (instance == null) {
            synchronized (FileEcosystemRepository.class) {
                if (instance == null) {
                    instance = new FileEcosystemRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(Ecosystem ecosystem) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FOLDERPATH + ecosystem.getName() + ".txt"))) {
            writer.println("Temperature:" + ecosystem.getEnvironmentSettings().getTemperature());
            writer.println("WaterVolume:" + ecosystem.getEnvironmentSettings().getWaterVolume());
            writer.println("Humidity:" + ecosystem.getEnvironmentSettings().getHumidity());
            writer.println("PrecipitationChance:" + ecosystem.getEnvironmentSettings().getPrecipitationChance());

            ecosystem.getAnimals().forEach(animal ->
                    writer.println("Animal:" + animal.getName() + "," + animal.getPopulation() + "," + animal.getAnimalType())
            );
            ecosystem.getPlants().forEach(plant ->
                    writer.println("Plant:" + plant.getName() + "," + plant.getQuantity())
            );
        } catch (IOException e) {
            System.err.println("Could not save ecosystem");
        }
    }

    @Override
    public Ecosystem load(String name) {
        Ecosystem ecosystem = new Ecosystem(name);
        try (BufferedReader reader = new BufferedReader(new FileReader(FOLDERPATH + name + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }
                String type = parts[0];
                String[] details = parts[1].split(",");

                switch (type) {
                    case "temperature" -> ecosystem.getEnvironmentSettings().setTemperature(Integer.parseInt(parts[1]));
                    case "WaterVolume" ->
                            ecosystem.getEnvironmentSettings().setWaterVolume(Double.parseDouble(parts[1]));
                    case "Humidity" -> ecosystem.getEnvironmentSettings().setHumidity(Integer.parseInt(parts[1]));
                    case "PrecipitationChance" ->
                            ecosystem.getEnvironmentSettings().setPrecipitationChance(Integer.parseInt(parts[1]));
                    case "Animal" -> {
                        if (details.length < 3) {
                            System.err.println("Invalid animal format: " + parts[1]);
                            continue;
                        }
                        AnimalType animalType = AnimalType.valueOf(details[2].toUpperCase());
                        ecosystem.addAnimal(new Animal(details[0], Integer.parseInt(details[1]), animalType));
                    }
                    case "Plant" -> {
                        if (details.length < 2) {
                            System.err.println("Invalid plant format: " + parts[1]);
                            continue;
                        }
                        ecosystem.addPlant(new Plant(details[0], Integer.parseInt(details[1])));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("such file name does not exist: " + name);
            return null;
        }
        return ecosystem;
    }


    @Override
    public void delete(String name) {
        try {
            Files.delete(Paths.get(FOLDERPATH + name + ".txt"));
        } catch (IOException e) {
            System.out.println("such file name does not exist: " + name);
        }
    }

    @Override
    public List<String> getAllEcosystems() {
        List<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(FOLDERPATH))) {
            for (Path entry : stream) {
                if (entry.toString().endsWith(".txt")) {
                    names.add(entry.getFileName().toString().replace(".txt", ""));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not list ecosystems");
        }
        return names;
    }
}
