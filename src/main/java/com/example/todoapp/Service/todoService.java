package com.example.todoapp.Service;

import com.example.todoapp.DTO.todoRequestDTO;
import com.example.todoapp.DTO.todoResponseDTO;
import com.example.todoapp.Model.todoModel;
import com.example.todoapp.Repository.todoRepository;
import com.example.todoapp.exception.todoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class todoService {

    private final todoRepository todoRepo;

    public todoService(todoRepository todoRepo) {
        this.todoRepo = todoRepo;
    }

    public todoResponseDTO addTodo(todoRequestDTO dto, String email) {
        todoModel todo = new todoModel();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setUserId(email);

        // If 'completed' is missing in JSON, default to false
        todo.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);

        todoModel saved = todoRepo.save(todo);
        return mapToResponseDTO(saved);
    }

    public List<todoResponseDTO> getAllTodos(String email) {
        return todoRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteTodo(String id) {
        if (!todoRepo.existsById(id)) {
            throw new todoNotFoundException("Todo not found with ID: " + id);
        }
        todoRepo.deleteById(id);
    }

    public todoResponseDTO updateTodo(String id, todoRequestDTO dto) {
        todoModel todo = todoRepo.findById(id)
                .orElseThrow(() -> new todoNotFoundException("Todo not found with ID: " + id));

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        // Update status if provided, otherwise keep existing
        if (dto.getCompleted() != null) {
            todo.setCompleted(dto.getCompleted());
        }

        todoModel updated = todoRepo.save(todo);
        return mapToResponseDTO(updated);
    }

    // Helper to keep code clean
    private todoResponseDTO mapToResponseDTO(todoModel model) {
        return new todoResponseDTO(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.isCompleted()
        );
    }
}