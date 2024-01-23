package com.harbour.clearview.api.datasource.dao;

import com.harbour.clearview.api.application.dto.TodoListDTO;

import java.util.Map;

public interface TodoDao {
    void addTodoList(TodoListDTO todoListDTO);

    TodoListDTO getTodoList(String title);

    void deleteTodoList(String title);
}
