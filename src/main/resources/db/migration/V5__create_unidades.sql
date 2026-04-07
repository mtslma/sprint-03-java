-- 1. Criação das Tabelas (Seus comandos originais)
CREATE TABLE TB_UNIDADE_SAUDE (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    endereco VARCHAR2(255) NOT NULL
);

CREATE TABLE TB_SALA (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero VARCHAR2(10) NOT NULL,
    nome VARCHAR2(50),
    disponibilidade VARCHAR2(100),
    unidade_id NUMBER NOT NULL,
    CONSTRAINT fk_sala_unidade_saude FOREIGN KEY (unidade_id) REFERENCES TB_UNIDADE_SAUDE(id) ON DELETE CASCADE
);

-- 2. Dados Iniciais
INSERT INTO TB_UNIDADE_SAUDE (nome, endereco) VALUES ('Unidade Central', 'Rua das Flores, 123');
INSERT INTO TB_SALA (numero, nome, disponibilidade, unidade_id) VALUES ('101', 'Consultório Clínico', '08:00 - 18:00', 1);

-- 3. TRIGGER DE AUDITORIA
CREATE OR REPLACE TRIGGER TRG_AUDIT_UNIDADE
AFTER INSERT OR UPDATE OR DELETE ON TB_UNIDADE_SAUDE
FOR EACH ROW
DECLARE
    v_op VARCHAR2(10);
BEGIN
    IF INSERTING THEN v_op := 'INSERT';
    ELSIF UPDATING THEN v_op := 'UPDATE';
    ELSE v_op := 'DELETE';
    END IF;

    -- Inserindo na tabela de auditoria que você já possui
    INSERT INTO TB_AUDITORIA (nome_tabela, operacao, usuario_bd, valor_antigo, valor_novo)
    VALUES ('TB_UNIDADE_SAUDE', v_op, USER, :OLD.nome, :NEW.nome);
END;
/