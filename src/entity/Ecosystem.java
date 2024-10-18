package entity;


import java.util.HashSet;

public class Ecosystem {
    private String name;
    private HashSet<Animal> animals;
    private HashSet<Plant> plants;

    public Ecosystem(String name) {
        this.name = name;
        this.animals = new HashSet<>();
        this.plants = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAnimal(Animal animal) {
        if (animals.add(animal)) {
            System.out.println("Added new animal: " + animal);
        } else {
            System.out.println("Animal with name " + animal.getName() + " already exists.");
        }
    }

    public HashSet<Animal> getAnimals() {
        return animals;
    }

    public HashSet<Plant> getPlants() {
        return plants;
    }

    public Plant getPlant(String name) {
        for(Plant plant : plants) {
            if(plant.getName().equals(name)) {
                return plant;
            }
        }return null;
    }

    public Animal getAnimal(String name) {
      for(Animal animal : animals) {
          if(animal.getName().equals(name)) {
              return animal;
          }
      }return null;
    }
    public void deleteAnimal(String animalName) {
        animals.removeIf(animal -> animal.getName().equals(animalName));
    }

    public void addPlant(Plant plant) {
        if (plants.add(plant)) {
            System.out.println("Added new plant: " + plant);
        } else {
            System.out.println("Plant with name " + plant.getName() + " already exists.");
        }
    }

    public void deletePlant(String plantName) {
        plants.removeIf(plant -> plant.getName().equals(plantName));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ecosystem: " + name + "\nAnimals:\n");
        animals.forEach(animal -> sb.append("- ").append(animal).append("\n"));
        sb.append("Plants:\n");
        plants.forEach(plant -> sb.append("- ").append(plant).append("\n"));
        return sb.toString();
    }
}