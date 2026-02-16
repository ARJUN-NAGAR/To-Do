package com.example.todoapp.Repository;

import com.example.todoapp.Model.userModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface userRepository extends MongoRepository<userModel, String> {

    Optional<userModel> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}