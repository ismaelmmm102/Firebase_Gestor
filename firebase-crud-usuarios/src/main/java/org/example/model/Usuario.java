package org.example.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty correo = new SimpleStringProperty();

    public Usuario() {}

    public Usuario(String nombre, String correo) {
        this.nombre.set(nombre);
        this.correo.set(correo);
    }

    public Usuario(String id, String nombre, String correo) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.correo.set(correo);
    }

    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public StringProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getCorreo() { return correo.get(); }
    public void setCorreo(String correo) { this.correo.set(correo); }
    public StringProperty correoProperty() { return correo; }
}
