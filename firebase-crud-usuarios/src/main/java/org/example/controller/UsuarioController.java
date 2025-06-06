package org.example.controller;

import com.google.firebase.database.*;
import org.example.model.Usuario;

import java.util.*;
import java.util.function.Consumer;

public class UsuarioController {

    private final DatabaseReference dbRef;
    private static final String IMAGEN_POR_DEFECTO = "https://cdn-icons-png.flaticon.com/512/1946/1946429.png";

    public UsuarioController() {
        dbRef = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public void obtenerUsuarios(Consumer<List<Usuario>> callback) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Usuario> lista = new ArrayList<>();
                for (DataSnapshot hijo : snapshot.getChildren()) {
                    Usuario usuario = hijo.getValue(Usuario.class);
                    if (usuario != null) {
                        usuario.setId(hijo.getKey());
                        lista.add(usuario);
                    }
                }
                callback.accept(lista);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error al obtener usuarios: " + error.getMessage());
            }
        });
    }

    public void agregarUsuario(String nombre, String correo, String imagenUrl) {
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, nombre, correo);

        if (imagenUrl == null || imagenUrl.trim().isEmpty()) {
            usuario.setImagenPerfil(IMAGEN_POR_DEFECTO);
        } else {
            usuario.setImagenPerfil(imagenUrl);
        }

        dbRef.child(id).setValueAsync(usuario);
    }

    public void actualizarUsuario(Usuario usuario) {
        if (usuario.getId() != null) {
            dbRef.child(usuario.getId()).setValueAsync(usuario);
        }
    }

    public void eliminarUsuario(String id) {
        dbRef.child(id).removeValueAsync();
    }

    public void correoYaExiste(String correo, Consumer<Boolean> callback) {
        dbRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                callback.accept(snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error al comprobar correo: " + error.getMessage());
                callback.accept(false);
            }
        });
    }
}
