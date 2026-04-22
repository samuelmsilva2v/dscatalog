# DSCatalog - Backend

API REST para gerenciamento de um catálogo de produtos, desenvolvida com Spring Boot.

## Tecnologias

- Java 21
- Spring Boot 4.0.5
- Spring Security + OAuth2 Authorization Server
- Spring Data JPA
- Spring Web MVC
- Bean Validation
- Lombok
- H2 Database (perfil de teste)
- PostgreSQL (produção)

## Modelo de domínio

```
Category (1) ----< (N) Product_Category (N) >---- (1) Product
User (N) >---- (N) Role
```

- **Category**: id, name, createdAt
- **Product**: id, name, description, price, imgUrl, date, categories (ManyToMany)
- **User**: id, firstName, lastName, email, password, roles (ManyToMany)
- **Role**: id, authority

## Autenticação

A API utiliza **OAuth2 com JWT** (Spring Authorization Server). O fluxo é o `password` grant type customizado.

### Obtendo o token

```
POST /oauth2/token
```

**Headers:**
```
Content-Type: application/x-www-form-urlencoded
Authorization: Basic <base64(client_id:client_secret)>
```

**Body (form-urlencoded):**
```
username=user@example.com
password=123456
grant_type=password
```

**Resposta:**
```json
{
  "access_token": "<jwt>",
  "token_type": "Bearer",
  "expires_in": 86400
}
```

O token JWT inclui os claims `username` e `authorities` (roles do usuário).

### Usando o token

Envie o header `Authorization: Bearer <token>` nas requisições protegidas.

### Roles disponíveis

| Role             | Permissões                                      |
|------------------|-------------------------------------------------|
| `ROLE_OPERATOR`  | Criar/editar/deletar categorias e produtos      |
| `ROLE_ADMIN`     | Tudo acima + gerenciamento completo de usuários |

---

## Endpoints

### Autenticação

| Método | Rota              | Acesso | Descrição          |
|--------|-------------------|--------|--------------------|
| POST   | `/oauth2/token`   | Público | Obtém token JWT   |

---

### Categorias — `/categories`

| Método | Rota               | Acesso                          | Descrição                 |
|--------|--------------------|---------------------------------|---------------------------|
| GET    | `/categories`      | Público                         | Lista todas as categorias |
| GET    | `/categories/{id}` | Público                         | Busca categoria por ID    |
| POST   | `/categories`      | ROLE_ADMIN ou ROLE_OPERATOR     | Cria uma nova categoria   |
| PUT    | `/categories/{id}` | ROLE_ADMIN ou ROLE_OPERATOR     | Atualiza uma categoria    |
| DELETE | `/categories/{id}` | ROLE_ADMIN ou ROLE_OPERATOR     | Remove uma categoria      |

---

### Produtos — `/products`

| Método | Rota              | Acesso                          | Descrição                |
|--------|-------------------|---------------------------------|--------------------------|
| GET    | `/products`       | Público                         | Lista produtos (paginado)|
| GET    | `/products/{id}`  | Público                         | Busca produto por ID     |
| POST   | `/products`       | ROLE_ADMIN ou ROLE_OPERATOR     | Cria um novo produto     |
| PUT    | `/products/{id}`  | ROLE_ADMIN ou ROLE_OPERATOR     | Atualiza um produto      |
| DELETE | `/products/{id}`  | ROLE_ADMIN ou ROLE_OPERATOR     | Remove um produto        |

**Query params de `GET /products`:**

| Parâmetro    | Tipo   | Padrão | Descrição                               |
|--------------|--------|--------|-----------------------------------------|
| `name`       | String | `""`   | Filtra produtos pelo nome               |
| `categoryId` | String | `"0"`  | Filtra por ID de categoria (0 = todos)  |
| `page`       | int    | `0`    | Número da página                        |
| `size`       | int    | `12`   | Itens por página                        |
| `sort`       | String | —      | Campo e direção (ex: `name,asc`)        |

---

### Usuários — `/users`

Todos os endpoints exigem **ROLE_ADMIN**.

| Método | Rota           | Descrição                  |
|--------|----------------|----------------------------|
| GET    | `/users`       | Lista todos os usuários    |
| GET    | `/users/{id}`  | Busca usuário por ID       |
| POST   | `/users`       | Cria um novo usuário       |
| PUT    | `/users/{id}`  | Atualiza um usuário        |
| DELETE | `/users/{id}`  | Remove um usuário          |

**Body de `POST /users`:**
```json
{
  "firstName": "Maria",
  "lastName": "Silva",
  "email": "maria@example.com",
  "password": "123456",
  "roles": [{ "id": 1 }]
}
```

**Body de `PUT /users/{id}`:**
```json
{
  "firstName": "Maria",
  "lastName": "Silva",
  "email": "maria@example.com",
  "roles": [{ "id": 1 }]
}
```

---

## Perfis

| Perfil  | Banco de dados          | Ativado por padrão |
|---------|-------------------------|--------------------|
| `test`  | H2 in-memory (`testdb`) | Sim                |
| padrão  | PostgreSQL              | Não                |

### H2 Console (perfil `test`)

Disponível em: `http://localhost:8080/h2-console`

```
JDBC URL: jdbc:h2:mem:testdb
User:     sa
Password: (vazio)
```

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Rodando localmente (perfil test)

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` com banco H2 e dados de seed carregados automaticamente via `import.sql`.

**Credenciais padrão (perfil test):**
- Client ID: `myclientid`
- Client Secret: `myclientsecret`

### Build

```bash
./mvnw clean package
java -jar target/dscatalog-0.0.1-SNAPSHOT.jar
```

### Produção (PostgreSQL)

Configure as variáveis de ambiente:

| Variável        | Descrição                           |
|-----------------|-------------------------------------|
| `CLIENT_ID`     | ID do client OAuth2                 |
| `CLIENT_SECRET` | Secret do client OAuth2             |
| `JWT_DURATION`  | Duração do token em segundos        |
| `CORS_ORIGINS`  | Origens permitidas (separadas por `,`) |

Ou via `application.properties`:

```properties
spring.profiles.active=
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
spring.datasource.username=<user>
spring.datasource.password=<password>
security.client-id=<client-id>
security.client-secret=<client-secret>
security.jwt.duration=86400
cors.origins=https://meu-frontend.com
```

---

## Estrutura do projeto

```
src/main/java/com/devsuperior/dscatalog/
├── config/
│   ├── customgrant/       # Grant type "password" customizado
│   ├── AuthorizationServerConfig.java
│   ├── ResourceServerConfig.java  # CORS, JWT converter
│   └── SecurityConfig.java
├── entities/              # Entidades JPA (Category, Product, User, Role)
├── dto/                   # DTOs de requisição/resposta + validações
├── projections/           # Interfaces de projeção SQL nativa
├── repositories/          # Interfaces Spring Data JPA
├── services/
│   ├── validation/        # Validators customizados (@UserInsertValid, @UserUpdateValid)
│   └── exceptions/        # ResourceNotFoundException, DatabaseException
└── resources/             # Controllers REST
    └── exceptions/        # Handler global de exceções
```
