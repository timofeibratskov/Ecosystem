package repository;
 import entity.Animal;
 import entity.Ecosystem;
 import entity.Plant;

 import java.nio.file.*;
 import java.io.*;

public class FileEcosystemRepository implements EcosystemRepository {
    private static final String DIRECTORY = "ecosystems/";

    public FileEcosystemRepository() {
        try {
            Files.createDirectories(Paths.get(DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String ecosystemName, Ecosystem ecosystem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORY + ecosystemName + ".txt"))) {
            writer.write(ecosystem.getName());
            writer.newLine();

            for (Animal animal : ecosystem.getAnimals()) {
                writer.write("Animal," + animal.getName() + "," + animal.getPopulation());
                writer.newLine();
            }

            for (Plant plant : ecosystem.getPlants()) {
                writer.write("Plant," + plant.getName() + "," + plant.getQuantity());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ecosystem load(String ecosystemName) {
        Ecosystem ecosystem = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(DIRECTORY + ecosystemName + ".txt"))) {
            String name = reader.readLine();
            ecosystem = new Ecosystem(name);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("Animal")) {
                    ecosystem.addAnimal(new Animal(parts[1], Integer.parseInt(parts[2])));
                } else if (parts[0].equals("Plant")) {
                    ecosystem.addPlant(new Plant(parts[1], Integer.parseInt(parts[2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ecosystem;
    }
}
