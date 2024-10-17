package repository;

import entity.Animal;
import entity.Ecosystem;
import entity.Plant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileEcosystemRepository implements EcosystemRepository {
    private final String folderPath = "ecosystems/";
    private static FileEcosystemRepository instance;


    private FileEcosystemRepository() {
        try {
            Files.createDirectories(Paths.get(folderPath));
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(folderPath + ecosystem.getName() + ".txt"))) {
            for (Animal animal : ecosystem.getAnimals()) {
                writer.println("Animal:" + animal.getName() + "," + animal.getPopulation());
            }
            for (Plant plant : ecosystem.getPlants()) {
                writer.println("Plant:" + plant.getName() + "," + plant.getQuantity());
            }
        } catch (IOException e) {
            System.err.println("Could not save ecosystem: " + e.getMessage());
        }
    }

    @Override
    public Ecosystem load(String name) {
        Ecosystem ecosystem = new Ecosystem(name);
        try (BufferedReader reader = new BufferedReader(new FileReader(folderPath + name + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String type = parts[0];
                String[] details = parts[1].split(",");
                if (type.equals("Animal")) {
                    ecosystem.addAnimal(new Animal(details[0], Integer.parseInt(details[1])));
                } else if (type.equals("Plant")) {
                    ecosystem.addPlant(new Plant(details[0], Integer.parseInt(details[1])));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load ecosystem: " + e.getMessage());
            return null;
        }
        return ecosystem;
    }

    @Override
    public void delete(String name) {
        try {
            Files.delete(Paths.get(folderPath + name + ".txt"));
        } catch (IOException e) {
            System.err.println("Could not delete ecosystem: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllEcosystems() {
        List<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path entry : stream) {
                if (entry.toString().endsWith(".txt")) {
                    names.add(entry.getFileName().toString().replace(".txt", ""));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not list ecosystems: " + e.getMessage());
        }
        return names;
    }
}