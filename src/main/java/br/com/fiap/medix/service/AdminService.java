package br.com.fiap.medix.service;

import br.com.fiap.medix.enums.Role;
import br.com.fiap.medix.model.Auditoria;
import br.com.fiap.medix.model.Colaborador;
import br.com.fiap.medix.repository.AuditoriaRepository;
import br.com.fiap.medix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Transactional
    public Colaborador criarColaborador(Colaborador colaborador) {
        colaborador.setSenha(passwordEncoder.encode(colaborador.getSenha()));
        colaborador.setRole(Role.COLABORADOR);
        return usuarioRepository.save(colaborador);
    }

    public List<Auditoria> listarTodosLogs() {
        return auditoriaRepository.findAll();
    }

    // Aciona a procedure analítica no console do banco (Requisito Sprint)
    public List<String> dispararRelatorioAnalitico() {
        return auditoriaRepository.chamarRelatorioAnalitico();
    }

    public List<String> dispararExportacaoJson() {
        return auditoriaRepository.chamarExportacaoPacientesJson();
    }
}