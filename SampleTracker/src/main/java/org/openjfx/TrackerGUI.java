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
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.nio.file.Path;

public class TrackerGUI extends Application {

    private String desktopLocation = System.getProperty("user.home") + "/Desktop";

    private static SampleTracker sT;
    private static PropertiesHandler pH;
    private TableView<Client> table;

    private Stage entryStage;
    private VBox entryBox;

    private Stage changesStage;
    private VBox changesBox;

    private Stage plainTextStage;
    private VBox plainTextBox;

    private Stage configColumnsStage;
    private VBox configColumnsBox;

    private Image IconImage;

    private TextArea textArea;

    private boolean allowReorg = true;

    // List index is column position Map: (columnName, addToTableView)
    // private List<String[]> columnConfigData;


    private Integer certificateToRemove = 1; // Gets set right before confirm remove popup

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

    private Stage savePopup;
    private Stage deletePopup;
    private Stage removeCertificatePopup;
    private ContextMenu contextMenu;

    Client tempClient = new Client();


    public static void main(String[] args){
        sT = new SampleTracker();
        // sT.populate("");
        pH = new PropertiesHandler();

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
        }

        populateTrackerData();
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
        configureChangesWindow(primaryStage);
        configureSavePopup(primaryStage);
        configureContextMenu(primaryStage);
        configureDelete(primaryStage);
        configureRemoveCertificate(primaryStage);
        configureConfigColumnsStage(primaryStage);

        if (IconImage != null) {
            entryStage.getIcons().add(IconImage);
            changesStage.getIcons().add(IconImage);
            plainTextStage.getIcons().add(IconImage);
            savePopup.getIcons().add(IconImage);
            deletePopup.getIcons().add(IconImage);
            removeCertificatePopup.getIcons().add(IconImage);
            
            
        }

        Button searchButton = new Button("Search/Add New");
        searchButton.setOnAction(Event ->{
            plainTextStage.show();
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



        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(searchButton,lastButton, resetButton,saveButton, setDataPathButton);
        buttons.setPadding(new Insets(10, 10, 100, 100));



        propertiesBox.getChildren().addAll(topBox, table); //this line finalizes the changes and puts in the columns
        propertiesBox.prefWidthProperty().bind(scene.widthProperty().multiply(0.8));
        mainBorderPane.setCenter(propertiesBox);
        mainBorderPane.setBottom(buttons);

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        primaryStage.show();

    }

    private void setDataPath(Stage primaryStage) {
        String dataPath = pH.getTrackerDataPath();
        if (dataPath == null) {
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
            }
        }

        sT.setFileLocation(pH.getTrackerDataPath());
        sT.populate();
        table.setItems(FXCollections.observableArrayList(sT.getClientList()));
        table.scrollTo(0);

    }

