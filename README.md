# DSCatalog - Backend

API REST para gerenciamento de um catálogo de produtos, desenvolvida com Spring Boot.

## Tecnologias

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- Spring Web MVC
- Bean Validation
- Lombok
- H2 Database (perfil de teste)
- PostgreSQL (produção)

## Modelo de domínio

```
Category (1) ----< (N) Product_Category (N) >---- (1) Product
```

- **Category**: id, name, createdAt
- **Product**: id, name, description, price, imgUrl, date, categories (ManyToMany)

## Endpoints

### Categorias — `/categories`

| Método | Rota             | Descrição                    |
|--------|------------------|------------------------------|
| GET    | `/categories`    | Lista todas as categorias    |
| GET    | `/categories/{id}` | Busca categoria por ID     |
| POST   | `/categories`    | Cria uma nova categoria      |
| PUT    | `/categories/{id}` | Atualiza uma categoria     |
| DELETE | `/categories/{id}` | Remove uma categoria       |

### Produtos — `/products`

| Método | Rota              | Descrição                  |
|--------|-------------------|----------------------------|
| GET    | `/products`       | Lista todos os produtos    |
| GET    | `/products/{id}`  | Busca produto por ID       |
| POST   | `/products`       | Cria um novo produto       |
| PUT    | `/products/{id}`  | Atualiza um produto        |
| DELETE | `/products/{id}`  | Remove um produto          |

Os endpoints de listagem suportam paginação via parâmetros `page`, `size` e `sort` (Spring Pageable).

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

## Como executar

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Rodando localmente (perfil test)

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` com banco H2 e dados de seed carregados automaticamente via `import.sql`.

### Build

```bash
./mvnw clean package
java -jar target/dscatalog-0.0.1-SNAPSHOT.jar
```

### Produção (PostgreSQL)

Configure as variáveis de ambiente ou `application.properties` com as credenciais do banco e remova o perfil `test`:

```properties
spring.profiles.active=
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
spring.datasource.username=<user>
spring.datasource.password=<password>
```

## Estrutura do projeto

```
src/main/java/com/devsuperior/dscatalog/
├── entities/          # Entidades JPA (Category, Product)
├── dto/               # DTOs de requisição/resposta
├── repositories/      # Interfaces Spring Data JPA
├── services/          # Regras de negócio
│   └── exceptions/    # ResourceNotFoundException, DatabaseException
└── resources/         # Controllers REST
    └── exceptions/    # Handler global de exceções
```
