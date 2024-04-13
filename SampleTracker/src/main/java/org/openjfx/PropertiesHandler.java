package org.openjfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHandler {
    private Properties trackerProperties = new Properties();
    private String trackerConfigPath;

    public PropertiesHandler() {
        // String rootPath = System.getProperty("user.dir");
        // System.out.println("Home directory: " + System.getProperty("user.home"));
        String rootPath = System.getProperty("user.home") + "\\AppData\\Local\\Tracker";
        // Should make this safer so it's only creating the Tracker folder and not possibly other folders
        try {
            Files.createDirectories(Paths.get(rootPath));
        } catch (Exception e) {
            System.out.println("Could not create directory: " + e.getMessage());
        }


        trackerConfigPath = rootPath + "\\Tracker.properties";

        try {
            trackerProperties.load(new FileInputStream(trackerConfigPath));
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("File Path: " + trackerConfigPath);
            createPropertiesFile(trackerConfigPath);
        }

    }
    private boolean createPropertiesFile(String filePath) {
        try {
            File propertiesFile = new File(filePath);
            if (propertiesFile.createNewFile()) {
                System.out.println("File created: " + propertiesFile.getName());
            } else {
                System.out.println("Could not create file: " + propertiesFile.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred creating file: " + filePath);
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    private boolean StoreProperties() {
        try {
            trackerProperties.store(new FileWriter(trackerConfigPath), null);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public String getTrackerDataPath() {
        return trackerProperties.getProperty("csvLocation");
    }

    public String getTrackerImagesPath() {
        return trackerProperties.getProperty("imagesFolder");
    }

    public String getDefaultPath() {
        return trackerProperties.getProperty("defaultPath");
    }

    public boolean setTrackerDataPath(String newPath) {
        trackerProperties.setProperty("csvLocation", newPath);
        if (!StoreProperties()) {
            return false;
        }
        return true;
    }

    public boolean setTrackerImagesPath(String newPath) {
        trackerProperties.setProperty("imagesFolder", newPath);
        if (!StoreProperties()) {
            return false;
        }
        return true;
    }

    public boolean setDefaultPath(String newPath) {
        trackerProperties.setProperty("defaultPath", newPath);
        if (!StoreProperties()) {
            return false;
        }
        return true;
    }
}
