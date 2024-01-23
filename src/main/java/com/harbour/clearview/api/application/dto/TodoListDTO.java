package com.harbour.clearview.api.application.dto;

import java.util.List;

public class TodoListDTO {
    private String title;
    private String date;
    private List<TodoDTO> todos;

    public TodoListDTO() {}

    public TodoListDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TodoDTO> getTodos() {
        return todos;
    }

    public void setTodos(List<TodoDTO> todos) {
        this.todos = todos;
    }
}
