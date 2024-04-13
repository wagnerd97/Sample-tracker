package org.openjfx;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    private Stage savePopup;
    private Stage deletePopup;
    private Stage removeCertificatePopup;
    private ContextMenu contextMenu;

    Client tempClient;


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

        Button resetButton = new Button("Reset Search");
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

    private void configureTable(){
        table = new TableView<>();
        ObservableList<Client> clientList = FXCollections.observableArrayList(sT.getClientList());
        table.setItems(clientList);

        //set table to fill the entire scene
        table.setFixedCellSize(25);
        table.setPrefHeight(550);

        double colWidth = ((1.0-0.04)/16.0);

        TableColumn<Client, Integer> indexCol = new TableColumn<>("#");
        indexCol.setCellValueFactory(new PropertyValueFactory<>("index"));
        indexCol.prefWidthProperty().bind(table.widthProperty().multiply(0.031));

        TableColumn<Client, String> soldToCol = new TableColumn<>("*Sold To");
        soldToCol.setCellValueFactory(new PropertyValueFactory<>("soldTo"));
        soldToCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth-0.0042));

        TableColumn<Client, String> soldPhoneCol = new TableColumn<>("*Sold Phone");
        soldPhoneCol.setCellValueFactory(new PropertyValueFactory<>("soldPhone"));
        soldPhoneCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> shipToCol = new TableColumn<>("*Shipped To");
        shipToCol.setCellValueFactory(new PropertyValueFactory<>("shipTo"));
        shipToCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> companyCol = new TableColumn<>("*Company");
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        companyCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> address1Col = new TableColumn<>("*Address1");
        address1Col.setCellValueFactory(new PropertyValueFactory<>("address1"));
        address1Col.prefWidthProperty().bind(table.widthProperty().multiply(colWidth+0.025));

        TableColumn<Client, String> address2Col = new TableColumn<>("*Address2");
        address2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
        address2Col.prefWidthProperty().bind(table.widthProperty().multiply(colWidth-0.025));

        TableColumn<Client, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        regionCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth/2));

        TableColumn<Client, String> postCodeCol = new TableColumn<>("Postal Code");
        postCodeCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        postCodeCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth-0.03));

        TableColumn<Client, String> shipPhoneCol = new TableColumn<>("*Shipped Phone");
        shipPhoneCol.setCellValueFactory(new PropertyValueFactory<>("shipPhone"));
        shipPhoneCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> emailCol = new TableColumn<>("*Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth+0.04));

        TableColumn<Client, String> shippedDateCol = new TableColumn<>("Shipped Date");
        shippedDateCol.setCellValueFactory(new PropertyValueFactory<>("shippedDate"));
        shippedDateCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> licenseNumCol = new TableColumn<>("*License #");
        licenseNumCol.setCellValueFactory(new PropertyValueFactory<>("licenseNum"));
        licenseNumCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));

        TableColumn<Client, String> certificateCompanyCol = new TableColumn<>("Certificate Company");
        certificateCompanyCol.setCellValueFactory(new PropertyValueFactory<>("certificateCompany"));
        certificateCompanyCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth+0.03));

        TableColumn<Client, String> commentCol = new TableColumn<>("Comments");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
        commentCol.prefWidthProperty().bind(table.widthProperty().multiply(colWidth));


        table.setRowFactory( tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) ) {
                    tempClient = row.getItem();
                    if(tempClient.getCertificate().equals("")){
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


        table.getColumns().setAll(indexCol, soldToCol, soldPhoneCol, shipToCol, companyCol, address1Col, address2Col, cityCol,
                regionCol, postCodeCol, countryCol, shipPhoneCol, emailCol, shippedDateCol,
                licenseNumCol,certificateCompanyCol, commentCol);
    }

    private void configureEntryWindow(Stage primaryStage){
        entryStage = new Stage();
        entryStage.initModality(Modality.APPLICATION_MODAL);
        entryStage.initOwner(primaryStage);
        entryStage.setResizable(false);
        entryBox = new VBox(12);
        entryBox.setAlignment(Pos.CENTER);
        Scene entryScene = new Scene(entryBox,450, 700);
        entryStage.setScene(entryScene);

        try {
            FileInputStream inputStream = new FileInputStream("TrackerIcon.png");
            Image image = new Image(inputStream);
            entryStage.getIcons().add(image);
        } catch (Exception e) {
            System.out.println("loading icon exception: " + e.getMessage());
        }


        Label soldToLabel =             new Label(" *Sold To:                ");
        HBox soldTo = new HBox(10);
        soldTo.setAlignment(Pos.BASELINE_CENTER);
        soldToField.setPrefWidth(250);
        soldToField.setPromptText("Eyelash Express");
        soldTo.getChildren().addAll(soldToLabel, soldToField);


        Label soldPhoneLabel =          new Label(" *Sold Phone:          ");
        HBox soldPhone = new HBox(10);
        soldPhone.setAlignment(Pos.BASELINE_CENTER);
        soldPhoneField.setPrefWidth(250);
        soldPhoneField.setPromptText("555-555-5555");
        soldPhone.getChildren().addAll(soldPhoneLabel, soldPhoneField);

        Label shippedToLabel =          new Label(" *Shipped To:          ");
        HBox shippedTo = new HBox(10);
        shippedTo.setAlignment(Pos.BASELINE_CENTER);
        shippedToField.setPrefWidth(250);
        shippedToField.setPromptText("Jane Doe");
        shippedTo.getChildren().addAll(shippedToLabel, shippedToField);

        Label companyLabel =          new Label(" *Company:          ");
        HBox company = new HBox(10);
        company.setAlignment(Pos.BASELINE_CENTER);
        companyField.setPrefWidth(250);
        companyField.setPromptText("Lash inc.");
        company.getChildren().addAll(companyLabel, companyField);

        Label address1Label =           new Label(" *Address 1:           ");
        HBox address1 = new HBox(10);
        address1.setAlignment(Pos.BASELINE_CENTER);
        address1Field.setPrefWidth(250);
        address1Field.setPromptText("12345 Pine ST");
        address1.getChildren().addAll(address1Label, address1Field);

        Label address2LAbel =           new Label(" *Address 2:           ");
        HBox address2 = new HBox(10);
        address2.setAlignment(Pos.BASELINE_CENTER);
        address2Field.setPrefWidth(250);
        address2.getChildren().addAll(address2LAbel, address2Field);

        Label cityLabel =               new Label(" City:                ");
        HBox city = new HBox(10);
        city.setAlignment(Pos.BASELINE_CENTER);
        cityField.setPrefWidth(250);
        cityField.setPromptText("New York");
        city.getChildren().addAll(cityLabel, cityField);

        Label regionLabel =             new Label(" Region:              ");
        HBox region = new HBox(10);
        region.setAlignment(Pos.BASELINE_CENTER);
        regionField.setPrefWidth(250);
        regionField.setPromptText("AB");
        region.getChildren().addAll(regionLabel, regionField);

        Label postCodeLAbel =           new Label(" Postal Code:         ");
        postCodeField.setPrefWidth(250);
        postCodeField.setPromptText("T7X 1J4");
        HBox postCode = new HBox(10);
        postCode.setAlignment(Pos.BASELINE_CENTER);
        postCode.getChildren().addAll(postCodeLAbel, postCodeField);

        Label countryLabel =            new Label(" Country:             ");
        HBox country = new HBox(10);
        country.setAlignment(Pos.BASELINE_CENTER);
        countryField.setPrefWidth(250);
        countryField.setPromptText("CA");
        country.getChildren().addAll(countryLabel, countryField);

        Label shippedPhoneLabel =       new Label(" *Shipped Phone:            ");
        HBox phone = new HBox(10);
        phone.setAlignment(Pos.BASELINE_CENTER);
        shippedPhoneField.setPrefWidth(250);
        shippedPhoneField.setPromptText("555-555-5555");
        phone.getChildren().addAll(shippedPhoneLabel, shippedPhoneField);

        Label emailLabel =              new Label(" *Email:                    ");
        HBox email = new HBox(10);
        email.setAlignment(Pos.BASELINE_CENTER);
        emailField.setPrefWidth(250);
        emailField.setPromptText("janedoe@gmail.com");
        email.getChildren().addAll(emailLabel, emailField);

        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd YYYY").toFormatter();
        datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null){
                    return dtf.format(localDate).toUpperCase().replace(".", "");
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

        Label shippedDateLabel =        new Label(" Shipped Date:            ");
        HBox shippedDate = new HBox(10);
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.getChildren().addAll(shippedDateLabel, datePicker);

        Label licenseLabel =            new Label(" *License Number:       ");
        HBox license = new HBox(10);
        license.setAlignment(Pos.BASELINE_CENTER);
        licenseField.setPrefWidth(250);
        license.getChildren().addAll(licenseLabel, licenseField);

        Label certificateCompanyLabel = new Label(" Certificate Company: ");
        HBox certificateCompany = new HBox(10);
        certificateCompany.setAlignment(Pos.BASELINE_CENTER);
        certificateCompanyField.setPrefWidth(250);
        certificateCompany.getChildren().addAll(certificateCompanyLabel, certificateCompanyField);

        Label commentLabel = new Label(" *Comments: ");
        HBox comment = new HBox(10);
        comment.setAlignment(Pos.BASELINE_CENTER);
        commentField.setPrefWidth(300);
        comment.getChildren().addAll(commentLabel, commentField);


        Button addButton = new Button("Add Client");
        addButton.setOnAction(Event ->{
            entryStage.close();
            List<String> tempClient= new ArrayList<>();
            tempClient.add(soldToField.getText());
            tempClient.add(soldPhoneField.getText());
            tempClient.add(shippedToField.getText());
            tempClient.add(companyField.getText());
            tempClient.add(address1Field.getText());
            tempClient.add(address2Field.getText());
            tempClient.add(cityField.getText());
            tempClient.add(regionField.getText());
            tempClient.add(postCodeField.getText());
            tempClient.add(countryField.getText());
            tempClient.add(shippedPhoneField.getText());
            tempClient.add(emailField.getText());
            tempClient.add(datePicker.getConverter().toString(datePicker.getValue()));
            tempClient.add(licenseField.getText());
            tempClient.add(certificateCompanyField.getText());
            tempClient.add(commentField.getText());
            sT.addClient(tempClient);
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

        Label searchable = new Label("\"*\" Denotes searchable fields");


        entryBox.getChildren().addAll(searchable, soldTo,soldPhone,shippedTo,company,address1,address2,city, region, postCode, country,
                phone, email, shippedDate, license, certificateCompany,comment, buttons);

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


        Label soldToLabel =             new Label(" Sold To:                ");
        HBox soldTo = new HBox(10);
        soldTo.setAlignment(Pos.BASELINE_CENTER);
        soldToField2.setPrefWidth(250);
        soldTo.getChildren().addAll(soldToLabel, soldToField2);


        Label soldPhoneLabel =          new Label(" Sold Phone:          ");
        HBox soldPhone = new HBox(10);
        soldPhone.setAlignment(Pos.BASELINE_CENTER);
        soldPhoneField2.setPrefWidth(250);
        soldPhone.getChildren().addAll(soldPhoneLabel, soldPhoneField2);

        Label shippedToLabel =          new Label(" Shipped To:          ");
        HBox shippedTo = new HBox(10);
        shippedTo.setAlignment(Pos.BASELINE_CENTER);
        shippedToField2.setPrefWidth(250);
        shippedTo.getChildren().addAll(shippedToLabel, shippedToField2);

        Label companyLabel =          new Label(" Company:          ");
        HBox company = new HBox(10);
        company.setAlignment(Pos.BASELINE_CENTER);
        companyField2.setPrefWidth(250);
        company.getChildren().addAll(companyLabel, companyField2);

        Label address1Label =           new Label(" Address 1:           ");
        HBox address1 = new HBox(10);
        address1.setAlignment(Pos.BASELINE_CENTER);
        address1Field2.setPrefWidth(250);
        address1.getChildren().addAll(address1Label, address1Field2);

        Label address2LAbel =           new Label(" Address 2:           ");
        HBox address2 = new HBox(10);
        address2.setAlignment(Pos.BASELINE_CENTER);
        address2Field2.setPrefWidth(250);
        address2.getChildren().addAll(address2LAbel, address2Field2);

        Label cityLabel =               new Label(" City:                ");
        HBox city = new HBox(10);
        city.setAlignment(Pos.BASELINE_CENTER);
        cityField2.setPrefWidth(250);
        city.getChildren().addAll(cityLabel, cityField2);

        Label regionLabel =             new Label(" Region:              ");
        HBox region = new HBox(10);
        region.setAlignment(Pos.BASELINE_CENTER);
        regionField2.setPrefWidth(250);
        region.getChildren().addAll(regionLabel, regionField2);

        Label postCodeLAbel =           new Label(" Postal Code:         ");
        postCodeField2.setPrefWidth(250);
        HBox postCode = new HBox(10);
        postCode.setAlignment(Pos.BASELINE_CENTER);
        postCode.getChildren().addAll(postCodeLAbel, postCodeField2);

        Label countryLabel =            new Label(" Country:             ");
        HBox country = new HBox(10);
        country.setAlignment(Pos.BASELINE_CENTER);
        countryField2.setPrefWidth(250);
        country.getChildren().addAll(countryLabel, countryField2);

        Label shippedPhoneLabel =       new Label(" Shipped Phone:            ");
        HBox phone = new HBox(10);
        phone.setAlignment(Pos.BASELINE_CENTER);
        shippedPhoneField2.setPrefWidth(250);
        phone.getChildren().addAll(shippedPhoneLabel, shippedPhoneField2);

        Label emailLabel =              new Label(" Email:                    ");
        HBox email = new HBox(10);
        email.setAlignment(Pos.BASELINE_CENTER);
        emailField2.setPrefWidth(250);
        email.getChildren().addAll(emailLabel, emailField2);

        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd YYYY").toFormatter();
        datePicker2 = new DatePicker();
        datePicker2.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                if(localDate != null){
                    return dtf.format(localDate).toUpperCase().replace(".", "");
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

        Label shippedDateLabel =        new Label(" Shipped Date:            ");
        HBox shippedDate = new HBox(10);
        shippedDate.setAlignment(Pos.BASELINE_CENTER);
        shippedDate.getChildren().addAll(shippedDateLabel, datePicker2);

        Label licenseLabel =            new Label(" License Number:       ");
        HBox license = new HBox(10);
        license.setAlignment(Pos.BASELINE_CENTER);
        licenseField2.setPrefWidth(250);
        license.getChildren().addAll(licenseLabel, licenseField2);

        Label certificateCompanyLabel = new Label(" Certificate Company: ");
        HBox certificateCompany = new HBox(10);
        certificateCompany.setAlignment(Pos.BASELINE_CENTER);
        certificateCompanyField2.setPrefWidth(250);
        certificateCompany.getChildren().addAll(certificateCompanyLabel, certificateCompanyField2);

        Label commentLabel = new Label(" Comments: ");
        HBox comment = new HBox(10);
        comment.setAlignment(Pos.BASELINE_CENTER);
        commentField2.setPrefWidth(300);
        comment.getChildren().addAll(commentLabel, commentField2);

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

        changesBox.getChildren().addAll(soldTo,soldPhone,shippedTo,company,address1,address2,city, region, postCode, country,
                phone, email, shippedDate, license, certificateCompany,comment, buttons2);
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
        List<String> tempClient= new ArrayList<>();
        tempClient.add(soldToField.getText());
        tempClient.add(shippedToField.getText());
        tempClient.add(address1Field.getText());
        tempClient.add(address2Field.getText());
        tempClient.add(soldPhoneField.getText());
        tempClient.add(shippedPhoneField.getText());
        tempClient.add(emailField.getText());
        tempClient.add(licenseField.getText());
        tempClient.add(companyField.getText());
        tempClient.add(commentField.getText());

        filteredClients = sT.filterClients(tempClient, filteredClients);
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
