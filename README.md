# API REST Royal Enfield

API REST desenvolvida em Java 21 e Spring Boot para o ecossistema digital da marca Royal Enfield. O projeto provê gerenciamento completo de catálogo de motocicletas, versões/variantes de acabamento, galeria de imagens para carrossel, especificações técnicas detalhadas, rede de concessionárias, agendamentos de test ride e controle de acesso com perfis de usuários.

A documentação detalhada dos endpoints, estruturas de requisição, exemplos com cURL e formatos de resposta está disponível no meu GitHub em [Contrato da API](https://github.com/VicenteAlef/api-royal-enfield-spring-boot/blob/main/contract.md).

* Aplicação online: [royalenfield.vicentedeveloper.com](https://royalenfield.vicentedeveloper.com/)
* Repositório da API (Spring Boot): [api-royal-enfield-spring-boot](https://github.com/VicenteAlef/api-royal-enfield-spring-boot)
* Repositório do Front-end/Protótipo (React.js): [royal-enfield-react-app-prototype](https://github.com/VicenteAlef/royal-enfield-react-app-prototype)
* Teste de API: [api-re.vicentedeveloper.com.br](https://api-re.vicentedeveloper.com.br/api/v1/motorcycles)

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
* **CI/CD**: GitHub Actions
* **Infraestrutura / Deploy**: Oracle Cloud Infrastructure (OCI - Compute Instance)
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

## Deploy e Integração Contínua (CI/CD)

O ciclo de vida de entrega contínua do projeto é totalmente automatizado:

* **Pipelines de CI/CD**: Implementadas via **GitHub Actions**, executando a compilação, validações estáticas e suíte de testes automatizados a cada push ou pull request.
* **Infraestrutura em Produção**: A aplicação e os serviços correlacionados estão hospedados em uma instância de computação na **Oracle Cloud (OCI)**, executados de forma isolada via contêineres Docker para garantir consistência e estabilidade no ambiente de produção.

---

## Armazenamento de Arquivos e Mídia

A aplicação possui um serviço desacoplado (`FileStorageService`) para armazenamento de imagens fisicamente no disco local:

* **Diretório base**: `./uploads/` (com subpastas `/variants/` e `/gallery/`).
* **Formatos suportados**: `.jpg`, `.jpeg`, `.png`, `.webp`.
* **Acesso público**: A rota `/uploads/**` é servida diretamente pelo Spring Web MVC.
* **Ciclo de vida e exclusão**: Ao excluir uma variante, foto de galeria ou motocicleta, os arquivos correspondentes são automaticamente removidos do disco.

---

## Como Executar o Projeto

### Pré-requisitos

* Java Development Kit (JDK) 21 instalado
* Docker e Docker Compose instalados

### 1. Clonar o Repositório

```bash
git clone git@github.com:VicenteAlef/api-royal-enfield-spring-boot.git
cd api-royal-enfield-spring-boot
git checkout dev

```

### 2. Iniciar o Banco de Dados PostgreSQL

Suba a instância local do PostgreSQL via Docker Compose:

```bash
docker compose up -d

```

### 3. Executar os Testes Automatizados

Valide a integridade do banco de dados, migrações Flyway e endpoints REST:

```bash
./mvnw test

```

### 4. Executar a Aplicação

Inicie o servidor de desenvolvimento:

```bash
./mvnw spring-boot:run

```

A API estará disponível em `http://localhost:8080/api/v1`.

---

## Estratégia de Branching (Git Flow)

* `main`: Código de produção e versões estáveis consolidadas (gatilho de deploy em produção via CI/CD).
* `dev`: Branch de desenvolvimento ativo e integração contínua de features.

---

## Roadmap de Desenvolvimento

* **Fase 1 (Planejamento & Setup)**: Modelagem relacional, Docker Compose, configuração do PostgreSQL e migração inicial com Flyway. [Concluída]
* **Fase 2 (Catálogo, Variantes, Galeria, Ficha Técnica e Uploads)**: Entidades JPA, DTOs, Mappers, Reposositórios com Specification, FileStorageService, controladores REST e testes de integração. [Concluída]
* **Fase 3 (Concessionárias e Test Rides)**: CRUD de Concessionárias e fluxo de agendamento de Test Rides com validação de regras de negócio. [Concluída]
* **Fase 4 (Segurança, 2FA e RBAC)**: Autenticação JWT com 2FA por e-mail, alertas automáticos ao Admin, rastreamento de acessos (last_login_at), 3 perfis (VISITOR, USER, ADMIN) e gestão de usuários. [Concluída]
* **Fase 5 (Documentação e Containerização)**: Documentação interativa via OpenAPI 3 / Swagger e Dockerfile multi-stage. [Próxima Etapa]
