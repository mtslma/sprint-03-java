package br.com.fiap.medix.controller;

import br.com.fiap.medix.model.UnidadeSaude;
import br.com.fiap.medix.model.Usuario;
import br.com.fiap.medix.service.UnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService service;

    @GetMapping
    public ResponseEntity<List<UnidadeSaude>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeSaude> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UnidadeSaude> criar(@RequestBody UnidadeSaude unidade, @AuthenticationPrincipal Usuario logado) {
        var novaUnidade = service.salvar(unidade, logado);
        return ResponseEntity.status(201).body(novaUnidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeSaude> atualizar(@PathVariable Long id, @RequestBody UnidadeSaude unidade, @AuthenticationPrincipal Usuario logado) {
        var unidadeAtualizada = service.atualizar(id, unidade, logado);
        return ResponseEntity.ok(unidadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        service.excluir(id, logado);
        return ResponseEntity.noContent().build();
    }
}