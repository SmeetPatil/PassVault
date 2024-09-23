package passvault.passvault.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginSignUpScreen.fxml"));
        Parent root = loader.load();

        // Set the scene with the FXML content
        Scene scene = new Scene(root, 640, 800);  // Increased height to accommodate new content

        // Set up the primary stage
        primaryStage.setTitle("PassVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}