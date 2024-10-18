package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import  javafx.scene.input.*;
import passvault.passvault.utils.DatabaseManager;



import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.List;
import java.util.Optional;

import static passvault.passvault.controllers.LoginController.send_user;

public class PasswordManagementController {

    @FXML private TextField sizeField;
    @FXML private TextField passwordUsernameField;
    @FXML private TextField passwordField;
    @FXML private TextField websiteField;
    @FXML private Label passwordLabel;
    @FXML private Button generateButton;
    @FXML private Button saveButton;
    @FXML private Button viewButton;
    @FXML private Button logoutButton;
    @FXML private Button copyUsernameButton;
    @FXML private Button copyPasswordButton;
    @FXML private Label domainLabel;
    @FXML private Label cUserLabel;

    private DatabaseManager dbManager;
    private String currentUser;

    @FXML
    public void initialize() {
        dbManager = new DatabaseManager();
        currentUser = send_user;
        cUserLabel.setText(" " + currentUser);
        try {
            dbManager.createUserTable(currentUser);
        } catch (SQLException e) {
            showAlert("Error", "Failed to initialize user table: " + e.getMessage());
        }
        generateButton.setOnAction(event -> generatePassword());
        saveButton.setOnAction(event -> savePassword());
        viewButton.setOnAction(event -> viewPasswords());
        logoutButton.setOnAction(event -> handleLogout());
    }

    @FXML
    public void generatePassword() {
        int size;

        try {
            size = Integer.parseInt(sizeField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for password size.");
            return;
        }

        if (size <= 6) {
            showAlert("Error", "Password size must be greater than 6");
            return;
        }

        StringBuilder password = new StringBuilder(size);
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+[]{}|;:,.<>?";

        for (int i = 0; i < size; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        String generatedPassword = password.toString();
        passwordField.setText(generatedPassword);
        passwordLabel.setText("Generated Password: " + generatedPassword);
    }

    @FXML
    public void savePassword() {
        String password = passwordField.getText();
        String username = passwordUsernameField.getText();
        String website = websiteField.getText();

        if (username.isEmpty() || website.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter username, website, and password");
            return;
        }

        try {
            if (dbManager.entryExists(currentUser, website, username, password)) {
                showAlert("Error", "This exact entry already exists");
                return;
            }

            dbManager.savePassword(currentUser, website, username, password);
            showAlert("Success", "Password saved successfully");
            passwordUsernameField.clear();
            passwordField.clear();
            sizeField.clear();
            websiteField.clear();
            passwordLabel.setText("Generated Password will appear here");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Entry already exists");
                alert.setContentText("An entry for this website and username already exists. Do you want to update the password?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        dbManager.savePassword(currentUser, website, username, password);
                        showAlert("Success", "Password updated successfully");
                        passwordUsernameField.clear();
                        passwordField.clear();
                        sizeField.clear();
                        websiteField.clear();
                        passwordLabel.setText("Generated Password will appear here");
                    } catch (SQLException ex) {
                        showAlert("Error", "Error updating password: " + ex.getMessage());
                    }
                }
            } else {
                showAlert("Error", "Error saving password: " + e.getMessage());
            }
        }
    }


