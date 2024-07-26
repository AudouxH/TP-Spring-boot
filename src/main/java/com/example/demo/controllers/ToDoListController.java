package com.example.demo.controllers;

import com.example.demo.Utils.JwtUtil;
import com.example.demo.models.ToDoList;
import com.example.demo.repositories.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todolist")
public class ToDoListController {
    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private JwtUtil jwtUtils;

    @GetMapping("/{username}")
    public ResponseEntity<List<ToDoList>> getToDoListsByUsername(@PathVariable("username") String username) {
        List<ToDoList> toDoLists = toDoListRepository.findByUsername(username);
        return ResponseEntity.ok(toDoLists);
    }

    @PostMapping
    public ResponseEntity<ToDoList> createToDoList(@RequestBody ToDoList toDoList) {
        ToDoList savedToDoList = toDoListRepository.save(toDoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToDoList);
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getToDoListByUsername(@RequestHeader("authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            if (jwtUtils.validateToken(token, jwtUtils.extractUsername(token))) {
                String username = jwtUtils.extractUsername(token);

                List<Long> toDoListIds = toDoListRepository.findIdsByUsername(username);

                return ResponseEntity.ok(toDoListIds);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error processing Token");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoList> updateToDoList(@PathVariable("id") Long id, @RequestBody ToDoList toDoListDetails) {
        Optional<ToDoList> optionalToDoList = toDoListRepository.findById(id);
        if (optionalToDoList.isPresent()) {
            ToDoList toDoList = optionalToDoList.get();
            toDoList.setUsername(toDoListDetails.getUsername());
            toDoList.setTasks(toDoListDetails.getTasks());
            ToDoList updatedToDoList = toDoListRepository.save(toDoList);
            return ResponseEntity.ok(updatedToDoList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDoList(@PathVariable("id") Long id) {
        toDoListRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
