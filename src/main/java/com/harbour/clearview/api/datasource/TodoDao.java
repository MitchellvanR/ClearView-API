package com.harbour.clearview.api.datasource;

import com.harbour.clearview.api.application.dto.TodoDTO;

public interface TodoDao {
    void addTodo(TodoDTO todoDTO);
}
