package com.harbour.clearview.api.datasource.dao;

import com.harbour.clearview.api.application.dto.TodoListDTO;

import java.util.Map;

public interface TodoDao {
    void addTodoList(TodoListDTO todoListDTO);

    TodoListDTO getTodoList(String title);

    void updateTodo(String title, Map<String, Object> data);

    void deleteTodo(String title);
}
