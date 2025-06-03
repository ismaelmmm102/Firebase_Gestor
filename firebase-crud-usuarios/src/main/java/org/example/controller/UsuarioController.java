package org.example.controller;

import com.google.firebase.database.*;
import org.example.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioController {

    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");

    public interface UsuarioCallback {
        void onUsuariosCargados(List<Usuario> usuarios);
    }

    public interface UsuarioCallbackBoolean {
        void onResultado(boolean existe);
    }

    public void obtenerUsuarios(UsuarioCallback callback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Usuario> usuarios = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Usuario usuario = child.getValue(Usuario.class);
                    if (usuario != null) {
                        usuario.setId(child.getKey());
                        usuarios.add(usuario);
                    }
                }
                callback.onUsuariosCargados(usuarios);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("❌ Error al acceder: " + error.getMessage());
                callback.onUsuariosCargados(new ArrayList<>());
            }
        });
    }

    public void agregarUsuario(String nombre, String correo) {
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, nombre, correo);
        ref.child(id).setValueAsync(usuario);
    }

    public void eliminarUsuario(String id) {
        ref.child(id).removeValueAsync();
    }

    public void actualizarUsuario(Usuario usuario) {
        ref.child(usuario.getId()).setValueAsync(usuario);
    }

    public void correoYaExiste(String correo, UsuarioCallbackBoolean callback) {
        ref.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                callback.onResultado(snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("❌ Error al verificar correo: " + error.getMessage());
                callback.onResultado(false);
            }
        });
    }
}
