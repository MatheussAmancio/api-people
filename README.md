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

## Questões do teste
### 1. Durante a implementação de uma nova funcionalidade de software solicitada, quais critérios você avalia e implementa para garantia de qualidade de software?
Para implementar uma nova funcionalidade, alguns pontos devem ser levados em conta. Partindo do entendimento da demanda com negócios, garantindo que não haja nenhuma pendencia/dúvida de negócios e apenas atuação técnica.
Tendo isso mapeado o próximo passo é refinar preferencialmente em time, toda a parte técnica, mapeando os pontos de atuação, possíveis problemas que surgirão e cenários de testes que devem ser cobertos.
Feito o refinamento, podemos seguir com a implementação, pensando sempre em um código limpo, seguro e escalável, nesse momento os testes unitários e se possível integrados, garantirão que a nova funcionalidade funcione como esperado, e não causa nenhum problema no fluxo atual da aplicação.

### 2.	Em qual etapa da implementação você considera a qualidade de software?
A qualidade do software é algo a ser pensado desde o inicio da implementação, isso não descarta a possibilidade de futuras refatorações, mas o ideal é que toda a implementação seja feita seguindo alguns padrões de qualidade, pensando na qualidade e clareza do código, além da segurança e da escalabilidade dele.

## Agradeço a oportunidade
### Fico à disposição.
