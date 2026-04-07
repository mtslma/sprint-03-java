package br.com.fiap.medix.controller;

import br.com.fiap.medix.dto.AgendamentoRequest;
import br.com.fiap.medix.model.Agendamento;
import br.com.fiap.medix.model.Usuario;
import br.com.fiap.medix.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    public ResponseEntity<Agendamento> agendar(@RequestBody AgendamentoRequest dto, @AuthenticationPrincipal Usuario logado) {
        return ResponseEntity.status(201).body(service.criarAgendamento(dto, logado));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable Long id) {
        service.confirmarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}