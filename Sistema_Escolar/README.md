# Sistema Escolar

Projeto de Sistema Escolar desenvolvido em Java + Swing + SQLite.

## Banco de dados

O banco é criado pelo Java através da classe `dao.conexao`.

Ao executar o projeto, o arquivo `sistema_escolar.db` é criado automaticamente caso ainda não exista.

As tabelas criadas são:

- administrador
- professor
- aluno
- disciplina
- turma
- professor_disciplina
- professor_turma
- turma_disciplina
- aluno_turma
- nota

## Acesso inicial do administrador

Email: `admin@email.com`
Senha: `1234`

## Etapa atual

Nesta etapa foram adicionadas as classes de modelo, conexão com SQLite, operações de cadastro/listagem/autenticação e consultas usadas pelas telas.

As telas continuam sendo feitas em Java Swing. O próximo trabalho é continuar ampliando o CRUD, incluindo alterações e exclusões onde ainda faltarem, além dos testes unitários e da pipeline.
