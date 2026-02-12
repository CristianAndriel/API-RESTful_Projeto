package com.example.apirestful.controller;

import com.example.apirestful.dto.TaskRequestDTO;
import com.example.apirestful.dto.TaskResponseDTO;
import com.example.apirestful.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(
            @RequestParam(required = false) Boolean completed) {

        List<TaskResponseDTO> tasks;
        if (completed != null) {
            tasks = taskService.getTasksByStatus(completed);
        } else {
            tasks = taskService.getAllTasks();
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO requestDTO) {

        TaskResponseDTO response = taskService.updateTask(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> markAsCompleted(@PathVariable Long id) {
        TaskResponseDTO response = taskService.markAsCompleted(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/pending")
    public ResponseEntity<TaskResponseDTO> markAsPending(@PathVariable Long id) {
        TaskResponseDTO response = taskService.markAsPending(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedTasks() {
        taskService.deleteCompletedTasks();
        return ResponseEntity.noContent().build();
    }

}

