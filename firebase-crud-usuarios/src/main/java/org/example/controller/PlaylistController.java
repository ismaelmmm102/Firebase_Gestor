package org.example.controller;

import com.google.firebase.database.*;
import org.example.model.Playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlaylistController {

    private final DatabaseReference dbRef;

    public PlaylistController() {
        dbRef = FirebaseDatabase.getInstance().getReference("usuarios");
    }

    public void obtenerPlaylists(String userId, Consumer<List<Playlist>> callback) {
        dbRef.child(userId).child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Playlist> playlists = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Playlist p = data.getValue(Playlist.class);
                    if (p != null) {
                        p.setId(data.getKey());
                        playlists.add(p);
                    }
                }
                callback.accept(playlists);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.accept(new ArrayList<>());
            }
        });
    }

    public void eliminarPlaylist(String userId, String playlistId) {
        dbRef.child(userId).child("playlists").child(playlistId).removeValueAsync();
    }

    public void actualizarPlaylist(String userId, Playlist playlist) {
        DatabaseReference ref = dbRef.child(userId).child("playlists").child(playlist.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", playlist.getNombre());
        updates.put("descripcion", playlist.getDescripcion());
        updates.put("esPrivada", playlist.isEsPrivada());

        ref.updateChildrenAsync(updates);
    }

}