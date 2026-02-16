package com.example.todoapp.DTO;

public record todoResponseDTO(
        String id,
        String title,
        String description,
        boolean completed
) {}