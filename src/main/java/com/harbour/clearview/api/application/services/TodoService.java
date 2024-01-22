package com.harbour.clearview.api.application.services;

import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.dto.TodoListDTO;

public class TodoService {

    public TodoService() {}

    public TodoListDTO getHardcodedTodos() {
        var todo1 = new TodoDTO("Todo1", "Todo1 description");
        var todo2 = new TodoDTO("Todo2", "Todo2 description");
        var todo3 = new TodoDTO("Todo3", "Todo3 description");
        var todos = new TodoListDTO("TodoList Monday");
        todos.add(todo1);
        todos.add(todo2);
        todos.add(todo3);
        return todos;
    }

}
