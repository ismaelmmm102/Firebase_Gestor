package org.example.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty correo = new SimpleStringProperty();
    private final StringProperty imagenPerfil = new SimpleStringProperty();
    private final StringProperty imagenPerfilBase64 = new SimpleStringProperty();

    public Usuario() {}

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

    public String getImagenPerfil() { return imagenPerfil.get(); }
    public void setImagenPerfil(String imagenPerfil) { this.imagenPerfil.set(imagenPerfil); }
    public StringProperty imagenPerfilProperty() { return imagenPerfil; }

    public String getImagenPerfilBase64() { return imagenPerfilBase64.get(); }
    public void setImagenPerfilBase64(String imagenPerfilBase64) { this.imagenPerfilBase64.set(imagenPerfilBase64); }
    public StringProperty imagenPerfilBase64Property() { return imagenPerfilBase64; }
}
