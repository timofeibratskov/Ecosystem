package entity;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
    private String name;
    private List<Animal> animals;
    private List<Plant> plants;

    public Ecosystem(String name) {
        this.name = name;
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    // Метод для получения животного по имени
    public Animal getAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null; // Если животное не найдено
    }

    public Plant getPlantByName(String name) {
        for (Plant plant : plants) {
            if (plant.getName().equalsIgnoreCase(name)) {
                return plant;
            }
        }
        return null; // Если растение не найдено
    }

    public boolean removeAnimal(String name) {
        return animals.removeIf(animal -> animal.getName().equalsIgnoreCase(name));
    }

    public boolean removePlant(String name) {
        return plants.removeIf(plant -> plant.getName().equalsIgnoreCase(name));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ecosystem: " + name + "\nAnimals:\n");
        for (Animal animal : animals) {
            sb.append("- ").append(animal.getName()).append(", ").append(animal.getPopulation()).append("\n");
        }
        sb.append("Plants:\n");
        for (Plant plant : plants) {
            sb.append("- ").append(plant.getName()).append(", ").append(plant.getQuantity()).append("\n");
        }
        return sb.toString();
    }
}