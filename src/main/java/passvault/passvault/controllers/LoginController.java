package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import passvault.passvault.models.UserManager;
import javafx.scene.input.KeyCode;


import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button signUpButton;

    private UserManager userManager;

    public static String send_user;

    @FXML
    public void initialize() {
        userManager = new UserManager();
        loginButton.setOnAction(event -> handleLogin());
        signUpButton.setOnAction(event -> handleSignUp());
    }

    @FXML
    public void handleLogin() {
        String username = send_user = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please fill in both username and password.");
        } else {
            if (userManager.authenticateUser(username, password)) {
                showAlert("Login Success", "Welcome, " + username + "!");
                openPasswordManagementScreen();
            } else {
                showAlert("Login Error", "Invalid username or password.");
            }
        }
    }

    @FXML
    public void handleSignUp() {
        String username = signupUsernameField.getText();
        String password = signupPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Sign-up Error", "All fields must be filled.");
        } else if (!password.equals(confirmPassword)) {
            showAlert("Sign-up Error", "Passwords do not match.");
        } else {
            if (userManager.addUser(username, password)) {
                showAlert("Sign-up Success", "Account created for: " + username);
                clearSignUpFields();
            } else {
                showAlert("Sign-up Error", "Username already exists.");
            }
        }
    }

    private void clearSignUpFields() {
        signupUsernameField.clear();
        signupPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void openPasswordManagementScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PasswordManagementScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 640, 480));
            stage.setTitle("Password Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open password management screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}