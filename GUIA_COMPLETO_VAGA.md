# 🎯 Guia Completo: Como Desenvolver um Projeto para Passar na Vaga

## 📊 Análise dos Requisitos da Vaga

### ✅ O que a Vaga Exige

#### Atividades Principais:
1. **Desenvolver e manter aplicações backend, frontend ou mobile**
2. **Criar e consumir APIs RESTful** ⭐ (ESSENCIAL)
3. **Evolução, correção de bugs e melhoria contínua**
4. **Colaborar em revisões de código e boas práticas**
5. **Discussões técnicas sobre arquitetura e soluções**

#### Diferenciais (Que te colocam à frente):
- ⭐ **Experiência com Docker e CI/CD**
- ⭐ **Conhecimento em arquitetura de software**
- ⭐ **Vivência com metodologias ágeis**

---

## 💡 Minha Recomendação: API RESTful de Gerenciamento de Tarefas

### Por que este projeto é PERFEITO para a vaga?

| Requisito da Vaga | Como o Projeto Atende |
|-------------------|------------------------|
| Desenvolver backend | ✅ API REST completa com Spring Boot |
| Criar APIs RESTful | ✅ CRUD completo + endpoints avançados |
| Boas práticas | ✅ Arquitetura em camadas, SOLID, DTOs |
| Evolução contínua | ✅ Roadmap com fases progressivas |
| **Docker** (diferencial) | ✅ Dockerfile + docker-compose |
| **CI/CD** (diferencial) | ✅ GitHub Actions configurado |
| **Arquitetura** (diferencial) | ✅ Documentação da arquitetura |

### 🎓 O que Você Vai Demonstrar

- ✅ Competência técnica em Java/Spring Boot
- ✅ Capacidade de criar APIs profissionais
- ✅ Conhecimento de banco de dados
- ✅ Experiência com testes automatizados
- ✅ Habilidades DevOps (Docker, CI/CD)
- ✅ Documentação técnica de qualidade
- ✅ Visão de arquitetura e escalabilidade

---

## 🚀 Roadmap de Desenvolvimento

### 📅 Cronograma Recomendado

#### **Se você tem 2 semanas:**
```
Semana 1 (Dias 1-7):  API REST completa funcionando
Semana 2 (Dias 8-14): Testes + Docker + CI/CD + Documentação
```

#### **Se você tem 1 mês:**
```
Semana 1: API REST básica + banco de dados
Semana 2: Testes unitários e de integração
Semana 3: Docker + CI/CD + melhorias
Semana 4: Frontend simples (opcional) + funcionalidades avançadas
```

---

## 📋 FASE 1: Backend API RESTful (PRIORIDADE MÁXIMA)

**⏱️ Tempo: 5-7 dias**

### Stack Tecnológica Obrigatória

```yaml
Linguagem: Java 17+
Framework: Spring Boot 3.2.x
Banco de Dados: PostgreSQL ou MySQL
Build: Maven
Documentação: Swagger/OpenAPI
```

### O que Implementar

#### 1️⃣ Estrutura Base do Projeto

```
taskapi/
├── controller/      # Endpoints REST
├── service/         # Lógica de negócio
├── repository/      # Acesso a dados (JPA)
├── model/           # Entidades
├── dto/             # Data Transfer Objects
├── exception/       # Tratamento de erros
└── config/          # Configurações
```

**Por quê?** Demonstra conhecimento de **arquitetura em camadas** (diferencial da vaga).

#### 2️⃣ Endpoints REST Completos

```
✅ POST   /api/v1/tasks              # Criar tarefa
✅ GET    /api/v1/tasks              # Listar todas (com filtros)
✅ GET    /api/v1/tasks/{id}         # Buscar por ID
✅ PUT    /api/v1/tasks/{id}         # Atualizar
✅ DELETE /api/v1/tasks/{id}         # Deletar
✅ PATCH  /api/v1/tasks/{id}/complete  # Marcar como concluída
✅ GET    /api/v1/tasks/statistics   # Estatísticas
```

**Por quê?** Atende diretamente o requisito **"Criar APIs RESTful"**.

#### 3️⃣ Boas Práticas Essenciais

```java
✅ DTOs separados de Entidades
✅ Validações com Bean Validation (@NotBlank, @Size, etc)
✅ Exception Handling global (@RestControllerAdvice)
✅ Códigos HTTP corretos (200, 201, 204, 400, 404, 500)
✅ ResponseEntity para respostas padronizadas
✅ Versionamento da API (/api/v1)
```

**Por quê?** Demonstra **aplicação de boas práticas** (requisito da vaga).

#### 4️⃣ Banco de Dados com JPA

```sql
CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    completed   BOOLEAN DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT NOW(),
    updated_at  TIMESTAMP DEFAULT NOW()
);
```