    private void populateTrackerData() {
        String trackerDataPath;
        trackerDataPath = pH.getTrackerDataPath();
        if (trackerDataPath != null) {
            sT.setFileLocation(trackerDataPath);
            sT.populate();
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
                    if (columnConfigData.get(i)[0] == attributeKey){
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
        
        tempCol.prefWidthProperty().addListener(new ChangeListener<Number>() {
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
                if (Boolean.parseBoolean(configData.get(i)[1]) == true) {
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
            return row ;
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

        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getindexDispString(),                    true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipNameDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipPhoneDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipCompanyDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipAddress1DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipAddress2DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipCityDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipRegionDispString(),               true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipPostCodeDispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipCountryDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getshipEmailDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillNameDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillPhoneDispString(),                true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillCompanyDispString(),              true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillAddress1DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillAddress2DispString(),             true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillCityDispString(),                 true));
        configColumnsBox.getChildren().add(configColumnsWindowLineCreate(tempClient.getbillRegionDispString(),               true));
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

        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipNameDispString(),                 "Eyelash Express",       250, shipNameField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPhoneDispString(),                "555-555-5555",          250, shipPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCompanyDispString(),              "Lash Inc.",             250, shipCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipAddress1DispString(),             "12345 Pine ST",         250, shipAddress1Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipAddress2DispString(),             "Unit 22",               250, shipAddress2Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCityDispString(),                 "New York",              250, shipCityField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipRegionDispString(),               "Alberta",               250, shipRegionField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPostCodeDispString(),             "T7X 1J4",               250, shipPostCodeField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCountryDispString(),              "Canada",                250, shipCountryField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipEmailDispString(),                "janedoe@gmail.com",     250, shipEmailField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillNameDispString(),                 "John Doe",              250, billNameField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillPhoneDispString(),                "444-444-4444",          250, billPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCompanyDispString(),              "Lashfast",              250, billCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillAddress1DispString(),             "35 King St",            250, billAddress1Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillAddress2DispString(),             "6",                     250, billAddress2Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCityDispString(),                 "Palm Springs",          250, billCityField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getbillRegionDispString(),               "California",            250, billRegionField));
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
            performSearch();

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
    private void fillEntryStageWithMap(Map<String, String> newClientData) {
        for(Map.Entry<String, String> entry : newClientData.entrySet()) {
            String entryKey = entry.getKey();
            switch (entryKey) {
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


        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipNameDispString(),                 "Eyelash Express",       250, shipNameField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPhoneDispString(),                "555-555-5555",          250, shipPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCompanyDispString(),              "Lash Inc.",             250, shipCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipAddress1DispString(),             "12345 Pine ST",         250, shipAddress1Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipAddress2DispString(),             "Unit 22",               250, shipAddress2Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCityDispString(),                 "New York",              250, shipCityField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipRegionDispString(),               "Alberta",               250, shipRegionField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPostCodeDispString(),             "T7X 1J4",               250, shipPostCodeField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipCountryDispString(),              "Canada",                250, shipCountryField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipEmailDispString(),                "janedoe@gmail.com",     250, shipEmailField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillNameDispString(),                 "John Doe",              250, billNameField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillPhoneDispString(),                "444-444-4444",          250, billPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCompanyDispString(),              "Lashfast",              250, billCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillAddress1DispString(),             "35 King St",            250, billAddress1Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillAddress2DispString(),             "6",                     250, billAddress2Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillCityDispString(),                 "Palm Springs",          250, billCityField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getbillRegionDispString(),               "California",            250, billRegionField2));
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
            clearSecondaryEntryFields();
            sT.makeChanges(tempClientList, itemEditing);

            itemEditing = 0;
            changesStage.close();
            table.refresh();
        });

        HBox buttons2 = new HBox(30);
        buttons2.setAlignment(Pos.CENTER);
        buttons2.getChildren().addAll(cancelButton, changesButton);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        changesIntermediateBox.getChildren().addAll(changesScrollPane, buttons2, spacer2);

    }



