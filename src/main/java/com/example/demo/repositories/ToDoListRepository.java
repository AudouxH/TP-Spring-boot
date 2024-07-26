package com.example.demo.repositories;

import com.example.demo.models.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    List<ToDoList> findByUsername(String username);

    @Query("SELECT t.id FROM ToDoList t WHERE t.username = :username")
    List<Long> findIdsByUsername(@Param("username") String username);
}