```java
// Entidade JPA com relacionamentos, anotações corretas
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

**Por quê?** Mostra competência em **persistência de dados**.

#### 5️⃣ Documentação Swagger

```java
// Configuração OpenAPI
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI taskApiOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Task API")
                .description("API para gerenciamento de tarefas")
                .version("1.0"));
    }
}
```

**Acesso:** `http://localhost:8080/swagger-ui.html`

**Por quê?** Documentação interativa é **essencial para APIs profissionais**.

---

## 📋 FASE 2: Testes Automatizados (DIFERENCIAL IMPORTANTE)

**⏱️ Tempo: 3-4 dias**

### O que Implementar

#### 1️⃣ Testes Unitários (JUnit 5 + Mockito)

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository repository;
    
    @InjectMocks
    private TaskServiceImpl service;
    
    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Test", "Desc");
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        
        when(repository.save(any(Task.class))).thenReturn(task);
        
        // When
        TaskResponseDTO response = service.createTask(request);
        
        // Then
        assertThat(response.getTitle()).isEqualTo("Test");
        verify(repository).save(any(Task.class));
    }
}
```

**Meta:** Cobertura mínima de **70%**.

#### 2️⃣ Testes de Integração (TestContainers)

```java
@SpringBootTest
@Testcontainers
class TaskApiIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateAndRetrieveTask() {
        // POST - Criar
        TaskRequestDTO request = new TaskRequestDTO("Test", "Desc");
        ResponseEntity<TaskResponseDTO> createResponse = 
            restTemplate.postForEntity("/api/v1/tasks", request, TaskResponseDTO.class);
        
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        // GET - Buscar
        Long taskId = createResponse.getBody().getId();
        ResponseEntity<TaskResponseDTO> getResponse = 
            restTemplate.getForEntity("/api/v1/tasks/" + taskId, TaskResponseDTO.class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

#### 3️⃣ Cobertura de Código (JaCoCo)

```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Comando:** `mvn clean test jacoco:report`

**Por quê?** Testes demonstram **qualidade e confiabilidade** do código.

---

## 📋 FASE 3: Docker e CI/CD (DIFERENCIAL CRÍTICO!) ⭐

**⏱️ Tempo: 2-3 dias**

### 1️⃣ Dockerfile (Multi-stage Build)

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Por quê?** Docker é **diferencial explícito** na vaga.

### 2️⃣ docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: taskdb
      POSTGRES_USER: taskuser
      POSTGRES_PASSWORD: taskpass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U taskuser"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/taskdb
      SPRING_DATASOURCE_USERNAME: taskuser
      SPRING_DATASOURCE_PASSWORD: taskpass
    ports:
      - "8080:8080"

volumes:
  postgres_data:
```

**Uso:** `docker-compose up -d`

### 3️⃣ CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: testdb
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      
      - name: Build with Maven
        run: mvn clean install -DskipTests
      
      - name: Run Tests
        run: mvn test
      
      - name: Generate Coverage Report
        run: mvn jacoco:report
      
      - name: Upload Coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./target/site/jacoco/jacoco.xml

  docker-build:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Build Docker Image
        run: docker build -t taskapi:latest .
      
      - name: Log in to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}
      
