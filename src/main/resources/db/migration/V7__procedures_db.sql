-- PROCEDURE 1: Finalização Automática de Agendamentos Passados
CREATE OR REPLACE PROCEDURE SP_FINALIZA_CONSULTAS_ANTIGAS AS
BEGIN
    UPDATE TB_AGENDAMENTO
    SET status = 'FINALIZADO'
    WHERE status = 'AGENDADO'
    AND data_hora_fim < CURRENT_TIMESTAMP;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20001, 'Erro ao finalizar consultas antigas.');
END;
/

-- PROCEDURE 2: Atualizar Status com Validação
-- Útil para o Java chamar e garantir que o status existe
CREATE OR REPLACE PROCEDURE SP_ATUALIZA_STATUS_AGENDAMENTO(
    p_id IN NUMBER,
    p_status IN VARCHAR2
) AS
BEGIN
    UPDATE TB_AGENDAMENTO
    SET status = p_status
    WHERE id = p_id;

    IF SQL%NOTFOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Agendamento não encontrado para atualização.');
    END IF;

    COMMIT;
END;
/

-- FUNCTION 2: Verifica se uma sala está ocupada em determinado horário
CREATE OR REPLACE FUNCTION FN_VERIFICA_SALA_DISPONIVEL(
    p_sala_id NUMBER,
    p_inicio TIMESTAMP
) RETURN VARCHAR2 IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM TB_AGENDAMENTO
    WHERE sala_id = p_sala_id
    AND status != 'CANCELADO'
    AND p_inicio BETWEEN data_hora_inicio AND data_hora_fim;

    IF v_count > 0 THEN
        RETURN 'OCUPADA';
    ELSE
        RETURN 'LIVRE';
    END IF;
END;
/