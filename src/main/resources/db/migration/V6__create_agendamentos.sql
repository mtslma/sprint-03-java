-- 1. Criação da Tabela de Agendamentos (Conecta as 4 entidades obrigatórias)
CREATE TABLE TB_AGENDAMENTO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    paciente_id NUMBER NOT NULL,
    medico_id NUMBER NOT NULL,
    unidade_id NUMBER NOT NULL,
    sala_id NUMBER NOT NULL,
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    tipo VARCHAR2(20) NOT NULL, -- CONSULTA, EXAME, CIRURGIA
    status VARCHAR2(20) DEFAULT 'AGENDADO' NOT NULL, -- AGENDADO, CONFIRMADO, CANCELADO, FINALIZADO
    especialidade VARCHAR2(100) NOT NULL,

    -- Chaves Estrangeiras (Relacionamentos de 2+ tabelas - Pág 7)
    CONSTRAINT fk_age_paciente FOREIGN KEY (paciente_id) REFERENCES TB_PACIENTE(usuario_id),
    CONSTRAINT fk_age_medico FOREIGN KEY (medico_id) REFERENCES TB_COLABORADOR(usuario_id),
    CONSTRAINT fk_age_unidade FOREIGN KEY (unidade_id) REFERENCES TB_UNIDADE_SAUDE(id),
    CONSTRAINT fk_age_sala FOREIGN KEY (sala_id) REFERENCES TB_SALA(id) ON DELETE CASCADE
);

-- 2. Função com Cálculo Matemático (Requisito: Pág 9)
-- Calcula o total de minutos ocupados em uma unidade específica
CREATE OR REPLACE FUNCTION FN_CALCULA_DURACAO_TOTAL(p_unidade_id NUMBER)
RETURN NUMBER IS
    v_total_minutos NUMBER;
BEGIN
    SELECT SUM(
        EXTRACT(HOUR FROM (data_hora_fim - data_hora_inicio)) * 60 +
        EXTRACT(MINUTE FROM (data_hora_fim - data_hora_inicio))
    )
    INTO v_total_minutos
    FROM TB_AGENDAMENTO
    WHERE unidade_id = p_unidade_id
    AND status != 'CANCELADO';

    RETURN NVL(v_total_minutos, 0);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0; -- Exceção 1 (Pág 9)
    WHEN VALUE_ERROR THEN
        RETURN -2; -- Exceção 2 (Pág 9)
    WHEN OTHERS THEN
        RETURN -1; -- Exceção 3 (Pág 9)
END;
/