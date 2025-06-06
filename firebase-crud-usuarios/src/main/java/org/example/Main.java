package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.view.LoginView;
import org.example.view.UsuarioView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        FirebaseConfig.initialize();

        // Mostrar vista de login
        LoginView login = new LoginView();
        login.mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void mostrarVentanaPrincipal(Stage stage) {
        UsuarioView root = new UsuarioView();
        Scene scene = new Scene(root, 700, 500);

        scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/socialmusic.png")));

        stage.setTitle("Social Music - Gestión de Usuarios");
        stage.setScene(scene);
        stage.show();
    }
}
