# VHLchallenge



As tecnologias utilizadas para a parte de código foram:

- Java (18.0.2)
- Spring (3.1.2)
- JPA/JPQL

E o banco de dados utilizado é o MySQL 8.1.0.

*Obs. São necessários o Java e o MySQL pora rodar, porém como o JPA foi utilizado, o PostGreSQL pode ser utilizado. É só mudar a dependência dentro do "pom.xml" do projeto, juntamente com as informações relevantes em "application.properties".*

# O Projeto
Este projeto contem uma API de gerenciamento para uma biblioteca.
## Objetivo:
O projeto tem como objetivo a criação de um CRUD para uma biblioteca.
- Deve permitir a criação, exclusão e edição de usuários e livros;
- Deve permitir a pesquisa de usuários por nome;
- Deve permitir a pesquisa de livros por nome, autor.
- Deve permitir que um usuário tenha somente dois livros emprestados ao mesmo tempo.

## Instalação do projeto

Faça um clone do projeto em um diretório, ou baixe ele com um zip e extraia, e é só rodar como se fosse uma aplicação Java, ou numa IDE (Intellij, por exemplo).

## Banco de dados

O hibernate gera as tabelas com base nas entidades com a anotação @Entity, porém o SQL para a geração manual de tabelas deve ser algo como:
```
CREATE TABLE livro (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    TITULO VARCHAR(255),
    AUTOR VARCHAR(255),
    excluido BIT,
    PRIMARY KEY (ID)
);

CREATE TABLE usuario (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    NOME VARCHAR(255),
    excluido BIT,
    PRIMARY KEY (ID)
);

CREATE TABLE usuario_livro (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    USUARIO_ID BIGINT,
    LIVRO_ID BIGINT,
    ativo BIT,
    PRIMARY KEY (ID),
    CONSTRAINT FK_USUARIO_LIVRO_USUARIO FOREIGN KEY (USUARIO_ID) REFERENCES usuario(ID),
    CONSTRAINT FK_USUARIO_LIVRO_LIVRO FOREIGN KEY (LIVRO_ID) REFERENCES livro(ID)
);
```
*Obs. Esse SQL é baseado nas entidades, e como é o hibernate que gera, pode ser que não funcione se gerar no Workbench ou Terminal usando esse bloco de código. Caso o SQL não funcionar, é só dar "drop" nas tabelas e deixar o hibernate gerar na hora que o projeto for inicializado pela primeira vez.*

## Utilização
Depois de rodar o projeto, você poderá utilizar o projeto, e a utilização do projeto é feito por meio de endpoints que permitem a interação. Essencialmente, você deve utilizar algo como o Postman o Insomnia ou até mesmo o console do seu browser.

Os corpos esperados estão nos blocos de código.
(O endereço inicial geralmente vai ser 'http://localhost:8080/'. Deve-se adicionar os endpoints DEPOIS do '/'. Ex: 'http://localhost:8080/usuario/adicionar')
Os endpoints são o seguinte:
- /livro
  - POST /adicionar
    ```
    {
    "nome":"Usuario"
    }
    ```
  - GET /pesquisarPorTitulo
    ```
    {
    "titulo":"Titulo"
    }
    ```
  - GET /pesquisarPorAutor
    ```
    {
    "autor":"Autor"
    }
    ```
  - GET /pesquisarPorAutorETitulo
    ```
    {
    "titulo":"Titulo"
    }
    ```
  - PUT /editar/{id} (Obs. Não é necessário ter o autor E o título, só um OU o outro já serve. Trocar o {id} por um número. Ex: 'http://localhost:8080/livro/editar/1')
    ```
    {
    "titulo":"Titulo",
    "autor":"Autor"
    }
    ```
  - DEL /excluir/{id} (Obs. Trocar o {id} por um número. Ex: 'http://localhost:8080/livro/editar/1')
  - POST /emprestar
    ```
    {
    "usuario_id":1,
    "livro_id":1
    }
    ```
  - POST /devolver
    ```
    {
    "usuario_id":1,
    "livro_id":1
    }
    ```
- /usuario
  - POST /adicionar
    ```
    {
    "nome":"lucas"
    }
    ```
  - GET /pesquisarPorNome
    ```
    {
    "nome":"lucas"
    }
    ```
  - PUT /editar/{id}
    ```
    {
    "nome":"lucas"
    }
    ```
  - DEL /excluir/{id} (Obs. Trocar o {id} por um número. Ex: 'http://localhost:8080/livro/editar/1')
