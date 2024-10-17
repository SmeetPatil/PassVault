package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import passvault.passvault.models.UserManager;
import passvault.passvault.utils.EmailService;
import passvault.passvault.utils.OTPGenerator;

import java.io.IOException;

public class ResetPasswordController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField otpField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Button sendOTPButton;
    @FXML private Button resetPasswordButton;
    @FXML private Button backToLoginButton;
    @FXML private ImageView statusView;

    private UserManager userManager;
    private EmailService emailService;
    private OTPGenerator otpGenerator;
    private String generatedOTP;

    @FXML
    public void initialize() {
        userManager = new UserManager();
        emailService = new EmailService();
        otpGenerator = new OTPGenerator();

        otpField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Allow only digits and limit the length to 6 characters
            if (!newValue.matches("\\d*") || newValue.length() > 6) {
                otpField.setText(oldValue);
            }
            if(newValue.matches("\\d*") || newValue.length() == 6) {
                if (!newValue.equals(generatedOTP)) {
                    Image wrong = new Image(getClass().getResourceAsStream("/cross.png"));
                    statusView.setImage(wrong);
                } else if (newValue.equals(generatedOTP)) {
                    Image right = new Image(getClass().getResourceAsStream("/correct.png"));
                    statusView.setImage(right);
                }
            }
        });

        sendOTPButton.setOnAction(event -> handleSendOTP());
        resetPasswordButton.setOnAction(event -> handleResetPassword());
        backToLoginButton.setOnAction(event -> navigateToLogin());
    }

    private void handleSendOTP() {
        String username = usernameField.getText();
        String email = emailField.getText();

        if (userManager.isUserRegistered(username, email)) {
            generatedOTP = otpGenerator.generateOTP();
            emailService.sendOTP(email, generatedOTP);
            showAlert("OTP Sent", "OTP sent successfully");
        } else {
            showAlert("Error", "Username/Associated Email not found in our records.");
        }
    }


    private void handleResetPassword() {
        String username = usernameField.getText();
        String enteredOTP = otpField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();
        ImageView statusView = new ImageView();

        if (!enteredOTP.equals(generatedOTP)) {
            showAlert("Error", "Invalid OTP.");
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        if(newPassword.length() == 0)
        {
            showAlert("Error" , "Password must not be empty");
        }
        else {
            if (userManager.resetPassword(username, newPassword)) {
                showAlert("Success", "Password has been reset successfully.");
                navigateToLogin();
            } else {
                showAlert("Error", "Failed to reset password. Please try again.");
            }
        }
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not navigate to login screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}