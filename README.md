# EcoDenuncia API

API RESTful acadêmica de tema **ESG** para gestão de **denúncias de descarte irregular de resíduos da construção civil**.

Permite que usuários cadastrem denúncias informando localização geográfica, categoria do resíduo e status. Inclui autenticação JWT, controle por roles (`USER` / `ADMIN`), validação de dados, tratamento global de exceções e migrations versionadas com Flyway sobre Oracle Database.

## Stack

- Java 21
- Spring Boot 3.3
- Spring Web, Spring Data JPA, Bean Validation
- Spring Security + JWT (jjwt 0.12)
- Oracle Database + Oracle JDBC (`ojdbc11`)
- Flyway (migrations Oracle)
- Lombok
- Docker (multi-stage)

## Arquitetura em camadas

```
src/main/java/br/com/ecodenuncia/api
├── config/security      # SecurityConfig, JwtService, JwtAuthenticationFilter
├── controller           # AuthController, DenunciaController
├── service              # AuthService, DenunciaService
├── repository           # UsuarioRepository, DenunciaRepository
├── model                # Usuario, Denuncia, Role, CategoriaResiduo, StatusDenuncia
├── dto                  # Requests/Responses com Bean Validation
└── exception            # GlobalExceptionHandler (@RestControllerAdvice), ErrorResponse, BusinessException, ResourceNotFoundException
```

## Endpoints

| Método | Rota                          | Auth      | Role       | Descrição                                  |
|--------|-------------------------------|-----------|------------|--------------------------------------------|
| POST   | `/auth/register`              | público   | -          | Cadastro de usuário (role padrão `USER`)   |
| POST   | `/auth/login`                 | público   | -          | Login com email/senha, retorna JWT         |
| GET    | `/denuncias`                  | JWT       | USER/ADMIN | Lista denúncias (paginada)                 |
| GET    | `/denuncias/{id}`             | JWT       | USER/ADMIN | Detalha uma denúncia                       |
| POST   | `/denuncias`                  | JWT       | USER/ADMIN | Cria denúncia                              |
| PUT    | `/denuncias/{id}`             | JWT       | USER/ADMIN | Atualiza denúncia (própria ou ADMIN)       |
| PATCH  | `/denuncias/{id}/status`      | JWT       | **ADMIN**  | Atualiza apenas o status                   |
| DELETE | `/denuncias/{id}`             | JWT       | **ADMIN**  | Remove denúncia                            |

> Endpoints de criação/alteração/exclusão exigem header `Authorization: Bearer <token>`.

## Configuração (variáveis de ambiente)

| Variável            | Default                                         |
|---------------------|-------------------------------------------------|
| `DB_URL`            | `jdbc:oracle:thin:@//localhost:1521/XEPDB1`     |
| `DB_USERNAME`       | `ecodenuncia`                                   |
| `DB_PASSWORD`       | `ecodenuncia`                                   |
| `SERVER_PORT`       | `8080`                                          |
| `JWT_SECRET`        | chave em Base64 (academic default — troque)    |
| `JWT_EXPIRATION_MS` | `3600000` (1h)                                  |
| `JWT_ISSUER`        | `ecodenuncia-api`                               |

## Migrations (Flyway)

Localizadas em `src/main/resources/db/migration`:

- `V1__create_usuario_table.sql` — tabela `TB_USUARIO`
- `V2__create_denuncia_table.sql` — tabela `TB_DENUNCIA`

## Executando localmente

Pré-requisitos: JDK 21, Maven 3.9+, instância Oracle acessível.

```bash
export DB_URL="jdbc:oracle:thin:@//localhost:1521/XEPDB1"
export DB_USERNAME=ecodenuncia
export DB_PASSWORD=ecodenuncia

mvn spring-boot:run
```

## Executando com Docker

```bash
docker build -t ecodenuncia-api .

docker run --rm -p 8080:8080 \
  -e DB_URL="jdbc:oracle:thin:@//host.docker.internal:1521/XEPDB1" \
  -e DB_USERNAME=ecodenuncia \
  -e DB_PASSWORD=ecodenuncia \
  ecodenuncia-api
```

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
  -d '{"email":"maria@email.com","senha":"123456"}'
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
    "categoria":"CONCRETO"
  }'
```

### Atualizar status (apenas ADMIN)

```bash
curl -X PATCH http://localhost:8080/denuncias/1/status \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"status":"EM_ANALISE"}'
```

> Para promover um usuário a `ADMIN`, atualize a coluna `DS_ROLE` para `ADMIN` na tabela `TB_USUARIO` (acadêmico).

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
