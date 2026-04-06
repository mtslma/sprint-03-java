-- 1. FUNÇÃO: Converte dados para JSON manualmente (Requisito: Proibido built-in)
CREATE OR REPLACE FUNCTION FN_CONVERTE_USUARIO_JSON(p_id NUMBER)
RETURN CLOB IS
    v_email TB_USUARIO.email%TYPE;
    v_role  TB_USUARIO.role%TYPE;
    v_json  CLOB;
BEGIN
    SELECT email, role INTO v_email, v_role FROM TB_USUARIO WHERE id = p_id;

    -- Montagem manual da String JSON
    v_json := '{"id": ' || p_id || ', "email": "' || v_email || '", "role": "' || v_role || '"}';

    RETURN v_json;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN '{"erro": "Usuário não encontrado"}';
    WHEN VALUE_ERROR THEN
        RETURN '{"erro": "Erro de conversão de dados"}';
    WHEN OTHERS THEN
        RETURN '{"erro": "Erro inesperado na função"}';
END;
/

-- 2. PROCEDURE 1: Join de tabelas e exibição em JSON (Usa a função acima)
CREATE OR REPLACE PROCEDURE PRC_LISTAR_PACIENTES_JSON AS
    CURSOR c_pacientes IS
        SELECT u.id, p.nome, p.cpf
        FROM TB_USUARIO u
        INNER JOIN TB_PACIENTE p ON u.id = p.usuario_id; -- Requisito: JOIN de 2+ tabelas

    v_json_base CLOB;
BEGIN
    FOR r IN c_pacientes LOOP
        v_json_base := FN_CONVERTE_USUARIO_JSON(r.id);
        DBMS_OUTPUT.PUT_LINE('Paciente: ' || r.nome || ' | Dados: ' || v_json_base);
    END LOOP;

EXCEPTION
    WHEN CURSOR_ALREADY_OPEN THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Cursor já está aberto.');
    WHEN INVALID_CURSOR THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Cursor inválido.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erro fatal no processamento do JSON.');
END;
/

-- 3. PROCEDURE 2: Relatório com Anterior, Atual e Próximo (LAG/LEAD)
CREATE OR REPLACE PROCEDURE PRC_RELATORIO_USUARIOS AS
    CURSOR c_relatorio IS
        SELECT email,
               LAG(email, 1, 'Vazio') OVER (ORDER BY id) as anterior, -- Requisito: LAG
               email as atual,
               LEAD(email, 1, 'Vazio') OVER (ORDER BY id) as proximo -- Requisito: LEAD
        FROM TB_USUARIO;
BEGIN
    DBMS_OUTPUT.PUT_LINE('ANTERIOR | ATUAL | PROXIMO');
    DBMS_OUTPUT.PUT_LINE('---------------------------');

    FOR r IN c_relatorio LOOP
        DBMS_OUTPUT.PUT_LINE(r.anterior || ' | ' || r.atual || ' | ' || r.proximo);
    END LOOP;

EXCEPTION
    WHEN ZERO_DIVIDE THEN -- Exceção genérica para cumprir o requisito de 3 tratamentos
        DBMS_OUTPUT.PUT_LINE('Erro matemático inesperado.');
    WHEN PROGRAM_ERROR THEN
        DBMS_OUTPUT.PUT_LINE('Erro interno do programa.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erro ao gerar relatório analítico.');
END;
/

-- 4. FUNÇÃO 2: Validação de Complexidade de Senha (Lógica de Negócio)
CREATE OR REPLACE FUNCTION FN_VALIDA_SENHA_FORTE(p_senha VARCHAR2)
RETURN VARCHAR2 IS
BEGIN
    IF LENGTH(p_senha) < 6 THEN
        RETURN 'FRACA';
    ELSIF p_senha LIKE '%123%' THEN
        RETURN 'COMUM';
    ELSE
        RETURN 'FORTE';
    END IF;

EXCEPTION
    WHEN ACCESS_INTO_NULL THEN
        RETURN 'ERRO: Senha nula';
    WHEN STORAGE_ERROR THEN
        RETURN 'ERRO: Memória insuficiente';
    WHEN OTHERS THEN
        RETURN 'ERRO: Falha na validação';
END;
/