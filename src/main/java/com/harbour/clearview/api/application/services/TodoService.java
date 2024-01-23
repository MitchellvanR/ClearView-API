package com.harbour.clearview.api.application.services;

import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.dto.TodoListDTO;
import com.harbour.clearview.api.datasource.dao.TodoDao;
import jakarta.inject.Inject;

import java.util.Map;

public class TodoService {
    private TodoDao todoDao;

    public void addTodoList(TodoListDTO todoListDTO) {
        todoDao.addTodoList(todoListDTO);
    }

    public TodoDTO getTodo(String title) {
        return todoDao.getTodo(title);
    }

    public void updateTodo(String title, Map<String, Object> data) {
        this.todoDao.updateTodo(title, data);
    }

    public void deleteTodo(String title) {
        this.todoDao.deleteTodo(title);
    }

    @Inject
    public void setTodoDao(TodoDao todoDao) {
        this.todoDao = todoDao;
    }
}
