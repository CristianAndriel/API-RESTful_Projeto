package com.example.apirestful.service;

import com.example.apirestful.dto.TaskRequestDTO;
import com.example.apirestful.dto.TaskResponseDTO;
import com.example.apirestful.model.Task;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO requestDTO);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO getTaskById(Long id);
    List<TaskResponseDTO> getTasksByStatus(Boolean completed);
    TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO);
    TaskResponseDTO markAsCompleted(Long id);
    TaskResponseDTO markAsPending(Long id);
    void deleteTask(Long id);
    void deleteCompletedTasks();
}

