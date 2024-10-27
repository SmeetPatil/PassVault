package passvault.passvault.controllers;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashController {
    private Stage stage;
    private Runnable onSplashFinished;

    public void showSplash(Runnable onFinished) {
        this.onSplashFinished = onFinished;
        try {
            stage = new Stage();

            // Create a pane
            Pane root = new Pane();
            root.setPrefSize(800, 449);

            // Set the background image using CSS
            root.setStyle("-fx-background-image: url('/splash.png');" +
                    "-fx-background-size: cover;" +
                    "-fx-background-position: center center;" +
                    "-fx-background-repeat: no-repeat;");

            // Create the scene
            Scene scene = new Scene(root, 800, 449);

            // Configure the stage
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            // Start the timer to close splash and show login
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Display for 3 seconds
                    Platform.runLater(() -> {
                        stage.close();
                        if (onSplashFinished != null) {
                            onSplashFinished.run();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeStage() {
        if (stage != null) {
            stage.close();
            if (onSplashFinished != null) {
                onSplashFinished.run();
            }
        }
    }
}