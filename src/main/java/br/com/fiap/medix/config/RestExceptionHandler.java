package br.com.fiap.medix.config;

import br.com.fiap.medix.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    // Captura erros de lógica (Ex: "Não há médicos", "Cancelamento 24h")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var body = new ErroResponse(
                status.value(),
                "Erro de Regra de Negócio",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    // Captura erros de banco (Ex: ORA-00942 que você teve agora)
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<ErroResponse> handleDatabase(org.springframework.dao.DataAccessException ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = new ErroResponse(
                status.value(),
                "Erro de Banco de Dados",
                "O banco está com problemas ou tabelas faltando. Verifique os logs.",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    // Erro genérico (Para qualquer outra coisa que quebrar)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = new ErroResponse(
                status.value(),
                "Erro Crítico",
                "Ocorreu um erro inesperado: " + ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }
}