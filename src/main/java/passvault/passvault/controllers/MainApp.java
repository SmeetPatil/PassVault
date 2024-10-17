package passvault.passvault.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Login FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
        Parent root = loader.load();

        // Set the scene with the FXML content
        Scene scene = new Scene(root, 600, 400);

        // Set up the primary stage
        primaryStage.setTitle("PassVault - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}