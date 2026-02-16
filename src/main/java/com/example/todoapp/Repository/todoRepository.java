package com.example.todoapp.Repository;

import com.example.todoapp.Model.todoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface todoRepository extends MongoRepository<todoModel,String> {
    List<todoModel> findByUserId(String userId);
}
