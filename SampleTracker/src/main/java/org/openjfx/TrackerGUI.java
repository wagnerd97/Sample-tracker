package org.openjfx;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.nio.file.Path;

public class TrackerGUI extends Application {

    private String desktopLocation = System.getProperty("user.home");

    private static SampleTracker sT;
    private static PropertiesHandler pH;
    private TableView<Client> table;

    private Stage entryStage;
    private VBox entryBox;

    private Stage changesStage;
    private VBox changesBox;

    private Stage plainTextStage;
    private VBox plainTextBox;

    private Stage URLTextStage;
    private VBox URLTextBox;

    private Stage configColumnsStage;
    private VBox configColumnsBox;

    private Image IconImage;

    private TextArea textArea;

    private static String errorString = new String();
    private Label errorLabel;

    private boolean allowReorg = true;

    private Integer certificateNumber = 1;

    private Integer newClientsAdded = 0;

    // List index is column position Map: (columnName, addToTableView)
    // private List<String[]> columnConfigData;


    private Integer certificateToRemove = 1; // Gets set right before confirm remove popup

    private TextField  clientIdField =                 new TextField();
    private TextField  shipNameField =                 new TextField();
    private TextField  shipPhoneField =                new TextField();
    private TextField  shipCompanyField =              new TextField();
    private TextField  shipAddress1Field =             new TextField();
    private TextField  shipAddress2Field =             new TextField();
    private TextField  shipCityField =                 new TextField();
    private TextField  shipRegionField =               new TextField();
    private TextField  shipPostCodeField =             new TextField();
    private TextField  shipCountryField =              new TextField();
    private TextField  shipEmailField =                new TextField();
    private TextField  billNameField =                 new TextField();
    private TextField  billPhoneField =                new TextField();
    private TextField  billCompanyField =              new TextField();
    private TextField  billAddress1Field =             new TextField();
    private TextField  billAddress2Field =             new TextField();
    private TextField  billCityField =                 new TextField();
    private TextField  billRegionField =               new TextField();
    private TextField  billPostCodeField =             new TextField();
    private TextField  billCountryField =              new TextField();
    private TextField  billEmailField =                new TextField();
    private DatePicker dateShippedPicker =             new DatePicker();
    private TextField  firstLicenseNumField =          new TextField();
    private TextField  firstCertificateCompanyField =  new TextField();
    private TextField  secondLicenseNumField =         new TextField();
    private TextField  secondCertificateCompanyField = new TextField();
    private TextField  commentsField =                 new TextField();

    private TextField  clientIdField2 =                 new TextField();
    private TextField  shipNameField2 =                 new TextField();
    private TextField  shipPhoneField2 =                new TextField();
    private TextField  shipCompanyField2 =              new TextField();
    private TextField  shipAddress1Field2 =             new TextField();
    private TextField  shipAddress2Field2 =             new TextField();
    private TextField  shipCityField2 =                 new TextField();
    private TextField  shipRegionField2 =               new TextField();
    private TextField  shipPostCodeField2 =             new TextField();
    private TextField  shipCountryField2 =              new TextField();
    private TextField  shipEmailField2 =                new TextField();
    private TextField  billNameField2 =                 new TextField();
    private TextField  billPhoneField2 =                new TextField();
    private TextField  billCompanyField2 =              new TextField();
    private TextField  billAddress1Field2 =             new TextField();
    private TextField  billAddress2Field2 =             new TextField();
    private TextField  billCityField2 =                 new TextField();
    private TextField  billRegionField2 =               new TextField();
    private TextField  billPostCodeField2 =             new TextField();
    private TextField  billCountryField2 =              new TextField();
    private TextField  billEmailField2 =                new TextField();
    private DatePicker dateShippedPicker2 =             new DatePicker();
    private TextField  firstLicenseNumField2 =          new TextField();
    private TextField  firstCertificateCompanyField2 =  new TextField();
    private TextField  secondLicenseNumField2 =         new TextField();
    private TextField  secondCertificateCompanyField2 = new TextField();
    private TextField  commentsField2 =                 new TextField();

    private Integer itemEditing = 0;

    private Integer entrySceneWidth = 450;
    private Integer entrySceneHeight = 700;
    private double defaultColWidth = ((1.0-0.04)/16.0);

    private Stage errorPopup;
    private Stage savePopup;
    private Stage deletePopup;
    private Stage removeCertificatePopup;
    private ContextMenu contextMenu;

    Client tempClient = new Client();


    public static void main(String[] args){

        sT = new SampleTracker();
        
        try {
            pH = new PropertiesHandler();
        } catch (Exception e) {
            errorString = e.getMessage();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FileInputStream inputStream = new FileInputStream("TrackerIcon.png");
            IconImage = new Image(inputStream);
            primaryStage.getIcons().add(IconImage);
        } catch (Exception e) {
            System.out.println("loading icon exception: " + e.getMessage());
            // errorString = e.getMessage();
        }

        try {
            populateTrackerData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorString = e.getMessage();
        }
        // columnConfigData = pH.getColumnConfigData();
        // setColumnsShown(); // Try to load visible column data from properties

        primaryStage.setTitle("Data to send free samples");

        BorderPane mainBorderPane = new BorderPane();
        VBox propertiesBox = new VBox(10);
        propertiesBox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(mainBorderPane, 1500, 700);
        primaryStage.setScene(scene);
        final Label tableLabel = new Label("Table of clients"); //sets the main title
        tableLabel.setFont(new Font("Arial", 16));

        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(10,10,10,10));

        Button undoButton = new Button("Undo");
        undoButton.setOnAction(Event->{
            if(sT.undo()){
                table.refresh();
            }
        });

        Button redoButton = new Button("Redo");
        redoButton.setOnAction(Event->{
            if(sT.redo()){
                table.refresh();
            }
        });

        Button configColumnsButton = new Button("Configure Columns");
        configColumnsButton.setOnAction(Event->{
            setColumnsShownCheckboxes();
            configColumnsStage.show();
        });
        
        // topBox.getChildren().addAll(tableLabel,undoButton,redoButton);
        topBox.getChildren().addAll(tableLabel, configColumnsButton);

        configureTable();
        configurePlainTextWindow(primaryStage);
        configureEntryWindow(primaryStage);
        configureURLTextWindow(primaryStage);
        configureChangesWindow(primaryStage);
        configureErrorPopup(primaryStage);
        configureSavePopup(primaryStage);
        configureContextMenu(primaryStage);
        configureDelete(primaryStage);
        configureRemoveCertificate(primaryStage);
        configureConfigColumnsStage(primaryStage);

        if (IconImage != null) {
            entryStage.getIcons().add(IconImage);
            changesStage.getIcons().add(IconImage);
            plainTextStage.getIcons().add(IconImage);
            URLTextStage.getIcons().add(IconImage);
            savePopup.getIcons().add(IconImage);
            deletePopup.getIcons().add(IconImage);
            removeCertificatePopup.getIcons().add(IconImage);
            
            
        }

