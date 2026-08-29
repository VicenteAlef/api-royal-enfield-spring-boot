# API REST Royal Enfield

API REST desenvolvida em Java 21 e Spring Boot para o ecossistema digital da marca Royal Enfield. O projeto provê gerenciamento completo de catálogo de motocicletas, versões/variantes de acabamento, galeria de imagens para carrossel, especificações técnicas detalhadas, rede de concessionárias, agendamentos de test ride e controle de acesso com perfis de usuários.

A documentação detalhada dos endpoints, estruturas de requisição, exemplos com cURL e formatos de resposta está disponível no [Contrato da API](contract.md).

---

## Tecnologias e Ferramentas

* **Linguagem**: Java 21 (LTS)
* **Framework**: Spring Boot 4
* **Persistência de Dados**: Spring Data JPA / Hibernate
* **Banco de Dados**: PostgreSQL 16
* **Versionamento de Banco de Dados**: Flyway Migration
* **Segurança**: Spring Security
* **Validação**: Jakarta Bean Validation
* **Produtividade**: Lombok
* **Containerização**: Docker e Docker Compose
* **Formato de Erros**: RFC 7807 (ProblemDetail)

---

## Arquitetura e Estrutura de Pastas

O projeto adota uma Arquitetura em Camadas (Layered Architecture) estrita com separação de responsabilidades e princípios Clean Code e SOLID:

```text
src/main/java/com/vicentedev/api_re
├── config/              # Configuracoes (Seguranca, MVC ResourceHandlers)
├── controller/          # Controladores REST da aplicacao
├── dto/                 # Objetos de Transferencia de Dados
│   ├── request/         # Payloads de entrada com validacao
│   └── response/        # Respostas imutaveis
├── entity/              # Entidades JPA mapeadas para PostgreSQL
├── exception/           # Tratamento global de excecoes (@RestControllerAdvice)
├── mapper/              # Mapeadores desacoplados entre DTOs e Entidades
├── repository/          # Interfaces Spring Data JPA e Specifications
│   └── specification/   # Filtros dinamicos de consulta
└── service/             # Interfaces e regras de negocio
    └── impl/            # Implementacoes dos servicos e armazenamento local
```

---

## Armazenamento de Arquivos e Midia

A aplicacao possui um servico desacoplado (`FileStorageService`) para armazenamento de imagens fisicamente no disco local:

* **Diretorio base**: `./uploads/` (com subpastas `/variants/` e `/gallery/`).
* **Formatos suportados**: `.jpg`, `.jpeg`, `.png`, `.webp`.
* **Acesso publico**: A rota `/uploads/**` e servida diretamente pelo Spring Web MVC.
* **Ciclo de vida e exclusao**: Ao excluir uma variante, foto de galeria ou motocicleta, os arquivos correspondentes sao automaticamente removidos do disco.

---

## Como Executar o Projeto

### Pre-requisitos
* Java Development Kit (JDK) 21 instalado
* Docker e Docker Compose instalados

### 1. Clonar o Repositorio
```bash
git clone git@github.com:VicenteAlef/api-royal-enfield-spring-boot.git
cd api-royal-enfield-spring-boot
git checkout dev
```

### 2. Iniciar o Banco de Dados PostgreSQL
Suba a instancia local do PostgreSQL via Docker Compose:
```bash
docker compose up -d
```

### 3. Executar os Testes Automatizados
Valide a integridade do banco de dados, migricoes Flyway e endpoints REST:
```bash
./mvnw test
```

### 4. Executar a Aplicacao
Inicie o servidor de desenvolvimento:
```bash
./mvnw spring-boot:run
```

A API estara disponivel em `http://localhost:8080/api/v1`.

---

## Estrategia de Branching (Git Flow)

* `main`: Codigo de producao e versoes estaveis consolidadas.
* `dev`: Branch de desenvolvimento ativo e integracao de fases e features.

---

## Roadmap de Desenvolvimento

* **Fase 1 (Planejamento & Setup)**: Modelagem relacional, Docker Compose, configuracao do PostgreSQL e migracao inicial com Flyway. [Concluida]
* **Fase 2 (Catalogo, Variantes, Galeria, Ficha Tecnica e Uploads)**: Entidades JPA, DTOs, Mappers, Repositorios com Specification, FileStorageService, controladores REST e testes de integracao. [Concluida]
* **Fase 3 (Concessionarias e Test Rides)**: CRUD de Concessionarias e fluxo de agendamento de Test Rides. [Proxima Etapa]
* **Fase 4 (Seguranca e Autenticacao)**: Autenticacao com JWT, controle de perfis (USER/ADMIN) e protecao de rotas. [Pendente]
* **Fase 5 (Documentacao e Containerizacao)**: Documentacao interativa via OpenAPI 3 / Swagger e Dockerfile multi-stage. [Pendente]