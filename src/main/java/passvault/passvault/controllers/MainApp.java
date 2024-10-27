package passvault.passvault.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showSplashThenLogin();
    }

    private void showSplashThenLogin() {
        SplashController splashController = new SplashController();
        // Pass the login screen initialization as a callback
        splashController.showSplash(() -> {
            try {
                showLoginScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showLoginScreen() throws Exception {
        // Load the Login FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
        Parent root = loader.load();

        // Set the scene with the FXML content
        Scene scene = new Scene(root, 600, 400);

        // Set up the primary stage
        primaryStage.setTitle("PassVault - Login");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/passvault.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}