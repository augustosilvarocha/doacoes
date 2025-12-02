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

## Autor

**José Augusto da Silva Rocha** - IFRN

---

Projeto desenvolvido como atividade acadêmica.