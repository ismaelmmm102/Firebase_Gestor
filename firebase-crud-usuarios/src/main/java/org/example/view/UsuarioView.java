package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.controller.UsuarioController;
import org.example.model.Usuario;

public class UsuarioView extends VBox {
    private final TableView<Usuario> tablaUsuarios;
    private final ObservableList<Usuario> usuariosData;
    private final UsuarioController controller;

    private final TextField nombreField = new TextField();
    private final TextField correoField = new TextField();

    public UsuarioView() {
        tablaUsuarios = new TableView<>();
        usuariosData = FXCollections.observableArrayList();
        controller = new UsuarioController();

        TableColumn<Usuario, String> colImagen = new TableColumn<>("Imagen");
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagenPerfil"));
        colImagen.setPrefWidth(80);
        colImagen.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new javafx.scene.image.Image(imageUrl, true));
                    setGraphic(imageView);
                }
            }
        });

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.4));

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCorreo.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.5));

        tablaUsuarios.getColumns().addAll(colImagen, colNombre, colCorreo);
        tablaUsuarios.setItems(usuariosData);
        tablaUsuarios.setPrefHeight(280);
        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        nombreField.setPromptText("Nombre");
        correoField.setPromptText("Correo");

        Button actualizarBtn = new Button("Actualizar");
        Button agregarBtn = new Button("Agregar");
        Button eliminarBtn = new Button("Eliminar");

        agregarBtn.setOnAction(e -> mostrarVentanaAgregar());

        actualizarBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                seleccionado.setNombre(nombreField.getText());
                controller.actualizarUsuario(seleccionado);
                mostrarAlerta("Usuario actualizado correctamente.", Alert.AlertType.INFORMATION);
                cargarUsuarios();
                correoField.setDisable(false);
            }
        });

        eliminarBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controller.eliminarUsuario(seleccionado.getId());
                mostrarAlerta("Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
                cargarUsuarios();
                nombreField.clear();
                correoField.clear();
                correoField.setDisable(false);
            }
        });

        tablaUsuarios.setOnMouseClicked(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                nombreField.setText(seleccionado.getNombre());
                correoField.setText(seleccionado.getCorreo());
                correoField.setDisable(true);
            }
        });

        HBox controles = new HBox(10, nombreField, correoField, actualizarBtn, agregarBtn, eliminarBtn);
        controles.setPadding(new Insets(10));

        this.getChildren().addAll(tablaUsuarios, controles);
        this.setPadding(new Insets(10));
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        controller.obtenerUsuarios(lista -> Platform.runLater(() -> usuariosData.setAll(lista)));
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Mensaje");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarVentanaAgregar() {
        Stage ventana = new Stage();
        ventana.setTitle("Agregar nuevo usuario");

        TextField nombreNuevo = new TextField();
        TextField correoNuevo = new TextField();
        nombreNuevo.setPromptText("Nombre");
        correoNuevo.setPromptText("Correo");

        Button guardarBtn = new Button("Guardar");
        guardarBtn.setOnAction(event -> {
            String nombre = nombreNuevo.getText().trim();
            String correo = correoNuevo.getText().trim();

            if (nombre.isEmpty() || correo.isEmpty()) {
                mostrarAlerta("Los campos no pueden estar vacíos.", Alert.AlertType.WARNING);
                return;
            }

            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                mostrarAlerta("Introduce un correo electrónico válido.", Alert.AlertType.ERROR);
                return;
            }

            controller.correoYaExiste(correo, existe -> {
                if (existe) {
                    Platform.runLater(() ->
                            mostrarAlerta("Ya existe un usuario con ese correo.", Alert.AlertType.ERROR)
                    );
                } else {
                    controller.agregarUsuario(nombre, correo, null);
                    Platform.runLater(() -> {
                        mostrarAlerta("Usuario agregado correctamente.", Alert.AlertType.INFORMATION);
                        cargarUsuarios();
                        ventana.close();
                    });
                }
            });
        });

        VBox layout = new VBox(10, nombreNuevo, correoNuevo, guardarBtn);
        layout.setPadding(new Insets(20));
        Scene escena = new Scene(layout, 300, 150);
        ventana.setScene(escena);
        ventana.show();
    }
}
