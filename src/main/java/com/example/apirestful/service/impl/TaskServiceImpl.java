package com.example.apirestful.service.impl;

import com.example.apirestful.dto.TaskRequestDTO;
import com.example.apirestful.dto.TaskResponseDTO;
import com.example.apirestful.exception.TaskNotFoundException;
import com.example.apirestful.model.Task;
import com.example.apirestful.repository.TaskRepository;
import com.example.apirestful.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = new Task();
        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());
        return mapToResponseDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getTasksByStatus(Boolean completed) {
        return taskRepository.findByCompleted(completed)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());

        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());

        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());

        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO markAsPending(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());

        task.setCompleted(false);
        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCompletedTasks() {
        List<Task> completedTasks = taskRepository.findByCompleted(true);
        taskRepository.deleteAll(completedTasks);
    }

    // Método auxiliar para conversão
    private TaskResponseDTO mapToResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}

