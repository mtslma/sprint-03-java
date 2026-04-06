-- Tabela Base
CREATE TABLE TB_USUARIO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR2(100) NOT NULL UNIQUE,
    senha VARCHAR2(255) NOT NULL,
    role VARCHAR2(20) NOT NULL
);

-- Tabela Admin (Sem atributos extras, apenas FK)
CREATE TABLE TB_ADMIN (
    usuario_id NUMBER PRIMARY KEY,
    CONSTRAINT fk_admin_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id)
);

-- Tabela Colaborador
CREATE TABLE TB_COLABORADOR (
    usuario_id NUMBER PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    cpf VARCHAR2(14) NOT NULL UNIQUE,
    tipo_colaborador VARCHAR2(30) NOT NULL,
    CONSTRAINT fk_colaborador_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id)
);

-- Tabela Paciente
CREATE TABLE TB_PACIENTE (
    usuario_id NUMBER PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    cpf VARCHAR2(14) NOT NULL UNIQUE,
    tipo_sanguineo VARCHAR2(3),
    altura NUMBER(3,2),
    CONSTRAINT fk_paciente_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id)
);

-- Tabela para a Lista de Alergias (ElementCollection)
CREATE TABLE TB_PACIENTE_ALERGIAS (
    paciente_id NUMBER NOT NULL,
    alergia VARCHAR2(100),
    CONSTRAINT fk_alergias_paciente FOREIGN KEY (paciente_id) REFERENCES TB_PACIENTE(usuario_id)
);