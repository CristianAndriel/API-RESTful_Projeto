# 🚀 Guia de Início Rápido - Migração para Spring Boot

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:
- [ ] Java 17 ou superior ([Download](https://www.oracle.com/java/technologies/downloads/))
- [ ] Maven 3.8+ ou Gradle ([Maven Download](https://maven.apache.org/download.cgi))
- [ ] IDE (IntelliJ IDEA recomendado, ou VS Code com Java Extension Pack)
- [ ] PostgreSQL ou MySQL ([PostgreSQL Download](https://www.postgresql.org/download/))
- [ ] Postman ou Insomnia (para testar APIs)
- [ ] Docker Desktop (opcional, mas recomendado)
- [ ] Git

---

## 🎯 PASSO 1: Criar Projeto Spring Boot

### Opção A: Usando Spring Initializr (Recomendado)

1. Acesse: https://start.spring.io/
2. Configure:
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.2.x (última stable)
   - **Group**: com.seunome
   - **Artifact**: taskapi
   - **Name**: TaskAPI
   - **Package name**: com.seunome.taskapi
   - **Packaging**: Jar
   - **Java**: 17

3. Adicione as dependências:
   - Spring Web
   - Spring Data JPA
   - PostgreSQL Driver (ou MySQL Driver)
   - Lombok
   - Validation
   - Spring Boot DevTools
   - Spring Boot Actuator
   - Springdoc OpenAPI (para Swagger)

4. Clique em **GENERATE** e baixe o ZIP

5. Extraia e abra no IntelliJ IDEA

### Opção B: Usando IntelliJ IDEA

1. File → New → Project
2. Selecione **Spring Initializr**
3. Configure conforme acima
4. Next → Selecione dependências → Finish

---

## 🎯 PASSO 2: Estrutura do Projeto

Crie a seguinte estrutura de pastas:

```
taskapi/
├── src/main/java/com/seunome/taskapi/
│   ├── TaskApiApplication.java     # Já existe
│   ├── controller/
│   │   └── TaskController.java
│   ├── service/
│   │   ├── TaskService.java
│   │   └── impl/
│   │       └── TaskServiceImpl.java
│   ├── repository/
│   │   └── TaskRepository.java
│   ├── model/
│   │   └── Task.java
│   ├── dto/
│   │   ├── TaskRequestDTO.java
│   │   └── TaskResponseDTO.java
│   ├── exception/
│   │   ├── TaskNotFoundException.java
│   │   └── GlobalExceptionHandler.java
│   └── config/
│       └── SwaggerConfig.java
├── src/main/resources/
│   ├── application.properties
│   └── application-dev.properties
└── pom.xml
```

---

## 🎯 PASSO 3: Configurar Banco de Dados

### 3.1 - Criar banco de dados MySQL

```sql
CREATE DATABASE taskdb;
```

### 3.2 - Configurar application.properties

Edite `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=TaskAPI
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
```

---

## 🎯 PASSO 4: Criar Entidade Task (Modelo)

Crie `model/Task.java`:

```java
package com.seunome.taskapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

---

## 🎯 PASSO 5: Criar DTOs

### TaskRequestDTO.java

```java
package com.seunome.taskapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDTO {
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    private String title;
    
    @Size(max = 1000, message = "Descrição pode ter no máximo 1000 caracteres")
    private String description;
}
```

### TaskResponseDTO.java

```java
package com.seunome.taskapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 🎯 PASSO 6: Criar Repository

Crie `repository/TaskRepository.java`:

```java
package com.seunome.taskapi.repository;

import com.seunome.taskapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByCompleted(Boolean completed);
    
    List<Task> findByTitleContainingIgnoreCase(String title);
}
```

---

## 🎯 PASSO 7: Criar Service

### TaskService.java (Interface)

```java
package com.seunome.taskapi.service;

import com.seunome.taskapi.dto.TaskRequestDTO;
import com.seunome.taskapi.dto.TaskResponseDTO;

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
```

### TaskServiceImpl.java (Implementação)

```java
package com.seunome.taskapi.service.impl;

import com.seunome.taskapi.dto.TaskRequestDTO;
import com.seunome.taskapi.dto.TaskResponseDTO;
import com.seunome.taskapi.exception.TaskNotFoundException;
import com.seunome.taskapi.model.Task;
import com.seunome.taskapi.repository.TaskRepository;
import com.seunome.taskapi.service.TaskService;
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
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada com ID: " + id));
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
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada com ID: " + id));
        
        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        
        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }
    
    @Override
    @Transactional
    public TaskResponseDTO markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada com ID: " + id));
        
        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }
    
    @Override
    @Transactional
    public TaskResponseDTO markAsPending(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Tarefa não encontrada com ID: " + id));
        
        task.setCompleted(false);
        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }
    
    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Tarefa não encontrada com ID: " + id);
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
```

---

## 🎯 PASSO 8: Criar Controller

Crie `controller/TaskController.java`:

```java
package com.seunome.taskapi.controller;

import com.seunome.taskapi.dto.TaskRequestDTO;
import com.seunome.taskapi.dto.TaskResponseDTO;
import com.seunome.taskapi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
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
```

---

## 🎯 PASSO 9: Tratamento de Exceções

### TaskNotFoundException.java

```java
package com.seunome.taskapi.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
```

### GlobalExceptionHandler.java

```java
package com.seunome.taskapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.badRequest().body(response);
    }
    
    // Classe interna para resposta de erro
    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}
```

---

## 🎯 PASSO 10: Executar e Testar

### 10.1 - Executar a aplicação

No terminal do IntelliJ ou CMD:
```bash
mvn spring-boot:run
```

Ou execute a classe `TaskApiApplication.java` diretamente na IDE.

### 10.2 - Acessar Swagger

Abra o navegador: http://localhost:8080/swagger-ui.html

### 10.3 - Testar endpoints com curl

```bash
# Criar tarefa
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Estudar Spring Boot\",\"description\":\"Aprender REST APIs\"}"

# Listar todas
curl http://localhost:8080/api/v1/tasks

# Buscar por ID
curl http://localhost:8080/api/v1/tasks/1

# Marcar como concluída
curl -X PATCH http://localhost:8080/api/v1/tasks/1/complete

# Deletar
curl -X DELETE http://localhost:8080/api/v1/tasks/1
```

---

## 🎯 PASSO 11: Adicionar Dependência do Swagger

Se não adicionou no início, adicione ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

---

## ✅ CHECKLIST DE VERIFICAÇÃO

- [ ] Projeto Spring Boot criado e funcionando
- [ ] Banco de dados conectado
- [ ] Entidade Task criada com JPA
- [ ] Repository criado e testado
- [ ] Service implementado
- [ ] Controller com todos os endpoints
- [ ] Tratamento de exceções funcionando
- [ ] Validações ativas
- [ ] Swagger acessível
- [ ] Testes manuais realizados

---

## 📚 PRÓXIMOS PASSOS

1. ✅ Adicionar testes unitários
2. ✅ Adicionar testes de integração
3. ✅ Criar Dockerfile
4. ✅ Criar docker-compose.yml
5. ✅ Configurar CI/CD
6. ✅ Melhorar documentação
7. ✅ Adicionar funcionalidades extras

---

## 🆘 PROBLEMAS COMUNS

### Erro de conexão com banco
- Verifique se PostgreSQL está rodando
- Confirme usuário e senha no application.properties

### Lombok não funciona
- Instale o plugin Lombok na IDE
- Enable Annotation Processing (IntelliJ: Settings → Build → Compiler → Annotation Processors)

### Porta 8080 já em uso
- Mude no application.properties: `server.port=8081`

---

## 📞 RECURSOS ÚTEIS

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

---

**Boa sorte! Você está pronto para começar! 🚀**

