package br.com.fiap.medix.config;

import br.com.fiap.medix.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * CAPTURA ERROS DAS PROCEDURES ORACLE (RAISE_APPLICATION_ERROR)
     * Trata os erros ORA-20001, ORA-20002, etc., definidos nas suas V7 e V6.
     */
    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ErroResponse> handleOracleProcedures(JpaSystemException ex) {
        String fullMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        String cleanMessage = "Erro no processamento do banco de dados.";

        // Lógica para limpar o código ORA-XXXXX e mostrar apenas o texto da sua procedure
        if (fullMessage != null && fullMessage.contains("ORA-")) {
            // Tenta pegar apenas a mensagem após o código do erro (ex: ORA-20002: Mensagem aqui)
            String[] parts = fullMessage.split(":");
            cleanMessage = parts.length > 1 ? parts[1].split("\n")[0].trim() : fullMessage;
        }

        var status = HttpStatus.BAD_REQUEST;
        var body = new ErroResponse(
                status.value(),
                "Erro de Banco (PL/SQL)",
                cleanMessage,
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    /**
     * CAPTURA ERRO 403 - ACESSO NEGADO
     * Quando um Paciente tenta acessar rota de Admin, por exemplo.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleForbidden(AccessDeniedException ex) {
        var status = HttpStatus.FORBIDDEN; // 403
        var body = new ErroResponse(
                status.value(),
                "Acesso Negado",
                "Você não tem permissão para acessar este recurso.",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    // Captura erros de lógica do Java (Ex: Antecedência de 30min)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var body = new ErroResponse(
                status.value(),
                "Regra de Negócio",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    // Captura erros de integridade (E-mail/CPF duplicado)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDuplicidade(org.springframework.dao.DataIntegrityViolationException ex) {
        var status = HttpStatus.CONFLICT; // 409
        var body = new ErroResponse(
                status.value(),
                "Conflito de Dados",
                "Este registro (E-mail ou CPF) já existe no sistema.",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    // Erro genérico para falhas catastróficas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = new ErroResponse(
                status.value(),
                "Erro Interno",
                "Ocorreu um erro inesperado no servidor.",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }
}