    @FXML
    public void viewPasswords() {
        try {
            List<String> passwords = dbManager.getPasswords(currentUser);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewStageForHbox.fxml"));
            Parent root = loader.load();
            VBox container = (VBox) root.lookup("#container");
            container.setAlignment(Pos.TOP_CENTER);

            if (container == null) {
                System.out.println("Container VBox not found!");
                return;
            }

            // Add a visible label to show we're in the right place
            Label titleLabel = new Label("Saved Passwords - " + currentUser);
            titleLabel.setStyle("-fx-text-fill: magenta; -fx-font-size: 24px; -fx-font-weight: bold;");
            container.getChildren().add(titleLabel);

            Label website1 = new Label("Website");
            Label username1 = new Label("Username");
            Label password1 = new Label("Password");

            website1.setStyle("-fx-text-fill: #1174ed; -fx-font-size: 16px; -fx-font-weight: bold;");
            website1.setPrefWidth(250.0);
            website1.setAlignment(javafx.geometry.Pos.CENTER);
            username1.setStyle("-fx-text-fill: #1174ed; -fx-font-size: 16px; -fx-font-weight: bold;");
            username1.setPrefWidth(116.0);
            username1.setAlignment(javafx.geometry.Pos.CENTER);
            password1.setStyle("-fx-text-fill: #1174ed; -fx-font-size: 16px; -fx-font-weight: bold;");
            password1.setPrefWidth(129.0);
            password1.setAlignment(javafx.geometry.Pos.CENTER);

            HBox headers = new HBox();
            headers.setAlignment(javafx.geometry.Pos.CENTER);
            headers.setPrefHeight(36.0);
            headers.setPrefWidth(915.0);
            headers.setSpacing(50.0);
            headers.setStyle("-fx-background-color: Black;");
            headers.setPadding(new Insets(5, 0, 5, 0)); // Spacing between labels
            headers.getChildren().addAll(website1, username1, password1);

            container.getChildren().add(headers);



            for (String entry : passwords) {
                String[] parts = entry.split(" \\| ");

                if (parts.length == 3) {
                    String website = parts[0].replace("Website: ", "").trim();
                    String username = parts[1].replace("Username: ", "").trim();
                    String password = parts[2].replace("Password: ", "").trim();

                    HBox recordHBox = createRecordHBox(website, username, password);
                    container.getChildren().add(recordHBox);

                }
            }

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Saved Passwords for: " + currentUser);
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error displaying passwords: " + e.getMessage());
        }
    }

    private HBox createRecordHBox(String website, String username, String password) {
        HBox hbox = new HBox();
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        hbox.setPrefHeight(36.0);
        hbox.setPrefWidth(915.0);
        hbox.setSpacing(50.0);
        hbox.setStyle("-fx-background-color: Black;");
        hbox.setPadding(new Insets(5, 0, 5, 0));

        Label domainLabel = new Label(website);
        domainLabel.setTextFill(Color.WHITE);
        domainLabel.setPrefWidth(250.0);
        domainLabel.setAlignment(javafx.geometry.Pos.CENTER);
        domainLabel.setStyle("-fx-border-color: #1174ed; -fx-border-radius: 6; -fx-border-width: 2;");

        Button copyUsernameButton = new Button(username);
        copyUsernameButton.setStyle("-fx-background-color: #1174ed; -fx-background-radius: 6; -fx-font-weight: bold;");
        copyUsernameButton.setTextFill(Color.WHITE);
        copyUsernameButton.setPrefWidth(116.0);
        copyUsernameButton.setMaxWidth(Double.MAX_VALUE);
        copyUsernameButton.setMaxHeight(Double.MAX_VALUE);
        copyUsernameButton.setOnAction(e -> copyToClipboard(username));

        Button copyPasswordButton = new Button(password);
        copyPasswordButton.setStyle("-fx-background-color: #1174ed; -fx-background-radius: 6; -fx-font-weight: bold;");
        copyPasswordButton.setTextFill(Color.WHITE);
        copyPasswordButton.setPrefWidth(129.0);
        copyPasswordButton.setMaxWidth(Double.MAX_VALUE);
        copyPasswordButton.setMaxHeight(Double.MAX_VALUE);
        copyPasswordButton.setOnAction(e -> copyToClipboard(password));

        hbox.getChildren().addAll(domainLabel, copyUsernameButton, copyPasswordButton);

        return hbox;
    }

    private void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
        showAlert("Copied", "Copied to clipboard");
    }



    @FXML
    public void handleLogout() {
        try {
            showAlert("Logging out" , currentUser + " is logged out");
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the login screen
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");

            // Clear the current user data
            currentUser = null;
            // You might want to clear other user-specific data here

            // Show the login screen
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load login screen: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
