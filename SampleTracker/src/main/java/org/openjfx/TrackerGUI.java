package org.openjfx;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
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
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.nio.file.Path;

public class TrackerGUI extends Application {

    private String desktopLocation = System.getProperty("user.home") + "/Desktop";
    // private String desktopLocation = "C:\\Users\\User\\Desktop";
    // private String desktopLocation = "C:\\Users\\sandy\\OneDrive\\Desktop";

    private static SampleTracker sT;
    private static PropertiesHandler pH;
    private TableView<Client> table;

    private Stage entryStage;
    private VBox entryBox;

    private Stage changesStage;
    private VBox changesBox;

    private Stage plainTextStage;
    private VBox plainTextBox;

    private TextArea textArea;

    private TextField soldToField = new TextField();
    private TextField soldPhoneField = new TextField();
    private TextField shippedToField = new TextField();
    private TextField companyField = new TextField();
    private TextField address1Field = new TextField();
    private TextField address2Field = new TextField();
    private TextField cityField = new TextField();
    private TextField regionField = new TextField();
    private TextField postCodeField = new TextField();
    private TextField countryField = new TextField();
    private TextField shippedPhoneField = new TextField();
    private TextField emailField = new TextField();
    private DatePicker datePicker = new DatePicker();
    private TextField licenseField = new TextField();
    private TextField certificateCompanyField = new TextField();
    private TextField commentField = new TextField();

    private TextField soldToField2 = new TextField();
    private TextField soldPhoneField2 = new TextField();
    private TextField shippedToField2 = new TextField();
    private TextField companyField2 = new TextField();
    private TextField address1Field2 = new TextField();
    private TextField address2Field2 = new TextField();
    private TextField cityField2 = new TextField();
    private TextField regionField2 = new TextField();
    private TextField postCodeField2 = new TextField();
    private TextField countryField2 = new TextField();
    private TextField shippedPhoneField2 = new TextField();
    private TextField emailField2 = new TextField();
    private DatePicker datePicker2 = new DatePicker();
    private TextField licenseField2 = new TextField();
    private TextField certificateCompanyField2 = new TextField();
    private TextField commentField2 = new TextField();

    private Integer itemEditing = 0;

