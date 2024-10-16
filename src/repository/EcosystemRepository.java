package repository;

import entity.Ecosystem;

public interface EcosystemRepository {
    void save(String EcosystemName,Ecosystem ecosystem);
    Ecosystem load(String EcosystemName);
}
