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

    @Override
    public String toString() {
        return "Ecosystem: " + name + "\nAnimals: " + animals + "\nPlants: " + plants;
    }
}
