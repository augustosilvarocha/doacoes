# Sistema de Doações - API REST

Projeto de estudos desenvolvido para colocar em prática os conceitos de Spring Boot aprendidos em sala de aula.

## Sobre o projeto

Este é um sistema de doações onde usuários podem cadastrar itens para doar e criar pedidos de itens que precisam. A API faz o matching automático entre itens disponíveis e pedidos compatíveis.

O foco principal é demonstrar a aplicação prática das camadas e padrões arquiteturais do Spring Boot.

## Conceitos implementados

Neste projeto foram aplicados os seguintes conceitos:

- **Models** - Entidades JPA com relacionamentos e validações
- **Repositories** - Acesso a dados com Spring Data JPA
- **Services** - Implementação das regras de negócio
- **Controllers** - Endpoints REST da API
- **DTOs** - Objetos de transferência entre camadas
- **Enums** - Tipos enumerados para padronização

## Tecnologias utilizadas

- Java 21
- Spring Boot (Web, Data JPA)
- PostgreSQL
- Lombok

## Estrutura do projeto

```
src/main/java/
└── augusto/rocha/ifrn/edu/br/doacoes/
    ├── model/          # Entidades do sistema
    ├── repository/     # Camada de acesso a dados
    ├── service/        # Camada de negócio
    ├── controller/     # Camada de apresentação (REST)
    ├── dto/            # Data Transfer Objects
    └── security/       # Configurações de segurança
```

## Como executar

### Com Docker

1. Clone o repositório
2. Crie um arquivo `.env` na raiz do projeto:
```env
DB_URL=jdbc:postgresql://db:5432/sistema_doacoes
DB_USER=postgres
DB_PASS=sua_senha
```
3. Execute o comando:
```bash
docker-compose up -d
```
4. A API estará disponível em `http://localhost:8080`

### Sem Docker

1. Clone o repositório
2. Configure o banco de dados no `application.properties`
3. Execute com `mvn spring-boot:run`
4. A API estará disponível em `http://localhost:8080`

## Modelo de dados

O sistema possui 4 entidades principais:
- **Usuario** - Cadastro de doadores e solicitantes
- **Item** - Itens disponíveis para doação
- **Pedido** - Solicitações de itens necessários
- **Match** - Conexão entre item e pedido

## Endpoints da API

### Usuario Controller
- `GET /api/v1/usuario` - Listar todos os usuários
- `POST /api/v1/usuario` - Criar novo usuário
- `GET /api/v1/usuario/{id}` - Buscar usuário por ID
- `PATCH /api/v1/usuario/{id}` - Atualizar usuário
- `DELETE /api/v1/usuario/{id}` - Deletar usuário
- `GET /api/v1/usuario/{id}/pedidos` - Listar pedidos do usuário
- `GET /api/v1/usuario/{id}/itens` - Listar itens do usuário

### Pedido Controller
- `GET /pedido` - Listar todos os pedidos
- `POST /pedido` - Criar novo pedido
- `GET /pedido/{id}` - Buscar pedido por ID
- `PATCH /pedido/{id}` - Atualizar pedido
- `DELETE /pedido/{id}` - Deletar pedido
- `GET /pedido/buscar-por-usuario/{idUsuario}` - Buscar pedidos por usuário

### Item Controller
- `GET /itens` - Listar todos os itens
- `POST /itens` - Criar novo item
- `GET /itens/{id}` - Buscar item por ID
- `PATCH /itens/{id}` - Atualizar item
- `DELETE /itens/{id}` - Deletar item

### Match Controller
- `GET /api/v1/matches` - Listar todos os matches
- `GET /api/v1/matches/{id}` - Buscar match por ID
- `GET /api/v1/matches/pendentes` - Listar matches pendentes
- `GET /api/v1/matches/ativos` - Listar matches ativos
- `POST /api/v1/matches/criar` - Criar novo match
- `PATCH /api/v1/matches/{idMatch}/doador/{idUsuario}/aceitar` - Doador aceita match
- `PATCH /api/v1/matches/{idMatch}/doador/{idUsuario}/recusar` - Doador recusa match
- `PATCH /api/v1/matches/{idMatch}/solicitante/{idUsuario}/aceitar` - Solicitante aceita match
- `PATCH /api/v1/matches/{idMatch}/solicitante/{idUsuario}/recusar` - Solicitante recusa match

**Documentação completa com Swagger UI:**
- Interface interativa: `http://localhost:8080/api/swagger-ui/index.html`
- Especificação OpenAPI: `http://localhost:8080/api/docs`

## Executando os testes

Para rodar os testes automatizados do projeto, execute:

```bash
.\mvnw.cmd test 
```

## Autor

**José Augusto da Silva Rocha** - IFRN

---