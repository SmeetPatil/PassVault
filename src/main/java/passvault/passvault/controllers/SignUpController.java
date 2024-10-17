package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import passvault.passvault.models.UserManager;

import java.io.IOException;

public class SignUpController {

    @FXML private TextField signupUsernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField signupPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button signUpButton;
    @FXML private Button toLoginButton;

    private UserManager userManager;

    @FXML
    public void initialize() {
        userManager = new UserManager();
        signUpButton.setOnAction(event -> handleSignUp());
        toLoginButton.setOnAction(event -> openLoginScreen());
    }

    @FXML
    public void handleSignUp() {
        String username = signupUsernameField.getText();
        String email = emailField.getText();
        String password = signupPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Sign-up Error", "All fields must be filled.");
        } else if (!password.equals(confirmPassword)) {
            showAlert("Sign-up Error", "Passwords do not match.");
        } else {
            if (userManager.addUser(username, password, email)) {
                showAlert("Sign-up Success", "Account created for: " + username);
                clearSignUpFields();
                openLoginScreen();
            } else {
                showAlert("Sign-up Error", "Username already exists or database error occurred.");
            }
        }
    }

    private void clearSignUpFields() {
        signupUsernameField.clear();
        emailField.clear();
        signupPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void openLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open login screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}