package com.harbour.clearview.api.application.resources;

import com.harbour.clearview.api.application.dto.TodoListDTO;
import com.harbour.clearview.api.application.services.TodoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Map;

@Path("/todoLists")
public class TodoResource {
    private TodoService todoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTodoList(TodoListDTO todoListDTO) {
        todoService.addTodoList(todoListDTO);
        return Response
                .created(URI.create("/" + todoListDTO.getTitle()))
                .entity(todoListDTO)
                .build();
    }

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoList(@PathParam("title") String title) {
        return Response
                .ok()
                .entity(todoService.getTodoList(title))
                .build();
    }

    @PATCH
    @Path("/{todoListTitle}/todos/{todoTitle}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTodoValue(@PathParam("todoListTitle") String todoListTitle, @PathParam("todoTitle") String todoTitle, Map<String, Object> data) {
        todoService.updateTodoValue(todoListTitle, todoTitle, data);
        return Response
                .noContent()
                .entity(data)
                .build();
    }

    @DELETE
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodoList(@PathParam("title") String title) {
        todoService.deleteTodoList(title);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{todoListTitle}/todos/{todoTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTodoFromTodoList(@PathParam("todoListTitle") String todoListTitle, @PathParam("todoTitle") String todoTitle) {
        todoService.deleteTodoFromTodoList(todoListTitle, todoTitle);
        return Response.ok().build();
    }

    @Inject
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }
}