        Button searchButton = new Button("Search/Add New");
        searchButton.setOnAction(Event ->{
            // plainTextStage.show();
            clearPrimaryEntryFields();
            entryStage.show();

        });

        Button importNewClientsButton = new Button("Import Clients");
        importNewClientsButton.setOnAction(Event ->{
            importNewClientsFromFile(primaryStage);

        });

        Button lastButton = new Button("Last");
        lastButton.setOnAction(Event ->{
            entryStage.show();
        });

        Button resetButton = new Button("View All");
        resetButton.setOnAction(Event->{
            table.setItems(FXCollections.observableArrayList(sT.getClientList()));
            table.scrollTo(0);
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(Event ->{
            sT.save();
            //pH.setColumnConfigData(columnConfigData);
            pH.StoreProperties();
        });

        Button setDataPathButton = new Button("Set Location of Client Data");
        setDataPathButton.setOnAction(Event ->{
            setDataPath(primaryStage);
        });

        Button bulkSearchButton = new Button("Repeat Bulk Search");
        bulkSearchButton.setOnAction(Event ->{
            bulkSearchNewClients(primaryStage);
        });



        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(searchButton,importNewClientsButton,lastButton, resetButton,saveButton, setDataPathButton, bulkSearchButton);
        buttons.setPadding(new Insets(10, 10, 100, 100));



        propertiesBox.getChildren().addAll(topBox, table); //this line finalizes the changes and puts in the columns
        propertiesBox.prefWidthProperty().bind(scene.widthProperty().multiply(0.8));
        mainBorderPane.setCenter(propertiesBox);
        mainBorderPane.setBottom(buttons);

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        primaryStage.show();

        if (pH == null) {
            errorString = "Error: 2987684. Could not initialize property handler";
        }

        // System.out.println("Program has started");
        if (!errorString.equals("")) {
            errorLabel.setText(errorString);
            errorString = "";
            errorPopup.show();
        }

    }

    private void importNewClientsFromFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.csv"));

        String filePath = desktopLocation;
        fileChooser.setInitialDirectory(new File(filePath));
        File selectedFile =  fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Function to import the clients
            List<Integer> clients_added_list = sT.importNewClients(selectedFile);
            newClientsAdded = clients_added_list.get(0);
            if (newClientsAdded > 0) {
                table.setItems(FXCollections.observableArrayList(sT.getClientList()));
                sT.setSaveRequired();
            }    
                errorLabel.setText("Imported " + newClientsAdded + " new Clients. Rejected " + clients_added_list.get(1) + " clients");
                errorPopup.show();
        }
    }

    private void bulkSearchNewClients(Stage primaryStage) {
        if (newClientsAdded > 0) {
            List<Client> clientList = sT.getClientList();
            Set<Client> filteredClientSet = new LinkedHashSet<>();
            List<Client> filteredClientList = new ArrayList<>();
            if (clientList.size() > newClientsAdded) {
                List<Client> newClientSubList = clientList.subList(clientList.size() - newClientsAdded, clientList.size());

                for (Client newClient : newClientSubList) {
                    List<Client> tempClientList = sT.filterClients(newClient, clientList);
                    if (tempClientList.size() > 1) {
                        filteredClientSet.addAll(tempClientList);
                    }
                    
                }
                filteredClientList.addAll(filteredClientSet);
                table.setItems(FXCollections.observableArrayList(filteredClientList));
                table.scrollTo(0);
                if (filteredClientList.size() < 1) {
                    errorLabel.setText("Bulk search did not return any results");
                    errorPopup.show();
                }
            }
        } else {
            errorLabel.setText("No record of newly imported clients to search");
            errorPopup.show();
        }
    }

    private void setDataPath(Stage primaryStage) {
        String dataPath = pH.getTrackerDataPath();
        if (dataPath == null) {
            dataPath = desktopLocation;
        }
        
        File file = new File(dataPath);
        if (!file.exists()) {
            dataPath = desktopLocation;
        }
        
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setInitialDirectory(new File(dataPath));
        
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory == null) {
            // no directory selected
        } else {
            if (!pH.setTrackerDataPath(selectedDirectory.getAbsolutePath())) {
                System.out.println("Could not save directory path");
                errorLabel.setText("Error: 87205812. Could not set data path");
                errorPopup.show();
            }
        }

        sT.setFileLocation(pH.getTrackerDataPath());
        try {
            if (!sT.populate()) {
                errorLabel.setText("No Client data found");
                errorPopup.show();
            }
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
            errorPopup.show();
        }
        table.setItems(FXCollections.observableArrayList(sT.getClientList()));
        table.scrollTo(0);

    }

    private void populateTrackerData() throws Exception{
        String trackerDataPath = new String();
        if (pH != null) {
            trackerDataPath = pH.getTrackerDataPath();
        }
        if (!trackerDataPath.equals("")) {
            sT.setFileLocation(trackerDataPath);
            try {
                if (!sT.populate()) {
                    throw new Exception("No data found");
                }
            } catch (IOException e) {
                errorString = e.getMessage();
            }
        }
    }

    private void setColumnsShownCheckboxes() {
        List<String[]> configData = pH.getColumnConfigData();
        for (int i = 0; i < configColumnsBox.getChildren().size(); i++) {
            Node nodeOut = configColumnsBox.getChildren().get(i);
            if (nodeOut instanceof HBox) {
                String columnKey = new String();
                CheckBox tempCheckBox = null;
                for (Node nodeIn:((HBox)nodeOut).getChildren()) {
                    if(nodeIn instanceof Label) {
                        columnKey = tempClient.getAttributeNameAsString(((Label)nodeIn).getText());
                    } else if (nodeIn instanceof CheckBox) {
                        tempCheckBox = (CheckBox)nodeIn;
                    }
                }
                if (tempCheckBox != null) {
                    tempCheckBox.setSelected(false);
                    if (configData != null) {
                        for (int j = 0; j < configData.size(); j++) {
                            if (Objects.equals(configData.get(j)[0], columnKey)) {
                                tempCheckBox.setSelected(true);
                            }
                        }
                    } else {
                        tempCheckBox.setSelected(true);
                    }
                    
                }
            }
        }
    }

    private List<String[]> reorganizeColumns(boolean resize) {
        List<String[]> columnConfigData = pH.getColumnConfigData();
        List<String[]> newOrder = new ArrayList<String[]>();
        for(TableColumn<Client, ?> column : table.getColumns()) {
            String attributeKey = tempClient.getAttributeNameAsString(column.getText());
            String[] columnList = new String[3];
            boolean columnFound = false;
            if (columnConfigData != null) {
                for (int i = 0; i < columnConfigData.size(); i++) {
                    if (Objects.equals(columnConfigData.get(i)[0], attributeKey)){
                        columnList = columnConfigData.get(i);
                        columnFound = true;
                    }
                }
            }
            if (resize) {
                double columnWidth = column.getWidth();
                columnList[2] = String.valueOf(((1 / table.widthProperty().get()) * columnWidth));
            }
            if (!columnFound) {
                columnList[0] = attributeKey;
                columnList[1] = "true";
                double columnWidth = column.getWidth();
                columnList[2] = String.valueOf(((1 / table.widthProperty().get()) * columnWidth));
            }
            newOrder.add(columnList);
        }
        if (!resize) { // crude way to stop requiring save when the table loads up initially
            sT.setSaveRequired();
        }
        return newOrder;
    } 

    private TableColumn<Client, ?> tableColumnCreate(String columnLabel, String CellFactoryKey, double colWidthFactor, List<String[]> configData, boolean configNull) {
        TableColumn<Client, String> tempCol = new TableColumn<>(columnLabel);
        tempCol.setCellValueFactory(new PropertyValueFactory<>(CellFactoryKey));
        tempCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidthFactor));

        if (configNull) {
            configData.add(new String[]{CellFactoryKey, "true", String.valueOf(colWidthFactor)});
        }
        tempCol.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                pH.setColumnConfigData(reorganizeColumns(true));
            }
        });
        return tempCol;
    }

    private void configureTable(){
        if (table == null) {
            table = new TableView<>();
        } else {
            table.getColumns().clear();
        }
        
        ObservableList<Client> clientList = FXCollections.observableArrayList(sT.getClientList());
        table.setItems(clientList);


        //set table to fill the entire scene
        table.setFixedCellSize(25);
        table.setPrefHeight(550);

        List<String[]> configData = pH.getColumnConfigData();
        if (configData == null) {
            configData = new ArrayList<String[]>();
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("index"),                    "index",                     0.031,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("clientID"),                 "clientID",                  0.031,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipName"),                 "shipName",                  defaultColWidth-0.0042, configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipPhone"),                "shipPhone",                 defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipCompany"),              "shipCompany",               defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipAddress1"),             "shipAddress1",              defaultColWidth+0.025,  configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipAddress2"),             "shipAddress2",              defaultColWidth-0.025,  configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipCity"),                 "shipCity",                  defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipRegion"),               "shipRegion",                defaultColWidth/2,      configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipPostCode"),             "shipPostCode",              defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipCountry"),              "shipCountry",               defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("shipEmail"),                "shipEmail",                 defaultColWidth+0.04,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billName"),                 "billName",                  defaultColWidth-0.0042, configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billPhone"),                "billPhone",                 defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billCompany"),              "billCompany",               defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billAddress1"),             "billAddress1",              defaultColWidth+0.025,  configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billAddress2"),             "billAddress2",              defaultColWidth-0.025,  configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billCity"),                 "billCity",                  defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billRegion"),               "billRegion",                defaultColWidth/2,      configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billPostCode"),             "billPostCode",              defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billCountry"),              "billCountry",               defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("billEmail"),                "billEmail",                 defaultColWidth+0.04,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("dateShipped"),              "dateShipped",               defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("firstLicenseNum"),          "firstLicenseNum",           defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("firstCertificateCompany"),  "firstCertificateCompany",   defaultColWidth+0.03,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("secondLicenseNum"),         "secondLicenseNum",          defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("secondCertificateCompany"), "secondCertificateCompany",  defaultColWidth+0.03,   configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("comments"),                 "comments",                  defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("firstCertificate"),         "firstCertificate",          defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("secondCertificate"),        "secondCertificate",         defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("dateClientAdded"),          "dateClientAdded",           defaultColWidth,        configData, true));
            table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString("dateClientEdited"),         "dateClientEdited",          defaultColWidth,        configData, true));
            pH.setColumnConfigData(configData);
        } else {
            for(int i = 0; i<configData.size(); i++){
                if (Boolean.parseBoolean(configData.get(i)[1])) {
                    table.getColumns().add(tableColumnCreate(tempClient.getAttributeNameDispString(configData.get(i)[0]), configData.get(i)[0], Double.parseDouble(configData.get(i)[2]), configData, false));
                }
            }
        }

        table.setRowFactory( tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) ) {
                    tempClient = row.getItem();
                    if(tempClient.getFirstCertificate().isEmpty()){
                        contextMenu.getItems().get(1).setDisable(false);
                        contextMenu.getItems().get(3).setDisable(true);
                        contextMenu.getItems().get(5).setDisable(true);
                        contextMenu.getItems().get(7).setDisable(true);
                    }else{
                        contextMenu.getItems().get(1).setDisable(true);
                        contextMenu.getItems().get(3).setDisable(false);
                        contextMenu.getItems().get(5).setDisable(false);
                        contextMenu.getItems().get(7).setDisable(false);
                    }
                    if(tempClient.getSecondCertificate().isEmpty()){
                        contextMenu.getItems().get(2).setDisable(false);
                        contextMenu.getItems().get(4).setDisable(true);
                        contextMenu.getItems().get(6).setDisable(true);
                        contextMenu.getItems().get(8).setDisable(true);
                    } else {
                        contextMenu.getItems().get(2).setDisable(true);
                        contextMenu.getItems().get(4).setDisable(false);
                        contextMenu.getItems().get(6).setDisable(false);
                        contextMenu.getItems().get(8).setDisable(false);
                    }
                    contextMenu.show(table, event.getScreenX(), event.getScreenY());

                }else if(event.getButton() == MouseButton.PRIMARY){
                    if(contextMenu.isShowing()){
                        contextMenu.hide();
                    }
                }
            });
            return row;
        });

        table.getColumns().addListener(new ListChangeListener<TableColumn<Client, ?>>() {
            public void onChanged(Change<? extends TableColumn<Client, ?>> c) {
                System.out.println("Table List was changed");
                if (allowReorg) { //done to prevent wiping configs while redrawing table
                    pH.setColumnConfigData(reorganizeColumns(false));
                }   
            }
        });
    }

    private HBox configColumnsWindowLineCreate(String labelString, boolean defaultState) {
        HBox tempHBox = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tempLabel = new Label(labelString);
        tempHBox.setAlignment(Pos.BASELINE_CENTER);
        CheckBox tempCheckBox = new CheckBox();

        tempHBox.setMaxWidth(entrySceneWidth - 40);
        tempHBox.getChildren().addAll(tempLabel, spacer, tempCheckBox);
        return tempHBox;
    }

    private void configureConfigColumnsStage(Stage primaryStage) {
        configColumnsStage = new Stage();
        configColumnsBox = new VBox(12);
        VBox configColumnsIntermediateBox = new VBox();
        ScrollPane configColumnsScrollPane = new ScrollPane();
        Scene configColumnsScene = new Scene(configColumnsIntermediateBox,entrySceneWidth, entrySceneHeight);

        configColumnsStage.initModality(Modality.APPLICATION_MODAL);
        configColumnsStage.initOwner(primaryStage);
        configColumnsStage.setResizable(false);
        configColumnsStage.setScene(configColumnsScene);

        configColumnsBox.setAlignment(Pos.CENTER);

        configColumnsScrollPane.setContent(configColumnsBox);

        List<String[]> configData = pH.getColumnConfigData();

        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getIndexDispString(),                    true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getclientIDDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipNameDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipPhoneDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipCompanyDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipAddress1DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipAddress2DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipCityDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipRegionDispString(),               true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipPostCodeDispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipCountryDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getShipEmailDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillNameDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillPhoneDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillCompanyDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillAddress1DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillAddress2DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillCityDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getBillRegionDispString(),               true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillPostCodeDispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillCountryDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillEmailDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getdateShippedDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getfirstLicenseNumDispString(),          true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getfirstCertificateCompanyDispString(),  true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getsecondLicenseNumDispString(),         true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getsecondCertificateCompanyDispString(), true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getcommentsDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getfirstCertificateDispString(),         true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getsecondCertificateDispString(),        true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getdateClientAddedDispString(),          true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getdateClientEditedDispString(),         true));



        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            configColumnsStage.close();
        });
        Button searchButton = new Button("Set Columns");
        searchButton.setOnAction(Event->{
            userSetColumnsShown();
            configColumnsStage.close();
        });

        HBox buttons = new HBox(30);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancelButton, searchButton);
        configColumnsIntermediateBox.getChildren().addAll(configColumnsScrollPane, buttons);
    }

    private void userSetColumnsShown() {
        pH.setColumnConfigData(reorganizeColumns(true));
        List<String[]> configData = pH.getColumnConfigData();
        for (int i = 0; i < configColumnsBox.getChildren().size(); i++) {
            Node nodeOut = configColumnsBox.getChildren().get(i);
            if (nodeOut instanceof HBox) {
                String[] columnData = new String[3];
                for (Node nodeIn:((HBox)nodeOut).getChildren()) {
                    if(nodeIn instanceof Label) {
                        columnData[0] = tempClient.getAttributeNameAsString(((Label)nodeIn).getText());
                    } else if (nodeIn instanceof CheckBox) {
                        columnData[1] = String.valueOf(((CheckBox)nodeIn).isSelected());
                    }
                }
                columnData[2] = String.valueOf(defaultColWidth);

                boolean found = false;
                for (int j = 0; j < configData.size(); j++) {
                    if (Objects.equals(configData.get(j)[0], columnData[0])) {
                        found = true;
                        if (Objects.equals(columnData[1], "false")) {
                            configData.remove(j);
                        }
                    }
                }
                if (!found && Objects.equals(columnData[1], "true")) {
                    configData.add(columnData);
                }
            }
        }
        pH.setColumnConfigData(configData);
        allowReorg = false; // Don't let it erase configs
        configureTable();
        allowReorg = true;
        table.refresh();
    }



    private HBox entryWindowLineCreate(String labelString, String promptString, Integer fieldWidth, TextField textField) {
        HBox tempHBox = new HBox();
        Region spacer = new Region();
        Region spacer1 = new Region();
        spacer1.setPrefWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label tempLabel = new Label(labelString);
        tempHBox.setAlignment(Pos.BASELINE_CENTER);
        textField.setPrefWidth(fieldWidth);
        textField.setPromptText(promptString);
        tempHBox.setMaxWidth(entrySceneWidth - 40);
        tempHBox.getChildren().addAll(spacer1,tempLabel, spacer, textField);
        return tempHBox;
    }
    

    private void configureEntryWindow(Stage primaryStage){
        entryStage = new Stage();
        entryBox = new VBox(12);
        VBox entryIntermediateBox = new VBox(12);
        ScrollPane entryScrollPane = new ScrollPane();
        Scene entryScene = new Scene(entryIntermediateBox,entrySceneWidth, entrySceneHeight);

        entryStage.initModality(Modality.APPLICATION_MODAL);
        entryStage.initOwner(primaryStage);
        entryStage.setResizable(false);
        entryStage.setScene(entryScene);

        entryBox.setAlignment(Pos.CENTER);
        entryIntermediateBox.setAlignment(Pos.CENTER);

        entryScrollPane.setContent(entryBox);

        Label searchable = new Label("\"*\" Denotes searchable fields");
        entryBox.getChildren().add(searchable);

        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd yyyy").toFormatter();
        dateShippedPicker = new DatePicker();
        dateShippedPicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null){
                    return dtf.format(localDate).toUpperCase();
                }else{
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String s) {
                if(s != null && !s.isEmpty()){
                    return LocalDate.parse(s, dtf);
                }else{
                    return null;
                }
            }
        });

        Label shippedDateLabel = new Label(tempClient.getdateShippedDispString());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Region spacer1 = new Region();
        spacer1.setPrefWidth(10);
        HBox shippedDate = new HBox();
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.setMaxWidth(entrySceneWidth - 40);
        shippedDate.getChildren().addAll(spacer1, shippedDateLabel, spacer, dateShippedPicker);

        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getclientIDDispString(),                 "123456",                250, clientIdField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipNameDispString(),                 "Eyelash Express",       250, shipNameField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipPhoneDispString(),                "555-555-5555",          250, shipPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCompanyDispString(),              "Lash Inc.",             250, shipCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipAddress1DispString(),             "12345 Pine ST",         250, shipAddress1Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipAddress2DispString(),             "Unit 22",               250, shipAddress2Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCityDispString(),                 "New York",              250, shipCityField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipRegionDispString(),               "Alberta",               250, shipRegionField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipPostCodeDispString(),             "T7X 1J4",               250, shipPostCodeField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCountryDispString(),              "Canada",                250, shipCountryField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getShipEmailDispString(),                "janedoe@gmail.com",     250, shipEmailField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillNameDispString(),                 "John Doe",              250, billNameField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillPhoneDispString(),                "444-444-4444",          250, billPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillCompanyDispString(),              "Lashfast",              250, billCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillAddress1DispString(),             "35 King St",            250, billAddress1Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillAddress2DispString(),             "6",                     250, billAddress2Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillCityDispString(),                 "Palm Springs",          250, billCityField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getBillRegionDispString(),               "California",            250, billRegionField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillPostCodeDispString(),             "12345-1234",            250, billPostCodeField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCountryDispString(),              "United States",         250, billCountryField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillEmailDispString(),                "JohnRules@hotmail.com", 250, billEmailField));
        entryBox.getChildren().add(shippedDate);
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getfirstLicenseNumDispString(),          "",                      250, firstLicenseNumField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getfirstCertificateCompanyDispString(),  "",                      250, firstCertificateCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getsecondLicenseNumDispString(),         "",                      250, secondLicenseNumField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getsecondCertificateCompanyDispString(), "",                      250, secondCertificateCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcommentsDispString(),                 "",                      300, commentsField));

        Button addButton = new Button("Add Client");
        addButton.setOnAction(Event ->{
            entryStage.close();
            List<String> tempClientList= new ArrayList<>();

            for (int i = 1; i < entryBox.getChildren().size(); i++) { // Skip the first element. that's a fixed string
                Node nodeOut = entryBox.getChildren().get(i);
                if (nodeOut instanceof HBox) {
                    for (Node nodeIn:((HBox)nodeOut).getChildren()) {
                        if(nodeIn instanceof TextField) {
                            tempClientList.add(((TextField)nodeIn).getText());
                        } else if (nodeIn instanceof DatePicker) {
                            tempClientList.add(dateShippedPicker.getConverter().toString(((DatePicker)nodeIn).getValue()));
                        }
                    }
                }
            }

            sT.addClient(tempClientList);
            table.setItems(FXCollections.observableArrayList(sT.getClientList()));
            table.refresh();
            table.scrollTo(sT.getClientList().get(sT.getClientList().size()-1));

        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            entryStage.close();
        });
        Button searchButton = new Button("Search");
        searchButton.setOnAction(Event->{
            entryStage.close();
            searchCurrentClient();

        });

        HBox buttons = new HBox(30);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancelButton, searchButton, addButton);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        entryIntermediateBox.getChildren().addAll(entryScrollPane, buttons, spacer2);

    }

    private void configurePlainTextWindow(Stage primaryStage) throws Exception {
        plainTextStage = new Stage();
        plainTextStage.initModality(Modality.APPLICATION_MODAL);
        plainTextStage.initOwner(primaryStage);
        plainTextStage.setResizable(false);

        plainTextBox = new VBox(10);
        plainTextBox.setAlignment(Pos.CENTER);
        Scene plainTextScene = new Scene(plainTextBox,450, 400);
        plainTextStage.setScene(plainTextScene);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(Event ->{
            plainTextStage.close();
            clearPrimaryEntryFields();
            String textData = textArea.getText();
            Map<String, String> attributeMap = new HashMap<>();
            if (!textData.isEmpty()) {
                attributeMap = sT.parseJson(textArea.getText());
            }
            if (!attributeMap.isEmpty()) {
                fillEntryStageWithMap(attributeMap);
                textArea.setText("");
            }
            plainTextStage.close();
            entryStage.show();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            textArea.setText("");
            plainTextStage.close();
        });

        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(cancelButton, continueButton);
        buttons.setAlignment(Pos.CENTER);

        textArea = new TextArea();
        textArea.setPrefRowCount(12);

        plainTextBox.getChildren().add(new Text("Paste client info"));
        plainTextBox.getChildren().addAll(textArea, buttons);

    }

    private void configureURLTextWindow(Stage primaryStage) throws Exception {
        URLTextStage = new Stage();
        URLTextStage.initModality(Modality.APPLICATION_MODAL);
        URLTextStage.initOwner(primaryStage);
        URLTextStage.setResizable(false);

        URLTextBox = new VBox(10);
        URLTextBox.setAlignment(Pos.CENTER);
        Scene URLTextScene = new Scene(URLTextBox,400, 200);
        URLTextStage.setScene(URLTextScene);

        Button continueButton = new Button("Save URL");
        continueButton.setOnAction(Event ->{
            URLTextStage.close();
            String textData = textArea.getText();
            if (!textData.isEmpty()) {
                Client clientToChange = sT.getClient(tempClient.getIndex().toString());
                itemEditing = clientToChange.getIndex();
                try {
                    sT.addCertificateURL(itemEditing, textData, certificateNumber);
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                    errorPopup.show();
                }
            }
            textArea.setText("");
            URLTextStage.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            textArea.setText("");
            URLTextStage.close();
        });

        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(cancelButton, continueButton);
        buttons.setAlignment(Pos.CENTER);

        textArea = new TextArea();
        textArea.setPrefRowCount(1);

        URLTextBox.getChildren().add(new Text("Paste link to image"));
        URLTextBox.getChildren().addAll(textArea, buttons);
    }

    private void fillEntryStageWithMap(Map<String, String> newClientData) {
        for(Map.Entry<String, String> entry : newClientData.entrySet()) {
            String entryKey = entry.getKey();
            switch (entryKey) {
                case "clientID":
                    clientIdField.setText(entry.getValue());
                    break;
                case "shipName":
                    shipNameField.setText(entry.getValue());
                    break;
                case "shipPhone":
                    shipPhoneField.setText(entry.getValue());
                    break;
                case "shipCompany":
                    shipCompanyField.setText(entry.getValue());
                    break;
                case "shipAddress1":
                    shipAddress1Field.setText(entry.getValue());
                    break;
                case "shipAddress2":
                    shipAddress2Field.setText(entry.getValue());
                    break;
                case "shipCity":
                    shipCityField.setText(entry.getValue());
                    break;
                case "shipRegion":
                    shipRegionField.setText(entry.getValue());
                    break;
                case "shipPostCode":
                    shipPostCodeField.setText(entry.getValue());
                    break;
                case "shipCountry":
                    shipCountryField.setText(entry.getValue());
                    break;
                case "shipEmail":
                    shipEmailField.setText(entry.getValue());
                    break;
                case "billName":
                    billNameField.setText(entry.getValue());
                    break;
                case "billPhone":
                    billPhoneField.setText(entry.getValue());
                    break;
                case "billCompany":
                    billCompanyField.setText(entry.getValue());
                    break;
                case "billAddress1":
                    billAddress1Field.setText(entry.getValue());
                    break;
                case "billAddress2":
                    billAddress2Field.setText(entry.getValue());
                    break;
                case "billCity":
                    billCityField.setText(entry.getValue());
                    break;
                case "billRegion":
                    billRegionField.setText(entry.getValue());
                    break;
                case "billPostCode":
                    billPostCodeField.setText(entry.getValue());
                    break;
                case "billCountry":
                    billCountryField.setText(entry.getValue());
                    break;
                case "billEmail":
                    billEmailField.setText(entry.getValue());
                    break;
                default:
            }
        }
    }

    private void configureChangesWindow(Stage primaryStage){
        changesStage = new Stage();
        changesBox = new VBox(12);
        VBox changesIntermediateBox = new VBox(12);
        ScrollPane changesScrollPane = new ScrollPane();
        Scene changesScene = new Scene(changesIntermediateBox,entrySceneWidth, entrySceneHeight);

        changesStage.initModality(Modality.APPLICATION_MODAL);
        changesStage.initOwner(primaryStage);
        changesStage.setResizable(false);
        changesStage.setScene(changesScene);
        
        changesBox.setAlignment(Pos.CENTER);
        
        changesScrollPane.setContent(changesBox);

        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd yyyy").toFormatter();
        dateShippedPicker2 = new DatePicker();
        dateShippedPicker2.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null){
                    return dtf.format(localDate).toUpperCase();
                }else{
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String s) {
                if(s != null && !s.isEmpty()){
                    return LocalDate.parse(s, dtf);
                }else{
                    return null;
                }
            }
        });
        Label shippedDateLabel = new Label(tempClient.getdateShippedDispString());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Region spacer1 = new Region();
        spacer1.setPrefWidth(10);
        HBox shippedDate = new HBox();
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.setMaxWidth(entrySceneWidth - 40);
        shippedDate.getChildren().addAll(spacer1, shippedDateLabel, spacer, dateShippedPicker2);

        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getclientIDDispString(),                 "123456",                250, clientIdField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipNameDispString(),                 "Eyelash Express",       250, shipNameField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipPhoneDispString(),                "555-555-5555",          250, shipPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCompanyDispString(),              "Lash Inc.",             250, shipCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipAddress1DispString(),             "12345 Pine ST",         250, shipAddress1Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipAddress2DispString(),             "Unit 22",               250, shipAddress2Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCityDispString(),                 "New York",              250, shipCityField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipRegionDispString(),               "Alberta",               250, shipRegionField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipPostCodeDispString(),             "T7X 1J4",               250, shipPostCodeField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipCountryDispString(),              "Canada",                250, shipCountryField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getShipEmailDispString(),                "janedoe@gmail.com",     250, shipEmailField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillNameDispString(),                 "John Doe",              250, billNameField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillPhoneDispString(),                "444-444-4444",          250, billPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillCompanyDispString(),              "Lashfast",              250, billCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillAddress1DispString(),             "35 King St",            250, billAddress1Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillAddress2DispString(),             "6",                     250, billAddress2Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillCityDispString(),                 "Palm Springs",          250, billCityField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getBillRegionDispString(),               "California",            250, billRegionField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillPostCodeDispString(),             "12345-1234",            250, billPostCodeField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCountryDispString(),              "United States",         250, billCountryField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillEmailDispString(),                "JohnRules@hotmail.com", 250, billEmailField2));
        changesBox.getChildren().add(shippedDate);
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getfirstLicenseNumDispString(),          "",                      250, firstLicenseNumField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getfirstCertificateCompanyDispString(),  "",                      250, firstCertificateCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getsecondLicenseNumDispString(),         "",                      250, secondLicenseNumField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getsecondCertificateCompanyDispString(), "",                      250, secondCertificateCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcommentsDispString(),                 "",                      300, commentsField2));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            changesStage.close();
        });

        Button changesButton = new Button("Save Changes");
        changesButton.setOnAction(Event -> {
            List<String> tempClientList = new ArrayList<>();
            for (int i = 0; i < changesBox.getChildren().size(); i++) {
                Node nodeOut = changesBox.getChildren().get(i);
                if (nodeOut instanceof HBox) {
                    for (Node nodeIn:((HBox)nodeOut).getChildren()) {
                        if(nodeIn instanceof TextField) {
                            tempClientList.add(((TextField)nodeIn).getText());
                        } else if (nodeIn instanceof DatePicker) {
                            tempClientList.add(((DatePicker)nodeIn).getConverter().toString(((DatePicker)nodeIn).getValue()));
                        }
                    }
                }
            }
            clearSecondaryEntryFields();
            sT.makeChanges(tempClientList, itemEditing);

            itemEditing = 0;
            changesStage.close();
            table.refresh();
        });

        // Add search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(Event->{
            changesStage.close();
            copyFields2ToFields();
            searchCurrentClient();

        });

        HBox buttons2 = new HBox(30);
        buttons2.setAlignment(Pos.CENTER);
        buttons2.getChildren().addAll(cancelButton, changesButton, searchButton);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        changesIntermediateBox.getChildren().addAll(changesScrollPane, buttons2, spacer2);

    }



    private void searchCurrentClient(){
        Client temp_client = new Client();
        temp_client.setClientID(                clientIdField.getText());
        temp_client.setShipName(                shipNameField.getText());
        temp_client.setShipPhone(               shipPhoneField.getText());
        temp_client.setShipCompany(             shipCompanyField.getText());
        temp_client.setShipAddress1(            shipAddress1Field.getText());
        temp_client.setShipAddress2(            shipAddress2Field.getText());
        temp_client.setShipCity(                shipCityField.getText());
        temp_client.setShipRegion(              shipRegionField.getText());
        temp_client.setShipPostCode(            shipPostCodeField.getText());
        temp_client.setShipCountry(             shipCountryField.getText());
        temp_client.setShipEmail(               shipEmailField.getText());
        temp_client.setBillName(                billNameField.getText());
        temp_client.setBillPhone(               billPhoneField.getText());
        temp_client.setBillCompany(             billCompanyField.getText());
        temp_client.setBillAddress1(            billAddress1Field.getText());
        temp_client.setBillAddress2(            billAddress2Field.getText());
        temp_client.setBillCity(                billCityField.getText());
        temp_client.setBillRegion(              billRegionField.getText());
        temp_client.setBillPostCode(            billPostCodeField.getText());
        temp_client.setBillCountry(             billCountryField.getText());
        temp_client.setBillEmail(               billEmailField.getText());
        temp_client.setFirstLicenseNum(         firstLicenseNumField.getText());
        temp_client.setFirstCertificateCompany( firstCertificateCompanyField.getText());
        temp_client.setSecondLicenseNum(        secondLicenseNumField.getText());
        temp_client.setSecondCertificateCompany(secondCertificateCompanyField.getText());
        temp_client.setComments(                commentsField.getText());

        performSearch(temp_client);
    }

    private void performSearch(Client temp_client) {
        List<Client> filteredClients = sT.getClientList();
        filteredClients = sT.filterClients(temp_client, filteredClients);
        table.setItems(FXCollections.observableArrayList(filteredClients));
        table.scrollTo(0);
        if(filteredClients.size()<1){
            errorLabel.setText("Search did not return any results");
            errorPopup.show();
        }
    }

    public void clearPrimaryEntryFields() {
        clientIdField.clear();
        shipNameField.clear();
        shipPhoneField.clear();
        shipCompanyField.clear();
        shipAddress1Field.clear();
        shipAddress2Field.clear();
        shipCityField.clear();
        shipRegionField.clear();
        shipPostCodeField.clear();
        shipCountryField.clear();
        shipEmailField.clear();
        billNameField.clear();
        billPhoneField.clear();
        billCompanyField.clear();
        billAddress1Field.clear();
        billAddress2Field.clear();
        billCityField.clear();
        billRegionField.clear();
        billPostCodeField.clear();
        billCountryField.clear();
        billEmailField.clear();
        dateShippedPicker.setValue(null);
        dateShippedPicker.getEditor().clear();
        firstLicenseNumField.clear();
        firstCertificateCompanyField.clear();
        secondLicenseNumField.clear();
        secondCertificateCompanyField.clear();
        commentsField.clear();
    }

    public void clearSecondaryEntryFields(){
        clientIdField2.clear();
        shipNameField2.clear();
        shipPhoneField2.clear();
        shipCompanyField2.clear();
        shipAddress1Field2.clear();
        shipAddress2Field2.clear();
        shipCityField2.clear();
        shipRegionField2.clear();
        shipPostCodeField2.clear();
        shipCountryField2.clear();
        shipEmailField2.clear();
        billNameField2.clear();
        billPhoneField2.clear();
        billCompanyField2.clear();
        billAddress1Field2.clear();
        billAddress2Field2.clear();
        billCityField2.clear();
        billRegionField2.clear();
        billPostCodeField2.clear();
        billCountryField2.clear();
        billEmailField2.clear();
        dateShippedPicker2.setValue(null);
        dateShippedPicker2.getEditor().clear();
        firstLicenseNumField2.clear();
        firstCertificateCompanyField2.clear();
        secondLicenseNumField2.clear();
        secondCertificateCompanyField2.clear();
        commentsField2.clear();
    }

    private void configureErrorPopup(Stage primaryStage) {
        errorPopup = new Stage();
        errorPopup.initModality(Modality.APPLICATION_MODAL);
        errorPopup.initOwner(primaryStage);
        errorPopup.setResizable(false);

        VBox errorPopBox = new VBox(20);
        errorLabel = new Label("");
        errorPopBox.setAlignment(Pos.CENTER);
        Scene loadingPopScene = new Scene(errorPopBox,700, 100);
        errorPopup.setScene(loadingPopScene);

        HBox buttons = new HBox(20);

        Button okButton = new Button("Ok");
        okButton.setOnAction(Event ->{
            if (errorLabel.getText().contains("Imported ")) {
                errorLabel.setText("");
                errorPopup.close();
                bulkSearchNewClients(primaryStage);
            } else {
                errorLabel.setText("");
                errorPopup.close();
            }
        });

        buttons.getChildren().add(okButton);

        errorPopBox.getChildren().addAll(errorLabel,buttons);
        errorLabel.setAlignment(Pos.BASELINE_CENTER);
        buttons.setAlignment(Pos.BASELINE_CENTER);

    }

    private void configureSavePopup(Stage primaryStage){
        savePopup = new Stage();
        savePopup.initModality(Modality.APPLICATION_MODAL);
        savePopup.initOwner(primaryStage);
        savePopup.setResizable(false);

        //savePopup.setTitle("");

        VBox savePopBox = new VBox(20);
        Label label = new Label("Save your changes to this file?");
        savePopBox.setAlignment(Pos.CENTER);
        Scene loadingPopScene = new Scene(savePopBox,300, 100);
        savePopup.setScene(loadingPopScene);

        HBox buttons = new HBox(20);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(Event ->{
            sT.save();
            pH.StoreProperties();
            Platform.exit();
        });

        Button dontSaveButton = new Button("Don't Save");
        dontSaveButton.setOnAction(Event ->{
            savePopup.close();
            Platform.exit();
        });


        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event ->{
            savePopup.close();
        });

        buttons.getChildren().addAll(saveButton, dontSaveButton, cancelButton);

        savePopBox.getChildren().addAll(label,buttons);
        label.setAlignment(Pos.BASELINE_CENTER);
        buttons.setAlignment(Pos.BASELINE_CENTER);
    }

    private void closeWindowEvent(WindowEvent event){
        if(sT.isSaveRequired()){
            event.consume();
            savePopup.show();
        }

    }

    private File getCertificateImageFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg",
                        "*.tif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String imagesPath = pH.getTrackerImagesPath();
        if (imagesPath == null) {
            imagesPath = desktopLocation;
        }
        fileChooser.setInitialDirectory(new File(imagesPath));
        File selectedFile =  fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            Path path = Paths.get(selectedFile.toString());
            if (!pH.setTrackerImagesPath(path.getParent().toString())) {
                System.out.println("Could not save images path");
                errorLabel.setText("Error: 98725762. Could not set images path");
                errorPopup.show();
            }
        }
        return selectedFile;
    }

    private void configureContextMenu(Stage primaryStage){
        contextMenu = new ContextMenu();

        MenuItem searchItem = new MenuItem("Search");
        searchItem.setOnAction(Event -> {
            Client clientToSearch = sT.getClient(tempClient.getIndex().toString());
            performSearch(clientToSearch);
        });

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(Event -> {
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();

            clientIdField2.setText(                clientToChange.getClientID());
            shipNameField2.setText(                clientToChange.getShipName());
            shipPhoneField2.setText(               clientToChange.getShipPhone());
            shipCompanyField2.setText(             clientToChange.getShipCompany());
            shipAddress1Field2.setText(            clientToChange.getShipAddress1());
            shipAddress2Field2.setText(            clientToChange.getShipAddress2());
            shipCityField2.setText(                clientToChange.getShipCity());
            shipRegionField2.setText(              clientToChange.getShipRegion());
            shipPostCodeField2.setText(            clientToChange.getShipPostCode());
            shipCountryField2.setText(             clientToChange.getShipCountry());
            shipEmailField2.setText(               clientToChange.getShipEmail());
            billNameField2.setText(                clientToChange.getBillName());
            billPhoneField2.setText(               clientToChange.getBillPhone());
            billCompanyField2.setText(             clientToChange.getBillCompany());
            billAddress1Field2.setText(            clientToChange.getBillAddress1());
            billAddress2Field2.setText(            clientToChange.getBillAddress2());
            billCityField2.setText(                clientToChange.getBillCity());
            billRegionField2.setText(              clientToChange.getBillRegion());
            billPostCodeField2.setText(            clientToChange.getBillPostCode());
            billCountryField2.setText(             clientToChange.getBillCountry());
            billEmailField2.setText(               clientToChange.getBillEmail());
            dateShippedPicker2.setValue(           clientToChange.getDateShippedDate());
            firstLicenseNumField2.setText(         clientToChange.getFirstLicenseNum());
            firstCertificateCompanyField2.setText( clientToChange.getFirstCertificateCompany());
            secondLicenseNumField2.setText(        clientToChange.getSecondLicenseNum());
            secondCertificateCompanyField2.setText(clientToChange.getSecondCertificateCompany());
            commentsField2.setText(                clientToChange.getComments());

            changesStage.show();
        });

        Menu addCertificate1 = new Menu("Add Certificate 1");
        Menu addCertificate2 = new Menu("Add Certificate 2");

        MenuItem addCertificate1LocalImage = new MenuItem("Local Image");
        addCertificate1LocalImage.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.addCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 1);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });

        MenuItem addCertificate1URL = new MenuItem("URL");
        addCertificate1URL.setOnAction(Event ->{
            certificateNumber = 1;
            URLTextStage.show();
        });

        addCertificate1.getItems().addAll(addCertificate1LocalImage, addCertificate1URL);

        MenuItem addCertificate2LocalImage = new MenuItem("Local Image");
        addCertificate2LocalImage.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.addCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 2);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });

        MenuItem addCertificate2URL = new MenuItem("URL");
        addCertificate2URL.setOnAction(Event ->{
            certificateNumber = 2;
            URLTextStage.show();
        });

        addCertificate2.getItems().addAll(addCertificate2LocalImage, addCertificate2URL);

        Menu replaceCertificate1 = new Menu("Replace Certificate 1");
        Menu replaceCertificate2 = new Menu("Replace Certificate 2");

        MenuItem replaceCertificate1LocalImage = new MenuItem("Local Image");
        replaceCertificate1LocalImage.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.replaceCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 1);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }

        });

        MenuItem replaceCertificate1URL = new MenuItem("URL");
        replaceCertificate1URL.setOnAction(Event ->{
            certificateNumber = 1;
            try {
                sT.removeCertificateImage(itemEditing, certificateNumber);
                URLTextStage.show();
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });

        replaceCertificate1.getItems().addAll(replaceCertificate1LocalImage, replaceCertificate1URL);

        MenuItem replaceCertificate2LocalImage = new MenuItem("Local Image");
        replaceCertificate2LocalImage.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.replaceCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 2);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }

        });

        MenuItem replaceCertificate2URL = new MenuItem("URL");
        replaceCertificate2URL.setOnAction(Event ->{
            certificateNumber = 2;
            try {
                sT.removeCertificateImage(itemEditing, certificateNumber);
                URLTextStage.show();
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });

        replaceCertificate2.getItems().addAll(replaceCertificate2LocalImage, replaceCertificate2URL);

        MenuItem removeCertificate1Button = new MenuItem("Remove Certificate 1");
        removeCertificate1Button.setOnAction(Event ->{
            certificateToRemove = 1;
            removeCertificatePopup.show();
        });
        MenuItem removeCertificate2Button = new MenuItem("Remove Certificate 2");
        removeCertificate2Button.setOnAction(Event ->{
            certificateToRemove = 2;
            removeCertificatePopup.show();
        });

        MenuItem viewCertificate1Button = new MenuItem("View Certificate 1");
        viewCertificate1Button.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.viewCertificateImage(itemEditing, 1);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });
        MenuItem viewCertificate2Button = new MenuItem("View Certificate 2");
        viewCertificate2Button.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.viewCertificateImage(itemEditing, 2);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
        });



        MenuItem deleteItem = new MenuItem("Delete Client");
        deleteItem.setOnAction(Event -> {
            deletePopup.show();
        });

        contextMenu.getItems().add(searchItem);
        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(addCertificate1);
        contextMenu.getItems().add(addCertificate2);
        contextMenu.getItems().add(viewCertificate1Button);
        contextMenu.getItems().add(viewCertificate2Button);
        contextMenu.getItems().add(replaceCertificate1);
        contextMenu.getItems().add(replaceCertificate2);
        contextMenu.getItems().add(removeCertificate1Button);
        contextMenu.getItems().add(removeCertificate2Button);
        contextMenu.getItems().add(deleteItem);

    }


    private void configureDelete(Stage primaryStage){
        deletePopup = new Stage();
        deletePopup.initModality(Modality.APPLICATION_MODAL);
        deletePopup.initOwner(primaryStage);
        deletePopup.setResizable(false);

        VBox deletePopBox = new VBox(20);
        Label label = new Label("Confirm delete");
        deletePopBox.setAlignment(Pos.CENTER);
        Scene deletePopScene = new Scene(deletePopBox,300, 100);
        deletePopup.setScene(deletePopScene);
        HBox buttons = new HBox(20);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(Event ->{
            try {
                sT.deleteClient(tempClient.getIndex().toString());
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
            deletePopup.close();
            table.setItems(FXCollections.observableArrayList(sT.getClientList()));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event ->{
            deletePopup.close();
        });

        buttons.getChildren().addAll(deleteButton, cancelButton);

        deletePopBox.getChildren().addAll(label,buttons);
        label.setAlignment(Pos.BASELINE_CENTER);
        buttons.setAlignment(Pos.BASELINE_CENTER);
    }



    private void configureRemoveCertificate(Stage primaryStage){
        removeCertificatePopup = new Stage();
        removeCertificatePopup.initModality(Modality.APPLICATION_MODAL);
        removeCertificatePopup.initOwner(primaryStage);
        removeCertificatePopup.setResizable(false);

        VBox deletePopBox = new VBox(20);
        Label label = new Label("Confirm Remove Certificate");
        deletePopBox.setAlignment(Pos.CENTER);
        Scene deletePopScene = new Scene(deletePopBox,300, 100);
        removeCertificatePopup.setScene(deletePopScene);
        HBox buttons = new HBox(20);

        Button deleteButton = new Button("Remove");
        deleteButton.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            try {
                sT.removeCertificateImage(itemEditing, certificateToRemove);
            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
                errorPopup.show();
            }
            removeCertificatePopup.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event ->{
            removeCertificatePopup.close();
        });

        buttons.getChildren().addAll(deleteButton, cancelButton);

        deletePopBox.getChildren().addAll(label,buttons);
        label.setAlignment(Pos.BASELINE_CENTER);
        buttons.setAlignment(Pos.BASELINE_CENTER);
    }

    private void copyFields2ToFields(){
        clientIdField                 = clientIdField2;
        shipNameField                 = shipNameField2;
        shipPhoneField                = shipPhoneField2;
        shipCompanyField              = shipCompanyField2;
        shipAddress1Field             = shipAddress1Field2;
        shipAddress2Field             = shipAddress2Field2;
        shipCityField                 = shipCityField2;
        shipRegionField               = shipRegionField2;
        shipPostCodeField             = shipPostCodeField2;
        shipCountryField              = shipCountryField2;
        shipEmailField                = shipEmailField2;
        billNameField                 = billNameField2;
        billPhoneField                = billPhoneField2;
        billCompanyField              = billCompanyField2;
        billAddress1Field             = billAddress1Field2;
        billAddress2Field             = billAddress2Field2;
        billCityField                 = billCityField2;
        billRegionField               = billRegionField2;
        billPostCodeField             = billPostCodeField2;
        billCountryField              = billCountryField2;
        billEmailField                = billEmailField2;
        dateShippedPicker             = dateShippedPicker2;
        firstLicenseNumField          = firstLicenseNumField2;
        firstCertificateCompanyField  = firstCertificateCompanyField2;
        secondLicenseNumField         = secondLicenseNumField2;
        secondCertificateCompanyField = secondCertificateCompanyField2;
        commentsField                 = commentsField2;
    }


}
