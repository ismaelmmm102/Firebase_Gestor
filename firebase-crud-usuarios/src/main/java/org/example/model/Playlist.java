package org.example.model;

import javafx.beans.property.*;

public class Playlist {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final BooleanProperty esPrivada = new SimpleBooleanProperty(false);

    public Playlist() {}

    public Playlist(String id, String nombre, String descripcion, boolean esPrivada) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.descripcion.set(descripcion);
        this.esPrivada.set(esPrivada);
    }

    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public StringProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public StringProperty descripcionProperty() { return descripcion; }

    public boolean isEsPrivada() { return esPrivada.get(); }
    public void setEsPrivada(boolean esPrivada) { this.esPrivada.set(esPrivada); }
    public BooleanProperty esPrivadaProperty() { return esPrivada; }
}