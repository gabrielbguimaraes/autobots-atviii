# Atividade Prática ATVII - Automanager (Microsserviço Spring Boot)

**Aluno:** João Gabriel Barros Guimarães  
**Turma:** 3º DSM  
**Professor:** Prof. Dr. Eng. Gerson Penha  
**Disciplina:** Desenvolvimento Web III  

---

## 📌 Descrição do Projeto

Aplicação de microsserviço `automanager` desenvolvida em **Java 17** e **Spring Boot 2.6.4**, contemplando integralmente os **4 Níveis do Modelo de Maturidade de Richardson (RMM)**:

- **Nível 0 (Protocolo HTTP):** Tráfego integral via HTTP.
- **Nível 1 (Recursos e URIs Bem Definidas):** Endpoints REST canônicos para cada entidade do domínio (`/clientes`, `/documentos`, `/enderecos`, `/telefones`).
- **Nível 2 (Verbos HTTP e Status Codes Reais):** Uso correto de `GET` (200), `POST` (201 com header `Location`), `PUT` (200), `DELETE` (204), e tratamento global com `@ControllerAdvice` retornando `404 Not Found` e `400 Bad Request`.
- **Nível 3 (HATEOAS):** Hipermídia navegável (`_links`) em todas as entidades e coleções via `spring-boot-starter-hateoas` e `RepresentationModel`.

**Easter Egg Científico:**  
- `application.properties`: `easteregg=rho_P=c^5/(hbar*G^2)` (Densidade de Planck).

---

## 🚀 Como Executar o Projeto Localmente

### 1. Pré-requisitos
- **Java JDK 17** instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
- Git instalado.

### 2. Clonar o Repositório
```bash
git clone https://github.com/gabrielbguimaraes/autobots-atviii.git
cd autobots-atviii
```

### 3. Executar a Aplicação
Você pode utilizar o Maven Wrapper embutido no projeto:

- **No Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

- **No Linux / macOS (Bash):**
  ```bash
  ./mvnw spring-boot:run
  ```

A aplicação inicializará por padrão na porta `8080` com banco de dados em memória **H2** e seed automático de dados.

### 4. Executar os Testes Automatizados
Para rodar toda a suíte de testes (incluindo testes de integração RMM e HATEOAS):
```bash
.\mvnw.cmd test
```

---

## 🌐 Principais Endpoints da API

### Clientes
- `GET /clientes` - Listagem de clientes com nós `_links` individuais e da coleção.
- `GET /clientes/{id}` - Obtenção de cliente por ID com links HATEOAS e sub-recursos.
- `POST /clientes` - Cadastro de cliente (retorna `201 Created` e cabeçalho `Location`).
- `PUT /clientes/{id}` - Atualização integral do cliente (retorna `200 OK`).
- `DELETE /clientes/{id}` - Exclusão de cliente (retorna `204 No Content`).
- `GET /clientes/{id}/endereco` - Sub-recurso de endereço do cliente.
- `GET /clientes/{id}/documentos` - Sub-recurso de documentos do cliente.
- `GET /clientes/{id}/telefones` - Sub-recurso de telefones do cliente.

### Demais Recursos Independentes
- `GET /documentos` e `GET /documentos/{id}` (CRUD completo em `/documentos`)
- `GET /enderecos` e `GET /enderecos/{id}` (CRUD completo em `/enderecos`)
- `GET /telefones` e `GET /telefones/{id}` (CRUD completo em `/telefones`)
