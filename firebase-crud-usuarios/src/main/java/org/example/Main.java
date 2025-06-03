package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.view.UsuarioView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        FirebaseConfig.initialize();

        UsuarioView root = new UsuarioView();
        Scene scene = new Scene(root, 700, 500);

        // Cargar hoja de estilos CSS
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Establecer icono de la ventana
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/socialmusic.png")));

        stage.setTitle("Social Music - Gestión de Usuarios");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
