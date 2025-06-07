package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.AmigoController;
import org.example.model.Amigo;

public class AmigoView extends VBox {
    private final TableView<Amigo> tabla;
    private final ObservableList<Amigo> amigoData;
    private final AmigoController controller;
    private final String userId;

    public AmigoView(String userId) {
        this.userId = userId;
        this.controller = new AmigoController();
        this.amigoData = FXCollections.observableArrayList();
        this.tabla = new TableView<>();

        tabla.setItems(amigoData);
        tabla.setPrefHeight(250);

        TableColumn<Amigo, String> correoCol = new TableColumn<>("Correo");
        correoCol.setCellValueFactory(new PropertyValueFactory<>("correo"));
        correoCol.prefWidthProperty().bind(tabla.widthProperty());

        tabla.getColumns().add(correoCol);

        Button eliminarBtn = new Button("Eliminar");
        eliminarBtn.setOnAction(e -> {
            Amigo seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controller.eliminarAmigo(userId, seleccionado.getId());
                mostrarAlerta("Amigo eliminado correctamente.", Alert.AlertType.INFORMATION);
                cargarAmigos();
            } else {
                mostrarAlerta("Selecciona un amigo para eliminar.", Alert.AlertType.WARNING);
            }
        });

        HBox botones = new HBox(10, eliminarBtn);
        botones.setPadding(new Insets(10));

        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(tabla, botones);

        cargarAmigos();
    }

    private void cargarAmigos() {
        controller.obtenerAmigos(userId, lista -> {
            ObservableList<Amigo> filtrados = FXCollections.observableArrayList();
            for (Amigo a : lista) {
                if (a.getCorreo() != null && !a.getCorreo().isEmpty()) {
                    filtrados.add(a);
                }
            }
            amigoData.setAll(filtrados);
        });
    }

    public void mostrarEnNuevaVentana(String nombreUsuario) {
        Stage stage = new Stage();
        Scene scene = new Scene(this, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/socialmusic.png")));
        stage.setTitle("Amigos de " + nombreUsuario);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
