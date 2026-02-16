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

        todoModel saved = todoRepo.save(todo);
        return new todoResponseDTO(saved.getId(), saved.getTitle(), saved.getDescription(), saved.isCompleted());
    }

    public List<todoResponseDTO> getAllTodos(String email) {
        return todoRepo.findAll().stream()
                .map(t -> new todoResponseDTO(t.getId(), t.getTitle(), t.getDescription(), t.isCompleted()))
                .collect(Collectors.toList());
    }

    public void deleteTodo(String id) {
        if (!todoRepo.existsById(id)) {
            throw new todoNotFoundException("Todo with ID " + id + " not found");
        }
        todoRepo.deleteById(id);
    }

    public todoResponseDTO updateTodo(String id, todoRequestDTO dto) {
        todoModel todo = todoRepo.findById(id)
                .orElseThrow(() -> new todoNotFoundException("Todo not found"));

        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        todoModel updated = todoRepo.save(todo);
        return new todoResponseDTO(updated.getId(), updated.getTitle(), updated.getDescription(), updated.isCompleted());
    }
}