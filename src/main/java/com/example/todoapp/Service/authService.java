package com.example.todoapp.Service;

import com.example.todoapp.DTO.LoginRequestDTO;
import com.example.todoapp.DTO.RegisterRequestDTO;
import com.example.todoapp.DTO.TokenResponseDTO;
import com.example.todoapp.Model.userModel;
import com.example.todoapp.Repository.userRepository;
import com.example.todoapp.Utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class authService {

    private final userRepository userRepo;
    private final JwtUtil jwtUtil;

    public authService(userRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponseDTO register(RegisterRequestDTO dto) {
        // Check if username already exists
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        userModel newUser = new userModel();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        // Reminder: You'll want to add BCrypt password encoding soon!
        newUser.setPassword(dto.getPassword());

        userRepo.save(newUser);

        // Generating token using email as the subject
        String token = jwtUtil.generateToken(newUser.getEmail());
        return new TokenResponseDTO(token);
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        userModel user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new TokenResponseDTO(token);
    }
}