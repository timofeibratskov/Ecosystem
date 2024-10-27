package entity;

import enums.AnimalType;

public class Animal {
    private String name;
    private int population;
    private AnimalType animalType;


    public Animal(String name, int population, AnimalType animalType) {
        this.name = name;
        this.population = population;
        this.animalType = animalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Animal)) return false;
        Animal animal = (Animal) obj;
        return name.equals(animal.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Animal: %s, Population: %d, Type: %s", name, population, animalType);
    }


}