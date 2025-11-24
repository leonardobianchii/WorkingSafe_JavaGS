# WorkingSafe – Plataforma de Bem-Estar Corporativo (Java / Spring Boot)

## 👥 Integrantes do Projeto

* **Leonardo Bianchi – RM558576**
* **Mateus Teni Pierro – RM555125**
* **Heitor Romero – RM557825**

---

## 📌 Visão Geral

O **WorkingSafe** é um sistema de bem-estar corporativo desenvolvido em **Java Spring Boot**, integrando tecnologias avançadas como **IA Generativa**, **Mensageria Assíncrona**, **Segurança**, **Cache**, **Internacionalização**, **Paginação** e **Deploy em Nuvem**.

A plataforma permite que colaboradores realizem *check-ins de bem-estar*, e com base nesses dados o sistema gera **recomendações personalizadas via IA (Spring AI + Ollama)**. Os gestores têm acesso a visualizações agregadas dos dados, preservando sempre a privacidade dos colaboradores.

O projeto atende plenamente os requisitos do módulo **Java Advanced**.

---

# 📂 Estrutura do Projeto

Com base nas pastas reais do repositório:

```
src/main/java/br/com/workingsafe
│
├── config        → Configurações gerais, segurança, i18n, cache, RabbitMQ
├── controller    → Controllers Web e REST
├── dto           → Objetos de transferência de dados
├── exception     → Tratamento global de exceções
├── mapper        → Conversores Model ⇄ DTO
├── model         → Entidades JPA
├── rabbit        → Produtores e consumidores de RabbitMQ
├── repository    → Interfaces Spring Data JPA
├── service       → Camada de regras de negócio + IA generativa
└── web           → Controllers e páginas MVC

src/main/resources
│
├── i18n          → Arquivos de internacionalização
├── static        → CSS, JS, assets
├── templates     → Views Thymeleaf
└── application.yml
```

---

# 🧠 IA Generativa (Spring AI + Ollama)

A IA é utilizada para gerar recomendações personalizadas conforme o humor, foco, pausas e horas trabalhadas.

### 🔧 Configuração do Ollama via Docker

```bash
docker run -d --name ollama -p 11434:11434 ollama/ollama:latest
```

Após isso, baixe um modelo leve:

```bash
ollama pull phi3:mini
```

E configure no `application.yml`:

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      model: phi3:mini
```

---

# 📨 Mensageria com RabbitMQ

Usado para eventos e processos assíncronos.

### 🔧 Subir RabbitMQ (uma linha)

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Painel disponível em:
👉 [http://localhost:15672](http://localhost:15672)
Usuario/Senha: **guest / guest**

Config:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

---

# 🗄️ Banco de Dados em Nuvem

O projeto utiliza PostgreSQL hospedado em nuvem (Railway/Neon/Supabase).

Exemplo de configuração:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>/<db>
    username: <user>
    password: <pass>
```

---

# 🌐 Internacionalização (i18n)

Arquivos em:

```
/i18n/messages.properties

```

Suporte: **pt-BR** e **en-US** automaticamente.

---

# ⚙️ Cache

Ativado globalmente via `@EnableCaching`.
Usado em consultas para melhorar tempo de resposta.

---

# 🔐 Segurança – Spring Security

* Login por email/senha
* Roles: **USER** e **ADMIN**
* Autorização por página

---

# 📄 Paginação

Listagens longas usam:

```java
Page<Objeto> listar(Pageable pageable)
```

Com suporte nativo no frontend.

---

# 🚨 Tratamento Global de Erros

Implementado em:

```
/exception
```

Com respostas JSON padronizadas.

---

# 🏗️ Subindo o Ambiente Completo (Ollama + RabbitMQ)

Crie `docker-compose.yml`:

```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"

  ollama:
    image: ollama/ollama:latest
    ports:
      - "11434:11434"
    volumes:
      - ollama:/root/.ollama

volumes:
  ollama:
```

Rodar:

```bash
docker compose up -d
```
