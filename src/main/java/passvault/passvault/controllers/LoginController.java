package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import passvault.passvault.models.UserManager;
import passvault.passvault.utils.DatabaseManager_App;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button forgotButton;
    @FXML private Button toSignUpButton;

    private UserManager userManager;
    private DatabaseManager_App dbManagerA;

    public static String send_user;

    @FXML
    public void initialize() {
        userManager = new UserManager();
        dbManagerA = new DatabaseManager_App();
        loginButton.setOnAction(event -> handleLogin());
        forgotButton.setOnAction(event -> openResetPasswordScreen());
        toSignUpButton.setOnAction(event -> openSignUpScreen());
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please fill in both username and password.");
        } else {
            if (userManager.authenticateUser(username, password)) {
                send_user = username;
                showAlert("Login Success", "Welcome, " + username + "!");
                openPasswordManagementScreen();
            } else {
                showAlert("Login Error", "Invalid username or password.");
            }
        }
    }

    private void openSignUpScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUpScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 623, 483));
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open sign-up screen.");
        }
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

    private void openResetPasswordScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPasswordScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) forgotButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 600));
            stage.setTitle("Reset Password");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open reset password screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}