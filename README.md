# api-pessoas
Teste para processo seletivo

## Desafio
* Criar uma api que gerencie uma pessoa, permitindo operações comuns de:
  * Cadastro de pessoa
  * Edição de Pessoa
  * Listagem de pessoas
  * Informação detalhada de uma pessoa
  * Cadastro de endereços para uma pessoa
  * Visualização dos endereços de uma pessoa
  * Definir endereço como favorito

## Implementação
* O código foi implementado usando JAVA11 com as seguintes ferramentas:
  * SpringBot
  * Lombok
  * Jpa
  * H2
  * Swagger
  * Jackson

* Para testes foi utilizado JUNIT5

## Entendendo o código:

O projeto segue um padrão simples porém muito eficaz (MVC), o diagrama abaixo, exemplifica todo
o ciclo de vida da aplicação:

<div align="center">
  <img src="/diagram/diagrama-fluxo-img.png" title="diagram">
</div>

## Validando o projeto
### Testes
* Para validar os testes basta rodar o comando:
   ```sh
  gradle build
  ```

* Para validar a aplicação em live, basta startar a app e acessar o link: http://localhost:8080/swagger-ui.html

Toda a documentação de endpoints e payloads estão mapeadas e documentadas no swagger.

## Agradeço a oportunidade
### Fico à disposição.
