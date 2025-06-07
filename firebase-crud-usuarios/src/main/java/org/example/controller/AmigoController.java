package org.example.controller;

import com.google.firebase.database.*;
import org.example.model.Amigo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AmigoController {

    private final DatabaseReference ref;

    public AmigoController() {
        this.ref = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public void obtenerAmigos(String userId, Consumer<List<Amigo>> callback) {
        ref.child(userId).child("amigos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot amigosSnapshot) {
                List<Amigo> listaAmigos = new ArrayList<>();

                for (DataSnapshot amigoSnap : amigosSnapshot.getChildren()) {
                    String amigoId = amigoSnap.getKey();

                    ref.child(amigoId).child("correo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot correoSnapshot) {
                            String correo = correoSnapshot.getValue(String.class);
                            if (correo != null && !correo.isEmpty()) {
                                listaAmigos.add(new Amigo(amigoId, correo));
                            }
                            callback.accept(listaAmigos);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            callback.accept(new ArrayList<>());
                        }
                    });
                }

                if (!amigosSnapshot.hasChildren()) {
                    callback.accept(listaAmigos);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.accept(new ArrayList<>());
            }
        });
    }

    public void eliminarAmigo(String userId, String amigoId) {
        ref.child(userId).child("amigos").child(amigoId).removeValueAsync();
        ref.child(amigoId).child("amigos").child(userId).removeValueAsync();
    }
}
