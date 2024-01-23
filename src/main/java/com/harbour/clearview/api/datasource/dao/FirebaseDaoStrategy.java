package com.harbour.clearview.api.datasource.dao;

import com.google.cloud.firestore.*;
import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.dto.TodoListDTO;
import com.harbour.clearview.api.datasource.FirebaseInitializer;
import com.harbour.clearview.api.datasource.dao.exceptions.TodoListNotFoundException;
import com.harbour.clearview.api.datasource.dao.exceptions.TodosNotFoundException;
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
            if (querySnapshot.isEmpty()) throw new TodoListNotFoundException();
            return getTodoListDTO(querySnapshot);
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
        }
    }

    @Override
    public <T> void updateTodoValue(String todoListTitle, String todoTitle, Map<String, T> data) {
        try {
            DocumentReference documentReference = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(todoListTitle);
            DocumentSnapshot documentSnapshot = documentReference.get().get();
            if (!documentSnapshot.exists()) throw new TodoListNotFoundException();
            @SuppressWarnings("unchecked") // This is safe, because of the use of generics and a null check for todos. This ensures type safety.
            List<Map<String, T>> todos = (List<Map<String, T>>) documentSnapshot.get(FirebaseConstants.TODO_LIST_TODOS_KEY);
            if (todos == null) throw new TodosNotFoundException();
            for (Map<String, T> todo : todos) {
                if (todo.get(FirebaseConstants.TODO_TITLE_KEY).equals(todoTitle)) {
                    todo.putAll(data);
                    break;
                }
            }
            documentReference.update(FirebaseConstants.TODO_LIST_TODOS_KEY, todos);
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
        }
    }

    @Override
    public void deleteTodoList(String title) {
        database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(title).delete();
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
                Map<String, ?> todoMap = (Map<String, ?>) todoObj;
                todoDTOList.add(getTodoDTO(todoMap));
            }
        }
        return todoDTOList;
    }

    private TodoDTO getTodoDTO(Map<String, ?> todoMap) {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(Objects.requireNonNull((String) todoMap.get(FirebaseConstants.TODO_TITLE_KEY)));
        todoDTO.setDescription(Objects.requireNonNull((String) todoMap.get(FirebaseConstants.TODO_DESCRIPTION_KEY)));
        Boolean todoCompleted = (Boolean) todoMap.get(FirebaseConstants.TODO_COMPLETED_KEY);
        System.out.println(todoCompleted);
        todoDTO.setCompleted(todoCompleted);
        return todoDTO;
    }
}
