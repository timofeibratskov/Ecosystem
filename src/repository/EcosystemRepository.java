package repository;

import entity.Ecosystem;

import java.util.List;

public interface EcosystemRepository {
    void save(Ecosystem ecosystem);
    Ecosystem load(String name);
    void delete(String name);
    List<String> getAllEcosystems();
}
