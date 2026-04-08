package br.com.fiap.medix.service;

import br.com.fiap.medix.enums.Role;
import br.com.fiap.medix.model.Auditoria;
import br.com.fiap.medix.model.Colaborador;
import br.com.fiap.medix.repository.AuditoriaRepository;
import br.com.fiap.medix.repository.ColaboradorRepository;
import br.com.fiap.medix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired private ColaboradorRepository colaboradorRepository;
    @Autowired private AuditoriaRepository auditoriaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Colaborador criarColaborador(Colaborador colaborador) {
        colaborador.setSenha(passwordEncoder.encode(colaborador.getSenha()));
        colaborador.setRole(Role.COLABORADOR);
        return colaboradorRepository.save(colaborador);
    }

    public List<Auditoria> listarTodosLogs() {
        return auditoriaRepository.findAll();
    }

    // Acessa a FUNCTION 1
    public String obterUsuarioJson(Long id) {
        return usuarioRepository.getUsuarioCustomJson(id);
    }
}