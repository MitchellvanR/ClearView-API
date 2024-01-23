package com.harbour.clearview.api.datasource.dao;

import com.harbour.clearview.api.application.dto.TodoListDTO;

import java.util.Map;

public interface TodoDao {
    void addTodoList(TodoListDTO todoListDTO);

    TodoListDTO getTodoList(String title);

    void updateTodoValue(String todoListTitle, String todoTitle, Map<String, Object> data);

    void deleteTodoList(String title);

    void deleteTodoFromTodoList(String todoListTitle, String todoTitle);
}
