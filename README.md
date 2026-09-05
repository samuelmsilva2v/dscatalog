# DSCatalog
[🇺🇸 Read in English](#dscatalog-1)

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

---

# DSCatalog
[🇧🇷 Leia em Português](#dscatalog)

REST API for managing a product catalog, built with Spring Boot. DevSuperior course project.

## Technologies
- Java 21, Spring Boot 3.3.5
- Spring Security + OAuth2 Authorization Server (JWT, `password` grant type)
- Spring Data JPA, Bean Validation, Lombok
- H2 (test profile) / PostgreSQL (production)

## Domain
`Category` (N:N) `Product`, `User` (N:N) `Role` — roles `ROLE_OPERATOR` and `ROLE_ADMIN`.

## Authentication
```
POST /oauth2/token
Authorization: Basic <base64(client_id:client_secret)>
Content-Type: application/x-www-form-urlencoded

username=user@example.com&password=123456&grant_type=password
```
Returns a JWT (`Authorization: Bearer <token>`) with the `username` and `authorities` claims.

## Main endpoints
| Resource | Routes | Access |
|---|---|---|
| `/categories` | GET (public), POST/PUT/DELETE | ADMIN or OPERATOR |
| `/products` | GET paginated (public), POST/PUT/DELETE | ADMIN or OPERATOR |
| `/users` | Full CRUD | ADMIN |

`GET /products` accepts `name`, `categoryId`, `page`, `size` and `sort` as query params.

## How to run
```bash
./mvnw spring-boot:run
```
Runs on port `8080` with H2 and seed data (`import.sql`). H2 console at `/h2-console` (`jdbc:h2:mem:testdb`, user `sa`, no password). Default OAuth2 client: `myclientid` / `myclientsecret`.

For production with PostgreSQL, set `CLIENT_ID`, `CLIENT_SECRET`, `JWT_DURATION` and `CORS_ORIGINS` as environment variables.
