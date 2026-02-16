package com.example.todoapp.Controller;

import com.example.todoapp.DTO.todoRequestDTO;
import com.example.todoapp.DTO.todoResponseDTO;
import com.example.todoapp.Service.todoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class todoController {

    private final todoService service;

    public todoController(todoService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public todoResponseDTO addTodo(@Valid @RequestBody todoRequestDTO todo) {
        return service.addTodo(todo, "guest@example.com");
    }

    @GetMapping("/all")
    public List<todoResponseDTO> getAllTodos() {
        return service.getAllTodos("guest@example.com");
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTodo(@PathVariable String id) {
        service.deleteTodo(id);
        return "Todo Deleted Successfully";
    }

    @PutMapping("/update/{id}")
    public todoResponseDTO updateTodo(
            @PathVariable String id,
            @Valid @RequestBody todoRequestDTO todo) {
        return service.updateTodo(id, todo);
    }
}