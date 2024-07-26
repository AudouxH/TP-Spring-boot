package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.models.ToDoList;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/{todolistId}")
    public ResponseEntity<List<Task>> getTasksByToDoList(@PathVariable Long todolistId) {
        Optional<ToDoList> optionalToDoList = toDoListRepository.findById(todolistId);
        if (optionalToDoList.isPresent()) {
            ToDoList toDoList = optionalToDoList.get();
            return ResponseEntity.ok(toDoList.getTasks());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{todolistId}")
    public ResponseEntity<Task> createTask(@PathVariable("toDoListId") Long todolistId, @RequestBody Task task) {
        Optional<ToDoList> optionalToDoList = toDoListRepository.findById(todolistId);
        if (optionalToDoList.isPresent()) {
            ToDoList toDoList = optionalToDoList.get();
            task.setId(null); // Ensure that a new Task is created
            toDoList.getTasks().add(task);
            toDoListRepository.save(toDoList);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{todolistId}/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable("toDoListId") Long todolistId, @PathVariable("taskId") Long taskId, @RequestBody Task taskDetails) {
        Optional<ToDoList> optionalToDoList = toDoListRepository.findById(todolistId);
        if (optionalToDoList.isPresent()) {
            ToDoList toDoList = optionalToDoList.get();
            Optional<Task> optionalTask = toDoList.getTasks().stream().filter(task -> task.getId().equals(taskId)).findFirst();
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                task.setChecked(taskDetails.isChecked());
                task.setLibelle(taskDetails.getLibelle());
                toDoListRepository.save(toDoList);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{todolistId}/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("toDoListId") Long todolistId, @PathVariable("taskId") Long taskId) {
        Optional<ToDoList> optionalToDoList = toDoListRepository.findById(todolistId);
        if (optionalToDoList.isPresent()) {
            ToDoList toDoList = optionalToDoList.get();
            Optional<Task> optionalTask = toDoList.getTasks().stream().filter(task -> task.getId().equals(taskId)).findFirst();
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                toDoList.getTasks().remove(task);
                toDoListRepository.save(toDoList);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
