# EcoDenuncia API

API RESTful academica em Java 21 e Spring Boot para gestao de denuncias de descarte irregular de residuos da construcao civil.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- Spring Security com JWT
- Oracle Database
- Flyway
- Docker

## Configuracao

A aplicacao usa Oracle. Configure as variaveis abaixo conforme seu ambiente:

```bash
DB_URL=jdbc:oracle:thin:@localhost:1521/FREEPDB1
DB_USERNAME=ECODENUNCIA
DB_PASSWORD=ecodenuncia123
JWT_SECRET=ecodenuncia-api-secret-key-for-academic-project-2026
JWT_EXPIRATION_MS=86400000
```

As migrations ficam em `src/main/resources/db/migration` e criam as tabelas `usuarios` e `denuncias`.

## Executar

```bash
mvn spring-boot:run
```

Ou via Docker:

```bash
docker build -t ecodenuncia-api .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:oracle:thin:@host.docker.internal:1521/FREEPDB1" \
  -e DB_USERNAME="ECODENUNCIA" \
  -e DB_PASSWORD="ecodenuncia123" \
  ecodenuncia-api
```

## Endpoints principais

### Publicos

- `POST /api/auth/register` - cadastro de usuario
- `POST /api/auth/login` - login e geracao de JWT
- `GET /api/denuncias` - lista denuncias
- `GET /api/denuncias/{id}` - busca uma denuncia

### Protegidos por JWT

- `POST /api/denuncias` - cria denuncia
- `PUT /api/denuncias/{id}` - atualiza denuncia
- `DELETE /api/denuncias/{id}` - remove denuncia (role `ADMIN`)

Use o token no cabecalho:

```http
Authorization: Bearer <token>
```

## Exemplo de denuncia

```json
{
  "titulo": "Descarte irregular em terreno",
  "descricao": "Entulho de obra descartado em area publica.",
  "endereco": "Rua das Flores, 100",
  "bairro": "Centro",
  "cidade": "Sao Paulo",
  "estado": "SP",
  "latitude": -23.55052,
  "longitude": -46.633308,
  "categoria": "ENTULHO",
  "status": "ABERTA"
}
```
