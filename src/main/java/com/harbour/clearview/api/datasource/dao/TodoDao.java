package com.harbour.clearview.api.datasource.dao;

import com.harbour.clearview.api.application.dto.TodoListDTO;

import java.util.Map;

public interface TodoDao {
    void addTodoList(TodoListDTO todoListDTO);

    TodoListDTO getTodoList(String title);

    <T> void updateTodoValue(String todoListTitle, String todoTitle, Map<String, T> data);

    void deleteTodoList(String title);
}
