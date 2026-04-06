-- Tabela de Auditoria solicitada no PDF [cite: 79]
CREATE TABLE TB_AUDITORIA (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_tabela VARCHAR2(50),
    operacao VARCHAR2(10),
    usuario_bd VARCHAR2(100),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_antigo VARCHAR2(4000),
    valor_novo VARCHAR2(4000)
);

-- Trigger de Auditoria para a tabela de Usuários [cite: 30]
CREATE OR REPLACE TRIGGER TRG_AUDIT_USUARIO
AFTER INSERT OR UPDATE OR DELETE ON TB_USUARIO
FOR EACH ROW
DECLARE
    v_op VARCHAR2(10);
BEGIN
    IF INSERTING THEN v_op := 'INSERT';
    ELSIF UPDATING THEN v_op := 'UPDATE';
    ELSE v_op := 'DELETE';
    END IF;

    INSERT INTO TB_AUDITORIA (nome_tabela, operacao, usuario_bd, valor_antigo, valor_novo)
    VALUES ('TB_USUARIO', v_op, USER, :OLD.email, :NEW.email);
END;
/