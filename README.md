# 🏢 Sistema de Gerenciamento de Clientes

![Java](https://img.shields.io/badge/Java-8-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1.4-brightgreen)

Sistema RESTful para gerenciamento de clientes pessoa física e jurídica, desenvolvido com Spring Boot. O projeto implementa validações robustas de CPF/CNPJ, telefone e email, além de oferecer um CRUD completo com testes automatizados.

## ✨ Funcionalidades

- ✅ Cadastro de clientes pessoa física (CPF) e jurídica (CNPJ)
- ✅ Listagem completa de clientes
- ✅ Busca de cliente por ID
- ✅ Atualização de telefone e email
- ✅ Remoção de clientes
- ✅ Validação de CPF/CNPJ com algoritmo verificador
- ✅ Validação de email com domínio obrigatório
- ✅ Validação de telefone brasileiro
- ✅ Prevenção de cadastros duplicados
- ✅ Tratamento de erros centralizado
  
## 🚀 Tecnologias

- **Java 8**
- **Spring Boot 2.1.4**
- **Spring Data JPA**
- **H2 Database** (banco em memória)
- **Maven** (gerenciamento de dependências)
- **JUnit 4** (testes unitários)
- **Mockito** (mocks para testes)
- **Bean Validation** (validações)

## 📦 Pré-requisitos

Antes de começar, você precisará ter instalado:

- [Java JDK 8+](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven 3.6+](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- IDE de sua preferência (Eclipse, IntelliJ IDEA, VS Code)

## 🌐 Endpoints da API

### Clientes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/clientes` | Cadastra novo cliente |
| `GET` | `/clientes` | Lista todos os clientes |
| `GET` | `/clientes/{id}` | Busca cliente por ID |
| `PUT` | `/clientes/{id}` | Atualiza telefone e email |
| `DELETE` | `/clientes/{id}` | Remove cliente |

## 📝 Exemplos de Requisições

### Cadastrar Cliente Pessoa Física

**POST** `/clientes`

```json
{
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfCnpj": "113.934.780-26",
  "telefone": "(47) 99999-8888",
  "email": "joao@email.com"
}
```

**Resposta** (201 Created):
```json
{
  "id": 1,
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfCnpj": "11393478026",
  "telefone": "47999998888",
  "email": "joao@email.com"
}
```

### Cadastrar Cliente Pessoa Jurídica

**POST** `/clientes`

```json
{
  "nome": "Empresa XYZ LTDA",
  "tipoPessoa": "JURIDICA",
  "cpfCnpj": "04.252.011/0001-10",
  "telefone": "(47) 3333-4444",
  "email": "contato@empresa.com"
}
```

### Listar Todos os Clientes

**GET** `/clientes`

**Resposta** (200 OK):
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "tipoPessoa": "FISICA",
    "cpfCnpj": "11721384006",
    "telefone": "11988887777",
    "email": "joao.silva@email.com"
  },
  {
    "id": 2,
    "nome": "Tech Soluções LTDA",
    "tipoPessoa": "JURIDICA",
    "cpfCnpj": "36166241000152",
    "telefone": "4733334444",
    "email": "contato@techsolucoes.com"
  }
]
```

### Buscar Cliente por ID

**GET** `/clientes/1`

**Resposta** (200 OK):
```json
{
  "id": 1,
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfCnpj": "11721384006",
  "telefone": "11988887777",
  "email": "joao.silva@email.com"
}
```

### Atualizar Cliente

**PUT** `/clientes/1`

```json
{
  "telefone": "(47) 98888-7777",
  "email": "novo@email.com"
}
```

**Resposta** (200 OK):
```json
{
  "id": 1,
  "nome": "João Silva",
  "tipoPessoa": "FISICA",
  "cpfCnpj": "11721384006",
  "telefone": "47988887777",
  "email": "novo@email.com"
}
```

### Deletar Cliente

**DELETE** `/clientes/1`

**Resposta** (200 OK)

## ✔️ Validações

### CPF/CNPJ

- ✅ Validação com algoritmo de dígitos verificadores
- ✅ Rejeita números repetidos (ex: 111.111.111-11)
- ✅ Pessoa Física deve ter CPF (11 dígitos)
- ✅ Pessoa Jurídica deve ter CNPJ (14 dígitos)
- ✅ Não permite duplicação no sistema
- ✅ Aceita formatação (pontos, traços) ou apenas números

### Telefone

- ✅ Formato brasileiro: `(XX) XXXXX-XXXX` ou `(XX) XXXX-XXXX`
- ✅ Aceita com ou sem formatação
- ✅ Validação via regex

### Email

- ✅ Formato válido de email
- ✅ Domínio obrigatório (ex: `.com`, `.com.br`)
- ✅ Deve conter `@`

### Campos Obrigatórios

- Nome
- Tipo de Pessoa (FISICA ou JURIDICA)
- CPF/CNPJ
- Telefone

## 🧪 Testes

O projeto possui **47 testes automatizados** cobrindo:

- **Controller** (testes de integração)
- **Service** (testes unitários com mocks)
- **Repository** (testes de persistência)

### Cobertura de Testes

- ✅ Cadastro válido e inválido
- ✅ Listagem e busca
- ✅ Atualização de dados
- ✅ Remoção de registros
- ✅ Validações de CPF/CNPJ
- ✅ Validações de email e telefone
- ✅ Casos de erro (404, 400)

## 📁 Estrutura do Projeto

```
projeto-integrador/
│
├── src/
│   ├── main/
│   │   ├── java/br/com/wmw/projetointegrador/
│   │   │   ├── config/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── controller/
│   │   │   │   ├── ClienteController.java
│   │   │   │   └── form/
│   │   │   │       ├── ClienteForm.java
│   │   │   │       └── AtualizacaoClienteForm.java
│   │   │   ├── dto/
│   │   │   │   └── ClienteDto.java
│   │   │   ├── modelo/
│   │   │   │   ├── Cliente.java
│   │   │   │   └── TipoPessoa.java
│   │   │   ├── repository/
│   │   │   │   └── ClienteRepository.java
│   │   │   ├── service/
│   │   │   │   ├── ClienteService.java
│   │   │   │   └── ClienteServiceImpl.java
│   │   │   └── ProjetoIntegradorApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   │
│   └── test/
│       └── java/br/com/wmw/projetointegrador/
│           ├── controller/
│           │   └── ClienteControllerTest.java
│           ├── service/
│           │   └── ClienteServiceImplTest.java
│           └── repository/
│               └── ClienteRepositoryTest.java
│
├── pom.xml
└── README.md
```

## 💾 Banco de Dados

O projeto utiliza **H2 Database** (banco em memória) para desenvolvimento e testes.

### Dados Iniciais

O arquivo `data.sql` insere 4 clientes automaticamente ao iniciar:

1. João Silva (PF)
2. Maria Oliveira (PF)
3. Tech Soluções LTDA (PJ)
4. Padaria do Bairro ME (PJ)

### Esquema da Tabela

```sql
CREATE TABLE CLIENTE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo_pessoa VARCHAR(20) NOT NULL,
    cpf_cnpj VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(11) NOT NULL,
    email VARCHAR(255) NOT NULL
);
```

<div align="center">
  <sub>Projeto desenvolvido como trabalho acadêmico</sub>
</div>