      - name: Push to Docker Hub
        run: |
          docker tag taskapi:latest seu-usuario/taskapi:latest
          docker push seu-usuario/taskapi:latest
```

**Por quê?** CI/CD é o **segundo diferencial crítico** da vaga.

---

## 📋 FASE 4: Documentação e Apresentação (ESSENCIAL)

**⏱️ Tempo: 2 dias**

### O que Criar

#### 1️⃣ README.md Profissional

```markdown
# 📝 TaskAPI

![Build](https://github.com/user/repo/workflows/CI/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-85%25-green)
![Docker](https://img.shields.io/badge/docker-ready-blue)

## 🚀 Quick Start

### Com Docker (Recomendado)
```bash
docker-compose up -d
```

### Sem Docker
```bash
mvn spring-boot:run
```

## 📚 Documentação
- API Docs: http://localhost:8080/swagger-ui.html
- Cobertura: target/site/jacoco/index.html

## 🏗️ Arquitetura
[Incluir diagrama da arquitetura em camadas]

## 🧪 Testes
```bash
mvn test                    # Testes unitários
mvn verify                  # Testes de integração
mvn jacoco:report          # Relatório de cobertura
```

## 🐳 Docker
```bash
docker-compose up -d       # Iniciar
docker-compose logs -f     # Ver logs
docker-compose down        # Parar
```
```

#### 2️⃣ Diagramas de Arquitetura

Crie diagramas mostrando:
- ✅ Arquitetura em camadas
- ✅ Fluxo de dados (request → response)
- ✅ Estrutura do banco de dados
- ✅ Pipeline CI/CD

**Ferramentas:** Draw.io, Excalidraw, Mermaid

#### 3️⃣ Exemplos de Uso

```markdown
## Exemplos de Requisições

### Criar Tarefa
```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Aprender REST APIs"
  }'
```

### Listar Tarefas
```bash
curl http://localhost:8080/api/v1/tasks
```
```

#### 4️⃣ Screenshots/GIFs

- ✅ Swagger UI em funcionamento
- ✅ Postman testando endpoints
- ✅ Aplicação rodando com Docker
- ✅ Pipeline CI/CD executando

**Ferramenta para GIFs:** ScreenToGif, LICEcap

---

## 📋 FASE 5: Funcionalidades Extras (Se tiver tempo)

**⏱️ Tempo: 3-5 dias**

### Opcionais que Impressionam

#### 1️⃣ Autenticação JWT
```java
@PostMapping("/auth/login")
public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new JwtResponse(token));
}
```

#### 2️⃣ Paginação e Ordenação
```java
@GetMapping("/tasks")
public ResponseEntity<Page<TaskResponseDTO>> getTasks(
    @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
    Pageable pageable) {
    // ...
}
```

#### 3️⃣ Filtros Avançados
```java
@GetMapping("/tasks/search")
public ResponseEntity<List<TaskResponseDTO>> search(
    @RequestParam(required = false) String title,
    @RequestParam(required = false) Boolean completed,
    @RequestParam(required = false) LocalDate createdAfter) {
    // ...
}
```

#### 4️⃣ Cache com Redis
```java
@Cacheable(value = "tasks", key = "#id")
public TaskResponseDTO getTaskById(Long id) {
    // ...
}
```

#### 5️⃣ Frontend Simples (React)
- Interface básica para consumir a API
- Demonstra fullstack capabilities

---

## ✅ Checklist Final Antes de Enviar

### Código
- [ ] API REST funcionando perfeitamente
- [ ] Todos os endpoints implementados
- [ ] Validações em todos os inputs
- [ ] Tratamento de erros adequado
- [ ] Código limpo e bem organizado
- [ ] Comentários onde necessário
- [ ] Sem código comentado ou debug prints

### Testes
- [ ] Testes unitários (mínimo 70% cobertura)
- [ ] Testes de integração
- [ ] Todos os testes passando
- [ ] Relatório de cobertura gerado

### Docker
- [ ] Dockerfile funcionando
- [ ] docker-compose funcionando
- [ ] Variáveis de ambiente configuradas
- [ ] Health checks implementados
- [ ] Testado localmente

### CI/CD
- [ ] Pipeline configurado no GitHub Actions
- [ ] Build passando
- [ ] Testes rodando automaticamente
- [ ] Badge de status no README

### Documentação
- [ ] README.md completo e profissional
- [ ] Swagger documentado
- [ ] Exemplos de uso (curl, Postman)
- [ ] Instruções de como executar
- [ ] Diagramas de arquitetura
- [ ] Screenshots/GIFs

### Git
- [ ] Commits organizados e semânticos
- [ ] .gitignore apropriado
- [ ] Sem credenciais no código
- [ ] Histórico limpo (sem commits de "test" ou "fix")
- [ ] README atualizado

### Extras
- [ ] Collection do Postman exportada
- [ ] Arquivo .env.example
- [ ] LICENSE file
- [ ] CONTRIBUTING.md (opcional)

---

## 🎯 Preparação para a Entrevista

### Esteja Preparado para Explicar

#### 1️⃣ Decisões de Arquitetura
```
P: Por que você escolheu arquitetura em camadas?
R: Para separar responsabilidades - Controller (entrada), Service (lógica), 
   Repository (dados). Facilita manutenção, testes e escalabilidade.

P: Por que usou DTOs?
R: Para desacoplar a API das entidades do banco. Permite controle fino 
   sobre o que é exposto e facilita versionamento da API.
```

#### 2️⃣ Tecnologias Escolhidas
```
P: Por que Spring Boot?
R: Framework maduro, produtivo, com grande comunidade. Spring Data JPA 
   facilita acesso a dados, e o ecossistema Spring é padrão da indústria.

P: Por que PostgreSQL?
R: Banco relacional robusto, open source, com recursos avançados. 
   Adequado para produção e bem suportado pelo Spring.
```

#### 3️⃣ Boas Práticas Aplicadas
```
- Validações automáticas (Bean Validation)
- Exception handling global
- Códigos HTTP semânticos
- Versionamento de API
- Documentação automatizada (Swagger)
- Testes em múltiplas camadas
- Containerização com Docker
- Pipeline CI/CD automatizado
```

#### 4️⃣ Desafios Enfrentados
```
"O maior desafio foi configurar os testes de integração com TestContainers,
pois precisei entender como gerenciar o ciclo de vida dos containers de teste.
Resolvi estudando a documentação oficial e implementando health checks 
adequados."
```

#### 5️⃣ Próximos Passos
```
"Se tivesse mais tempo, implementaria:
1. Autenticação JWT para segurança
2. Cache com Redis para performance
3. Observabilidade com métricas e logs estruturados
4. Deploy em cloud (AWS/Azure)
5. Testes de carga com JMeter"
```

---

## 🚀 Recursos de Aprendizado

### Spring Boot
- 📚 [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- 🎥 [Spring Boot Tutorial - Full Course](https://www.youtube.com/results?search_query=spring+boot+full+course)
- 📖 [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

### REST APIs
- 📚 [RESTful API Design Best Practices](https://www.restapitutorial.com/)
- 📖 [HTTP Status Codes](https://httpstatuses.com/)

### Docker
- 📚 [Docker Official Docs](https://docs.docker.com/)
- 🎥 [Docker Tutorial for Beginners](https://www.youtube.com/results?search_query=docker+tutorial)

### Testes
- 📚 [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- 📚 [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- 📖 [TestContainers Guide](https://testcontainers.com/guides/)

### CI/CD
- 📚 [GitHub Actions Quickstart](https://docs.github.com/en/actions/quickstart)
- 🎥 [GitHub Actions Tutorial](https://www.youtube.com/results?search_query=github+actions+tutorial)

---

## 💼 Dicas Finais para Impressionar

### 1️⃣ No Código
```java
// ❌ Evite
public void doSomething(String s, int i) { ... }

// ✅ Prefira
public void createTask(String title, int priority) { ... }
```

### 2️⃣ Nos Commits
```bash
# ❌ Evite
git commit -m "fix"

# ✅ Prefira
git commit -m "feat: adiciona endpoint de estatísticas de tarefas"
```

### 3️⃣ Na Documentação
```markdown
# ❌ Evite README vago
"Este é um projeto de API"

# ✅ Seja específico e profissional
"API RESTful de gerenciamento de tarefas desenvolvida com Spring Boot 3.2,
implementando arquitetura em camadas, testes automatizados (85% cobertura),
containerização Docker e pipeline CI/CD com GitHub Actions."
```

### 4️⃣ No Repositório Git
```
✅ README.md completo
✅ .gitignore apropriado
✅ Commits semânticos
✅ Sem arquivos de IDE commitados
✅ Badges de status
✅ License file
```

---

## 🎯 Resumo Executivo

### O que Fazer (Ordem de Prioridade)

1. **✅ API REST completa** (5-7 dias) - OBRIGATÓRIO
2. **✅ Testes automatizados** (3-4 dias) - MUITO IMPORTANTE
3. **✅ Docker + docker-compose** (1-2 dias) - DIFERENCIAL CRÍTICO
4. **✅ CI/CD Pipeline** (1-2 dias) - DIFERENCIAL CRÍTICO
5. **✅ Documentação profissional** (2 dias) - ESSENCIAL
6. ⚪ Funcionalidades extras (se tiver tempo) - BÔNUS

### Tempo Total Mínimo Recomendado
- **2 semanas (dedicação integral)** - Projeto básico completo
- **3-4 semanas (dedicação parcial)** - Projeto completo + extras

### O que Vai Te Diferenciar
1. ⭐ **Docker funcionando** (diferencial explícito)
2. ⭐ **CI/CD configurado** (diferencial explícito)
3. ⭐ **Testes com boa cobertura** (demonstra qualidade)
4. ⭐ **Documentação impecável** (mostra profissionalismo)
5. ⭐ **Código limpo e organizado** (boas práticas)

---

## 📞 Próximos Passos IMEDIATOS

### Hoje:
1. ✅ Decida quando vai começar
2. ✅ Reserve tempo dedicado
3. ✅ Instale pré-requisitos (Java, Maven, PostgreSQL, Docker)

### Amanhã:
1. ✅ Crie projeto Spring Boot no Spring Initializr
2. ✅ Configure banco de dados
3. ✅ Crie primeira entidade (Task)
4. ✅ Implemente primeiro endpoint (POST /tasks)

### Esta Semana:
1. ✅ Complete todos os endpoints CRUD
2. ✅ Adicione validações e tratamento de erros
3. ✅ Configure Swagger
4. ✅ Teste todos os endpoints

---

<div align="center">

## 🎯 Boa Sorte! Você Consegue! 🚀

**Lembre-se:** Qualidade > Quantidade  
É melhor ter um projeto menor, mas COMPLETO e BEM FEITO,  
do que um projeto grande incompleto ou mal documentado.

---

**Dúvidas? Dificuldades? Continue estudando e praticando!**  
A prática leva à perfeição. 💪

</div>

