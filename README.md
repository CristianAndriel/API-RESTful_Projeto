# 📝 Task Manager API

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

### API RESTful completa para gerenciamento de tarefas

*Desenvolvida com as melhores práticas de mercado e padrões de arquitetura*

[Documentação](#-documentação) •
[Funcionalidades](#-funcionalidades) •
[Tecnologias](#-tecnologias) •
[Instalação](#-instalação) •
[Endpoints](#-endpoints) •
[Swagger](#-swagger-ui)

</div>

---

## 📋 Sobre o Projeto

API REST robusta para gerenciamento de tarefas, desenvolvida com **Spring Boot 4**, seguindo os princípios **SOLID**, padrões de projeto e boas práticas de desenvolvimento. A aplicação implementa operações CRUD completas, validações, tratamento global de exceções e documentação interativa com Swagger.

### 🎯 Principais Destaques

- ✅ **Arquitetura em Camadas** (Controller, Service, Repository, DTOs)
- ✅ **Validações Automáticas** com Bean Validation
- ✅ **Tratamento Global de Exceções** com @RestControllerAdvice
- ✅ **Documentação Interativa** com Swagger/OpenAPI 3.0
- ✅ **Persistência de Dados** com JPA/Hibernate
- ✅ **Clean Code** e boas práticas
- ✅ **DTOs para Separação de Responsabilidades**
- ✅ **Timestamps Automáticos** (createdAt, updatedAt)

---

## 🚀 Funcionalidades

### Operações CRUD Completas

- **Criar** novas tarefas
- **Listar** todas as tarefas
- **Buscar** tarefa por ID
- **Atualizar** informações de tarefas
- **Deletar** tarefas específicas
- **Filtrar** tarefas por status (completa/pendente)
- **Marcar** tarefas como completas ou pendentes
- **Deletar em massa** tarefas completadas

### Validações Implementadas

- Título obrigatório (mínimo 3, máximo 200 caracteres)
- Descrição opcional (máximo 1000 caracteres)
- Validação de formato de dados
- Tratamento de erros com mensagens descritivas

---

## 🛠️ Tecnologias

### Backend

- **Java 21** - Linguagem de programação
- **Spring Boot 4.0.2** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **Spring Validation** - Validações de dados
- **Lombok** - Redução de boilerplate

### Banco de Dados

- **MySQL 8.0** - Banco de dados relacional

### Documentação

- **Springdoc OpenAPI 3** (v2.7.0) - Documentação interativa
- **Swagger UI** - Interface visual para testes

### Ferramentas

- **Maven** - Gerenciamento de dependências
- **Spring DevTools** - Hot reload em desenvolvimento
- **Spring Actuator** - Monitoramento e métricas

---

## 📦 Instalação e Configuração

### Pré-requisitos

- Java 21 ou superior
- MySQL 8.0 ou superior
- Maven 3.6+ (ou use o wrapper incluído)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/seu-usuario/task-manager-api.git
cd task-manager-api
```

### 2️⃣ Configure o Banco de Dados

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE taskdb;
```

### 3️⃣ Configure as Credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
```

### 4️⃣ Execute a Aplicação

**Usando Maven Wrapper (recomendado):**

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Ou usando Maven instalado:**

```bash
mvn spring-boot:run
```

### 5️⃣ Acesse a Aplicação

A aplicação estará disponível em:

- **API Base:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **API Docs (JSON):** `http://localhost:8080/v3/api-docs`
- **Health Check:** `http://localhost:8080/actuator/health`

---

## 📚 Endpoints da API

### Base URL: `http://localhost:8080/api/tasks`

| Método | Endpoint | Descrição | Status Code |
|--------|----------|-----------|-------------|
| `POST` | `/api/tasks` | Criar nova tarefa | 201 Created |
| `GET` | `/api/tasks` | Listar todas as tarefas | 200 OK |
| `GET` | `/api/tasks/{id}` | Buscar tarefa por ID | 200 OK / 404 Not Found |
| `GET` | `/api/tasks/status?completed={boolean}` | Filtrar por status | 200 OK |
| `PUT` | `/api/tasks/{id}` | Atualizar tarefa | 200 OK / 404 Not Found |
| `PATCH` | `/api/tasks/{id}/complete` | Marcar como completa | 200 OK / 404 Not Found |
| `PATCH` | `/api/tasks/{id}/pending` | Marcar como pendente | 200 OK / 404 Not Found |
| `DELETE` | `/api/tasks/{id}` | Deletar tarefa | 204 No Content / 404 Not Found |
| `DELETE` | `/api/tasks/completed` | Deletar todas completas | 204 No Content |

---

## 💻 Exemplos de Uso

### Criar uma Nova Tarefa

**Request:**
```http
POST /api/tasks
Content-Type: application/json

{
  "title": "Implementar autenticação JWT",
  "description": "Adicionar segurança com tokens JWT na API"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Implementar autenticação JWT",
  "description": "Adicionar segurança com tokens JWT na API",
  "completed": false,
  "createdAt": "2026-02-12T19:40:00",
  "updatedAt": "2026-02-12T19:40:00"
}
```

### Listar Todas as Tarefas

**Request:**
```http
GET /api/tasks
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Implementar autenticação JWT",
    "description": "Adicionar segurança com tokens JWT na API",
    "completed": false,
    "createdAt": "2026-02-12T19:40:00",
    "updatedAt": "2026-02-12T19:40:00"
  },
  {
    "id": 2,
    "title": "Criar testes unitários",
    "description": "Adicionar cobertura de testes",
    "completed": true,
    "createdAt": "2026-02-12T20:00:00",
    "updatedAt": "2026-02-12T20:30:00"
  }
]
```

### Buscar Tarefa por ID

**Request:**
```http
GET /api/tasks/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Implementar autenticação JWT",
  "description": "Adicionar segurança com tokens JWT na API",
  "completed": false,
  "createdAt": "2026-02-12T19:40:00",
  "updatedAt": "2026-02-12T19:40:00"
}
```

### Atualizar Tarefa

**Request:**
```http
PUT /api/tasks/1
Content-Type: application/json

{
  "title": "Implementar autenticação JWT (ATUALIZADO)",
  "description": "Adicionar segurança com tokens JWT e refresh tokens"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Implementar autenticação JWT (ATUALIZADO)",
  "description": "Adicionar segurança com tokens JWT e refresh tokens",
  "completed": false,
  "createdAt": "2026-02-12T19:40:00",
  "updatedAt": "2026-02-12T21:15:00"
}
```

### Marcar Tarefa como Completa

**Request:**
```http
PATCH /api/tasks/1/complete
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Implementar autenticação JWT (ATUALIZADO)",
  "description": "Adicionar segurança com tokens JWT e refresh tokens",
  "completed": true,
  "createdAt": "2026-02-12T19:40:00",
  "updatedAt": "2026-02-12T21:30:00"
}
```

### Filtrar Tarefas por Status

**Request (Buscar tarefas completas):**
```http
GET /api/tasks/status?completed=true
```

**Request (Buscar tarefas pendentes):**
```http
GET /api/tasks/status?completed=false
```

### Deletar Tarefa

**Request:**
```http
DELETE /api/tasks/1
```

**Response:** `204 No Content`

### Deletar Todas as Tarefas Completas

**Request:**
```http
DELETE /api/tasks/completed
```

**Response:** `204 No Content`

---

## 🎨 Swagger UI

A API possui documentação interativa completa com **Swagger UI**, onde você pode:

- 📖 Visualizar todos os endpoints disponíveis
- 🧪 Testar requisições diretamente no navegador
- 📝 Ver exemplos de requests e responses
- ✅ Validar dados de entrada
- 🔍 Explorar os modelos de dados

**Acesse:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Preview do Swagger

O Swagger UI exibe:
- **task-controller** - Todos os endpoints de gerenciamento de tarefas
- **Schemas** - Modelos TaskRequestDTO e TaskResponseDTO
- **Try it out** - Teste interativo de cada endpoint

---

## 🏗️ Arquitetura do Projeto

```
src/
├── main/
│   ├── java/com/example/apirestful/
│   │   ├── ApirestfulApplication.java          # Classe principal
│   │   ├── config/
│   │   │   └── SwaggerConfig.java              # Configuração Swagger/OpenAPI
│   │   ├── controller/
│   │   │   └── TaskController.java             # Endpoints REST
│   │   ├── dto/
│   │   │   ├── TaskRequestDTO.java             # DTO de entrada
│   │   │   └── TaskResponseDTO.java            # DTO de saída
│   │   ├── exception/
│   │   │   ├── TaskNotFoundException.java      # Exception customizada
│   │   │   └── GlobalExceptionHandler.java     # Tratamento global
│   │   ├── model/
│   │   │   └── Task.java                       # Entidade JPA
│   │   ├── repository/
│   │   │   └── TaskRepository.java             # Acesso ao banco
│   │   └── service/
│   │       ├── TaskService.java                # Interface do serviço
│   │       └── impl/
│   │           └── TaskServiceImpl.java        # Implementação lógica
│   └── resources/
│       └── application.properties              # Configurações
└── test/
    └── java/com/example/apirestful/
        └── ApirestfulApplicationTests.java     # Testes
```

### Camadas da Aplicação

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST Endpoints (@RestController)
│     (TaskController.java)           │
└─────────────────────────────────────┘
              ↓ ↑
┌─────────────────────────────────────┐
│          Service Layer              │  ← Lógica de Negócio (@Service)
│     (TaskServiceImpl.java)          │
└─────────────────────────────────────┘
              ↓ ↑
┌─────────────────────────────────────┐
│        Repository Layer             │  ← Acesso a Dados (JpaRepository)
│     (TaskRepository.java)           │
└─────────────────────────────────────┘
              ↓ ↑
┌─────────────────────────────────────┐
│          Database Layer             │  ← MySQL
│            (taskdb)                 │
└─────────────────────────────────────┘
```

---

## 🎯 Padrões e Boas Práticas Aplicadas

### Design Patterns

- ✅ **DTO Pattern** - Separação entre entidades e objetos de transferência
- ✅ **Repository Pattern** - Abstração do acesso a dados
- ✅ **Service Layer Pattern** - Lógica de negócio isolada
- ✅ **Exception Handling Pattern** - Tratamento centralizado de erros

### Princípios SOLID

- ✅ **Single Responsibility** - Cada classe tem uma responsabilidade única
- ✅ **Open/Closed** - Aberto para extensão, fechado para modificação
- ✅ **Liskov Substitution** - Uso de interfaces e abstrações
- ✅ **Interface Segregation** - Interfaces específicas
- ✅ **Dependency Inversion** - Dependência de abstrações

### Clean Code

- ✅ Nomenclatura clara e descritiva
- ✅ Métodos pequenos e focados
- ✅ Comentários apenas quando necessário
- ✅ Código auto-explicativo
- ✅ Tratamento adequado de exceções

---

## 📊 Modelo de Dados

### Entidade Task

```java
@Entity
@Table(name = "tasks")
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
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### Estrutura da Tabela MySQL

```sql
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 🔒 Tratamento de Erros

A API implementa tratamento global de exceções com respostas padronizadas:

### Tarefa Não Encontrada (404)

```json
{
  "status": 404,
  "message": "Tarefa não encontrada",
  "timestamp": "2026-02-12T21:30:00"
}
```

### Validação de Dados (400)

```json
{
  "status": 400,
  "errors": {
    "title": "Título é obrigatório",
    "description": "A descrição deve ter no máximo 1000 caracteres"
  },
  "timestamp": "2026-02-12T21:30:00"
}
```

### Erro Interno do Servidor (500)

```json
{
  "status": 500,
  "message": "Erro interno do servidor",
  "timestamp": "2026-02-12T21:30:00"
}
```

---

## 📈 Monitoramento

A aplicação inclui **Spring Actuator** para monitoramento:

### Health Check

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

### Métricas Disponíveis

- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas gerais
- `/actuator/health` - Status de saúde

---

## 🧪 Testando a API

### Usando Swagger UI (Recomendado)

1. Acesse: `http://localhost:8080/swagger-ui/index.html`
2. Escolha um endpoint
3. Clique em "Try it out"
4. Preencha os dados
5. Clique em "Execute"

### Usando cURL

```bash
# Criar tarefa
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Minha tarefa","description":"Descrição da tarefa"}'

# Listar todas
curl http://localhost:8080/api/tasks

# Buscar por ID
curl http://localhost:8080/api/tasks/1

# Atualizar
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Tarefa atualizada","description":"Nova descrição"}'

# Deletar
curl -X DELETE http://localhost:8080/api/tasks/1
```

### Usando Postman

1. Importe a collection OpenAPI: `http://localhost:8080/v3/api-docs`
2. Configure a base URL: `http://localhost:8080`
3. Execute as requisições

---

## 🚀 Melhorias Futuras

- [ ] Implementar autenticação JWT
- [ ] Adicionar paginação e ordenação
- [ ] Implementar busca avançada (por título, data, etc.)
- [ ] Adicionar tags/categorias para tarefas
- [ ] Implementar sistema de prioridades
- [ ] Adicionar testes unitários e de integração
- [ ] Configurar CI/CD com GitHub Actions
- [ ] Dockerizar a aplicação
- [ ] Adicionar cache com Redis
- [ ] Implementar soft delete
- [ ] Adicionar versionamento da API
- [ ] Implementar rate limiting

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Cristian**

- LinkedIn: [Seu LinkedIn](https://www.linkedin.com/in/seu-perfil)
- GitHub: [Seu GitHub](https://github.com/seu-usuario)
- Email: seu.email@exemplo.com

---

## 🙏 Agradecimentos

Desenvolvido como parte de um projeto de estudos de Spring Boot e APIs RESTful.

---

<div align="center">

### ⭐ Se este projeto foi útil, considere dar uma estrela!

**Desenvolvido com ❤️ e ☕ usando Spring Boot**

</div>

