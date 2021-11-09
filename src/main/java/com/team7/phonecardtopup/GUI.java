/*
	Subject: Object Oriented Programming(CIT2004)
	Occurence: UE2
	Tutor: Oniel Charles
	Programmers: [
		Kiyana Gordon (1902726)
		Shawn Grant   (2002432)
		Malik Morgan  (2007793)
	]
	Purpse: This class creates and controls all GUI elements within the project
*/
package com.team7.phonecardtopup;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application {
    static final String DIGICEL_PASSWORD = "TheBiggerBetterNetwork2021";
    static final String FLOW_PASSWORD = "TheWayIFlow2021";
    static final double DEFAULT_WIDTH = 360; 
    static final double DEFAULT_HEIGHT = 400;
    ServiceProvider currentProvider;
    Stage mainWindow;
    
    @Override
    public void start(Stage stage) throws IOException {
        // setup the main window
        mainWindow = stage;
        mainWindow.setTitle("Phone Card Top-Up"); //heading for the application
        mainWindow.setResizable(false);
        mainWindow.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png"))); //app icon

        splashScreen();        
    }

    // A simple preload screen shown before the program's main screens
    public void splashScreen() {
        VBox layout = new VBox(20);
        layout.setId("splash");
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Phone Card Top-Up System");
        title.getStyleClass().add("title");
        Label team = new Label("Developed by \"Team 7\"");
        team.setId("team");
        Label names = new Label("[ Shawn Grant, Kiyana Gordon & Malik morgan ]");

        layout.getChildren().addAll(title, team, names);

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        // THIS EFFECTIVELY CREATES A DELAY
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(3000); // WAIT 3 SECONDS
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        // SHOW THE NEXT SECTION WHEN THE TASK IS FINISHED
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                stage.close();
                chooseSection();
                mainWindow.show();
            }
         });
        new Thread(sleeper).start(); //start the above task
    }

    // Menu to choose the section i.e. Administration or Customer
    public void chooseSection(){ 
        VBox layout = new VBox();
        layout.getStyleClass().add("section");
        layout.setId("choosesection");
        layout.setAlignment(Pos.TOP_CENTER);

        // create a header image
        ImageView topImage = new ImageView(getClass().getResource("/topwave.png").toExternalForm());
        topImage.setFitHeight(100);
        topImage.setFitWidth(DEFAULT_WIDTH);

        Label title = new Label("Choose Operation");
        title.getStyleClass().add("title");

        Button btn1 = new Button("ADMINISTRATION");
        btn1.setPrefWidth(200);
        btn1.setId("btn1");
        btn1.setOnAction(e -> chooseProvider());

        Button btn2 = new Button("CUSTOMER");
        btn2.setPrefWidth(200);
        btn2.setOnAction(e -> customerMenu());

        layout.getChildren().addAll(topImage, title, btn1, new Label("or"), btn2);
        changeScene(layout);
    }

    //Choosing the pervice provider, accepting the password and testing the input
    //to validate the password. if incorrect an error message is displayed
    public void chooseProvider() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("INCORRECT PASSWORD");
        alert.setGraphic(null);
        alert.setHeaderText(null);

        VBox layout = new VBox(10);
        HBox buttons = new HBox(10), form = new HBox();
        layout.setAlignment(Pos.BASELINE_CENTER);
        layout.setId("chooseprovider");
        layout.getStyleClass().add("section");
        buttons.setAlignment(Pos.CENTER);
        buttons.setId("loginbuttons");
        form.setAlignment(Pos.CENTER);
        form.setId("loginform");

        // Dispaying an image
        ImageView topImage = new ImageView(getClass().getResource("/topwave.png").toExternalForm());
        topImage.setFitHeight(100);
        topImage.setFitWidth(DEFAULT_WIDTH);

        Label title = new Label("Select Your Provider");
        title.getStyleClass().add("title");

        ToggleGroup toggles = new ToggleGroup();
        RadioButton digiBtn = new RadioButton("DIGICEL");
        digiBtn.setId("digi");
        RadioButton flowBtn = new RadioButton("FLOW");
        flowBtn.setId("flow");
        digiBtn.setSelected(true);
        digiBtn.setToggleGroup(toggles);
        flowBtn.setToggleGroup(toggles);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button submitBtn = new Button("LOGIN");
        submitBtn.setOnAction(e -> {
            if (toggles.getSelectedToggle() == digiBtn) {
                if (passwordField.getText().equals(DIGICEL_PASSWORD)) {
                    currentProvider = new Digicel();
                    adminMenu();
                } else {
                    //error message if password entered by user is incorrect
                    alert.setContentText("You must enter the correct password to access Digicel");
                    alert.showAndWait();
                }
            } else {
                if (passwordField.getText().equals(FLOW_PASSWORD)) {
                    currentProvider = new Flow();
                    adminMenu();
                } else {
                    //error message if password entered by user is incorrect
                    alert.setContentText("You must enter the correct password to access Flow");
                    alert.showAndWait();
                }
            }
        });

        Button cancel = new Button("<- Cancel");
        cancel.setId("cancel");
        cancel.setOnAction(e -> chooseSection());

        // populate the layout
        buttons.getChildren().addAll(digiBtn, flowBtn);
        form.getChildren().addAll(passwordField, submitBtn);
        layout.getChildren().addAll(topImage, title, buttons, new Label("Enter Provider Password:"), form, cancel);

        changeScene(layout);
    }
    
    // menu GUI for admins
    public void adminMenu() {
        VBox layout = new VBox();
        layout.getStyleClass().add("menu");
        layout.setAlignment(Pos.TOP_CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("MAIN MENU");
        title.getStyleClass().add("title");

        Button optBtn1 = new Button("ADD CUSTOMER");
        optBtn1.setId("opt1");
        optBtn1.setOnAction(e -> addCustomerGUI());

        Button optBtn2 = new Button("VIEW CUSTOMERS");
        optBtn2.setId("opt2");
        optBtn2.setOnAction(e -> viewCustomersGUI());

        Button optBtn3 = new Button("CREATE PHONE CREDIT");
        optBtn3.setId("opt3");
        optBtn3.setOnAction(e -> createCreditGUI());

        Button optBtn4 = new Button("VIEW PHONE CREDITS");
        optBtn4.setId("opt4");
        optBtn4.setOnAction(e -> viewCreditsGUI());

        Button optBtn5 = new Button("TOTAL CUSTOMERS");
        optBtn5.setId("opt5");
        optBtn5.setOnAction(e -> totalCustomersGUI());

        Button optBtn6 = new Button("VIEW COMPANY INFO");
        optBtn6.setId("opt6");
        optBtn6.setOnAction(e -> viewCompanyInfoGUI());

        Button optBtn7 = new Button("<- Log out");
        optBtn7.setId("cancel");
        optBtn7.setOnAction(e -> chooseProvider());

        grid.addColumn(0, optBtn1, optBtn2, optBtn3, optBtn4, optBtn5, optBtn6, optBtn7);
        layout.getChildren().addAll(title, grid);
        VBox.setVgrow(grid, Priority.ALWAYS);
        changeScene(layout);
    }

    // admin menu functions
    public void addCustomerGUI() {
        VBox layout = new VBox(10), form = new VBox(5);
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER);
        layout.setId("addcustomer");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.TOP_CENTER);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(250);

        ImageView image = new ImageView(getClass().getResource("/person-plus.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(30);
        Label title = new Label("ADD CUSTOMER");
        title.getStyleClass().add("title");

        TextField trnInput = new TextField();
        TextField lnameInput = new TextField();
        TextField addressInput = new TextField();
        TextField phoneInput = new TextField();
        Button submitButton = new Button("ADD CUSTOMER");
        submitButton.setId("submit");
        submitButton.setOnAction(e->{
            Customer newCustomer = new Customer(trnInput.getText(), lnameInput.getText(),
                    addressInput.getText(), phoneInput.getText());
            //return to menu if successfully added
            if (currentProvider.addCustomer(newCustomer) == 1) {
                adminMenu();
            }
        });

        Button cancel = new Button("<- Cancel");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());

        header.getChildren().addAll(image, title);
        form.getChildren().addAll(
            new Label("TRN (9 Digits):"), trnInput,
            new Label("Last Name:"), lnameInput,
            new Label("Address:"), addressInput,
            new Label("Phone Number (10 Digits):"), phoneInput,
            submitButton, cancel
        );
        layout.getChildren().addAll(header, form);
        VBox.setVgrow(form, Priority.ALWAYS);
        changeScene(layout);
    }

    public void viewCustomersGUI() {
        VBox layout = new VBox(10), content = new VBox(10);
        HBox header = new HBox(5);
        ScrollPane scrollPane = new ScrollPane();
        header.setAlignment(Pos.CENTER);
        layout.setId("viewcustomers");
        layout.getStyleClass().add("section");
        content.setId("content");
        layout.setAlignment(Pos.TOP_CENTER);
        content.setAlignment(Pos.TOP_CENTER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        ImageView image = new ImageView(getClass().getResource("/view.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(20);
        Label title = new Label("Customer Base");
        title.getStyleClass().add("title");

        ArrayList<Customer> list = currentProvider.getCustomerBase();
        if (list.size() == 0)
            scrollPane.setContent(new Label("No Customers Added Yet"));
        else {
            // add each customer to the content of the scrolllpane
            list.forEach(c -> {
                content.getChildren().add(
                    new Label(
                        "(" + c.getTrn() + ")  " + c.getLastName() + "\n" +
                        c.getAddress() + "\n" +
                        "Tel  : " + c.getTelNumber() + "\n" +
                        "Balance: $" + c.getCreditBal()
                    )
                );
            });

            scrollPane.setContent(content);
            scrollPane.setFitToWidth(true);
        }

        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());

        header.getChildren().addAll(image, title);
        layout.getChildren().addAll(header, scrollPane, cancel);
        changeScene(layout);
    }
    
    public void createCreditGUI() {
        VBox layout = new VBox(10);
        HBox header = new HBox(5);
        layout.setId("createcredit");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.TOP_CENTER);
        header.setId("header");
        header.setAlignment(Pos.CENTER);

        ImageView image = new ImageView(getClass().getResource("/phone-plus.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(30);
        Label title = new Label("Create Phone Credit");
        title.getStyleClass().add("title");

        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.getItems().addAll("$100", "$200", "$500", "$1,000");
        comboBox.getSelectionModel().select(0);

        Button createButton = new Button("CREATE CREDIT");
        createButton.setId("create-btn");
        createButton.setOnAction(e -> {
            switch (comboBox.getSelectionModel().getSelectedIndex()) {
                case 0:
                    currentProvider.createPhoneCredit(100);
                    break;
                
                case 1:
                    currentProvider.createPhoneCredit(200);
                    break;

                case 2:
                    currentProvider.createPhoneCredit(500);
                    break;

                case 3:
                    currentProvider.createPhoneCredit(1000);
                    break;
            }
        });

        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());

        header.getChildren().addAll(image, title);
        layout.getChildren().addAll(header, new Label("Select The Credit Amount:"), comboBox, createButton, cancel);
        changeScene(layout);
    }

    public void viewCreditsGUI() {
        VBox layout = new VBox(10), content = new VBox(10);
        HBox header = new HBox(5);
        ScrollPane scrollPane = new ScrollPane();
        header.setAlignment(Pos.CENTER);
        layout.setId("viewcredits");
        content.setId("content");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.TOP_CENTER);
        content.setAlignment(Pos.TOP_CENTER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        ImageView image = new ImageView(getClass().getResource("/view.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(20);
        Label title = new Label("All Phone Credits");
        title.getStyleClass().add("title");

        ArrayList<Credit> list = currentProvider.getAllPhoneCredit();
        if (list.size() == 0)
            scrollPane.setContent(new Label("No Phone Cards Added Yet"));
        else {
            // add each credit to the content of the scrolllpane
            list.forEach(c -> {
                content.getChildren().add(new Label("Card #: " + c.getCardNumber() + "\nValue : $" + c.getAmount()
                        + "\nStatus  : " + c.getStatus()));
            });

            scrollPane.setContent(content);
            scrollPane.setFitToWidth(true);
        }
        
        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());

        header.getChildren().addAll(image, title);
        layout.getChildren().addAll(header, scrollPane, cancel);
        changeScene(layout);
    }
    
    public void totalCustomersGUI() {
        // create a simple layout with a label to show the number of customers
        VBox layout = new VBox();
        HBox header = new HBox(5);
        layout.getStyleClass().add("section");
        layout.setId("totalcustomer");
        layout.setAlignment(Pos.CENTER);
        header.setAlignment(Pos.CENTER);

        ImageView image = new ImageView(getClass().getResource("/people.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(30);
        Label title = new Label("Total Customers");
        title.getStyleClass().add("title");

        Label amtLabel = new Label("" + currentProvider.getNumCustomers());
        amtLabel.setId("amt");

        Label label = new Label("Customers");
        label.setId("cust");

        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());

        header.getChildren().addAll(image, title);
        layout.getChildren().addAll(header, amtLabel, label, cancel);

        changeScene(layout);
    }

    public void viewCompanyInfoGUI() {
        VBox layout = new VBox(5);
        HBox header = new HBox(5);
        layout.setId("viewcompany");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.CENTER);
        header.setAlignment(Pos.CENTER);

        ImageView image = new ImageView(getClass().getResource("/view.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(20);
        Label title = new Label("Company Info");
        title.getStyleClass().add("title");

        // add the company info to the layout
        String info = currentProvider.getCompanyId() + "\n" + currentProvider.getAddress();
        if (currentProvider instanceof Digicel)
            info += "\n" + ((Digicel) currentProvider).getNumBranches() + " Branches \n";
        else
            info += "\nOwned By " + ((Flow) currentProvider).getParentName();

        Label infoLabel = new Label(info);
        infoLabel.setId("info");

        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> adminMenu());
    
        header.getChildren().addAll(image, title);
        layout.getChildren().addAll(header, infoLabel, cancel);
        changeScene(layout);
    }

    // CREATE AND SHOW CUSTOMER MENU UI
    public void customerMenu() {
        VBox layout = new VBox();
        layout.getStyleClass().add("menu");
        layout.setAlignment(Pos.TOP_CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("MAIN MENU");
        title.getStyleClass().add("title");

        Button optBtn1 = new Button("ADD CREDIT");
        optBtn1.setId("opt1");
        optBtn1.setOnAction(e -> addCreditGUI());

        Button optBtn2 = new Button("VIEW BALANCE");
        optBtn2.setId("opt2");
        optBtn2.setOnAction(e -> viewBalanceGUI());

        Button cancel = new Button("<- Back");
        cancel.setId("cancel");
        cancel.setOnAction(e -> chooseSection());

        grid.addColumn(0, optBtn1, optBtn2, cancel);
        layout.getChildren().addAll(title, grid);
        VBox.setVgrow(grid, Priority.ALWAYS);
        changeScene(layout);
    }

    public void addCreditGUI() {
        VBox layout = new VBox(10), form = new VBox(5);
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER);
        layout.setId("addcustomer");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.TOP_CENTER);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(250);

        ImageView image = new ImageView(getClass().getResource("/phone-plus.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(30);
        Label title = new Label("ADD CREDIT");
        title.getStyleClass().add("title");

        TextField codeInput = new TextField();

        Button submitButton = new Button("ADD");
        submitButton.setId("submit");
        submitButton.setOnAction(e->{
            if (new Customer().addCredit(codeInput.getText()) == 1) {
                customerMenu();///go ack to main menu if successful
            }
        });

        Button cancel = new Button("<- Cancel");
        cancel.setId("cancel");
        cancel.setOnAction(e -> customerMenu());

        header.getChildren().addAll(image, title);
        form.getChildren().addAll(
            new Label("Enter the credit code in format:\n*121*[card_num]*[phone_num]#"), codeInput,
            submitButton, cancel
        );
        layout.getChildren().addAll(header, form);
        VBox.setVgrow(form, Priority.ALWAYS);
        changeScene(layout);
    }
    
    public void viewBalanceGUI() {
        VBox layout = new VBox(10), form = new VBox(5);
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER);
        layout.setId("addcustomer");
        layout.getStyleClass().add("section");
        layout.setAlignment(Pos.TOP_CENTER);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(250);

        ImageView image = new ImageView(getClass().getResource("/view.png").toExternalForm());
        image.setFitWidth(30);
        image.setFitHeight(20);
        Label title = new Label("CHECK BALANCE");
        title.getStyleClass().add("title");

        Label bal = new Label("$0.0");
        bal.setId("balance");

        TextField codeInput = new TextField();
        Button submitButton = new Button("CHECK");
        submitButton.setId("submit");
        submitButton.setOnAction(e->{
            bal.setText("$" + new Customer().checkCustomerBalance(codeInput.getText()));
        });

        Button cancel = new Button("<- Cancel");
        cancel.setId("cancel");
        cancel.setOnAction(e -> customerMenu());

        header.getChildren().addAll(image, title);
        form.getChildren().addAll(
            new Label("Enter the check code:\nformat: *120*[phone_number]#"), codeInput,
            submitButton, cancel
        );
        layout.getChildren().addAll(header, bal, form);
        changeScene(layout);
    }

    //create a scene and display it in the main window
    public void changeScene(Pane layout) {
        Scene scene = new Scene(layout, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //add our custom CSS to the scene
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        mainWindow.setScene(scene);
    }
}
