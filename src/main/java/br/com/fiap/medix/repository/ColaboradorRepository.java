package br.com.fiap.medix.repository;

import br.com.fiap.medix.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    @Query("SELECT c FROM Colaborador c WHERE c.tipoColaborador = 'OPERACIONAL' " +
            "AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :esp, '%')) " +
            "AND c.id NOT IN (SELECT a.medico.id FROM Agendamento a WHERE a.dataHoraInicio = :data)")
    Optional<Colaborador> findAvailableDoctor(String esp, LocalDateTime data);
}