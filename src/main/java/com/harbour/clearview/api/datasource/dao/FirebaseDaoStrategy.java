package com.harbour.clearview.api.datasource.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.dto.TodoListDTO;
import com.harbour.clearview.api.datasource.FirebaseInitializer;
import com.harbour.clearview.api.datasource.dao.exceptions.NoTodoListsFoundException;
import com.harbour.clearview.api.datasource.dao.exceptions.TodoListNotFoundException;
import com.harbour.clearview.api.datasource.dao.exceptions.TodosNotFoundException;
import com.harbour.clearview.api.datasource.util.FirebaseConstants;
import jakarta.enterprise.inject.Default;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

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
        Map<String, Object> addedTodoList = new HashMap<>();
        addedTodoList.put(FirebaseConstants.TODO_LIST_TITLE_KEY, todoListDTO.getTitle());
        addedTodoList.put(FirebaseConstants.TODO_LIST_DATE_KEY, todoListDTO.getDate());
        addedTodoList.put(FirebaseConstants.TODO_LIST_TODOS_KEY, todoListDTO.getTodos());
        documentReference.set(addedTodoList);
    }

    @Override
    public void addTodoToList(String todoListTitle, TodoDTO newTodo) {
        try {
            DocumentReference documentReference = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(todoListTitle);
            DocumentSnapshot documentSnapshot = documentReference.get().get();
            validateTodoListExists(documentSnapshot);
            List<Map<String, Object>> todos = extractTodosFromSnapshot(documentSnapshot);

            Map<String, Object> newTodoMap = new HashMap<>();
            newTodoMap.put(FirebaseConstants.TODO_TITLE_KEY, newTodo.getTitle());
            newTodoMap.put(FirebaseConstants.TODO_DESCRIPTION_KEY, newTodo.getDescription());
            newTodoMap.put(FirebaseConstants.TODO_COMPLETED_KEY, newTodo.isCompleted());

            todos.add(newTodoMap);

            documentReference.update(FirebaseConstants.TODO_LIST_TODOS_KEY, todos);
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
        }
    }

    @Override
    public TodoListDTO getTodoList(String title) {
        try {
            Query query = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).whereEqualTo(FirebaseConstants.TODO_LIST_TITLE_KEY, title);
            QuerySnapshot querySnapshot = query.get().get();
            validateTodoListExists(querySnapshot);
            return getTodoListDTO(querySnapshot);
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
        }
    }

    @Override
    public List<TodoListDTO> getAllTodoLists() {
        List<TodoListDTO> todoLists = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).get().get();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                TodoListDTO todoList = document.toObject(TodoListDTO.class);
                todoLists.add(todoList);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new NoTodoListsFoundException();
        }

        return todoLists;
    }

    @Override
    public void updateTodoValue(String todoListTitle, String todoTitle, Map<String, Object> updatedTodoValue) {
        try {
            DocumentReference documentReference = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(todoListTitle);
            DocumentSnapshot documentSnapshot = documentReference.get().get();
            validateTodoListExists(documentSnapshot);
            List<Map<String, Object>> todos = extractTodosFromSnapshot(documentSnapshot);
            for (Map<String, Object> todo : todos) {
                if (todo.get(FirebaseConstants.TODO_TITLE_KEY).equals(todoTitle)) {
                    todo.putAll(updatedTodoValue);
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

    @Override
    public void deleteTodoFromTodoList(String todoListTitle, String todoTitle) {
        try {
            DocumentReference documentReference = database.collection(FirebaseConstants.TODO_LISTS_COLLECTION_NAME).document(todoListTitle);
            DocumentSnapshot documentSnapshot = documentReference.get().get();
            validateTodoListExists(documentSnapshot);
            List<Map<String, Object>> todos = extractTodosFromSnapshot(documentSnapshot);
            int indexToRemove = findIndexOfTodoByTitle(todos, todoTitle);
            if (indexToRemove != -1) {
                todos.remove(indexToRemove);
                documentReference.update(FirebaseConstants.TODO_LIST_TODOS_KEY, todos);
            } else {
                throw new TodosNotFoundException();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new TodoListNotFoundException();
        }
    }

    private void validateTodoListExists(DocumentSnapshot documentSnapshot) {
        if (!documentSnapshot.exists()) throw new TodoListNotFoundException();
    }

    private void validateTodoListExists(QuerySnapshot querySnapshot) {
        if (querySnapshot.isEmpty()) throw new TodoListNotFoundException();
    }

    private List<Map<String, Object>> extractTodosFromSnapshot(DocumentSnapshot documentSnapshot) {
        @SuppressWarnings("unchecked") // This is safe because of the null check right after.
        List<Map<String, Object>> todos = (List<Map<String, Object>>) documentSnapshot.get(FirebaseConstants.TODO_LIST_TODOS_KEY);
        if (todos == null) {
            throw new TodosNotFoundException();
        }
        return todos;
    }

    private int findIndexOfTodoByTitle(List<Map<String, Object>> todos, String todoTitle) {
        return IntStream.range(0, todos.size())
                .filter(i -> todos.get(i).get(FirebaseConstants.TODO_TITLE_KEY).equals(todoTitle))
                .findFirst()
                .orElse(-1);
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
        todoDTO.setCompleted(todoCompleted);
        return todoDTO;
    }
}
