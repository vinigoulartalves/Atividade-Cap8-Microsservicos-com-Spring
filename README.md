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
├── repository           # UsuarioRepository, DenunciaRepository, CategoriaResiduoRepository
├── model                # Usuario, CategoriaResiduo, Denuncia, Role, StatusDenuncia
├── dto                  # Requests/Responses com Bean Validation
└── exception            # GlobalExceptionHandler (@RestControllerAdvice), ErrorResponse, BusinessException, ResourceNotFoundException
```

## Modelo de dados

- `TB_USUARIO` — `id, nome, email (UK), senha (BCrypt), role (USER/ADMIN)`
- `TB_CATEGORIA_RESIDUO` — `id, nome (UK), descricao`
- `TB_DENUNCIA` — `id, titulo, descricao, endereco, bairro, cidade, estado, latitude, longitude, status, dataCriacao` + FKs para `TB_USUARIO` e `TB_CATEGORIA_RESIDUO`

Enum `StatusDenuncia`: `ABERTA` (default), `EM_ANALISE`, `RESOLVIDA`, `CANCELADA`.

## Endpoints

| Método | Rota | Auth | Role |
|---|---|---|---|
| POST | `/auth/register` | público | – |
| POST | `/auth/login` | público | – |
| GET | `/denuncias` | JWT | USER/ADMIN |
| GET | `/denuncias/{id}` | JWT | USER/ADMIN |
| POST | `/denuncias` | JWT | USER/ADMIN |
| PUT | `/denuncias/{id}` | JWT | USER/ADMIN (própria) ou ADMIN |
| PATCH | `/denuncias/{id}/status` | JWT | **ADMIN** |
| DELETE | `/denuncias/{id}` | JWT | **ADMIN** |

Endpoints de criação/alteração/exclusão exigem header `Authorization: Bearer <token>`.

## Configuração (variáveis de ambiente)

| Variável            | Default                                       |
|---------------------|-----------------------------------------------|
| `DB_URL`            | `jdbc:oracle:thin:@//localhost:1521/XEPDB1`   |
| `DB_USERNAME`       | `ecodenuncia`                                 |
| `DB_PASSWORD`       | `ecodenuncia`                                 |
| `SERVER_PORT`       | `8080`                                        |
| `JWT_SECRET`        | chave em Base64 (default só para uso local)   |
| `JWT_EXPIRATION_MS` | `3600000` (1h)                                |
| `JWT_ISSUER`        | `ecodenuncia-api`                             |

## Migrations (Flyway)

Localizadas em `src/main/resources/db/migration`:

- `V1__create_tables.sql` — cria `TB_USUARIO`, `TB_CATEGORIA_RESIDUO` e `TB_DENUNCIA` (com FKs, índices e check constraints).
- `V2__insert_initial_data.sql` — semeia as 9 categorias de resíduo e o usuário administrador padrão.

Usuário admin padrão semeado pela V2: **admin@ecodenuncia.com / admin123** (hash BCrypt embutido na migration; troque em produção).

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
  -d '{"email":"admin@ecodenuncia.com","senha":"admin123"}'
```

### Criar denúncia (autenticado)

Campo `categoriaResiduoId` referencia uma linha de `TB_CATEGORIA_RESIDUO` (semeada pela V2, ids 1 a 9):

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

### Atualizar status (apenas ADMIN)

```bash
curl -X PATCH http://localhost:8080/denuncias/1/status \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"status":"EM_ANALISE"}'
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
