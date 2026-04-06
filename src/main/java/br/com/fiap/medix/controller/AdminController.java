package br.com.fiap.medix.controller;

import br.com.fiap.medix.dto.ColaboradorDTO;
import br.com.fiap.medix.model.Auditoria;
import br.com.fiap.medix.model.Colaborador;
import br.com.fiap.medix.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        Colaborador colaborador = toEntity(dto);
        var colabSalvo = service.criarColaborador(colaborador);
        return ResponseEntity.status(201).body(colabSalvo);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Auditoria>> visualizarLogs() {
        return ResponseEntity.ok(service.listarTodosLogs());
    }

    @GetMapping("/relatorio-analitico")
    public ResponseEntity<List<String>> rodarRelatorioAnalitico() {
        return ResponseEntity.ok(service.dispararRelatorioAnalitico());
    }

    @GetMapping("/exportar-pacientes")
    public ResponseEntity<List<String>> exportarPacientes() {
        return ResponseEntity.ok(service.dispararExportacaoJson());
    }

    private Colaborador toEntity(ColaboradorDTO dto) {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(dto.nome());
        colaborador.setCpf(dto.cpf());
        colaborador.setEmail(dto.email());
        colaborador.setSenha(dto.senha());
        colaborador.setTipoColaborador(dto.tipoColaborador());
        return colaborador;
    }
}