# 🐳 Docker e CI/CD - Guia Completo

Este documento complementa o guia de início rápido com configurações de Docker e CI/CD.

---

## 📦 PARTE 1: DOCKERIZAÇÃO

### 1.1 - Dockerfile

Crie um arquivo `Dockerfile` na raiz do projeto:

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

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente (podem ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 1.2 - docker-compose.yml

Crie `docker-compose.yml` na raiz:

```yaml
version: '3.8'

services:
  # Banco de dados PostgreSQL
  postgres:
    image: postgres:16-alpine
    container_name: taskapi-postgres
    environment:
      POSTGRES_DB: taskdb
      POSTGRES_USER: taskuser
      POSTGRES_PASSWORD: taskpass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - taskapi-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U taskuser -d taskdb"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Aplicação Spring Boot
  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: taskapi-app
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/taskdb
      SPRING_DATASOURCE_USERNAME: taskuser
      SPRING_DATASOURCE_PASSWORD: taskpass
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    ports:
      - "8080:8080"
    networks:
      - taskapi-network
    restart: unless-stopped

volumes:
  postgres_data:
    driver: local

networks:
  taskapi-network:
    driver: bridge
```

### 1.3 - .dockerignore

Crie `.dockerignore` na raiz:

```
target/
.mvn/
.idea/
.git/
*.iml
*.log
*.md
.gitignore
Dockerfile
docker-compose.yml
README.md
```

### 1.4 - Comandos Docker

#### Build da imagem:
```powershell
docker build -t taskapi:latest .
```

#### Executar com docker-compose:
```powershell
docker-compose up -d
```

#### Ver logs:
```powershell
docker-compose logs -f app
```

#### Parar containers:
```powershell
docker-compose down
```

#### Parar e remover volumes:
```powershell
docker-compose down -v
```

#### Acessar banco de dados:
```powershell
docker exec -it taskapi-postgres psql -U taskuser -d taskdb
```

### 1.5 - application-prod.properties

Crie `src/main/resources/application-prod.properties`:

```properties
# Production Configuration
spring.application.name=TaskAPI

# Database (será sobrescrito por variáveis de ambiente)
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# JPA
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:validate}
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.root=INFO
logging.level.com.seunome.taskapi=INFO

# Actuator - apenas health e info em produção
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=when-authorized
```

---

## 🔄 PARTE 2: CI/CD COM GITHUB ACTIONS

### 2.1 - Workflow Principal

Crie `.github/workflows/ci-cd.yml`:

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  # Job 1: Build e Testes
  build-and-test:
    name: Build and Test
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:16-alpine
        env:
          POSTGRES_DB: taskdb_test
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
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      
      - name: Build with Maven
        run: mvn clean install -DskipTests
      
      - name: Run Unit Tests
        run: mvn test
      
      - name: Run Integration Tests
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/taskdb_test
          SPRING_DATASOURCE_USERNAME: test
          SPRING_DATASOURCE_PASSWORD: test
        run: mvn verify -P integration-tests
      
      - name: Generate Test Coverage Report
        run: mvn jacoco:report
      
      - name: Upload Coverage to Codecov
        uses: codecov/codecov-action@v3
        with:
          files: ./target/site/jacoco/jacoco.xml
          fail_ci_if_error: false
      
      - name: Archive artifacts
        uses: actions/upload-artifact@v3
        with:
          name: taskapi-jar
          path: target/*.jar
          retention-days: 7

  # Job 2: Code Quality
  code-quality:
    name: Code Quality Analysis
    runs-on: ubuntu-latest
    needs: build-and-test
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      
      - name: Cache SonarCloud packages
        uses: actions/cache@v3
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar
      
      - name: Build and analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=seu-projeto

  # Job 3: Build Docker Image
  docker-build:
    name: Build Docker Image
    runs-on: ubuntu-latest
    needs: build-and-test
    if: github.event_name == 'push' && github.ref == 'refs/heads/main'
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      
      - name: Log in to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}
      
      - name: Extract metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: seu-usuario/taskapi
          tags: |
            type=ref,event=branch
            type=ref,event=pr
            type=semver,pattern={{version}}
            type=semver,pattern={{major}}.{{minor}}
            type=sha
      
      - name: Build and push Docker image
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
          cache-from: type=gha
          cache-to: type=gha,mode=max

  # Job 4: Security Scan
  security-scan:
    name: Security Scan
    runs-on: ubuntu-latest
    needs: build-and-test
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Run Trivy vulnerability scanner
        uses: aquasecurity/trivy-action@master
        with:
          scan-type: 'fs'
          scan-ref: '.'
          format: 'sarif'
          output: 'trivy-results.sarif'
      
      - name: Upload Trivy results to GitHub Security
        uses: github/codeql-action/upload-sarif@v2
        with:
          sarif_file: 'trivy-results.sarif'
```

### 2.2 - Workflow para Pull Requests

Crie `.github/workflows/pr-check.yml`:

```yaml
name: PR Check

on:
  pull_request:
    branches: [ main, develop ]

jobs:
  pr-validation:
    name: PR Validation
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      
      - name: Validate Maven POM
        run: mvn validate
      
      - name: Check code style
        run: mvn checkstyle:check
      
      - name: Run tests
        run: mvn test
      
      - name: Comment PR with results
        uses: actions/github-script@v7
        if: always()
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '✅ CI checks passed! Ready for review.'
            })
```

### 2.3 - Secrets Necessários

Configure no GitHub (Settings → Secrets and variables → Actions):

- `DOCKER_USERNAME`: Seu usuário do Docker Hub
- `DOCKER_PASSWORD`: Sua senha do Docker Hub
- `SONAR_TOKEN`: Token do SonarCloud (opcional)

---

## 🔧 PARTE 3: CONFIGURAÇÕES ADICIONAIS

### 3.1 - pom.xml - Adicionar Plugins

Adicione ao `pom.xml`:

```xml
<build>
    <plugins>
        <!-- Spring Boot Maven Plugin -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        
        <!-- JaCoCo para cobertura de testes -->
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
        
        <!-- Checkstyle para qualidade de código -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.3.1</version>
            <configuration>
                <configLocation>google_checks.xml</configLocation>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 3.2 - README.md - Badges

Adicione badges ao seu README:

```markdown
# TaskAPI

![CI/CD](https://github.com/seu-usuario/taskapi/workflows/CI%2FCD%20Pipeline/badge.svg)
![Coverage](https://codecov.io/gh/seu-usuario/taskapi/branch/main/graph/badge.svg)
![Docker](https://img.shields.io/docker/v/seu-usuario/taskapi?sort=semver)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

> API RESTful de gerenciamento de tarefas com Spring Boot

## 🚀 Quick Start

### Com Docker Compose (Recomendado)

```bash
docker-compose up -d
```

Acesse: http://localhost:8080/swagger-ui.html

### Sem Docker

```bash
mvn spring-boot:run
```
```

### 3.3 - Makefile (Automação)

Crie um `Makefile` na raiz (facilita comandos):

```makefile
.PHONY: help build test run docker-build docker-up docker-down clean

help: ## Mostra ajuda
	@echo "Comandos disponíveis:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Build do projeto
	mvn clean install -DskipTests

test: ## Executa testes
	mvn test

coverage: ## Gera relatório de cobertura
	mvn clean test jacoco:report

run: ## Executa aplicação
	mvn spring-boot:run

docker-build: ## Build da imagem Docker
	docker build -t taskapi:latest .

docker-up: ## Sobe containers com docker-compose
	docker-compose up -d

docker-down: ## Para containers
	docker-compose down

docker-logs: ## Mostra logs
	docker-compose logs -f

clean: ## Limpa build
	mvn clean
	docker-compose down -v

lint: ## Verifica qualidade do código
	mvn checkstyle:check

all: clean build test docker-build ## Build completo
```

**Uso no Windows PowerShell:**
```powershell
# Instalar Make para Windows
choco install make

# Depois pode usar:
make build
make test
make docker-up
```

---

## 📊 PARTE 4: MONITORAMENTO

### 4.1 - Prometheus

Adicione ao `docker-compose.yml`:

```yaml
  prometheus:
    image: prom/prometheus:latest
    container_name: taskapi-prometheus
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
    ports:
      - "9090:9090"
    networks:
      - taskapi-network
```

Crie `prometheus.yml`:

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'taskapi'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']
```

### 4.2 - Grafana

Adicione ao `docker-compose.yml`:

```yaml
  grafana:
    image: grafana/grafana:latest
    container_name: taskapi-grafana
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
    ports:
      - "3000:3000"
    volumes:
      - grafana_data:/var/lib/grafana
    networks:
      - taskapi-network
```

---

## ✅ CHECKLIST FINAL

### Docker
- [ ] Dockerfile multi-stage criado
- [ ] docker-compose.yml funcionando
- [ ] .dockerignore configurado
- [ ] Variáveis de ambiente externalizadas
- [ ] Health checks implementados
- [ ] Volumes para persistência

### CI/CD
- [ ] Workflow principal configurado
- [ ] Testes rodando no pipeline
- [ ] Build Docker automático
- [ ] Secrets configurados
- [ ] Badges no README

### Qualidade
- [ ] JaCoCo para cobertura
- [ ] Checkstyle configurado
- [ ] Testes com boa cobertura (>70%)
- [ ] Security scan implementado

### Documentação
- [ ] README completo
- [ ] Como executar documentado
- [ ] Arquitetura explicada
- [ ] Exemplos de uso

---

## 🎯 PRÓXIMOS PASSOS AVANÇADOS

1. **Deploy em Cloud**
   - Heroku (gratuito para começar)
   - AWS Elastic Beanstalk
   - Google Cloud Run
   - Azure App Service

2. **Kubernetes**
   - Deployment yamls
   - Services e Ingress
   - ConfigMaps e Secrets

3. **Observabilidade**
   - ELK Stack (Elasticsearch, Logstash, Kibana)
   - Distributed tracing com Zipkin
   - APM com New Relic ou Datadog

---

**Seu projeto estará pronto para produção! 🚀**

