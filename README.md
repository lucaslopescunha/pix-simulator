# 🚀 Pix Simulator
Este projeto é um simulador de pagamentos PIX desenvolvido com Spring Boot 3.5.x, Java 21 e OpenAPI.

## 🛠️ Pré-requisitos

Docker instalado.

Docker Compose (geralmente já vem com o Docker Desktop).

### 📦 Como Rodar com Docker
A maneira mais simples de subir a aplicação, incluindo a persistência de dados e configurações de rede, é usando o Docker Compose. 

### Build e Execução
   Na pasta onde está o arquivo docker-compose.yml, execute:
   ```bash
   docker compose up --build -d
   ```
Use o código com cuidado.

--build: Garante que a imagem seja reconstruída com as alterações mais recentes do seu código.

-d: Roda em modo detached (segundo plano).

### Acessando a Aplicação
   
#### Após o container subir, a aplicação estará disponível em:
   Swagger UI (Documentação): http://localhost:8080/swagger-ui.html
   
API Docs (JSON): http://localhost:8080/v3/api-docs

H2 Console (Banco de Dados): http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:/app/data/pixdb
User: sa | Password: (vazio)

📋 Comandos Úteis

   Ver Logs

Para acompanhar o que está acontecendo na aplicação em tempo real:
   
 ```bash
   docker compose logs -f pix-simulator
   ```
   Use o código com cuidado.

   Parar a Aplicação
   Para parar os containers mas manter os dados salvos:
   ```bash
   docker compose stop
   ```
   Use o código com cuidado.
   Remover Tudo
   Para remover os containers e a rede criada (isso não deleta o seu banco de dados local na pasta ./data):
   ```bash
   docker compose down
   ```
   Use o código com cuidado.
