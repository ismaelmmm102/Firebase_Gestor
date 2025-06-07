package org.example.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.35));

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCorreo.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.45));

        TableColumn<Usuario, String> colImagen = new TableColumn<>("Foto");
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagenPerfil"));
        colImagen.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty || url == null || url.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(url, 40, 40, true, true));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        colImagen.prefWidthProperty().bind(tablaUsuarios.widthProperty().multiply(0.2));

        tablaUsuarios.getColumns().addAll(colImagen, colNombre, colCorreo);
        tablaUsuarios.setItems(usuariosData);
        tablaUsuarios.setPrefHeight(280);
        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        nombreField.setPromptText("Nombre");
        correoField.setPromptText("Correo");

        Button agregarBtn = new Button("Agregar");
        Button actualizarBtn = new Button("Actualizar");
        Button eliminarBtn = new Button("Eliminar");
        Button verPlaylistsBtn = new Button("Ver Playlists");
        Button verAmigosBtn = new Button("Ver Amigos");

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

        verPlaylistsBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                new Stage() {{
                    setTitle("Playlists de " + seleccionado.getNombre());
                    setScene(new Scene(new PlaylistView(seleccionado.getId()), 500, 400));
                    getIcons().add(new Image(getClass().getResourceAsStream("/socialmusic.png")));
                    getScene().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                    show();
                }};
            } else {
                mostrarAlerta("Selecciona un usuario primero", Alert.AlertType.WARNING);
            }
        });


        verAmigosBtn.setOnAction(e -> {
            Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                new AmigoView(seleccionado.getId()).mostrarEnNuevaVentana(seleccionado.getNombre());
            } else {
                mostrarAlerta("Selecciona un usuario primero", Alert.AlertType.WARNING);
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

        HBox controles = new HBox(10, nombreField, correoField, actualizarBtn, agregarBtn, eliminarBtn, verPlaylistsBtn, verAmigosBtn);
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
                mostrarAlerta("Los campos no pueden estar vacíos.", Alert.AlertType.WARNING);
                return;
            }

            if (!correo.matches("^.+@.+\\..+$")) {
                mostrarAlerta("Correo no válido", Alert.AlertType.WARNING);
                return;
            }

            controller.correoYaExiste(correo, existe -> {
                if (existe) {
                    Platform.runLater(() -> mostrarAlerta("Ya existe un usuario con ese correo.", Alert.AlertType.WARNING));
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
        escena.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ventana.getIcons().add(new Image(getClass().getResourceAsStream("/socialmusic.png")));
        ventana.setScene(escena);
        ventana.show();
    }
}
