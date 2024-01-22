package com.harbour.clearview.api.application.dto;

import java.util.ArrayList;

public class TodoListDTO {
    private String title;
    private ArrayList<TodoDTO> todoDTOs;

    public TodoListDTO(String title) {
        this.title = title;
        todoDTOs = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TodoDTO> getTodoDTOs() {
        return todoDTOs;
    }

    public void setTodoDTOs(ArrayList<TodoDTO> todoDTOs) {
        this.todoDTOs = todoDTOs;
    }

    public void add(TodoDTO todoDTO) {
        todoDTOs.add(todoDTO);
    }
}
