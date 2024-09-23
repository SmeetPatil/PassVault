package passvault.passvault.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import passvault.passvault.models.User;

import java.io.*;
import java.util.Random;

public class PasswordManagementController {

    @FXML private TextField sizeField;
    @FXML private TextField passwordUsernameField;
    @FXML private TextField websiteField;
    @FXML private Label passwordLabel;
    @FXML private Button generateButton;
    @FXML private Button saveButton;
    @FXML private Button viewButton;

    private String generatedPassword;

    String filename = LoginController.send_user + "_passwords.txt";
    File file = new File(filename);

    @FXML
    public void initialize() {
        generateButton.setOnAction(event -> generatePassword());
        saveButton.setOnAction(event -> savePassword());
        viewButton.setOnAction(event -> viewPasswords());
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

        generatedPassword = password.toString();
        passwordLabel.setText("Generated Password: " + generatedPassword);
    }

    @FXML
    public void savePassword() {
        if (generatedPassword == null || generatedPassword.isEmpty()) {
            showAlert("Error", "Generate or Enter a password first");
            return;
        }

        String username = passwordUsernameField.getText();
        String website = websiteField.getText();

        if (username.isEmpty() || website.isEmpty() || generatedPassword.isEmpty()) {
            showAlert("Error", "Please enter username and website");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Website: " + website + "   |  Username: " + username + "   |  Password: " + generatedPassword);
            writer.newLine();
            int n = website.length() + username.length() + generatedPassword.length() + 50 ;
            for(int i = 0; i < n;i++)
                writer.write("-");
            writer.newLine();
            showAlert("Success", "Password saved successfully");
        } catch (IOException e) {
            showAlert("Error", "Error saving password");
        }
    }

    @FXML
    public void viewPasswords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }


            TextArea textArea = new TextArea(content.toString());
            textArea.setPrefSize(500,640);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Saved Passwords");
            dialog.getDialogPane().setContent(textArea);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (IOException e) {
            showAlert("Error", "Error reading passwords file");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}