# EcoDenuncia API

API RESTful acadêmica com tema ESG para gestão de denúncias de descarte irregular de resíduos da construção civil.

## Stack

- Java 21
- Spring Boot 3.3 (Web, Data JPA, Validation, Security)
- Oracle Database
- Flyway
- JWT

## Arquitetura em camadas

```
controller → service → repository → model (entity)
         ↘ dto
exception (@RestControllerAdvice)
config/security (JWT + Spring Security)
```

## Endpoints

| Método | Rota | Autenticação | Descrição |
|--------|------|--------------|-----------|
| POST | `/api/auth/register` | Não | Cadastro de usuário (role USER) |
| POST | `/api/auth/login` | Não | Login e retorno do token JWT |
| GET | `/api/denuncias` | Não | Lista todas as denúncias |
| GET | `/api/denuncias/{id}` | Não | Busca denúncia por ID |
| POST | `/api/denuncias` | JWT | Cria denúncia |
| PUT | `/api/denuncias/{id}` | JWT | Atualiza denúncia (autor ou ADMIN) |
| DELETE | `/api/denuncias/{id}` | JWT | Remove denúncia (autor ou ADMIN) |

## Roles

- `USER`: cadastra e gerencia próprias denúncias
- `ADMIN`: pode alterar/excluir qualquer denúncia

## Executar com Docker

```bash
docker compose up --build
```

A API ficará disponível em `http://localhost:8080`.

## Executar localmente

1. Subir Oracle (ex.: `docker compose up oracle -d`)
2. Configurar variáveis (opcional):

```bash
export ORACLE_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
export ORACLE_USER=ecodenuncia
export ORACLE_PASSWORD=ecodenuncia
export JWT_SECRET=ecodenuncia-jwt-secret-key-minimo-32-caracteres-academico
```

3. Compilar e executar:

```bash
mvn spring-boot:run
```

## Exemplo de uso

**Cadastro**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Silva","email":"maria@email.com","senha":"123456"}'
```

**Criar denúncia (com token)**

```bash
curl -X POST http://localhost:8080/api/denuncias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "titulo": "Entulho em via pública",
    "descricao": "Grande volume de entulho descartado irregularmente",
    "endereco": "Rua das Flores, 100",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "latitude": -23.5505200,
    "longitude": -46.6333080,
    "categoria": "ENTULHO"
  }'
```

## Migrations

Scripts Flyway em `src/main/resources/db/migration`:

- `V1__create_usuarios_table.sql`
- `V2__create_denuncias_table.sql`
