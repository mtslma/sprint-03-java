# Medix API - Sistema de Gestão de Agendamentos Médicos

**Link para o vídeo demonstrativo:** https://youtu.be/GH7bMLCIbZ4

O Medix é uma API robusta desenvolvida com Spring Boot para gerenciar o fluxo de agendamentos entre pacientes, médicos e unidades de saúde. O projeto utiliza práticas avançadas de segurança, versionamento de banco de dados e integração com objetos nativos do Oracle.

## Requisitos e Configuração

### Banco de Dados
As credenciais de acesso ao banco de dados (Oracle) já estão devidamente configuradas no arquivo application.yaml. O sistema está pronto para uso imediato sem necessidade de alterações manuais nas configurações de conexão.

As migrações do Flyway são executadas automaticamente na inicialização, criando a estrutura de tabelas, inserindo dados básicos, configurando triggers de auditoria e registrando as procedures necessárias.

## Como Executar a Aplicação

1. Certifique-se de ter o JDK 21 e Maven CLI instalado.
2. Navegue até a pasta raiz do projeto backend.
3. Execute o comando:
```bash
mvnw spring-boot:run
```
4. O servidor estará disponível em http://localhost:8080

## Níveis de Acesso (Spring Security)

A API implementa controle de acesso baseado em Roles (Perfis):

* Admin: Permite o cadastro de colaboradores e visualização de relatórios de auditoria.
* Colaborador: Acesso a funcionalidades operacionais de unidades de saúde.
* Paciente: Permissão para realizar agendamentos próprios e consultar histórico pessoal.

## Fluxos de Negócio Implementados

Para atender aos requisitos técnicos, a aplicação foca em dois fluxos principais:

1. Fluxo de Agendamento e Disponibilidade: Processo de criação de consultas que valida a disponibilidade do médico e a existência da unidade de saúde antes da persistência.
2. Fluxo de Auditoria e Conformidade: Registro automático de ações críticas através de uma camada de auditoria, permitindo rastrear quem realizou alterações no sistema.
3. Integração com Procedures: Execução de lógica complexa no banco de dados para geração de históricos em formato JSON e relatórios de navegação analíticos.


## Testes via Postman
Caso deseje, disponibilizamos uma Collection do Postman para testes:

* [Download da Collection](./postman/postman_collection_sprint03.json)

Basta importar o arquivo no Postman e configurar o `Bearer Token` após realizar o login.

## Integrantes do Grupo

- RM561061 - Arthur Thomas Mariano de Souza
- RM559873 - Davi Cavalcanti Jorge
- RM559728 - Mateus da Silveira Lima
