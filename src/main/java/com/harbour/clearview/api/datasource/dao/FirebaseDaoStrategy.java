package com.harbour.clearview.api.datasource.dao;

import com.google.cloud.firestore.*;
import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.datasource.FirebaseInitializer;
import com.harbour.clearview.api.datasource.dao.exceptions.CollectionNotFoundException;
import com.harbour.clearview.api.datasource.dao.exceptions.TodoNotFoundException;
import jakarta.enterprise.inject.Default;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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
        DocumentReference docRef = database.collection("todos").document(todoDTO.getTitle());
        Map<String, Object> data = new HashMap<>();
        data.put("title", todoDTO.getTitle());
        data.put("description", todoDTO.getDescription());
        data.put("isCompleted", todoDTO.isCompleted());
        docRef.set(data);
    }

    @Override
    public TodoDTO getTodo(String title) {
        try {
            Query query = database.collection("todos").whereEqualTo("title", title);
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                return getTodoDTO(querySnapshot);
            } else {
                throw new TodoNotFoundException();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new CollectionNotFoundException();
        }
    }

    @Override
    public void updateTodo(String title, Map<String, Object> data) {
        database.collection("todos").document(title).set(data, SetOptions.merge());
    }

    @Override
    public void deleteTodo(String title) {
        database.collection("todos").document(title).delete();
    }

    private TodoDTO getTodoDTO(QuerySnapshot querySnapshot) {
        QueryDocumentSnapshot document = querySnapshot.getDocuments().getFirst();

        TodoDTO todo = new TodoDTO();
        todo.setTitle(Objects.requireNonNull(document.getString("title")));
        todo.setDescription(Objects.requireNonNull(document.getString("description")));

        Boolean isCompleted = document.getBoolean("isCompleted");
        if (isCompleted != null && isCompleted) {
            todo.complete();
        } else {
            todo.incomplete();
        }
        return todo;
    }
}