    private Integer entrySceneWidth = 450;
    private Integer entrySceneHeight = 700;

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
            Image image = new Image(inputStream);
            primaryStage.getIcons().add(image);
        } catch (Exception e) {
            System.out.println("loading icon exception: " + e.getMessage());
        }
        // primaryStage.getIcons().add(new Image(TrackerGUI.class.getResourceAsStream("TrackerIcon.png")));

        populateTrackerData();

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
        topBox.getChildren().addAll(tableLabel,undoButton,redoButton);

        configureTable();
        configurePlainTextWindow(primaryStage);
        configureEntryWindow(primaryStage);
        configureChangesWindow(primaryStage);
        configureSavePopup(primaryStage);
        configureContextMenu(primaryStage);
        configureDelete(primaryStage);
        configureRemoveCertificate(primaryStage);

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

    private TableColumn<Client, ?> tableColumnCreate(String columnLabel, String CellFactoryKey, Double colWidthFactor ) {
        TableColumn<Client, String> tempCol = new TableColumn<>(columnLabel);
        tempCol.setCellValueFactory(new PropertyValueFactory<>(CellFactoryKey));
        tempCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidthFactor));
        return tempCol;
    }

    private void configureTable(){
        table = new TableView<>();
        ObservableList<Client> clientList = FXCollections.observableArrayList(sT.getClientList());
        table.setItems(clientList);

        //set table to fill the entire scene
        table.setFixedCellSize(25);
        table.setPrefHeight(550);

        double colWidth = ((1.0-0.04)/16.0);

        table.getColumns().add(tableColumnCreate(tempClient.getindexDispString()             , "index"             , 0.031));
        table.getColumns().add(tableColumnCreate(tempClient.getsoldToDispString()            , "soldTo"            , colWidth-0.0042));
        table.getColumns().add(tableColumnCreate(tempClient.getsoldPhoneDispString()         , "soldPhone"         , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getshipToDispString()            , "shipTo"            , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getcompanyDispString()           , "company"           , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getaddress1DispString()          , "address1"          , colWidth+0.025));
        table.getColumns().add(tableColumnCreate(tempClient.getaddress2DispString()          , "address2"          , colWidth-0.025));
        table.getColumns().add(tableColumnCreate(tempClient.getcityDispString()              , "city"              , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getregionDispString()            , "region"            , colWidth/2));
        table.getColumns().add(tableColumnCreate(tempClient.getpostCodeDispString()          , "postCode"          , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getcountryDispString()           , "country"           , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getshipPhoneDispString()         , "shipPhone"         , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getemailDispString()             , "email"             , colWidth+0.04));
        table.getColumns().add(tableColumnCreate(tempClient.getshippedDateDispString()       , "shippedDate"       , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getlicenseNumDispString()        , "licenseNum"        , colWidth));
        table.getColumns().add(tableColumnCreate(tempClient.getcertificateCompanyDispString(), "certificateCompany", colWidth+0.03));
        table.getColumns().add(tableColumnCreate(tempClient.getcommentsDispString()          , "comments"          , colWidth));

        table.setRowFactory( tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) ) {
                    tempClient = row.getItem();
                    if(tempClient.getCertificate().isEmpty()){
                        contextMenu.getItems().get(1).setDisable(false);
                        contextMenu.getItems().get(2).setDisable(true);
                        contextMenu.getItems().get(3).setDisable(true);
                        contextMenu.getItems().get(4).setDisable(true);
                    }else{
                        contextMenu.getItems().get(1).setDisable(true);
                        contextMenu.getItems().get(2).setDisable(false);
                        contextMenu.getItems().get(3).setDisable(false);
                        contextMenu.getItems().get(4).setDisable(false);
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

    }

    private HBox entryWindowLineCreate(String labelString, String promptString, Integer fieldWidth, TextField textField) {
        HBox tempHBox = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tempLabel = new Label(labelString);
        tempHBox.setAlignment(Pos.BASELINE_CENTER);
        textField.setPrefWidth(fieldWidth);
        textField.setPromptText(promptString);
        tempHBox.setMaxWidth(entrySceneWidth - 40);
        tempHBox.getChildren().addAll(tempLabel, spacer, textField);
        return tempHBox;
    }
    

    private void configureEntryWindow(Stage primaryStage){
        entryStage = new Stage();
        entryStage.initModality(Modality.APPLICATION_MODAL);
        entryStage.initOwner(primaryStage);
        entryStage.setResizable(false);
        entryBox = new VBox(12);
        entryBox.setAlignment(Pos.CENTER);
        Scene entryScene = new Scene(entryBox,entrySceneWidth, entrySceneHeight);
        entryStage.setScene(entryScene);


        try {
            FileInputStream inputStream = new FileInputStream("TrackerIcon.png");
            Image image = new Image(inputStream);
            entryStage.getIcons().add(image);
        } catch (Exception e) {
            System.out.println("loading icon exception: " + e.getMessage());
        }

        Label searchable = new Label("\"*\" Denotes searchable fields");
        entryBox.getChildren().add(searchable);

        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd yyyy").toFormatter();
        datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<>() {
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

        Label shippedDateLabel = new Label(tempClient.getshippedDateDispString());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox shippedDate = new HBox();
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.setMaxWidth(entrySceneWidth - 40);
        shippedDate.getChildren().addAll(shippedDateLabel, spacer, datePicker);

        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getsoldToDispString()            , "Eyelash Express"  , 250, soldToField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getsoldPhoneDispString()         , "555-555-5555"     , 250, soldPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipToDispString()            , "Jane Doe"         , 250, shippedToField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcompanyDispString()           , "Lash inc."        , 250, companyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getaddress1DispString()          , "12345 Pine ST"    , 250, address1Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getaddress2DispString()          , ""                 , 250, address2Field));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcityDispString()              , "New York"         , 250, cityField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getregionDispString()            , "Alberta"          , 250, regionField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getpostCodeDispString()          , "T7X 1J4"          , 250, postCodeField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcountryDispString()           , "Canada"           , 250, countryField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPhoneDispString()         , "555-555-5555"     , 250, shippedPhoneField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getemailDispString()             , "janedoe@gmail.com", 250, emailField));
        entryBox.getChildren().add(shippedDate);
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getlicenseNumDispString()        , ""                 , 250, licenseField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcertificateCompanyDispString(), ""                 , 250, certificateCompanyField));
        entryBox.getChildren().add(entryWindowLineCreate(tempClient.getcommentsDispString()          , ""                 , 300, commentField));


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
                            tempClientList.add(datePicker.getConverter().toString(((DatePicker)nodeIn).getValue()));
                        }
                    }
                }
            }

            sT.addClient(tempClientList);
            //clearEntryFields();
            table.setItems(FXCollections.observableArrayList(sT.getClientList()));
            table.refresh();
            table.scrollTo(sT.getClientList().get(sT.getClientList().size()-1));

        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            //clearEntryFields();
            entryStage.close();
        });
        Button searchButton = new Button("Search");
        searchButton.setOnAction(Event->{
            entryStage.close();
            performSearch();
            //clearEntryFields();

        });

        HBox buttons = new HBox(30);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(cancelButton, searchButton, addButton);
        entryBox.getChildren().add(buttons);

    }

    private void configurePlainTextWindow(Stage primaryStage) throws Exception {
        plainTextStage = new Stage();
        plainTextStage.initModality(Modality.APPLICATION_MODAL);
        plainTextStage.initOwner(primaryStage);
        plainTextStage.setResizable(false);
        try {
            FileInputStream inputStream = new FileInputStream("TrackerIcon.png");
            Image image = new Image(inputStream);
            plainTextStage.getIcons().add(image);
        } catch (Exception e) {
            System.out.println("loading icon exception: " + e.getMessage());
        }
        //plainTextStage.setAlwaysOnTop(true);
        plainTextBox = new VBox(10);
        plainTextBox.setAlignment(Pos.CENTER);
        Scene plainTextScene = new Scene(plainTextBox,450, 400);
        plainTextStage.setScene(plainTextScene);

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(Event ->{
            plainTextStage.close();
            clearPrimaryEntryFields();
            // List<String> newClientData = sT.parsePlain(textArea.getText());
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
                case "soldTo":
                    soldToField.setText(entry.getValue());
                    break;
                case "soldPhone":
                    soldPhoneField.setText(entry.getValue());
                    break;
                case "email":
                    emailField.setText(entry.getValue());
                    break;
                case "shippedTo":
                    shippedToField.setText(entry.getValue());
                    break;
                case "company":
                    companyField.setText(entry.getValue());
                    break;
                case "address1":
                    address1Field.setText(entry.getValue());
                    break;
                case "address2":
                    address2Field.setText(entry.getValue());
                    break;
                case "city":
                    cityField.setText(entry.getValue());
                    break;
                case "region":
                    regionField.setText(entry.getValue());
                    break;
                case "postCode":
                    postCodeField.setText(entry.getValue());
                    break;
                case "country":
                    countryField.setText(entry.getValue());
                    break;
                case "shippedPhone":
                    shippedPhoneField.setText(entry.getValue());
                    break;
                default:

            }
        }
    }

    /* Currently deprecated */
    private void fillEntryStage(List<String> newClientData){
        soldToField.setText(newClientData.get(0));
        soldPhoneField.setText(newClientData.get(1));
        emailField.setText(newClientData.get(2));
        shippedToField.setText(newClientData.get(3));
        companyField.setText(newClientData.get(4));
        address1Field.setText(newClientData.get(5));
        address2Field.setText(newClientData.get(6));
        cityField.setText(newClientData.get(7));
        regionField.setText(newClientData.get(8));
        postCodeField.setText(newClientData.get(10));
        countryField.setText(newClientData.get(9));
        shippedPhoneField.setText(newClientData.get(11));
    }



    private void configureChangesWindow(Stage primaryStage){
        changesStage = new Stage();
        changesStage.initModality(Modality.APPLICATION_MODAL);
        changesStage.initOwner(primaryStage);
        changesStage.setResizable(false);
        changesBox = new VBox(12);
        changesBox.setAlignment(Pos.CENTER);
        Scene changesScene = new Scene(changesBox,450, 700);
        changesStage.setScene(changesScene);


        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd yyyy").toFormatter();
        datePicker2 = new DatePicker();
        datePicker2.setConverter(new StringConverter<>() {
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
        Label shippedDateLabel = new Label(tempClient.getshippedDateDispString());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox shippedDate = new HBox();
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.setMaxWidth(entrySceneWidth - 40);
        shippedDate.getChildren().addAll(shippedDateLabel, spacer, datePicker2);


        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getsoldToDispString()            , "Eyelash Express"  , 250, soldToField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getsoldPhoneDispString()         , "555-555-5555"     , 250, soldPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipToDispString()            , "Jane Doe"         , 250, shippedToField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcompanyDispString()           , "Lash inc."        , 250, companyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getaddress1DispString()          , "12345 Pine ST"    , 250, address1Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getaddress2DispString()          , ""                 , 250, address2Field2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcityDispString()              , "New York"         , 250, cityField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getregionDispString()            , "Alberta"          , 250, regionField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getpostCodeDispString()          , "T7X 1J4"          , 250, postCodeField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcountryDispString()           , "Canada"           , 250, countryField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getshipPhoneDispString()         , "555-555-5555"     , 250, shippedPhoneField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getemailDispString()             , "janedoe@gmail.com", 250, emailField2));
        changesBox.getChildren().add(shippedDate);
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getlicenseNumDispString()        , ""                 , 250, licenseField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcertificateCompanyDispString(), ""                 , 250, certificateCompanyField2));
        changesBox.getChildren().add(entryWindowLineCreate(tempClient.getcommentsDispString()          , ""                 , 300, commentField2));

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(Event->{
            changesStage.close();
        });

        Button changesButton = new Button("Save Changes");
        changesButton.setOnAction(Event -> {
            List<String> changed = new ArrayList<>();
            changed.add(soldToField2.getText());
            changed.add(soldPhoneField2.getText());
            changed.add(shippedToField2.getText());
            changed.add(companyField2.getText());
            changed.add(address1Field2.getText());
            changed.add(address2Field2.getText());
            changed.add(cityField2.getText());
            changed.add(regionField2.getText());
            changed.add(postCodeField2.getText());
            changed.add(countryField2.getText());
            changed.add(shippedPhoneField2.getText());
            changed.add(emailField2.getText());
            changed.add(datePicker2.getConverter().toString(datePicker2.getValue()));
            changed.add(licenseField2.getText());
            changed.add(certificateCompanyField2.getText());
            changed.add(commentField2.getText());
            clearSecondaryEntryFields();
            sT.makeChanges(changed, itemEditing);

            itemEditing = 0;
            changesStage.close();
            table.refresh();
        });

        HBox buttons2 = new HBox(30);
        buttons2.setAlignment(Pos.CENTER);
        buttons2.getChildren().addAll(cancelButton, changesButton);
        changesBox.getChildren().add(buttons2);

    }



    private void performSearch(){
        Boolean flag = false;
        List<Client> filteredClients = sT.getClientList();
        /*search by fields
        sold to
        shipped to
        address1
        address2
        sold phone
        shipped phone
        email
        license #
        company
        comments
        */
        /*Searcheable fields */
        List<String> tempClientList= new ArrayList<>();
        tempClientList.add(soldToField.getText());
        tempClientList.add(shippedToField.getText());
        tempClientList.add(address1Field.getText());
        tempClientList.add(address2Field.getText());
        tempClientList.add(soldPhoneField.getText());
        tempClientList.add(shippedPhoneField.getText());
        tempClientList.add(emailField.getText());
        tempClientList.add(licenseField.getText());
        tempClientList.add(companyField.getText());
        tempClientList.add(commentField.getText());
        /*Searcheable fields */

        filteredClients = sT.filterClients(tempClientList, filteredClients);
        table.setItems(FXCollections.observableArrayList(filteredClients));
        table.scrollTo(0);
        if(filteredClients.size()<1){
            //create and show popup
        }
    }

    public void clearPrimaryEntryFields() {
        soldToField.clear();
        soldPhoneField.clear();
        shippedToField.clear();
        companyField.clear();
        address1Field.clear();
        address2Field.clear();
        cityField.clear();
        regionField.clear();
        postCodeField.clear();
        countryField.clear();
        shippedPhoneField.clear();
        emailField.clear();
        datePicker.setValue(null);
        datePicker.getEditor().clear();
        licenseField.clear();
        certificateCompanyField.clear();
        commentField.clear();
    }

    public void clearSecondaryEntryFields(){
        soldToField2.clear();
        soldPhoneField2.clear();
        shippedToField2.clear();
        companyField2.clear();
        address1Field2.clear();
        address2Field2.clear();
        cityField2.clear();
        regionField2.clear();
        postCodeField2.clear();
        countryField2.clear();
        shippedPhoneField2.clear();
        emailField2.clear();
        datePicker2.setValue(null);
        datePicker2.getEditor().clear();
        licenseField2.clear();
        certificateCompanyField2.clear();
        commentField2.clear();
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

        // Save, Don't Save, Cancel
        HBox buttons = new HBox(20);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(Event ->{
            sT.save();
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


    private void configureContextMenu(Stage primaryStage){
        contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(Event -> {
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            soldToField2.setText(clientToChange.getSoldTo());
            soldPhoneField2.setText(clientToChange.getSoldPhone());
            shippedToField2.setText(clientToChange.getShipTo());
            companyField2.setText(clientToChange.getCompany());
            address1Field2.setText(clientToChange.getAddress1());
            address2Field2.setText(clientToChange.getAddress2());
            cityField2.setText(clientToChange.getCity());
            regionField2.setText(clientToChange.getRegion());
            postCodeField2.setText(clientToChange.getPostCode());
            countryField2.setText(clientToChange.getCountry());
            shippedPhoneField2.setText(clientToChange.getShipPhone());
            emailField2.setText(clientToChange.getEmail());
            datePicker2.setValue(clientToChange.getDateFormat());
            licenseField2.setText(clientToChange.getLicenseNum());
            certificateCompanyField2.setText(clientToChange.getCertificateCompany());
            commentField2.setText(clientToChange.getComments());

            changesStage.show();
        });

        String dataPath = pH.getDefaultPath();
        if (dataPath == null) {
            dataPath = desktopLocation;
        }

        MenuItem addCertificateButton = new MenuItem("Add Certificate");
        addCertificateButton.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg",
                            ".tif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            fileChooser.setInitialDirectory(new File(desktopLocation));

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            try {
                sT.addCertificateImage(itemEditing, selectedFile);
            } catch (IOException e){
                System.out.println("Could not save image error: " + e.getMessage());
            }
            Path path = Paths.get(selectedFile.toString());
            pH.setDefaultPath(path.getParent().toString());
        });

        MenuItem replaceCertificateButton = new MenuItem("Replace Certificate");
        replaceCertificateButton.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg",
                            ".tif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialDirectory(new File(desktopLocation));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            sT.replaceCertificateImage(itemEditing, selectedFile);

        });

        MenuItem removeCertificateButton = new MenuItem("Remove Certificate");
        removeCertificateButton.setOnAction(Event ->{
            removeCertificatePopup.show();
        });

        MenuItem viewCertificateButton = new MenuItem("View Certificate");
        viewCertificateButton.setOnAction(Event ->{
            Client clientToChange = sT.getClient(tempClient.getIndex().toString());
            itemEditing = clientToChange.getIndex();
            sT.viewCertificateImage(itemEditing);
        });



        MenuItem deleteItem = new MenuItem("Delete Client");
        deleteItem.setOnAction(Event -> {
            deletePopup.show();
        });

        contextMenu.getItems().addAll(editItem,addCertificateButton,replaceCertificateButton,removeCertificateButton, viewCertificateButton, deleteItem);
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
            sT.removeCertificateImage(itemEditing);
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
