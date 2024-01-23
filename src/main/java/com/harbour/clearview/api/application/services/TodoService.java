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

    public void addTodoToList(String todoListTitle, TodoDTO newTodo) {
        todoDao.addTodoToList(todoListTitle, newTodo);
    }

    public TodoListDTO getTodoList(String title) {
        return todoDao.getTodoList(title);
    }

    public void updateTodoValue(String todoListTitle, String todoTitle, Map<String, Object> data) {
        todoDao.updateTodoValue(todoListTitle, todoTitle, data);
    }

    public void deleteTodoList(String title) {
        this.todoDao.deleteTodoList(title);
    }

    public void deleteTodoFromTodoList(String todoListTitle, String todoTitle) {
        this.todoDao.deleteTodoFromTodoList(todoListTitle, todoTitle);
    }

    @Inject
    public void setTodoDao(TodoDao todoDao) {
        this.todoDao = todoDao;
    }
}
