# DSCatalog

API REST para gerenciamento de um catálogo de produtos, desenvolvida com Spring Boot. Projeto do curso DevSuperior.

## Tecnologias
- Java 21, Spring Boot 3.3.5
- Spring Security + OAuth2 Authorization Server (JWT, grant type `password`)
- Spring Data JPA, Bean Validation, Lombok
- H2 (perfil de teste) / PostgreSQL (produção)

## Domínio
`Category` (N:N) `Product`, `User` (N:N) `Role` — roles `ROLE_OPERATOR` e `ROLE_ADMIN`.

## Autenticação
```
POST /oauth2/token
Authorization: Basic <base64(client_id:client_secret)>
Content-Type: application/x-www-form-urlencoded

username=user@example.com&password=123456&grant_type=password
```
Retorna um JWT (`Authorization: Bearer <token>`) com os claims `username` e `authorities`.

## Endpoints principais
| Recurso | Rotas | Acesso |
|---|---|---|
| `/categories` | GET (público), POST/PUT/DELETE | ADMIN ou OPERATOR |
| `/products` | GET paginado (público), POST/PUT/DELETE | ADMIN ou OPERATOR |
| `/users` | CRUD completo | ADMIN |

`GET /products` aceita `name`, `categoryId`, `page`, `size` e `sort` como query params.

## Como executar
```bash
./mvnw spring-boot:run
```
Sobe na porta `8080` com H2 e dados de seed (`import.sql`). Console H2 em `/h2-console` (`jdbc:h2:mem:testdb`, usuário `sa`, sem senha). Client OAuth2 padrão: `myclientid` / `myclientsecret`.

Para produção com PostgreSQL, configure `CLIENT_ID`, `CLIENT_SECRET`, `JWT_DURATION` e `CORS_ORIGINS` como variáveis de ambiente.
