package com.harbour.clearview.api.application.dto;

public class TodoDTO {
    private String title;
    private String description;
    private boolean completed;

    public TodoDTO() {}

    public TodoDTO(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }

    public void incomplete() { completed = false; }
}
