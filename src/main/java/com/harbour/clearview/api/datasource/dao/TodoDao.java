package com.harbour.clearview.api.datasource.dao;

import com.harbour.clearview.api.application.dto.TodoDTO;

import java.util.Map;

public interface TodoDao {
    void addTodo(TodoDTO todoDTO);

    TodoDTO getTodo(String title);

    void updateTodo(String title, Map<String, Object> data);
}
