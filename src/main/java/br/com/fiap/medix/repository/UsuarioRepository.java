package br.com.fiap.medix.repository;

import br.com.fiap.medix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // SPRINT 3: FUNCTION 1 (Conversão Manual JSON)
    @Query(value = "SELECT FN_CONVERTE_USUARIO_JSON(:id) FROM DUAL", nativeQuery = true)
    String getUsuarioCustomJson(@Param("id") Long id);
}