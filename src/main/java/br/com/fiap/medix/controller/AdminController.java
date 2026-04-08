package br.com.fiap.medix.controller;

import br.com.fiap.medix.dto.ColaboradorDTO;
import br.com.fiap.medix.model.Auditoria;
import br.com.fiap.medix.model.Colaborador;
import br.com.fiap.medix.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @PostMapping("/colaboradores")
    public ResponseEntity<Colaborador> cadastrarColaborador(@RequestBody @Valid ColaboradorDTO dto) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.nome());
        colaborador.setCpf(dto.cpf());
        colaborador.setEmail(dto.email());
        colaborador.setSenha(dto.senha());
        colaborador.setTipoColaborador(dto.tipoColaborador());
        return ResponseEntity.status(201).body(service.criarColaborador(colaborador));
    }

    // PROVAS DOS REQUISITOS (SPRINT 3)

    // PROVA DA TRIGGER (V3)
    @GetMapping("/logs")
    public ResponseEntity<List<Auditoria>> visualizarLogs() {
        return ResponseEntity.ok(service.listarTodosLogs());
    }

    // PROVA DA FUNCTION 1: Conversão Manual JSON (V4)
    @GetMapping(value = "/usuario/{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> obterUsuarioJson(@PathVariable Long id) {
        return ResponseEntity.ok(service.obterUsuarioJson(id));
    }
}