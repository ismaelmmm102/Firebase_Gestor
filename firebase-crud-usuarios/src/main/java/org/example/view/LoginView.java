package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginView {

    public void mostrar(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField correoField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Iniciar Sesión");
        Label mensaje = new Label();

        correoField.setPromptText("Correo");
        passwordField.setPromptText("Contraseña");

        loginButton.setOnAction(e -> {
            String correo = correoField.getText();
            String pass = passwordField.getText();

            if (validarCredenciales(correo, pass)) {
                Main.mostrarVentanaPrincipal(primaryStage);
            } else {
                mensaje.setText("Credenciales incorrectas.");
            }
        });

        layout.getChildren().addAll(new Label("Correo:"), correoField,
                new Label("Contraseña:"), passwordField,
                loginButton, mensaje);

        Scene scene = new Scene(layout, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/socialmusic.png")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();

    }

    private boolean validarCredenciales(String correo, String contrasena) {
        try (InputStream input = getClass().getResourceAsStream("/login.properties")) {
            if (input == null) return false;

            Properties props = new Properties();
            props.load(input);

            String storedPass = props.getProperty(correo);
            return storedPass != null && storedPass.equals(contrasena);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
