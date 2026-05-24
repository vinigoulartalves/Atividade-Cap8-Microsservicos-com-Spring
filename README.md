# EcoDenuncia API

API RESTful acadêmica de tema **ESG** para gestão de **denúncias de descarte irregular de resíduos da construção civil**.

Permite que usuários cadastrem denúncias informando localização geográfica, categoria do resíduo e status. Inclui autenticação JWT, controle por roles (`USER` / `ADMIN`), validação de dados, tratamento global de exceções e migrations versionadas com Flyway sobre Oracle Database (instância FIAP).

## Stack

- Java 21
- Spring Boot 3.3
- Spring Web, Spring Data JPA, Bean Validation
- Spring Security + JWT (jjwt 0.12, HS256+)
- Oracle Database FIAP + Oracle JDBC (`ojdbc11`)
- Flyway (migrations Oracle)
- Lombok
- Docker (single-stage, `eclipse-temurin:21-alpine`)

## Arquitetura em camadas

```
src/main/java/br/com/ecodenuncia/api
├── config/security      # SecurityConfig, TokenService, JwtAuthenticationFilter, CustomUserDetailsService
├── controller           # AuthController, DenunciaController, CategoriaResiduoController
├── service              # AuthService, UsuarioService, DenunciaService, CategoriaResiduoService
├── repository           # UsuarioRepository, DenunciaRepository, CategoriaResiduoRepository
├── model                # Usuario, CategoriaResiduo, Denuncia, Role, StatusDenuncia
├── dto                  # Requests/Responses com Bean Validation
└── exception            # GlobalExceptionHandler (@RestControllerAdvice), ErrorResponse,
                          RecursoNaoEncontradoException, RegraNegocioException
```

## Modelo de dados

- `TB_USUARIO` — `id, nome, email (UK), senha (BCrypt), role (USER/ADMIN)`
- `TB_CATEGORIA_RESIDUO` — `id, nome (UK), descricao`
- `TB_DENUNCIA` — `id, titulo, descricao, endereco, bairro, cidade, estado, latitude, longitude, status, dataCriacao` + FKs para `TB_USUARIO` e `TB_CATEGORIA_RESIDUO`

Enum `StatusDenuncia`: `ABERTA` (default), `EM_ANALISE`, `RESOLVIDA`, `CANCELADA`.

## Endpoints e autorização

| Método | Rota | Acesso | HTTP de sucesso |
|---|---|---|---|
| POST | `/auth/register` | **público** | 201 Created |
| POST | `/auth/login` | **público** | 200 OK |
| GET | `/denuncias` | **público** | 200 OK |
| GET | `/denuncias/{id}` | **público** | 200 OK |
| POST | `/denuncias` | autenticado | 201 Created (+ `Location`) |
| PUT | `/denuncias/{id}` | autenticado (dono ou ADMIN) | 200 OK |
| DELETE | `/denuncias/{id}` | autenticado (dono ou ADMIN) | 204 No Content |
| PATCH | `/denuncias/{id}/status` | **ADMIN** | 200 OK |
| GET | `/categorias` | **público** | 200 OK |
| GET | `/categorias/{id}` | **público** | 200 OK |
| POST | `/categorias` | **ADMIN** | 201 Created (+ `Location`) |
| PUT | `/categorias/{id}` | **ADMIN** | 200 OK |
| DELETE | `/categorias/{id}` | **ADMIN** | 204 No Content |

Endpoints autenticados exigem header `Authorization: Bearer <token>`.

## Como a segurança funciona

- **Stateless** (`SessionCreationPolicy.STATELESS`) — sem sessão HTTP; toda requisição autenticada precisa do JWT.
- **Senhas com BCrypt** (`BCryptPasswordEncoder`) — hash gerado no cadastro e validado no login.
- **`TokenService`** — gera e valida o JWT (HMAC com chave em Base64 vinda da config `security.jwt.secret`).
- **`JwtAuthenticationFilter`** (`OncePerRequestFilter`) — lê `Authorization: Bearer <token>`, valida via `TokenService` e popula o `SecurityContextHolder`.
- **`CustomUserDetailsService`** — carrega o `Usuario` (que implementa `UserDetails`) pelo email a partir do `UsuarioRepository`.
- **`SecurityConfig`** — registra o `SecurityFilterChain`, define os request matchers e habilita `@EnableMethodSecurity`.

