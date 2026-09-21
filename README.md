# Screen Match

Aplicação de console em Spring Boot que consome a API [OMDb](https://www.omdbapi.com/)
para buscar séries, listar temporadas e episódios e gerar estatísticas de avaliação
com a Stream API. Projeto do curso de Java da Alura.

## Stack

Java 26 · Spring Boot 4.1.1 · Maven · HttpClient (JDK) · Jackson 3 · JUnit 5

## Funcionalidades

- Busca uma série pelo nome e baixa todas as temporadas
- Top 10 episódios por avaliação
- Busca de episódio por trecho do título
- Média de avaliação por temporada e estatísticas gerais

## Como rodar

```bash
./mvnw spring-boot:run     # mvnw.cmd no Windows
```

Requer JDK 26 e internet. A chave da OMDb está fixa em `Principal.java` —
mova para variável de ambiente antes de publicar.
