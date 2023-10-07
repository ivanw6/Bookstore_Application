package FinalProject;

/**
 *
 * @author Group 15
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class Bookstore extends Application {

    // INSTANCE VARIABLES//
    private final Owner owner = new Owner();
    private Customer currentCust;
    private static final Files files = new Files();

    // BUTTONS//
    Button loginBTN = new Button("Login");
    Button logoutBTN = new Button("Logout");
    Button booksBTN = new Button("Books");
    Button customersBTN = new Button("Customers");
    Button backBTN = new Button("Back");
    Button redeempointsandbuy = new Button("Redeem points and Buy");
    Button buy = new Button("Buy");
    HBox hb = new HBox();

    // ALL SCENES//
    VBox v = new VBox();
    Scene ownerMainScene = new Scene(v, 600, 400); // OWNER MAIN SCREEN//

    VBox v2 = new VBox();
    Scene customerMainScene = new Scene(v2, 600, 400); // CUSTOMER MAIN SCREEN//

    VBox v3 = new VBox();
    Scene ownerCustomerScene = new Scene(v3, 600, 400); // OWNER CUSTOMER SCENE//

    StackPane root = new StackPane();
    Scene ownersCustomerScene = (new Scene(root, 600, 400));

    StackPane ownerBook = new StackPane();
    Scene ownerBookScene = (new Scene(ownerBook, 600, 400)); // OWNER BOOKS SCENE//

    VBox v4 = new VBox();
    Scene checkOut = new Scene(v4, 400, 300); // CHECKOUT SCENE//

    // OBJECTS FOR LOGIN AND BOOKS TABLE (USER/PASSWORD)//
    TextField userinput = new TextField(); // CREATES NEW TEXTFIELD (USERNAME INPUT)//
    PasswordField passwordInput = new PasswordField();// CREATES NEW TEXTFIELD (PASSWORD INPUT)//
    TableView<Book> booksTable = new TableView<>();

    final TableView.TableViewFocusModel<Book> defaultFocusModel = booksTable.getFocusModel();// table view
    ObservableList<Book> books = FXCollections.observableArrayList();
    TableView<Customer> customersTable = new TableView<>();
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    public ObservableList<Book> addBooks() {
        books.addAll(Owner.books);
        return books;
    }

    public ObservableList<Customer> addCustomers() {
        customers.addAll(owner.getCustomers());
        return customers;
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Bookstore App");
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
        primaryStage.show();

        try {
            owner.restock();
        } catch (IOException e) {
        }

        loginBTN.setOnAction(e -> {
            boolean loggedIn = false;

            String username = userinput.getText();
            String password = passwordInput.getText();

            if (username.equals(owner.getUsername()) && password.equals(owner.getPassword())) {

                primaryStage.setScene(new Scene(ownerMainScene(), 605, 550));

                loggedIn = true;
            }
            for (Customer c : owner.getCustomers()) {

                if (username.equals(c.getUsername()) && password.equals(c.getPassword())) {

                    currentCust = c;

                    primaryStage.setScene(new Scene(customerMainScene(), 605, 550));

                    loggedIn = true;
                }
            }

        });

        logoutBTN.setOnAction(e -> {
            primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
            for (Book book : Owner.books) {
                book.setSelect(new CheckBox());
            }
            userinput.clear();
            passwordInput.clear();
        });

        booksBTN.setOnAction(e -> primaryStage.setScene(new Scene(ownerBooksScene(), 605, 550)));
        customersBTN.setOnAction(e -> primaryStage.setScene(new Scene(customerScene(), 605, 550)));
        backBTN.setOnAction(e -> primaryStage.setScene(new Scene(ownerMainScene(), 605, 550)));

        redeempointsandbuy.setOnAction(e -> {
            boolean bookSelected = false;
            for (Book book : Owner.books) {
                if (book.getSelect().isSelected()) {
                    bookSelected = true;
                }
            }

        });

        buy.setOnAction(e -> {
            boolean bookSelected = false;
            for (Book book : Owner.books) {
                if (book.getSelect().isSelected()) {
                    bookSelected = true;
                }
            }
            if (bookSelected) {
                primaryStage.setScene(new Scene(checkoutScreen(false), 605, 550));
            } else {
                primaryStage.setScene(new Scene(customerMainScene(), 605, 550));
            }
        });

        primaryStage.setOnCloseRequest(e -> {

            try {
                files.bookTxtReset();
                files.customerTxtReset();
                files.bookTxtWrite(Owner.books);
                files.customerTxtWrite(owner.getCustomers());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public Group customerMainScene() {
        Group group1 = new Group();
        booksTable.getItems().clear();
        booksTable.getColumns().clear();
        booksTable.setFocusModel(null);

        Font font = new Font(14);
        Text welcomeMsg = new Text("Welcome, " + currentCust.getUsername());
        welcomeMsg.setFont(font);
        Text status1 = new Text(" Status: ");
        status1.setFont(font);
        Text status2 = new Text(currentCust.getStatus());

        if (currentCust.getStatus().equals("GOLD")) {
            status2.setFill(Color.GOLD);
        } else {
            status2.setFill(Color.SILVER);
        }

        Text points = new Text(" Points: " + currentCust.getPoints());
        points.setFont(font);

        // Book title column
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Book price column
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Checkbox column
        TableColumn<Book, String> selectColumn = new TableColumn<>("Select");
        selectColumn.setMinWidth(100);
        selectColumn.setStyle("-fx-alignment: CENTER;");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        booksTable.setItems(addBooks());
        booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn);

        HBox info = new HBox();
        info.getChildren().addAll(status1, status2, points);
        BorderPane head = new BorderPane();
        head.setLeft(welcomeMsg);
        head.setRight(info);

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.setSpacing(5);
        bottom.getChildren().addAll(buy, redeempointsandbuy, logoutBTN);

        VBox vbox = new VBox();
        String errMsg = "";
        Text warning = new Text(errMsg);
        warning.setFill(Color.RED);
        vbox.setStyle("-fx-background-color: #004c9b;");
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40, 200, 30, 100));
        vbox.getChildren().addAll(head, booksTable, bottom, warning);

        group1.getChildren().addAll(vbox);

        return group1;
    }

    public Group loginScreen(boolean Error) {
        Group group = new Group();

        HBox head;
        head = new HBox();
        Text text = new Text("The TMU Bookstore");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setFill(Color.YELLOW);
        head.getChildren().addAll(text);
        head.setAlignment(Pos.CENTER);

        VBox lBox;
        lBox = new VBox();
        lBox.setPadding(new Insets(30, 65, 45, 65));
        lBox.setStyle("-fx-background-color: YELLOW;" + "-fx-background-radius: 10 10 10 10;");
        lBox.setSpacing(6);
        Text user = new Text("Username");
        Text pass = new Text("Password");
        loginBTN.setMinWidth(174);
        lBox.getChildren().addAll(user, userinput, pass, passwordInput, loginBTN);

        VBox v;
        v = new VBox();
        v.getChildren().addAll(head, lBox);
        v.setStyle("-fx-background-color: #004c9b;");
        v.setPadding(new Insets(80, 280, 200, 150));
        v.setSpacing(80);

        group.getChildren().addAll(v);
        return group;
    }

    public VBox ownerMainScene() {
        VBox v5 = new VBox();
        v5.setStyle("-fx-background-color: #004c9b;");
        v5.setAlignment(Pos.CENTER);
        v5.setSpacing(100);
        v5.setPadding(new Insets(80, 0, 30, 0));

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(40);
        buttons.getChildren().addAll(booksBTN, customersBTN);
        booksBTN.setPrefSize(100, 50);
        customersBTN.setPrefSize(100, 50);

        v5.getChildren().addAll(buttons, logoutBTN);
        return v5;
    }

    public Group checkoutScreen(boolean usedPoints) {
        Group checkout = new Group();
        double total, subTotal = 0, discount;
        int pointsEarned, i = 0, bookCount = 0;
        String[][] booksBought = new String[25][2];

        for (Book book : Owner.books) {
            if (book.getSelect().isSelected()) {
                subTotal += book.getPrice();
                booksBought[i][0] = book.getName();
                booksBought[i][1] = String.valueOf(book.getPrice());
                i = i + 1;
            }
        }

        if (usedPoints) {
            if ((double) currentCust.getPoints() / 100 >= subTotal) {
                discount = subTotal;
                currentCust.setPoints(-(int) subTotal * 100);
            } else {
                discount = ((double) currentCust.getPoints() / 100);
                currentCust.setPoints(-currentCust.getPoints());
            }
        } else {
            discount = 0;
        }

        total = subTotal - discount;
        pointsEarned = (int) total * 10;
        currentCust.setPoints(pointsEarned);

        HBox head = new HBox();
        head.setAlignment(Pos.CENTER);
        head.setSpacing(15);
        head.setPadding(new Insets(0, 0, 25, 0));

        VBox receipt = new VBox();
        receipt.setSpacing(7);
        Text receiptTxt = new Text("Receipt");
        receiptTxt.setFont(Font.font(null, FontWeight.BOLD, 12));
        Line thickLine = new Line(0, 150, 400, 150);
        thickLine.setStrokeWidth(3);
        receipt.getChildren().addAll(receiptTxt, thickLine);

        VBox receiptItems = new VBox();
        receiptItems.setStyle("-fx-background-color: YELLOW;");
        receiptItems.setSpacing(7);
        for (i = 0; i < 25; i++) {
            if (booksBought[i][0] != null) {
                Text bookTitle = new Text(booksBought[i][0]);
                Text bookPrice = new Text(booksBought[i][1]);
                BorderPane item = new BorderPane();
                item.setLeft(bookTitle);
                item.setRight(bookPrice);
                Line thinLine = new Line(0, 150, 400, 150);
                receiptItems.getChildren().addAll(item, thinLine);
                bookCount++;
            }
        }

        ScrollPane scrollReceipt = new ScrollPane(receiptItems);
        scrollReceipt.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollReceipt.setStyle("-fx-background-color:transparent;");
        scrollReceipt.setFitToWidth(true);
        if (bookCount <= 4) {
            scrollReceipt.setFitToHeight(true);
        } else {
            scrollReceipt.setPrefHeight(130);
        }

        Text subTotalTxt = new Text("Subtotal: $" + (Math.round(subTotal * 100.0)) / 100.0);
        Text pointsDisc = new Text("Points Discount: $" + (Math.round(discount * 100.0)) / 100.0);
        Text totalTxt = new Text("Total: $" + (Math.round(total * 100.0)) / 100.0);
        totalTxt.setFont(new Font("Times New Roman", 15));
        receipt.getChildren().addAll(scrollReceipt, subTotalTxt, pointsDisc, totalTxt);

        VBox bottom = new VBox();
        bottom.setSpacing(40);
        bottom.setAlignment(Pos.CENTER);
        Text info = new Text("Points Earned: " + pointsEarned
                + "and status is " + currentCust.getStatus());
        bottom.getChildren().addAll(info, logoutBTN);

        VBox screen = new VBox();
        screen.setStyle("-fx-background-color: #004c9b;");
        screen.setPadding(new Insets(60, 105, 500, 100));
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(head, receipt, bottom);

        checkout.getChildren().addAll(screen);
        Owner.books.removeIf(b -> b.getSelect().isSelected());
        return checkout;
    }

    public Group ownerBooksScene() {
        Group value = new Group();
        hb.getChildren().clear();
        booksTable.getItems().clear();
        booksTable.getColumns().clear();
        booksTable.setFocusModel(defaultFocusModel);

        Label label = new Label("Books");
        label.setFont(new Font("Times New Roman", 20));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        booksTable.setItems(addBooks());
        booksTable.getColumns().addAll(titleColumn, priceColumn);

        final TextField addBookTitle = new TextField();
        addBookTitle.setPromptText("Title");
        addBookTitle.setMaxWidth(titleColumn.getPrefWidth());
        final TextField addBookPrice = new TextField();
        addBookPrice.setMaxWidth(priceColumn.getPrefWidth());
        addBookPrice.setPromptText("Price");
        addBookTitle.setStyle("-fx-background-color: #004c9b;");
        addBookPrice.setStyle("-fx-background-color: #004c9b;");

        VBox middle = new VBox();
        final Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #004c9b;");
        Label bookAddErr = new Label("Invalid");
        bookAddErr.setTextFill(Color.color(1, 0, 0));

        addButton.setOnAction(e -> {
            try {
                double price = Math.round((Double.parseDouble(addBookPrice.getText())) * 100);
                Owner.books.add(new Book(addBookTitle.getText(), price / 100));

                booksTable.getItems().clear();
                booksTable.setItems(addBooks());
                addBookTitle.clear();
                addBookPrice.clear();
                middle.getChildren().remove(bookAddErr);
            } catch (Exception exception) {
                if (!middle.getChildren().contains(bookAddErr)) {
                    middle.getChildren().add(bookAddErr);
                }
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #004c9b;");
        deleteButton.setOnAction(e -> {
            Book selectedItem = booksTable.getSelectionModel().getSelectedItem();
            booksTable.getItems().remove(selectedItem);
            Owner.books.remove(selectedItem);
        });

        hb.getChildren().addAll(addBookTitle, addBookPrice, addButton, deleteButton);
        hb.setSpacing(3);
        hb.setAlignment(Pos.CENTER);

        HBox back = new HBox();
        back.setPadding(new Insets(5));
        back.getChildren().addAll(backBTN);

        middle.setAlignment(Pos.CENTER);
        middle.setSpacing(5);
        middle.setPadding(new Insets(0, 0, 0, 150));
        middle.getChildren().addAll(label, booksTable, hb);

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #004c9b;");
        vbox.setPadding(new Insets(0, 200, 60, 0));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(back, middle);

        value.getChildren().addAll(vbox);

        return value;
    }

    public Group customerScene() {
        Group val = new Group();
        hb.getChildren().clear();
        customersTable.getItems().clear();
        customersTable.getColumns().clear();

        Label label = new Label("Customers");
        label.setFont(new Font("Times New Roman", 20));

        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(140);
        usernameCol.setStyle("-fx-alignment: CENTER;");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(140);
        passwordCol.setStyle("-fx-alignment: CENTER;");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(100);
        pointsCol.setStyle("-fx-alignment: CENTER;");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        customersTable.setItems(addCustomers());
        customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol);

        final TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setMaxWidth(usernameCol.getPrefWidth());
        final TextField addPassword = new TextField();
        addPassword.setMaxWidth(passwordCol.getPrefWidth());
        addPassword.setPromptText("Password");
        addPassword.setStyle("-fx-background-color: #004c9b;");
        addUsername.setStyle("-fx-background-color: #004c9b;");

        VBox middle = new VBox();
        Text customerAddErr = new Text("Customer exists");
        customerAddErr.setFill(Color.color(1, 0, 0));
        final Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color:#004c9b;");
        addButton.setOnAction(e -> {
            boolean duplicate = false;

            for (Customer c : owner.getCustomers()) {
                if ((c.getUsername().equals(addUsername.getText()) && c.getPassword().equals(addPassword.getText()))
                        || (addUsername.getText().equals(owner.getUsername())
                                && addPassword.getText().equals(owner.getPassword()))) {
                    duplicate = true;
                    if (!middle.getChildren().contains(customerAddErr)) {
                        middle.getChildren().add(customerAddErr);
                    }
                }
            }

            if (!(addUsername.getText().equals("") || addPassword.getText().equals("")) && !duplicate) {
                owner.addCustomer(new Customer(addUsername.getText(), addPassword.getText()));
                customersTable.getItems().clear();
                customersTable.setItems(addCustomers());
                middle.getChildren().remove(customerAddErr);
                addPassword.clear();
                addUsername.clear();
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #004c9b;");
        deleteButton.setOnAction(e -> {
            Customer selectedItem = customersTable.getSelectionModel().getSelectedItem();
            customersTable.getItems().remove(selectedItem);

            owner.deleteCustomer(selectedItem);
        });

        hb.getChildren().addAll(addUsername, addPassword, addButton, deleteButton);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(3);

        HBox back = new HBox();
        back.setPadding(new Insets(5));
        back.getChildren().addAll(backBTN);

        middle.setAlignment(Pos.CENTER);
        middle.setSpacing(5);
        middle.setPadding(new Insets(0, 0, 0, 110));
        middle.getChildren().addAll(label, customersTable, hb);

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #004c9b;");
        vbox.setPadding(new Insets(0, 150, 60, 0));
        vbox.getChildren().addAll(back, middle);
        vbox.setAlignment(Pos.CENTER);

        val.getChildren().addAll(vbox);
        return val;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
