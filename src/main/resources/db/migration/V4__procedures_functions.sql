-- 1. FUNÇÃO AUXILIAR: Montagem manual do JSON (Sem built-in)
CREATE OR REPLACE FUNCTION FN_CONVERTE_USUARIO_JSON(p_id NUMBER)
RETURN VARCHAR2 IS
    v_email TB_USUARIO.email%TYPE;
    v_role  TB_USUARIO.role%TYPE;
    v_json  VARCHAR2(4000);
BEGIN
    SELECT email, role INTO v_email, v_role FROM TB_USUARIO WHERE id = p_id;
    v_json := '{"id": ' || p_id || ', "email": "' || v_email || '", "role": "' || v_role || '"}';
    RETURN v_json;
EXCEPTION
    WHEN NO_DATA_FOUND THEN RETURN '{"erro": "Usuario nao encontrado"}';
    WHEN VALUE_ERROR THEN RETURN '{"erro": "Erro de conversao"}';
    WHEN OTHERS THEN RETURN '{"erro": "Erro inesperado na funcao"}';
END;
/

-- 2. FUNCTION PARA RELATÓRIO ANALÍTICO (LAG/LEAD)
-- Retorna os dados formatados para serem lidos pelo Java
CREATE OR REPLACE FUNCTION FN_GET_RELATORIO_ANALYTIC(p_id NUMBER) RETURN VARCHAR2 IS
    v_res VARCHAR2(4000);
BEGIN
    SELECT LAG(email, 1, 'Vazio') OVER (ORDER BY id) || ' | ' || email || ' | ' || LEAD(email, 1, 'Vazio') OVER (ORDER BY id)
    INTO v_res FROM TB_USUARIO WHERE id = p_id;
    RETURN v_res;
EXCEPTION
    WHEN OTHERS THEN RETURN 'Vazio | Erro | Vazio';
END;
/

-- 3. FUNCTION PARA LISTAGEM JSON COM JOIN
-- Esta função será chamada dentro do SELECT no Repository
CREATE OR REPLACE FUNCTION FN_GET_PACIENTE_JOIN_JSON(p_usuario_id NUMBER) RETURN VARCHAR2 IS
    v_nome TB_PACIENTE.nome%TYPE;
    v_json VARCHAR2(4000);
BEGIN
    SELECT p.nome INTO v_nome FROM TB_PACIENTE p WHERE p.usuario_id = p_usuario_id;
    v_json := 'Paciente: ' || v_nome || ' | Dados: ' || FN_CONVERTE_USUARIO_JSON(p_usuario_id);
    RETURN v_json;
EXCEPTION
    WHEN NO_DATA_FOUND THEN RETURN 'Paciente nao encontrado';
    WHEN OTHERS THEN RETURN 'Erro no processamento do JOIN';
END;
/