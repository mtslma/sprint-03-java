package br.com.fiap.medix.repository;

import br.com.fiap.medix.dto.AgendamentoRequest;
import br.com.fiap.medix.model.Agendamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Lista todos os agendamentos de um paciente específico
    List<Agendamento> findAllByPacienteIdOrderByDataHoraInicioDesc(Long pacienteId);

    // Busca o próximo agendamento (o mais próximo do horário atual)
    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataHoraInicio >= :agora " +
            "AND a.status != 'CANCELADO' " +
            "ORDER BY a.dataHoraInicio ASC")
    List<Agendamento> findNextAgendamento(Long pacienteId, LocalDateTime agora, Pageable pageable);
}
