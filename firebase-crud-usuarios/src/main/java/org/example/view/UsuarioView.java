package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Columnas proporcionales
        colNombre.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.45));
        colCorreo.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.55));

        tablaUsuarios.getColumns().addAll(colNombre, colCorreo);
        tablaUsuarios.setItems(usuariosData);

        tablaUsuarios.setPrefHeight(280); // altura reducida
        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        nombreField.setPromptText("Nombre");
        correoField.setPromptText("Correo");

        Button agregarBtn = new Button("Agregar");
        Button actualizarBtn = new Button("Actualizar");
        Button eliminarBtn = new Button("Eliminar");

        agregarBtn.setOnAction(e -> mostrarVentanaAgregar());

        actualizarBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                seleccionado.setNombre(nombreField.getText());
                controller.actualizarUsuario(seleccionado);
                mostrarAlerta("Usuario actualizado correctamente.");
                cargarUsuarios();
                correoField.setDisable(false);
            }
        });

        eliminarBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                controller.eliminarUsuario(seleccionado.getId());
                mostrarAlerta("Usuario eliminado correctamente.");
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

        HBox controles = new HBox(10, nombreField, correoField, agregarBtn, actualizarBtn, eliminarBtn);
        controles.setPadding(new Insets(10));

        this.getChildren().addAll(tablaUsuarios, controles);
        this.setPadding(new Insets(10));
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        System.out.println("📢 Llamando a obtenerUsuarios()");
        controller.obtenerUsuarios(lista -> Platform.runLater(() -> {
            System.out.println("📦 Usuarios recibidos: " + lista.size());
            usuariosData.setAll(lista);
        }));
    }



    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
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
            String nombre = nombreNuevo.getText();
            String correo = correoNuevo.getText();

            if (nombre.isEmpty() || correo.isEmpty()) {
                mostrarAlerta("Los campos no pueden estar vacíos.");
                return;
            }

            controller.correoYaExiste(correo, existe -> {
                if (existe) {
                    Platform.runLater(() -> mostrarAlerta("Ya existe un usuario con ese correo."));
                } else {
                    controller.agregarUsuario(nombre, correo);
                    Platform.runLater(() -> {
                        mostrarAlerta("Usuario agregado correctamente.");
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
