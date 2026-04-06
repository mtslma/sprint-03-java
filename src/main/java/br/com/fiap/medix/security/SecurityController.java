package br.com.fiap.medix.security;

import br.com.fiap.medix.enums.Role;
import br.com.fiap.medix.model.Admin;
import br.com.fiap.medix.model.Usuario;
import br.com.fiap.medix.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Depois eu vou apagar esse aqui
    private record UsuarioRequest(String email, String senha, String role) {}

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody @Valid UsuarioRequest data) {
        // Verifica se já existe
        if(usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já cadastrado");
        }

        Admin novoUsuario = new Admin();
        novoUsuario.setEmail(data.email());
        novoUsuario.setSenha(passwordEncoder.encode(data.senha()));
        novoUsuario.setRole(Role.valueOf(data.role().toUpperCase()));

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(token));
    }

    private record TokenResponse(String token){}
}