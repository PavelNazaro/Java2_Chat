package ru.pavelnazaro.chat.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.pavelnazaro.chat.controller.PrimaryController;

import java.io.IOException;

public class AppChat extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Чат");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        PrimaryController controller = loader.getController();
        stage.setOnHidden(e -> {
            try {
                controller.shutdown();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        stage.show();
    }
}
