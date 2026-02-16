package com.example.todoapp.Controller;

import com.example.todoapp.DTO.todoRequestDTO;
import com.example.todoapp.DTO.todoResponseDTO;
import com.example.todoapp.Service.todoService;
import com.example.todoapp.Utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class todoController {

    private final todoService service;
    private final JwtUtil jwtUtil;

    public todoController(todoService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // Helper method to extract identity from the Bearer token
    private String getEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateTokenAndGetEmail(token);
    }

    @PostMapping("/add")
    public todoResponseDTO addTodo(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody todoRequestDTO todo) {

        String userEmail = getEmailFromToken(authHeader);
        return service.addTodo(todo, userEmail); // Pass identity to service
    }

    @GetMapping("/all")
    public List<todoResponseDTO> getAllTodos(
            @RequestHeader("Authorization") String authHeader) {

        String userEmail = getEmailFromToken(authHeader);
        return service.getAllTodos(userEmail);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {

        String userEmail = getEmailFromToken(authHeader);
        service.deleteTodo(id, userEmail); // Service should check ownership
        return "Todo Deleted Successfully";
    }

    @PutMapping("/update/{id}")
    public todoResponseDTO updateTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id,
            @Valid @RequestBody todoRequestDTO todo) {

        String userEmail = getEmailFromToken(authHeader);
        return service.updateTodo(id, todo, userEmail);
    }
}