    private void performSearch(){
        List<Client> filteredClients = sT.getClientList();
        Map<String, String> tempClientMap= new HashMap<>();
        tempClientMap.put("shipName",                 shipNameField.getText());
        tempClientMap.put("shipPhone",                shipPhoneField.getText());
        tempClientMap.put("shipCompany",              shipCompanyField.getText());
        tempClientMap.put("shipAddress1",             shipAddress1Field.getText());
        tempClientMap.put("shipAddress2",             shipAddress2Field.getText());
        tempClientMap.put("shipCity",                 shipCityField.getText());
        tempClientMap.put("shipRegion",               shipRegionField.getText());
        tempClientMap.put("shipPostCode",             shipPostCodeField.getText());
        tempClientMap.put("shipCountry",              shipCountryField.getText());
        tempClientMap.put("shipEmail",                shipEmailField.getText());
        tempClientMap.put("billName",                 billNameField.getText());
        tempClientMap.put("billPhone",                billPhoneField.getText());
        tempClientMap.put("billCompany",              billCompanyField.getText());
        tempClientMap.put("billAddress1",             billAddress1Field.getText());
        tempClientMap.put("billAddress2",             billAddress2Field.getText());
        tempClientMap.put("billCity",                 billCityField.getText());
        tempClientMap.put("billRegion",               billRegionField.getText());
        tempClientMap.put("billPostCode",             billPostCodeField.getText());
        tempClientMap.put("billCountry",              billCountryField.getText());
        tempClientMap.put("billEmail",                billEmailField.getText());
        tempClientMap.put("firstLicenseNum",          firstLicenseNumField.getText());
        tempClientMap.put("firstCertificateCompany",  firstCertificateCompanyField.getText());
        tempClientMap.put("secondLicenseNum",         secondLicenseNumField.getText());
        tempClientMap.put("secondCertificateCompany", secondCertificateCompanyField.getText());
        tempClientMap.put("comments",                 commentsField.getText());

        filteredClients = sT.filterClients(tempClientMap, filteredClients);
        table.setItems(FXCollections.observableArrayList(filteredClients));
        table.scrollTo(0);
        if(filteredClients.size()<1){
            //create and show popup
        }
    }

    public void clearPrimaryEntryFields() {
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
        Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg",
                            ".tif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            fileChooser.setInitialDirectory(new File(desktopLocation));
            File selectedFile =  fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                Path path = Paths.get(selectedFile.toString());
                pH.setDefaultPath(path.getParent().toString());
            }
            return selectedFile;
    }

    private void configureContextMenu(Stage primaryStage){
        contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(Event -> {
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();

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
            dateShippedPicker2.setValue(           clientToChange.getDateShipped());
            firstLicenseNumField2.setText(         clientToChange.getFirstLicenseNum());
            firstCertificateCompanyField2.setText( clientToChange.getFirstCertificateCompany());
            secondLicenseNumField2.setText(        clientToChange.getSecondLicenseNum());
            secondCertificateCompanyField2.setText(clientToChange.getSecondCertificateCompany());
            commentsField2.setText(                clientToChange.getComments());

            changesStage.show();
        });

        String dataPath = pH.getDefaultPath();
        if (dataPath == null) {
            dataPath = desktopLocation;
        }

        MenuItem addCertificate1Button = new MenuItem("Add Certificate 1");
        addCertificate1Button.setOnAction(Event ->{
            sT.addCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 1);
        });
        MenuItem addCertificate2Button = new MenuItem("Add Certificate 2");
        addCertificate2Button.setOnAction(Event ->{
            sT.addCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 2);
        });

        MenuItem replaceCertificate1Button = new MenuItem("Replace Certificate 1");
        replaceCertificate1Button.setOnAction(Event ->{
            sT.replaceCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 1);

        });
        MenuItem replaceCertificate2Button = new MenuItem("Replace Certificate 2");
        replaceCertificate2Button.setOnAction(Event ->{
            sT.replaceCertificateImage(itemEditing, getCertificateImageFile(primaryStage), 2);

        });

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
            sT.viewCertificateImage(itemEditing, 1);
        });
        MenuItem viewCertificate2Button = new MenuItem("View Certificate 2");
        viewCertificate2Button.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            sT.viewCertificateImage(itemEditing, 2);
        });



        MenuItem deleteItem = new MenuItem("Delete Client");
        deleteItem.setOnAction(Event -> {
            deletePopup.show();
        });

        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(addCertificate1Button);
        contextMenu.getItems().add(addCertificate2Button);
        contextMenu.getItems().add(viewCertificate1Button);
        contextMenu.getItems().add(viewCertificate2Button);
        contextMenu.getItems().add(replaceCertificate1Button);
        contextMenu.getItems().add(replaceCertificate2Button);
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
            sT.deleteClient(tempClient.getIndex().toString());
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
            sT.removeCertificateImage(itemEditing, certificateToRemove);
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


}
