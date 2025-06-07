package org.example.model;

public class Amigo {
    private String id;
    private String correo;

    public Amigo() {}

    public Amigo(String id, String correo) {
        this.id = id;
        this.correo = correo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
