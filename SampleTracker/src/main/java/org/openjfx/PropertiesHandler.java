package org.openjfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PropertiesHandler {
    private Properties trackerProperties = new Properties();
    private String trackerConfigPath;

    public PropertiesHandler() throws Exception {
        String rootPath = System.getProperty("user.home") + "\\AppData\\Local\\Tracker";
        // Should make this safer so it's only creating the Tracker folder and not possibly other folders
        try {
            Files.createDirectories(Paths.get(rootPath));
        } catch (Exception e) {
            System.out.println("Could not create directory: " + e.getMessage());
            throw new Exception(e);
        }


        trackerConfigPath = rootPath + "\\Tracker.properties";

        try {
            trackerProperties.load(new FileInputStream(trackerConfigPath));
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("File Path: " + trackerConfigPath);
            try {
                createPropertiesFile(trackerConfigPath);
            } catch(IOException f) {
                throw new Exception(f);
            }
            
        }

    }
    private boolean createPropertiesFile(String filePath) throws IOException{
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
            // System.exit(1);
            throw new IOException(e);
        }
        return true;
    }

    public boolean StoreProperties() {
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

    public Double getColumnWidthProperty(String columnName) {
        try {
            return Double.parseDouble(trackerProperties.getProperty(columnName));
        } catch (Exception e) {
            return null;
        }
    }

    public List<String[]> getColumnConfigData() {
        List<String[]> listList = new ArrayList<String[]>();
        String columnNames = trackerProperties.getProperty("columnNames");
        String columnEnables = trackerProperties.getProperty("columnEnables");
        String columnWidths = trackerProperties.getProperty("columnWidths");
        if (columnNames == null || columnEnables == null) {
            return null;
        }
        String[] columnNamesList = columnNames.split(" ");
        String[] columnEnablesList = columnEnables.split(" ");
        String[] columnWidthsList = columnWidths.split(" ");

        for(int i = 0;i<columnNamesList.length;i++){
            String[] tempStringList = new String[3];
            tempStringList[0] = columnNamesList[i];
            tempStringList[1] = columnEnablesList[i];
            tempStringList[2] = columnWidthsList[i];
            listList.add(tempStringList);
        }
        
        return listList;
    }

    public boolean setColumnConfigData(List<String[]> configDataList) {
        StringBuilder columnNamesBuilder = new StringBuilder();
        StringBuilder columnEnablesBuilder = new StringBuilder();
        StringBuilder columnWidthsBuilder = new StringBuilder();

        for(int i = 0; i<configDataList.size(); i++){
            columnNamesBuilder.append(configDataList.get(i)[0]).append(" ");
            columnEnablesBuilder.append(configDataList.get(i)[1]).append(" ");
            columnWidthsBuilder.append(configDataList.get(i)[2]).append(" ");
        }
        trackerProperties.setProperty("columnNames", columnNamesBuilder.toString());
        trackerProperties.setProperty("columnEnables", columnEnablesBuilder.toString());
        trackerProperties.setProperty("columnWidths", columnWidthsBuilder.toString());

        if (!StoreProperties()) {
            return false;
        }
        return true;
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

    public boolean setColumnWidthProperty(String columnName, Double newValue) {
        trackerProperties.setProperty(columnName, newValue.toString());
        if (!StoreProperties()) {
            return false;
        }
        return true;
    }
}
