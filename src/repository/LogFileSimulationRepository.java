package repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFileSimulationRepository implements SimulationRepository {

    private static LogFileSimulationRepository instance;
    private final String FOLDERPATH = "logs/";

    public static LogFileSimulationRepository getInstance() {
        if (instance == null) {
            synchronized (LogFileSimulationRepository.class) {
                if (instance == null) {
                    instance = new LogFileSimulationRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void write(String ecosystemName, String log) {
        File folder = new File(FOLDERPATH);
        if (!folder.exists()) {
            folder.mkdirs(); // Создаем папку и все необходимые родительские директории
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FOLDERPATH + ecosystemName + "_log.txt", true))) {
            writer.println(log); // Добавляем новую запись
        } catch (IOException e) {
            System.err.println("Could not write to log file: " + e.getMessage());
        }
    }
}