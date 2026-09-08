# API REST Royal Enfield `v1.0.0`

API REST desenvolvida em Java 21 e Spring Boot para o ecossistema digital da marca Royal Enfield. O projeto provê gerenciamento completo de catálogo de motocicletas, versões/variantes de acabamento, galeria de imagens para carrossel, especificações técnicas detalhadas, rede de concessionárias, agendamentos de test ride, documentação interativa OpenAPI 3 / Swagger UI e controle de acesso baseado em papéis (RBAC) com autenticação em dois fatores (2FA via e-mail) e tokens JWT.

A documentação detalhada dos endpoints, estruturas de requisição, exemplos com cURL e formatos de resposta também está disponível em formato markdown no [Contrato da API](https://github.com/VicenteAlef/api-royal-enfield-spring-boot/blob/main/contract.md).

---

## 🔗 Links e Recursos

* **Aplicação Front-end (Online)**: [royalenfield.vicentedeveloper.com](https://royalenfield.vicentedeveloper.com/)
* **Documentação Swagger UI (Online)**: [api-re.vicentedeveloper.com.br/swagger-ui/index.html](https://api-re.vicentedeveloper.com.br/swagger-ui/index.html)
* **Especificação OpenAPI JSON (Online)**: [api-re.vicentedeveloper.com.br/v3/api-docs](https://api-re.vicentedeveloper.com.br/v3/api-docs)
* **Endpoint de Teste de API**: [api-re.vicentedeveloper.com.br/api/v1/motorcycles](https://api-re.vicentedeveloper.com.br/api/v1/motorcycles)
* **Repositório da API (Spring Boot)**: [api-royal-enfield-spring-boot](https://github.com/VicenteAlef/api-royal-enfield-spring-boot)
* **Repositório do Front-end (React.js)**: [royal-enfield-react-app-prototype](https://github.com/VicenteAlef/royal-enfield-react-app-prototype)
* **Website Oficial do Desenvolvedor**: [vicentedeveloper.com](https://vicentedeveloper.com)

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem**: Java 21 (LTS)
* **Framework**: Spring Boot 4
* **Documentação Interativa**: OpenAPI 3 / Swagger UI (`springdoc-openapi`)
* **Persistência de Dados**: Spring Data JPA / Hibernate
* **Banco de Dados**: PostgreSQL 16
* **Versionamento de Banco de Dados**: Flyway Migration
* **Segurança e Autenticação**: Spring Security + JJWT (JSON Web Token)
* **Segundo Fator de Autenticação (2FA) & E-mails**: Spring Boot Starter Mail (SMTP)
* **Validação**: Jakarta Bean Validation
* **Produtividade**: Lombok
* **Containerização**: Docker e Docker Compose
* **CI/CD**: GitHub Actions + GitHub Container Registry (GHCR)
* **Infraestrutura / Deploy**: Oracle Cloud Infrastructure (OCI - Compute Instance)
* **Formato de Erros**: RFC 7807 (ProblemDetail)

---

## 🏗️ Arquitetura e Estrutura de Pastas

O projeto adota uma Arquitetura em Camadas (Layered Architecture) estrita com separação de responsabilidades e princípios Clean Code e SOLID:

```text
src/main/java/com/vicentedev/api_re
├── config/              # Configuracoes (Security, Swagger/OpenAPI, WebMvc ResourceHandlers)
├── controller/          # Controladores REST da aplicacao
├── dto/                 # Objetos de Transferencia de Dados
│   ├── request/         # Payloads de entrada com validacao e schemas Swagger
│   └── response/        # Respostas imutaveis com schemas Swagger
├── entity/              # Entidades JPA mapeadas para PostgreSQL
├── exception/           # Tratamento global de excecoes (@RestControllerAdvice)
├── mapper/              # Mapeadores desacoplados entre DTOs e Entidades
├── repository/          # Interfaces Spring Data JPA e Specifications
│   └── specification/   # Filtros dinamicos de consulta
└── service/             # Interfaces e regras de negocio
    └── impl/            # Implementacoes dos servicos e armazenamento local
```

---

## 🚀 Deploy e Integração Contínua (CI/CD)

O ciclo de vida de entrega contínua do projeto é totalmente automatizado:

* **Pipelines de CI/CD**: Implementadas via **GitHub Actions**, executando a compilação, validações estáticas, suíte de testes automatizados, build da imagem Docker, publicação no GHCR e deploy automático via SSH na Oracle Cloud a cada push na branch `main`.
* **Infraestrutura em Produção**: A aplicação e o banco de dados PostgreSQL estão hospedados em uma instância de computação na **Oracle Cloud (OCI)**, orquestrados via contêineres Docker Compose com volumes persistentes para banco e uploads.

---

## 🖼️ Armazenamento de Arquivos e Mídia

A aplicação possui um serviço desacoplado (`FileStorageService`) para armazenamento de imagens fisicamente no disco local:

* **Diretório base**: `./uploads/` (com subpastas `/variants/` e `/gallery/`).
* **Formatos suportados**: `.jpg`, `.jpeg`, `.png`, `.webp`.
* **Acesso público**: A rota `/uploads/**` é servida diretamente pelo Spring Web MVC.
* **Ciclo de vida e exclusão**: Ao excluir uma variante, foto de galeria ou motocicleta, os arquivos físicos correspondentes são automaticamente removidos do disco.

---

## 💻 Como Executar o Projeto Localmente

### Pré-requisitos

* Java Development Kit (JDK) 21 instalado
* Docker e Docker Compose instalados

### 1. Clonar o Repositório

```bash
git clone git@github.com:VicenteAlef/api-royal-enfield-spring-boot.git
cd api-royal-enfield-spring-boot
git checkout dev
```

### 2. Executar via Docker Compose (Recomendado)

Gere o pacote `.jar` e inicie todos os contêineres:

```bash
./mvnw clean package -DskipTests
docker compose up -d --build
```

Acesse:
* **Swagger UI**: `http://localhost:3001/swagger-ui/index.html`
* **API Base**: `http://localhost:3001/api/v1`

### 3. Executar Testes Automatizados

Valide a integridade de todos os fluxos e endpoints com o banco de testes:

```bash
./mvnw test
```

---

## 🌿 Estratégia de Branching (Git Flow)

* `main`: Código de produção e versão estável consolidada `v1.0.0` (gatilho de deploy automático em produção).
* `dev`: Branch de desenvolvimento contínuo e validação de features.

---

## 🗺️ Roadmap de Desenvolvimento

* **Fase 1 (Planejamento & Setup)**: Modelagem relacional, Docker Compose, configuração do PostgreSQL e migração inicial com Flyway. `[Concluída]`
* **Fase 2 (Catálogo, Variantes, Galeria, Ficha Técnica e Uploads)**: Entidades JPA, DTOs, Mappers, Repositórios com Specification, FileStorageService, controladores REST e testes de integração. `[Concluída]`
* **Fase 3 (Concessionárias e Test Rides)**: CRUD de Concessionárias e fluxo de agendamento de Test Rides com validação de regras de negócio. `[Concluída]`
* **Fase 4 (Segurança, 2FA e RBAC)**: Autenticação JWT com 2FA por e-mail, alertas automáticos ao Admin, rastreamento de acessos (`last_login_at`), 3 perfis (`VISITOR`, `USER`, `ADMIN`) e gestão de usuários. `[Concluída]`
* **Fase 5 (Testes, Documentação OpenAPI / Swagger & Containerização)**: Documentação interativa via OpenAPI 3 / Swagger UI com Bearer JWT, suite de 41 testes de integração, Dockerfile e deploy automatizado via CI/CD. `[Concluída]`

