package com.harbour.clearview.api.datasource.dao;

import com.google.cloud.firestore.Firestore;
import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.datasource.FirebaseInitializer;
import jakarta.enterprise.inject.Default;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Default
public class FirebaseDaoStrategy implements TodoDao {

    private final Firestore database;

    public FirebaseDaoStrategy() {
        try {
            database = FirebaseInitializer.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTodo(TodoDTO todoDTO) {
        var docRef = database.collection("todos").document(todoDTO.getTitle());
        Map<String, Object> data = new HashMap<>();
        data.put("title", todoDTO.getTitle());
        data.put("description", todoDTO.getDescription());
        data.put("isCompleted", todoDTO.isCompleted());
        docRef.set(data);
    }
}
