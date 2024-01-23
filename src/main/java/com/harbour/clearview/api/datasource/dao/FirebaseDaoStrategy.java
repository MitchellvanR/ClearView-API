package com.harbour.clearview.api.datasource.dao;

import com.google.cloud.firestore.*;
import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.dto.TodoListDTO;
import com.harbour.clearview.api.datasource.FirebaseInitializer;
import com.harbour.clearview.api.datasource.dao.exceptions.TodoListNotFoundException;
import com.harbour.clearview.api.datasource.util.FirebaseConstants;
import jakarta.enterprise.inject.Default;

import java.io.IOException;
import java.util.*;
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
    public void addTodoList(TodoListDTO todoListDTO) {
        DocumentReference documentReference = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(todoListDTO.getTitle());
        Map<String, Object> data = new HashMap<>();
        data.put(FirebaseConstants.TODO_LIST_TITLE_KEY, todoListDTO.getTitle());
        data.put(FirebaseConstants.TODO_LIST_DATE_KEY, todoListDTO.getDate());
        data.put(FirebaseConstants.TODO_LIST_TODOS_KEY, todoListDTO.getTodos());
        documentReference.set(data);
    }

    @Override
    public TodoListDTO getTodoList(String title) {
        try {
            Query query = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).whereEqualTo(FirebaseConstants.TODO_LIST_TITLE_KEY, title);
            QuerySnapshot querySnapshot = query.get().get();
            return getTodoListDTO(querySnapshot);
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
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

    private TodoListDTO getTodoListDTO(QuerySnapshot querySnapshot) {
        QueryDocumentSnapshot document = querySnapshot.getDocuments().getFirst();
        TodoListDTO todoListDTO = new TodoListDTO();
        todoListDTO.setTitle(Objects.requireNonNull(document.getString(FirebaseConstants.TODO_LIST_TITLE_KEY)));
        todoListDTO.setDate(Objects.requireNonNull(document.getString(FirebaseConstants.TODO_LIST_DATE_KEY)));
        todoListDTO.setTodos(extractTodos(document));
        return todoListDTO;
    }

    private List<TodoDTO> extractTodos(QueryDocumentSnapshot document) {
        List<?> todosList = (List<?>) document.get(FirebaseConstants.TODO_LIST_TODOS_KEY);
        List<TodoDTO> todoDTOList = new ArrayList<>();
        if (todosList == null) return todoDTOList;
        for (Object todoObj : todosList) {
            if (todoObj instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked") // The warning can be suppressed, because in the condition type safety is ensured by use of generics.
                Map<String, Object> todoMap = (Map<String, Object>) todoObj;
                todoDTOList.add(getTodoDTO(todoMap));
            }
        }
        return todoDTOList;
    }

    private TodoDTO getTodoDTO(Map<String, Object> todoMap) {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(Objects.requireNonNull((String) todoMap.get(FirebaseConstants.TODO_TITLE_KEY)));
        todoDTO.setDescription(Objects.requireNonNull((String) todoMap.get(FirebaseConstants.TODO_DESCRIPTION_KEY)));
        todoDTO.setCompleted((boolean) todoMap.get(FirebaseConstants.TODO_COMPLETED_KEY));
        return todoDTO;
    }
}