## Configuração

Todos os parâmetros ficam em `src/main/resources/application.properties` — sem variáveis de ambiente nesta versão acadêmica.

**Conexão com o Oracle FIAP (já configurada):**

```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=RM564616
spring.datasource.password=120196
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

**JPA / Flyway / erros:**

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
server.error.include-stacktrace=never
```

> **Atenção:** as credenciais FIAP são compromisso académico e ficam *commitadas* no repositório de propósito. Em produção real, use `application-prod.properties` separado e/ou variáveis de ambiente / Vault.

## Migrations (Flyway)

Localizadas em `src/main/resources/db/migration`:

- `V1__create_tables.sql` — cria `TB_USUARIO`, `TB_CATEGORIA_RESIDUO` e `TB_DENUNCIA` (com FKs, índices e check constraints).
- `V2__insert_initial_data.sql` — semeia as 9 categorias de resíduo e o usuário administrador padrão.

Usuário admin padrão semeado pela V2: **admin@ecodenuncia.com / admin123** (hash BCrypt embutido na migration; troque em produção).

## Executando localmente

Pré-requisitos: JDK 21, Maven 3.9+, conectividade com `oracle.fiap.com.br:1521` (rede da FIAP / VPN).

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080` e roda automaticamente as migrations Flyway na conexão FIAP.

## Executando com Docker

O `Dockerfile` é **single-stage** (`eclipse-temurin:21-alpine`) e espera o jar já compilado em `target/`. Faça o build do jar primeiro:

```bash
mvn clean package -DskipTests
docker build -t ecodenuncia-api .
docker run --rm -p 8080:8080 ecodenuncia-api
```

> O container precisa de acesso à rede da FIAP para alcançar `oracle.fiap.com.br:1521`.

## Exemplos de uso

### Cadastro

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria","email":"maria@email.com","senha":"123456"}'
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ecodenuncia.com","senha":"admin123"}'
```

### Listar categorias (público)

```bash
curl http://localhost:8080/categorias
```

### Criar categoria (ADMIN)

```bash
curl -X POST http://localhost:8080/categorias \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"nome":"VIDRO","descricao":"Janelas, vidros temperados e laminados de obras"}'
```

### Criar denúncia (autenticado)

```bash
curl -X POST http://localhost:8080/denuncias \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo":"Entulho na calçada",
    "descricao":"Restos de concreto e tijolos abandonados há 3 dias",
    "endereco":"Rua das Flores, 123",
    "bairro":"Centro",
    "cidade":"São Paulo",
    "estado":"SP",
    "latitude":-23.5505,
    "longitude":-46.6333,
    "categoriaResiduoId":1
  }'
```

### Atualizar status (ADMIN)

```bash
curl -X PATCH http://localhost:8080/denuncias/1/status \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"status":"RESOLVIDA"}'
```

## Tratamento global de exceções

Toda resposta de erro segue o formato padronizado abaixo (gerado por `GlobalExceptionHandler` com `@RestControllerAdvice`):

```json
{
  "timestamp": "2026-05-24T11:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validacao nos campos enviados",
  "path": "/denuncias",
  "errors": [
    { "field": "titulo", "message": "O titulo e obrigatorio" }
  ]
}
```

Mapeamento de exceções → HTTP:

| Exceção | HTTP |
|---|---|
| `RecursoNaoEncontradoException` | 404 Not Found |
| `RegraNegocioException` | 400 Bad Request |
| `MethodArgumentNotValidException` | 400 (lista de erros por campo) |
| `HttpMessageNotReadableException` | 400 |
| `BadCredentialsException` / `AuthenticationException` | 401 Unauthorized |
| `AccessDeniedException` | 403 Forbidden |
| Exceção genérica | 500 Internal Server Error |
