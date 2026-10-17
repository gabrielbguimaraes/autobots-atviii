# Atividade Prática ATVII - Automanager (Microsserviço Spring Boot)

**Aluno:** João Gabriel Barros Guimarães  
**Turma:** 3º DSM  
**Professor:** Prof. Dr. Eng. Gerson Penha  
**Disciplina:** Desenvolvimento Web III  

---


---

## Como Executar o Projeto Localmente

### 1. Executar a Aplicação
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

### 2. Executar os Testes Automatizados
Para rodar os testes:
```bash
.\mvnw.cmd test
```

---

## Principais Endpoints da API

### Clientes
- `GET /clientes` - Listagem de clientes.
- `GET /clientes/{id}` - Obtenção de cliente por IDs.
- `POST /clientes` - Cadastro de cliente.
- `PUT /clientes/{id}` - Atualização do cliente.
- `DELETE /clientes/{id}` - Exclusão de cliente.
- `GET /clientes/{id}/endereco` - endereço do cliente.
- `GET /clientes/{id}/documentos` - documentos do cliente.
- `GET /clientes/{id}/telefones` - telefones do cliente.

### Demais Recursos Independentes
- `GET /documentos` e `GET /documentos/{id}` (CRUD completo em `/documentos`)
- `GET /enderecos` e `GET /enderecos/{id}` (CRUD completo em `/enderecos`)
- `GET /telefones` e `GET /telefones/{id}` (CRUD completo em `/telefones`)
